package net.alteiar.lendyr.ui.shared.component;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import net.alteiar.lendyr.ui.shared.component.frame.DecoratedFrame;
import net.alteiar.lendyr.ui.shared.component.frame.SimpleFrame;

public class UiFactory {

  public static final Color HEALTH_COLOR = new Color(0.651f, 0.122f, 0.086f, 0.5f);
  public static final Color GRAY_OVERLAY_COLOR = new Color(0.663f, 0.663f, 0.663f, 0.3f);
  public static final Color DARK_GRAY_OVERLAY_COLOR = new Color(0.163f, 0.163f, 0.163f, 0.3f);
  public static final Color GREEN_SELECTION_COLOR = new Color(0.008f, 0.541f, 0.059f, 1f);
  public static final Color WARN_SELECTION_COLOR = new Color(1f, 0.8f, 0f, 1f);

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

  public ActionSelector createActionSelector(ActionSelectorFactory.Icon icon) {
    return ActionSelectorFactory.create(this, icon);
  }

  public IconButtonActor createIconButton(IconButtonActor.ButtonType type) {
    return IconButtonActor.builder().buttonType(type).assetManager(assetManager).build();
  }

  public RadioButton createRadioButton() {
    return RadioButton.builder().uiFactory(this).build();
  }

  public DecoratedFrame createDecoratedFrame() {
    return DecoratedFrame.builder().uiFactory(this).build();
  }

  public SimpleFrame createSimpleFrame() {
    return SimpleFrame.builder().uiFactory(this).build();
  }

  public Texture getTexture(String fileName) {
    return assetManager.get(fileName, Texture.class);
  }
}
