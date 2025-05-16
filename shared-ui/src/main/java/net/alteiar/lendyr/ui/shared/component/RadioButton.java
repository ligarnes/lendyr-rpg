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

  @Builder
  public RadioButton(UiFactory uiFactory) {
    background = uiFactory.getTexture("fantasy-gui/button_06_bg.png");
    frame = uiFactory.getTexture("fantasy-gui/button_06_frame.png");
    selectedTexture = uiFactory.getTexture("fantasy-gui/button_06.png");

    this.setWidth(24);
    this.setHeight(24);
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

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.draw(background, getX(), getY(), getWidth(), getHeight());
    if (selected) {
      batch.draw(selectedTexture, getX(), getY(), getWidth(), getHeight());
    }
    batch.draw(frame, getX(), getY(), getWidth(), getHeight());
  }
}
