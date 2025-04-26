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
import net.alteiar.lendyr.game.state.WorldFactory;
import net.alteiar.lendyr.game.state.WorldState;

public class LoadingScreen extends ScreenAdapter {
  private final Game game;
  private final AssetManager assetManager;

  Stage stage;
  BitmapFont font;
  Label labelProgress;

  private WorldState worldState;

  @Builder
  public LoadingScreen(@NonNull Game game, @NonNull AssetManager assetManager) {
    this.game = game;
    this.assetManager = assetManager;
  }

  @Override
  public void dispose() {
    stage.dispose();
    font.dispose();
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

    assetManager.load("blank.png", Texture.class);

    assetManager.load("assets/soldier-orcs/Characters/Soldier/Soldier/Soldier.png", Texture.class);
    assetManager.load("maps/map.jpg", Texture.class);


    assetManager.load("token/ulfrik-token.png", Texture.class);
    assetManager.load("token/cyrilla-token.png", Texture.class);

    worldState = WorldFactory.createSimpleWorld();

    stage = new Stage();

    font = assetManager.finishLoadingAsset("black-crusader-ui/roboto.fnt");

    labelProgress = new Label("TEXT", new Label.LabelStyle(font, Color.WHITE));

    Table table = new Table();
    table.setFillParent(true);
    table.add(new Label("Loading...", new Label.LabelStyle(font, Color.WHITE)));
    table.row().pad(10);
    table.add(labelProgress);

    stage.addActor(table);
  }

  @Override
  public void render(float delta) {
    if (assetManager.update()) {
      game.setScreen(BattlemapScreen.builder().assetManager(assetManager).worldState(worldState).build());
    }

    ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

    String progress = String.format("%.0f %%", assetManager.getProgress() * 100f);
    labelProgress.setText(progress);

    stage.act(delta);
    stage.draw();
  }

}
