package net.alteiar.lendyr.game.encounter.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import lombok.Builder;
import net.alteiar.lendyr.ui.shared.component.TextButtonGroup;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.component.frame.DecoratedFrame;
import net.alteiar.lendyr.ui.shared.listener.ButtonClickListener;

public class OkDialog extends Group {
  private final DecoratedFrame background;
  private final Label text;
  private final TextButtonGroup button;

  @Builder
  public OkDialog(UiFactory uiFactory, String errorMessage) {
    background = uiFactory.createDecoratedFrame();
    background.setWidth(300);
    background.setHeight(300);

    this.text = uiFactory.createLabel(errorMessage, Color.BLACK);
    text.setPosition(background.getWidth() / 2f, background.getHeight() / 2f);

    text.setFontScale(0.8f);
    text.setWidth(background.getWidth() - 10);
    text.setHeight(background.getHeight() - 10);
    text.setX(background.getX());
    text.setY(background.getY());
    text.setAlignment(Align.center);
    text.setWrap(true);

    button = uiFactory.createTextButton("OK");
    button.setPosition(background.getX() + 150 - button.getWidth() / 2, background.getY() + 10);

    this.addActor(background);
    this.addActor(text);
    this.addActor(button);
  }

  public void addButtonListener(ButtonClickListener eventListener) {
    this.button.addClickListener(eventListener);
  }

  public void setText(String text) {
    this.text.setText(text);
  }
}
