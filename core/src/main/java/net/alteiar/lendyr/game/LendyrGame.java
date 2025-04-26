package net.alteiar.lendyr.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import net.alteiar.lendyr.game.screen.LoadingScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class LendyrGame extends Game {

    AssetManager assetManager;

    @Override
    public void create() {
        assetManager = new AssetManager();
        setScreen(LoadingScreen.builder().assetManager(assetManager).game(this).build());
    }


    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
