package net.alteiar.lendyr.game.encounter.ui.attack;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class Defense extends Group {
  private final Label label;

  @Builder
  Defense(@NonNull UiFactory uiFactory) {
    DefenseBackgound defenseBackgound = new DefenseBackgound(uiFactory);

    label = uiFactory.createLabel();
    label.setFontScale(1f);
    label.setAlignment(Align.center);
    label.setX(0);
    label.setY(defenseBackgound.getHeight() / 2f - label.getHeight() / 2f);
    label.setWidth(defenseBackgound.getWidth());

    this.addActor(defenseBackgound);
    this.addActor(label);

    this.setWidth(defenseBackgound.getWidth());
    this.setHeight(defenseBackgound.getHeight());
  }

  public void setDefense(int defense) {
    label.setText(Integer.toString(defense));
  }

  private static class DefenseBackgound extends Actor {
    private final Texture shield;

    DefenseBackgound(@NonNull UiFactory uiFactory) {
      shield = uiFactory.getTexture("encounter/shield.png");
      this.setWidth(120);
      this.setHeight(120);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
      batch.draw(shield, getX(), getY(), getWidth(), getHeight());
    }
  }
}
