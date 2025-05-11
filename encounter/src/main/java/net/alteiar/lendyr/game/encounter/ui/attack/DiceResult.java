package net.alteiar.lendyr.game.encounter.ui.attack;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class DiceResult extends Actor {

  @Setter
  private int result;
  private final Texture texture1;
  private final Texture texture2;
  private final Texture texture3;
  private final Texture texture4;
  private final Texture texture5;
  private final Texture texture6;

  @Builder
  DiceResult(@NonNull UiFactory uiFactory) {
    texture1 = uiFactory.getTexture("encounter/dice/dice-six-faces-one.png");
    texture2 = uiFactory.getTexture("encounter/dice/dice-six-faces-two.png");
    texture3 = uiFactory.getTexture("encounter/dice/dice-six-faces-three.png");
    texture4 = uiFactory.getTexture("encounter/dice/dice-six-faces-four.png");
    texture5 = uiFactory.getTexture("encounter/dice/dice-six-faces-five.png");
    texture6 = uiFactory.getTexture("encounter/dice/dice-six-faces-six.png");
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.draw(getDiceTexture(), getX(), getY(), getWidth(), getHeight());
  }

  private Texture getDiceTexture() {
    return switch (result) {
      case 1 -> texture1;
      case 2 -> texture2;
      case 3 -> texture3;
      case 4 -> texture4;
      case 5 -> texture5;
      case 6 -> texture6;
      default -> texture1;
    };
  }
}
