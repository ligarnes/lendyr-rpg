package net.alteiar.lendyr.game.battlemap.actor.action;

import com.badlogic.gdx.scenes.scene2d.Action;
import lombok.Builder;
import net.alteiar.lendyr.game.battlemap.actor.TokenCharacter;

public class DamageAction extends Action {

  private static final float ANIMATION_DURATION = 1.5f;

  private final int damage;
  private float remainingDuration;

  @Builder
  public DamageAction(int damage) {
    this.damage = damage;
    this.remainingDuration = ANIMATION_DURATION;
  }

  @Override
  public boolean act(float delta) {
    remainingDuration -= delta;

    if (actor instanceof TokenCharacter tokenCharacter) {
      tokenCharacter.showText("" + damage);
      if (remainingDuration <= 0) {
        tokenCharacter.hideText();
      }
    }

    return remainingDuration <= 0;
  }
}
