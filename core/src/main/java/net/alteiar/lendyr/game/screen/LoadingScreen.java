package net.alteiar.lendyr.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.game.state.GameEngine;

public class LoadingScreen extends ScreenAdapter {
  private final Game game;
  private final AssetManager assetManager;

  Stage stage;
  Label labelProgress;

  private GameEngine gameEngine;

  @Builder
  public LoadingScreen(@NonNull Game game, @NonNull AssetManager assetManager) {
    this.game = game;
    this.assetManager = assetManager;
  }

  @Override
  public void dispose() {
    stage.dispose();
  }

  @Override
  public void show() {
    assetManager.load("black-crusader-ui/roboto.fnt", BitmapFont.class);

    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_01_01.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_01_02.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_01_03.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_01_04.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_01_05.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_02_01.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_02_02.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_02_03.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_03_01.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_03_02.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_04.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_05.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_06_01.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_06_02.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_07.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_08.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_09.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_10.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_11.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_12.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_13.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_14.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_15.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_16.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_17.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_18.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_19.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_20.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_21.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_22.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_23_1.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_23_2.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_23_3.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_24.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_25.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_26.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_27.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_28.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_29.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_30.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_31.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_32.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_33.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_34.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_35.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_36.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_37.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_38.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_39.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_40.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_41.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_42.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_43.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_44.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_45.png", Texture.class);
    assetManager.load("black-crusader-ui/BlackCrusaderUI_el_46.png", Texture.class);

    assetManager.load("icon/TabletopBadges_15.PNG", Texture.class);
    assetManager.load("icon/TabletopBadges_47.PNG", Texture.class);
    assetManager.load("icon/TabletopBadges_47_overlay.png", Texture.class);

    assetManager.load("blank.png", Texture.class);
    assetManager.load("black.png", Texture.class);

    assetManager.load("maps/map.jpg", Texture.class);


    assetManager.load("token/ulfrik-token.png", Texture.class);
    assetManager.load("token/cyrilla-token.png", Texture.class);
    assetManager.load("token/izydor-token.png", Texture.class);

    assetManager.load("portrait/ulfrik-portrait.jpeg", Texture.class);
    assetManager.load("portrait/cyrilla-portrait.jpeg", Texture.class);
    assetManager.load("portrait/izydor-portrait.jpeg", Texture.class);
    assetManager.load("portrait/overlay-green.png", Texture.class);
    assetManager.load("portrait/overlay-red.png", Texture.class);
    assetManager.load("portrait/overlay-neutral.png", Texture.class);

    assetManager.load("encounter/move-overlay.png", Texture.class);
    assetManager.load("encounter/move-overlay-ok.png", Texture.class);
    assetManager.load("encounter/move-overlay-nok.png", Texture.class);
    assetManager.load("encounter/shield.png", Texture.class);
    assetManager.load("encounter/hurt.png", Texture.class);

    assetManager.load("encounter/cursor/walk.png", Texture.class);
    assetManager.load("encounter/cursor/attack.png", Texture.class);

    assetManager.load("encounter/dice/dice-six-faces-one.png", Texture.class);
    assetManager.load("encounter/dice/dice-six-faces-two.png", Texture.class);
    assetManager.load("encounter/dice/dice-six-faces-three.png", Texture.class);
    assetManager.load("encounter/dice/dice-six-faces-four.png", Texture.class);
    assetManager.load("encounter/dice/dice-six-faces-five.png", Texture.class);
    assetManager.load("encounter/dice/dice-six-faces-six.png", Texture.class);

    stage = new Stage();

    BitmapFont font = assetManager.finishLoadingAsset("black-crusader-ui/roboto.fnt");

    labelProgress = new Label("TEXT", new Label.LabelStyle(font, Color.WHITE));

    Table table = new Table();
    table.setFillParent(true);
    table.add(new Label("Loading...", new Label.LabelStyle(font, Color.WHITE)));
    table.row().pad(10);
    table.add(labelProgress);

    stage.addActor(table);

    gameEngine = GameEngine.builder().host("localhost").port(50051).build();
    gameEngine.load();
  }

  @Override
  public void render(float delta) {
    assetManager.update();
    if (assetManager.isFinished() && gameEngine.isLoaded()) {
      game.setScreen(BattlemapScreen.builder().game(game).assetManager(assetManager).gameEngine(gameEngine).build());
    }

    ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

    String progress = String.format("%.0f %%", assetManager.getProgress() * 100f);
    labelProgress.setText(progress);

    stage.act(delta);
    stage.draw();
  }

}
