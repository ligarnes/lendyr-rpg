package net.alteiar.lendyr.ui.shared.screen;

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

public class GameOverScreen extends ScreenAdapter {
  private final Game game;
  private final AssetManager assetManager;
  private Stage stage;

  @Builder
  public GameOverScreen(@NonNull Game game, @NonNull AssetManager assetManager) {
    this.game = game;
    this.assetManager = assetManager;
  }

  @Override
  public void show() {
    stage = new Stage();

    BitmapFont font = assetManager.get("black-crusader-ui/roboto.fnt");
    font.getData().setScale(1f);

    Table table = new Table();
    table.setFillParent(true);
    table.add(new Label("You Died", new Label.LabelStyle(font, Color.WHITE)));
    table.row().pad(10);
    table.add(new Label("Game Over !", new Label.LabelStyle(font, Color.WHITE)));

    stage.addActor(table);
  }

  @Override
  public void dispose() {
    stage.dispose();
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

    stage.act(delta);
    stage.draw();
  }
}
