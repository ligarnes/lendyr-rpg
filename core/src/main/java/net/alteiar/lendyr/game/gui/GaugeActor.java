package net.alteiar.lendyr.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class GaugeActor extends Actor {
  private final UiFactory uiFactory;
  private final Texture background;
  private Texture inner;
  private final Texture border;
  private final ShapeRenderer shapeRenderer;

  @Getter
  @Setter
  private Color color;
  @Getter
  @Setter
  private float ratio;

  @Builder
  private GaugeActor(@NonNull UiFactory uiFactory, float ratio, @NonNull Color color, String innerTexture) {
    this.uiFactory = uiFactory;

    border = uiFactory.getTexture("black-crusader-ui/BlackCrusaderUI_el_38.png");

    background = uiFactory.getTexture("black-crusader-ui/BlackCrusaderUI_el_39.png");
    if (innerTexture != null) {
      this.inner = uiFactory.getTexture(innerTexture);
    } else {
      inner = null;
    }
    setWidth(120f);
    setHeight(120f);
    this.shapeRenderer = new ShapeRenderer();
    this.shapeRenderer.setAutoShapeType(true);
    this.ratio = ratio;
    this.color = color;
  }

  public void setInnerTexture(String innerTexture) {
    this.inner = uiFactory.getTexture(innerTexture);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.draw(background, getX(), getY(), getWidth(), getHeight());
    if (inner != null) {
      batch.draw(inner, getX(), getY(), getWidth(), getHeight());
    }
    batch.end();

    // Draw blood level
    shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
    shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
    shapeRenderer.translate(getX(), getY(), 0);

    shapeRenderer.begin();
    drawMasks();
    drawMasked();
    Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    shapeRenderer.end();

    batch.begin();
    batch.draw(border, getX(), getY(), getWidth(), getHeight());
  }

  private void drawMasks() {
    /* Clear our depth buffer info from previous frame. */
    Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

    /* Set the depth function to LESS. */
    Gdx.gl.glDepthFunc(GL20.GL_LESS);

    /* Enable depth writing. */
    Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

    /* Disable RGBA color writing. */
    Gdx.gl.glColorMask(false, false, false, false);

    /* Render mask elements. */
    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.rect(getX(), getY(), getWidth(), getHeight() * ratio);
    shapeRenderer.flush();
  }


  private void drawMasked() {
    /* Enable RGBA color writing. */
    Gdx.gl.glColorMask(true, true, true, true);

    /* Set the depth function to EQUAL. */
    Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    
    /* Render masked elements. */
    shapeRenderer.setColor(UiFactory.HEALTH_COLOR);
    shapeRenderer.circle(getX() + getWidth() / 2, getY() + getHeight() / 2, (getWidth() / 2) - 1);
    shapeRenderer.flush();
  }

  public void dispose() {
    shapeRenderer.dispose();
  }
}
