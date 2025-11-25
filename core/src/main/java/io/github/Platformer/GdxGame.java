package io.github.Platformer;

import com.artemis.World;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.*;
import games.rednblack.editor.renderer.SceneConfiguration;
import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.resources.AsyncResourceManager;
import games.rednblack.editor.renderer.resources.ResourceManager;
import games.rednblack.editor.renderer.resources.ResourceManagerLoader;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GdxGame extends ApplicationAdapter {
    private AssetManager assetManager;

    private SceneLoader sceneLoader;
    private AsyncResourceManager asyncResourceManager;
    private Viewport viewport;
    private OrthographicCamera camera;

    private World engine;


    @Override
    public void create() {
        assetManager = new AssetManager();
        assetManager.setLoader(AsyncResourceManager.class, new ResourceManagerLoader(assetManager.getFileHandleResolver()));
        assetManager.load("project.dt", AsyncResourceManager.class);

        assetManager.finishLoading();

        asyncResourceManager = assetManager.get("project.dt", AsyncResourceManager.class);

        SceneConfiguration config = new SceneConfiguration();
        config.setResourceRetriever(asyncResourceManager);

        sceneLoader = new SceneLoader(config);
        engine = sceneLoader.getEngine();

        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);

        sceneLoader.loadScene("MainScene", viewport);

    }

    //Test commit

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
