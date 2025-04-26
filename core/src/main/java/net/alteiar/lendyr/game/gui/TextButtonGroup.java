package net.alteiar.lendyr.game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import lombok.Builder;

public class TextButtonGroup extends Group {
  private final Label label;

  @Builder
  private TextButtonGroup(UiFactory uiFactory, String text) {
    ButtonIcon icon = new ButtonIcon(uiFactory);
    label = uiFactory.createLabel(text, Color.LIGHT_GRAY);
    label.setFontScale(0.8f);
    label.setWidth(icon.getWidth() - 10);
    label.setHeight(icon.getHeight() - 10);
    label.setX(5);
    label.setY(5);
    label.setAlignment(Align.center);

    setWidth(210.784f);
    setHeight(50f);

    this.addActor(icon);
    this.addActor(label);
  }

  public void setText(String text) {
    this.label.setText(text);
  }

  private static class ButtonIcon extends Actor {
    private final Texture button;

    @Builder
    private ButtonIcon(UiFactory assetManager) {
      button = assetManager.getTexture("black-crusader-ui/BlackCrusaderUI_el_07.png");

      setWidth(210.784f);
      setHeight(50f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
      batch.draw(button, getX(), getY(), getWidth(), getHeight());
    }
  }
}
