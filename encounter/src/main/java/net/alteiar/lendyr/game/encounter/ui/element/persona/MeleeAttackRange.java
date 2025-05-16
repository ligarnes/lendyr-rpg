package net.alteiar.lendyr.game.encounter.ui.element.persona;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class MeleeAttackRange extends Actor {
  private final PersonaEntity personaEntity;

  private final Texture rangeTexture;

  @Builder
  MeleeAttackRange(@NonNull UiFactory uiFactory, @NonNull PersonaEntity personaEntity) {
    this.personaEntity = personaEntity;
    rangeTexture = uiFactory.getTexture("encounter/melee-range-overlay.png");
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Vector2 position = personaEntity.getPosition();
    float range = personaEntity.getAttack().getRange();

    for (int i = 1; i <= range; i++) {
      for (int j = 1; j <= range; j++) {
        batch.draw(rangeTexture, position.x + i, position.y + j, 1, 1);
        batch.draw(rangeTexture, position.x + i, position.y - j, 1, 1);
        batch.draw(rangeTexture, position.x - i, position.y + j, 1, 1);
        batch.draw(rangeTexture, position.x - i, position.y - j, 1, 1);
      }
      batch.draw(rangeTexture, position.x + i, position.y, 1, 1);
      batch.draw(rangeTexture, position.x - i, position.y, 1, 1);
      batch.draw(rangeTexture, position.x, position.y + i, 1, 1);
      batch.draw(rangeTexture, position.x, position.y - i, 1, 1);
    }
  }
}
