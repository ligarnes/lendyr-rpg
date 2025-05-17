package net.alteiar.lendyr.ui.shared.component;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.alteiar.lendyr.ui.shared.listener.ButtonClickListener;

public class RadioButton extends Actor {
  private final Texture background;
  private final Texture frame;
  private final Texture selectedTexture;

  @Getter
  @Setter
  private boolean selected;

  // Computed
  private float backgroundWidth;
  private float backgroundHeight;
  private float backgroundX;
  private float backgroundY;

  private float selectWidth;
  private float selectedHeight;
  private float selectedX;
  private float selectedY;

  private float frameWidth;
  private float frameHeight;

  @Builder
  public RadioButton(UiFactory uiFactory) {
    background = uiFactory.getTexture("fantasy-gui/button_06_bg.png");
    frame = uiFactory.getTexture("fantasy-gui/button_06_frame.png");
    selectedTexture = uiFactory.getTexture("fantasy-gui/button_06.png");

    //this.setSize(64, 64);
    this.setSize(18, 18);
  }

  @Override
  protected void positionChanged() {
    recompute();
  }

  @Override
  protected void sizeChanged() {
    float scale = getWidth() / frame.getWidth();
    this.setScale(scale);
    recompute();
  }

  @Override
  protected void scaleChanged() {
    this.setSize(frame.getWidth() * getScaleX(), frame.getHeight() * getScaleY());
  }

  public void addClickListener(ButtonClickListener actionSelectorClicked) {
    this.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        actionSelectorClicked.buttonClicked();
        event.stop();
      }
    });
  }

  private void recompute() {
    backgroundWidth = background.getWidth() * getScaleX();
    backgroundHeight = background.getHeight() * getScaleY();
    backgroundX = getX() + getWidth() / 2f - backgroundWidth / 2f;
    backgroundY = getY() + getHeight() / 2f - backgroundHeight / 2f;

    selectWidth = selectedTexture.getWidth() * getScaleX();
    selectedHeight = selectedTexture.getHeight() * getScaleY();
    selectedX = getX() + getWidth() / 2f - selectWidth / 2f;
    selectedY = getY() + getHeight() / 2f - selectedHeight / 2f;

    frameWidth = frame.getWidth() * getScaleX();
    frameHeight = frame.getHeight() * getScaleY();
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.draw(background, backgroundX, backgroundY, backgroundWidth, backgroundHeight);
    if (selected) {
      batch.draw(selectedTexture, selectedX, selectedY, selectWidth, selectedHeight);
    }
    batch.draw(frame, getX(), getY(), frameWidth, frameHeight);
  }
}
