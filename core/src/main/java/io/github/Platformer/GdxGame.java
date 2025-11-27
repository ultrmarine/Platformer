package io.github.Platformer;

import com.artemis.World;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.*;
import games.rednblack.editor.renderer.SceneConfiguration;
import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.resources.AsyncResourceManager;
import games.rednblack.editor.renderer.resources.ResourceManagerLoader;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.editor.renderer.utils.ItemWrapper;
import io.github.Platformer.component.PlayerComponent;
import io.github.Platformer.script.PlayerScript;
import io.github.Platformer.system.CameraSystem;
import io.github.Platformer.system.PlayerAnimationSystem;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GdxGame extends ApplicationAdapter {
    private AssetManager assetManager;

    private SceneLoader sceneLoader;
    private AsyncResourceManager asyncResourceManager;
    private Viewport viewport;
    private OrthographicCamera camera;

    private World engine;

    private ItemWrapper itemWrapper;


    @Override
    public void create() {
        assetManager = new AssetManager();
        assetManager.setLoader(AsyncResourceManager.class, new ResourceManagerLoader(assetManager.getFileHandleResolver()));
        assetManager.load("project.dt", AsyncResourceManager.class);

        assetManager.finishLoading();

        asyncResourceManager = assetManager.get("project.dt", AsyncResourceManager.class);

        SceneConfiguration config = new SceneConfiguration();
        config.setResourceRetriever(asyncResourceManager);
        config.addSystem(new PlayerAnimationSystem());
        CameraSystem cameraSystem = new CameraSystem();
        config.addSystem(cameraSystem);

        sceneLoader = new SceneLoader(config);
        engine = sceneLoader.getEngine();

        camera = new OrthographicCamera();
        viewport = new FitViewport(320,240,camera);

        sceneLoader.loadScene("MainScene", viewport);

        ItemWrapper root = new ItemWrapper(sceneLoader.getRoot(), engine);

        ComponentRetriever.addMapper(PlayerComponent.class);

        ItemWrapper player = root.getChild("player");
        ComponentRetriever.initialize(engine);
        ComponentRetriever.create(player.getChild("player-anim").getEntity(), PlayerComponent.class, engine);
        PlayerScript playerScript = new PlayerScript();
        player.addScript(playerScript);
        cameraSystem.setFocus(player.getEntity());
    }

    @Override
    public void render() {
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        engine.process();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

        if (width > 0 && height > 0){
            sceneLoader.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        sceneLoader.dispose();
    }
}
