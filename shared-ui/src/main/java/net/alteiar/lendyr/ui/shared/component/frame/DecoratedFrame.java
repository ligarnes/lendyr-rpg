package net.alteiar.lendyr.ui.shared.component.frame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import lombok.Builder;
import lombok.Getter;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class DecoratedFrame extends Group {
  // Core texture
  private final Texture background;
  private final Texture frameAngle;
  private final Texture anglePoint;
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

  private float edgeX;
  private float edgeY;
  private float edgeXend;
  private float edgeYend;
  private float edgeWidth;
  private float edgeHeight;


  @Builder
  public DecoratedFrame(UiFactory uiFactory, String backgroundTexture) {
    if (backgroundTexture == null) {
      background = uiFactory.getTexture("fantasy-gui/bg_05.png");
    } else {
      background = uiFactory.getTexture(backgroundTexture);
    }
    background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

    frameAngle = uiFactory.getTexture("fantasy-gui/frame_01_02.png");
    anglePoint = uiFactory.getTexture("fantasy-gui/frame_01_01.png");
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

    float spacing = borderThickness - 4 * ratio;
    edgeWidth = anglePoint.getWidth() * ratio;
    edgeHeight = anglePoint.getHeight() * ratio;

    edgeX = getX() + spacing;
    edgeXend = getX() + getWidth() - spacing - edgeWidth;
    edgeY = getY() + spacing;
    edgeYend = getY() + getHeight() - spacing - edgeHeight;
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

    // Little dot/edge
    batch.draw(anglePoint, edgeX, edgeY, edgeWidth, edgeHeight);
    batch.draw(anglePoint, edgeXend, edgeY, edgeWidth, edgeHeight);
    batch.draw(anglePoint, edgeX, edgeYend, edgeWidth, edgeHeight);
    batch.draw(anglePoint, edgeXend, edgeYend, edgeWidth, edgeHeight);

    // Draw child on top of the frame
    super.draw(batch, parentAlpha);
  }

}
