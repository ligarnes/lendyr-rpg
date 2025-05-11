package net.alteiar.lendyr.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.game.ResourceLoader;
import net.alteiar.lendyr.game.encounter.BattlemapScreen;
import net.alteiar.lendyr.game.encounter.GameEngine;

public class LoadingScreen extends ScreenAdapter {
  private final Game game;
  private final AssetManager assetManager;
  private GameEngine gameEngine;

  Stage stage;
  Label labelTitle;
  Label labelProgress;

  @Builder
  public LoadingScreen(@NonNull Game game, @NonNull GameEngine gameEngine, @NonNull AssetManager assetManager) {
    this.game = game;
    this.assetManager = assetManager;
    this.gameEngine = gameEngine;
  }

  @Override
  public void dispose() {
    stage.dispose();
  }

  @Override
  public void show() {
    ResourceLoader resourceLoader = ResourceLoader.builder().assetManager(assetManager).build();
    resourceLoader.load();

    stage = new Stage();

    BitmapFont font = assetManager.finishLoadingAsset("black-crusader-ui/roboto.fnt");
    labelProgress = new Label("TEXT", new Label.LabelStyle(font, Color.WHITE));

    labelTitle = new Label("Loading...", new Label.LabelStyle(font, Color.WHITE));

    Table table = new Table();
    table.setFillParent(true);
    table.add(labelTitle);
    table.row().pad(10);
    table.add(labelProgress);

    stage.addActor(table);

    this.gameEngine.load();
  }

  @Override
  public void render(float delta) {
    assetManager.update();
    if (assetManager.isFinished() && gameEngine.isLoaded()) {
      game.setScreen(BattlemapScreen.builder().game(game).assetManager(assetManager).gameEngine(gameEngine).build());
    }

    if (gameEngine.isNetworkError()) {
      labelTitle.setText("NETWORK ERROR");
      labelProgress.setText("Could not reach the server. Please shutdown and restart");
    } else if (assetManager.isFinished()) {
      labelTitle.setText("Loading game state...");
    } else {
      labelTitle.setText("Loading textures...");
      String progress = String.format("%.0f %%", assetManager.getProgress() * 100f);
      labelProgress.setText(progress);
    }

    ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
    stage.act(delta);
    stage.draw();
  }
}
