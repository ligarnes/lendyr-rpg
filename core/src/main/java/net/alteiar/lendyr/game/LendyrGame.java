package net.alteiar.lendyr.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import net.alteiar.lendyr.game.encounter.GameEngine;
import net.alteiar.lendyr.game.screen.LoadingScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class LendyrGame extends Game {
  GameEngine gameEngine;
  AssetManager assetManager;

  @Override
  public void create() {
    assetManager = new AssetManager();

    GameServer gameServer = GameServer.builder().port(50051).build();
    gameServer.start();

    gameEngine = GameEngine.builder().host("localhost").port(50051).build();
    setScreen(LoadingScreen.builder().assetManager(assetManager).gameEngine(gameEngine).game(this).build());
  }

  @Override
  public void dispose() {
    gameEngine.dispose();
    assetManager.dispose();
    getScreen().dispose();
  }
}
