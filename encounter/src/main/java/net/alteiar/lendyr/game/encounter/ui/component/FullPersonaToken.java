package net.alteiar.lendyr.game.encounter.ui.component;

import com.badlogic.gdx.scenes.scene2d.Group;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import net.alteiar.lendyr.entity.CharacterEntity;
import net.alteiar.lendyr.game.encounter.ui.element.persona.MeleeAttackRange;
import net.alteiar.lendyr.game.encounter.ui.element.persona.RangeAttackRange;
import net.alteiar.lendyr.game.encounter.ui.element.persona.TokenCharacter;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class FullPersonaToken extends Group {
  @Getter
  private final CharacterEntity characterEntity;
  private final TokenCharacter tokenCharacter;
  private final RangeAttackRange rangeAttackRange;
  private final MeleeAttackRange meleeAttackRange;

  @Builder
  public FullPersonaToken(@NonNull CharacterEntity characterEntity, @NonNull UiFactory uiFactory) {
    this.characterEntity = characterEntity;

    tokenCharacter = TokenCharacter.builder().uiFactory(uiFactory).characterEntity(characterEntity).build();
    rangeAttackRange = RangeAttackRange.builder().characterEntity(characterEntity).build();
    meleeAttackRange = MeleeAttackRange.builder().uiFactory(uiFactory).characterEntity(characterEntity).build();
    hideAttackRange();

    this.addActor(rangeAttackRange);
    this.addActor(meleeAttackRange);
    this.addActor(tokenCharacter);
  }

  public void setTargeted(boolean isTargeted) {
    tokenCharacter.setTargeted(isTargeted);
  }

  public boolean isTargeted() {
    return tokenCharacter.isTargeted();
  }

  public void showAttackRange() {
    switch (characterEntity.getAttack().getAttackType()) {
      case MELEE -> meleeAttackRange.setVisible(true);
      case RANGE, MAGIC -> rangeAttackRange.setVisible(true);
    }
  }

  public void hideAttackRange() {
    meleeAttackRange.setVisible(false);
    rangeAttackRange.setVisible(false);
  }

  public void dispose() {
    rangeAttackRange.dispose();
  }
}
