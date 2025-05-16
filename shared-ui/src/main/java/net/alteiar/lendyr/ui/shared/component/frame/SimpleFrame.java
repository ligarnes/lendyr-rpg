package net.alteiar.lendyr.ui.shared.component.frame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import lombok.Builder;
import lombok.Getter;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class SimpleFrame extends Group {
  // Core texture
  private final Texture background;
  private final Texture frameAngle;
  private final Texture frame;

  // Controlled variables
  @Getter
  private float borderThickness;

  // Pre-computed values to avoid re-calculation
  private int frameHeight;

  private float angleXend;
  private float angleYend;
  private float angleWidth;
  private float angleHeight;

  @Builder
  public SimpleFrame(UiFactory uiFactory, String backgroundTexture) {
    if (backgroundTexture == null) {
      background = uiFactory.getTexture("fantasy-gui/bg_05.png");
    } else {
      background = uiFactory.getTexture(backgroundTexture);
    }
    background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

    frameAngle = uiFactory.getTexture("fantasy-gui/frame_01_03.png");
    frame = uiFactory.getTexture("fantasy-gui/frame_01_05.png");
    frame.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.Repeat);

    borderThickness = 10;

    setWidth(300);
    setHeight(500);

    recompute();
  }

  public void setBorderThickness(float borderThickness) {
    this.borderThickness = borderThickness;
    this.recompute();
  }

  @Override
  protected void sizeChanged() {
    super.sizeChanged();
    recompute();
  }

  @Override
  protected void scaleChanged() {
    super.scaleChanged();
    recompute();
  }

  @Override
  protected void positionChanged() {
    super.positionChanged();
    recompute();
  }

  private void recompute() {
    float ratio = borderThickness / frame.getWidth();
    float repeatY = getHeight() / (frame.getHeight() * ratio);

    frameHeight = (int) (frame.getHeight() * repeatY);

    angleWidth = frameAngle.getWidth() * ratio;
    angleHeight = frameAngle.getHeight() * ratio;

    angleXend = getX() + getWidth() - angleWidth;
    angleYend = getY() + getHeight() - angleHeight;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.draw(background, getX(), getY(), getWidth(), getHeight());

    // Core frame
    batch.draw(frame, getX(), getY(), 0, 0,
      borderThickness, getHeight(), 1, 1, 0, 0, 0,
      frame.getWidth(), frameHeight, false, false);
    batch.draw(frame, getX() + getWidth() - borderThickness, getY(), 0, 0,
      borderThickness, getHeight(), 1, 1, 0, 0, 0,
      frame.getWidth(), frameHeight, true, false);
    batch.draw(frame, getX(), getY() + getHeight(), 0, 0,
      borderThickness, getWidth(), 1, 1, -90, 0, 0,
      frame.getWidth(), frameHeight, false, true);
    batch.draw(frame, getX(), getY() + borderThickness, 0, 0,
      borderThickness, getWidth(), 1, 1, -90, 0, 0,
      frame.getWidth(), frameHeight, true, false);

    // Angle frame
    batch.draw(frameAngle, getX(), angleYend, angleWidth, angleHeight);
    batch.draw(frameAngle, angleXend, angleYend, angleWidth, angleHeight, 0, 0, frameAngle.getWidth(), frameAngle.getHeight(), true, false);
    batch.draw(frameAngle, angleXend, getY(), angleWidth, angleHeight, 0, 0, frameAngle.getWidth(), frameAngle.getHeight(), true, true);
    batch.draw(frameAngle, getX(), getY(), angleWidth, angleHeight, 0, 0, frameAngle.getWidth(), frameAngle.getHeight(), false, true);

    // Draw child on top of the frame
    super.draw(batch, parentAlpha);
  }

}
