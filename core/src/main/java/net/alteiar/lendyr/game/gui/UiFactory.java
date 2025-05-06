package net.alteiar.lendyr.game.gui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

public class UiFactory {

  public static final Color HEALTH_COLOR = new Color(0.651f, 0.122f, 0.086f, 0.5f);

  @Getter
  private final AssetManager assetManager;

  @Builder
  public UiFactory(@NonNull AssetManager assetManager) {
    this.assetManager = assetManager;
  }

  public BitmapFont getFont() {
    return assetManager.get("black-crusader-ui/roboto.fnt", BitmapFont.class);
  }

  public Label createLabel() {
    return createLabel(Color.WHITE);
  }

  public Label createLabel(String text) {
    return createLabel(text, Color.WHITE);
  }

  public Label createLabel(Color color) {
    return new Label("", new Label.LabelStyle(getFont(), color));
  }

  public Label createLabel(String text, Color color) {
    return new Label(text, new Label.LabelStyle(getFont(), color));
  }

  public TextButtonGroup createTextButton() {
    return createTextButton("Text");
  }

  public TextButtonGroup createTextButton(String text) {
    return TextButtonGroup.builder().uiFactory(this).text(text).build();
  }


  public Window createWindow() {
    return Window.builder().assetManager(assetManager).build();
  }

  public Texture getTexture(String fileName) {
    return assetManager.get(fileName, Texture.class);
  }
}
