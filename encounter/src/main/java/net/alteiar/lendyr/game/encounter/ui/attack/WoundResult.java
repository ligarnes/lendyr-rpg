package net.alteiar.lendyr.game.encounter.ui.attack;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class WoundResult extends Group {
  private static final int SPACING = 5;

  private final BlackBackground background;
  private final WoundIcon wound;
  private final Label damageLabel;

  @Builder
  WoundResult(@NonNull UiFactory uiFactory) {
    background = new BlackBackground(uiFactory);
    wound = new WoundIcon(uiFactory);
    damageLabel = uiFactory.createLabel();
    damageLabel.setText("22");
    damageLabel.setAlignment(Align.left);
    damageLabel.setY(25);

    this.addActor(background);
    this.addActor(wound);
    this.addActor(damageLabel);
    this.pack();
  }

  private void pack() {
    GlyphLayout layout = damageLabel.getGlyphLayout();
    layout.setText(damageLabel.getStyle().font, damageLabel.getText());
    float labelWidth = layout.width;
    float totalItemWidth = wound.getWidth() + labelWidth + (SPACING * 3);

    float startX = (getWidth() / 2f) - (totalItemWidth / 2f);
    float labelX = startX + wound.getWidth() + (SPACING);

    background.setX(startX);
    background.setWidth(totalItemWidth);
    background.setHeight(50);

    wound.setX(startX);
    damageLabel.setX(labelX);
  }

  @Override
  public void setWidth(float width) {
    super.setWidth(width);
    pack();
  }

  public void setDamage(int damage) {
    this.damageLabel.setText(damage + "");
    pack();
  }

  private static class BlackBackground extends Actor {
    private final Texture background;

    public BlackBackground(UiFactory uiFactory) {
      background = uiFactory.getTexture("black.png");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
      batch.draw(background, getX(), getY(), getWidth(), getHeight());
    }
  }

  private static class WoundIcon extends Actor {
    private final Texture wound;

    WoundIcon(@NonNull UiFactory uiFactory) {
      wound = uiFactory.getTexture("encounter/hurt.png");
      this.setWidth(50);
      this.setHeight(50);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
      batch.draw(wound, getX(), getY(), getWidth(), getHeight());
    }
  }
}
