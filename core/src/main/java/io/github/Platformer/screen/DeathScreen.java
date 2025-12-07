package io.github.Platformer.screen;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import games.rednblack.editor.renderer.components.DimensionsComponent;
import games.rednblack.editor.renderer.components.MainItemComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.resources.AsyncResourceManager;
import games.rednblack.editor.renderer.resources.ResourceManagerLoader;
import io.github.Platformer.GdxGame;
import io.github.Platformer.audio.AudioService;
import io.github.Platformer.component.CoinComponent;
import io.github.Platformer.component.PlayerComponent;


/** First screen of the application. Displayed after the application is created. */
public class DeathScreen implements Screen {
    private GdxGame game;
    private SpriteBatch batch;
    private BitmapFont font;

    private AudioService audioService;

    public DeathScreen(GdxGame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont(); // позволяет что либо писать букавками внутри игры (русский кста не поддерживает) ((привет онга)) (((здаров)))
        this.audioService = new AudioService("GameOver.mp3");
    }

    @Override
    public void show() {
        game.assetManager.setLoader(AsyncResourceManager.class,
            new ResourceManagerLoader(game.assetManager.getFileHandleResolver()));
        game.assetManager.load("project.dt", AsyncResourceManager.class);
        audioService.play();
    }

    @Override
    public void render(float delta) {
            Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            font.draw(batch, "you died xdd:",100,100);
            font.draw(batch, "press R to restart",200,100);
            batch.end();

            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) { //рестартаем лвл
                game.setScreen(new GameScreen(game));
                dispose();
            }
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}
