package io.github.Platformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import io.github.Platformer.screen.MenuScreen;

public class GdxGame extends Game {
    public AssetManager assetManager;

    @Override
    public void create() {
        assetManager = new AssetManager();
        setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
