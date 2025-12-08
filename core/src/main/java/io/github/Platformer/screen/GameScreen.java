package io.github.Platformer.screen;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import games.rednblack.editor.renderer.SceneConfiguration;
import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.resources.AsyncResourceManager;
import games.rednblack.editor.renderer.resources.ResourceManagerLoader;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.editor.renderer.utils.ItemWrapper;
import io.github.Platformer.GdxGame;
import io.github.Platformer.audio.AudioService;
import io.github.Platformer.component.CoinComponent;
import io.github.Platformer.component.PlayerComponent;
import io.github.Platformer.component.SkeletonComponent;
import io.github.Platformer.component.SpikeComponent;
import io.github.Platformer.script.EnemyScript;
import io.github.Platformer.script.PlayerScript;
import io.github.Platformer.system.CameraSystem;
import io.github.Platformer.system.PlayerAnimationSystem;
import io.github.Platformer.system.SkeletonAnimationSystem;

public class GameScreen implements Screen {
    private GdxGame game;

    private SceneLoader sceneLoader;
    private AsyncResourceManager asyncResourceManager;
    private Viewport viewport;
    private OrthographicCamera camera;
    private World engine;
    private BitmapFont font;
    private SpriteBatch batch;
    private PlayerComponent playerComponent;

    private int playerEntityId; // это нужно дла проверки хп и экрана смерти/проигрыша

    private AudioService audioService;

    public GameScreen(GdxGame game) {
        this.game = game;
        audioService = new AudioService("ambient.mp3");
        this.font = new BitmapFont();
        this.batch = new SpriteBatch();
    }

    @Override
    public void show() {


        AssetManager assetManager = game.assetManager;
        assetManager.setLoader(AsyncResourceManager.class, new ResourceManagerLoader(assetManager.getFileHandleResolver()));
        assetManager.load("project.dt", AsyncResourceManager.class);

        SceneConfiguration config = new SceneConfiguration();
        config.setResourceRetriever(asyncResourceManager);
        config.addSystem(new PlayerAnimationSystem());
        config.addSystem(new SkeletonAnimationSystem());

        CameraSystem cameraSystem = new CameraSystem();
        config.addSystem(cameraSystem);

        sceneLoader = new SceneLoader(config);
        engine = sceneLoader.getEngine();
        audioService.play();
        audioService.setLooping(true); // зацикливание чёта не работает((

        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);

        sceneLoader.loadScene("MainScene", viewport);

        ItemWrapper root = new ItemWrapper(sceneLoader.getRoot(), engine);
        ComponentRetriever.addMapper(PlayerComponent.class);
        ComponentRetriever.addMapper(SkeletonComponent.class);

        ComponentRetriever.initialize(engine);

        ItemWrapper player = root.getChild("player");
        ComponentRetriever.create(player.getChild("player-anim").getEntity(), PlayerComponent.class, engine);

        playerEntityId = player.getChild("player-anim").getEntity(); // айди плеера, чтобы дальше проверять его хп

        PlayerScript playerScript = new PlayerScript();
        player.addScript(playerScript);
        cameraSystem.setFocus(player.getEntity());

        ItemWrapper skeleton1 = root.getChild("skeleton1");
        ItemWrapper skeleton2 = root.getChild("skeleton2");
        ItemWrapper skeleton3 = root.getChild("skeleton3");
        ItemWrapper skeleton4 = root.getChild("skeleton4");

        ComponentRetriever.create(skeleton1.getChild("skeleton-anim").getEntity(), SkeletonComponent.class, engine);
        ComponentRetriever.create(skeleton2.getChild("skeleton-anim").getEntity(), SkeletonComponent.class, engine);
        ComponentRetriever.create(skeleton3.getChild("skeleton-anim").getEntity(), SkeletonComponent.class, engine);
        ComponentRetriever.create(skeleton4.getChild("skeleton-anim").getEntity(), SkeletonComponent.class, engine);



        EnemyScript enemyScript1 = new EnemyScript();
        skeleton1.addScript(enemyScript1);
        EnemyScript enemyScript2 = new EnemyScript();
        skeleton2.addScript(enemyScript2);
        EnemyScript enemyScript3 = new EnemyScript();
        skeleton3.addScript(enemyScript3);
        EnemyScript enemyScript4 = new EnemyScript();
        skeleton4.addScript(enemyScript4);

        ComponentRetriever.addMapper(CoinComponent.class);
        sceneLoader.addComponentByTagName("coin", CoinComponent.class);

        ComponentRetriever.addMapper(SpikeComponent.class);
        sceneLoader.addComponentByTagName("spike", SpikeComponent.class);

    }

    @Override
    public void render(float delta) {
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        engine.process();

        PlayerComponent player = ComponentRetriever.get(playerEntityId, PlayerComponent.class, engine); //получаем самого плеера и его хп монеты и тд

        batch.begin();
        font.draw(batch, "Coins collected " + player.coinsCollected,100,100);
        batch.end();

        if (player.hp!=1){ // чекает хп и в случае получения урона показывает экран смерты
            game.setScreen(new DeathScreen(game));
            audioService.stop();
            dispose();
        } else if (player.win) {
            game.setScreen(new WinScreen(game));
            audioService.stop();
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;

        viewport.update(width, height);
        sceneLoader.resize(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (sceneLoader != null) {
            sceneLoader.dispose();
        }
    }
}
