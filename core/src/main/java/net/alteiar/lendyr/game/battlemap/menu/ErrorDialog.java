package net.alteiar.lendyr.game.battlemap.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Builder;
import net.alteiar.lendyr.game.gui.TextButtonGroup;
import net.alteiar.lendyr.game.gui.UiFactory;
import net.alteiar.lendyr.game.gui.Window;

public class ErrorDialog extends ViewLayer {
  private boolean isVisible;
  private final Window background;
  private final Label text;
  public final TextButtonGroup button;

  @Builder
  public ErrorDialog(UiFactory uiFactory, String errorMessage) {
    super(new ScreenViewport());

    background = uiFactory.createWindow();
    background.setWidth(300);
    background.setHeight(300);
    background.setPosition((Gdx.graphics.getWidth() - background.getWidth()) / 2, (Gdx.graphics.getHeight() - background.getHeight()) / 2);

    this.text = uiFactory.createLabel(errorMessage, Color.BLACK);
    text.setPosition((Gdx.graphics.getWidth() - background.getWidth()) / 2, (Gdx.graphics.getHeight() - background.getHeight()) / 2);

    text.setFontScale(0.8f);
    text.setWidth(background.getWidth() - 10);
    text.setHeight(background.getHeight() - 10);
    text.setX(background.getX());
    text.setY(background.getY());
    text.setAlignment(Align.center);
    text.setWrap(true);

    button = uiFactory.createTextButton("OK");
    button.setPosition(background.getX() + 150 - button.getWidth() / 2, background.getY() + 10);

    stage.addActor(background);
    stage.addActor(text);
    stage.addActor(button);

    this.isVisible = true;
  }

  public void addButtonListener(EventListener eventListener) {
    this.button.addListener(eventListener);
  }

  public void show() {
    this.isVisible = true;
  }

  public void hide() {
    this.isVisible = false;
  }

  public void setText(String text) {
    this.text.setText(text);
  }

  @Override
  public void act(float delta) {
    if (isVisible) {
      super.act(delta);
    }
  }

  @Override
  public void draw() {
    if (isVisible) {
      super.draw();
    }
  }
}
