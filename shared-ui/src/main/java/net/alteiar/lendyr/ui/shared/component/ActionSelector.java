package net.alteiar.lendyr.ui.shared.component;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.alteiar.lendyr.ui.shared.listener.ButtonClickListener;

public class ActionSelector extends Actor {
  private final Texture background;

  // Inner icon
  private final Texture icon;
  private final Vector2 iconPosition;
  private float iconWidth;
  private float iconHeight;

  // Normal overlay
  private final Texture overlayNormal;
  private float overlayNormalWidth;
  private float overlayNormalHeight;

  // Selected overlay
  private final Texture overlaySelected;
  private float overlaySelectedWidth;
  private float overlaySelectedHeight;
  private final Vector2 overlaySelectedPosition;


  @Getter
  @Setter
  private boolean selected;

  @Builder
  public ActionSelector(UiFactory uiFactory, String icon) {
    iconPosition = new Vector2();
    overlaySelectedPosition = new Vector2();

    background = uiFactory.getTexture("fantasy-gui/bg_06.png");
    overlayNormal = uiFactory.getTexture("fantasy-gui/slot_04.png");
    overlaySelected = uiFactory.getTexture("fantasy-gui/slot_05.png");
    this.icon = uiFactory.getTexture(icon);

    this.setWidth(45);
    this.setHeight(45);

    recomputeIconPosition();
    recomputeOverlayPosition();
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
      batch.draw(overlaySelected, overlaySelectedPosition.x, overlaySelectedPosition.y, overlaySelectedWidth, overlaySelectedHeight);
    } else {
      batch.draw(overlayNormal, getX(), getY(), overlayNormalWidth, overlayNormalHeight);
    }

    batch.draw(icon, iconPosition.x, iconPosition.y, iconWidth, iconHeight);
  }

  @Override
  protected void positionChanged() {
    recomputeIconPosition();
    recomputeOverlayPosition();
  }

  @Override
  protected void sizeChanged() {
    recomputeIconPosition();
    recomputeOverlayPosition();
  }

  private void recomputeOverlayPosition() {
    float ratio = getWidth() / overlayNormal.getWidth();

    overlayNormalWidth = overlayNormal.getWidth() * ratio;
    overlayNormalHeight = overlayNormal.getHeight() * ratio;

    overlaySelectedWidth = overlaySelected.getWidth() * ratio;
    overlaySelectedHeight = overlaySelected.getHeight() * ratio;
    float xAdjusted = (getWidth() - overlaySelectedWidth) / 2f;
    float yAdjusted = (getHeight() - overlaySelectedHeight) / 2f;
    overlaySelectedPosition.set(getX() + xAdjusted, getY() + yAdjusted);
  }

  private void recomputeIconPosition() {
    float iconSizeRatio;
    if (icon.getWidth() > icon.getHeight()) {
      iconSizeRatio = (getWidth() * 0.9f) / icon.getWidth();
    } else {
      iconSizeRatio = (getHeight() * 0.9f) / icon.getHeight();
    }

    iconWidth = icon.getWidth() * iconSizeRatio;
    iconHeight = icon.getHeight() * iconSizeRatio;
    float x = getX() + (getWidth() - iconWidth) / 2f;
    float y = getY() + (getHeight() - iconHeight) / 2f;
    iconPosition.set(x, y);
  }
}
