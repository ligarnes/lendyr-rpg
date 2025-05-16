package net.alteiar.lendyr.game.encounter.ui.component;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.game.encounter.controller.BattleMapUiState;
import net.alteiar.lendyr.game.encounter.ui.element.persona.MeleeAttackRange;
import net.alteiar.lendyr.game.encounter.ui.element.persona.RangeAttackRange;
import net.alteiar.lendyr.game.encounter.ui.element.persona.TokenCharacter;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

import java.util.Objects;

public class FullPersonaTokenComponent extends Group {
  private final BattleMapContext battleMapContext;
  @Getter
  private final PersonaEntity personaEntity;
  private final TokenCharacter tokenCharacter;
  private final RangeAttackRange rangeAttackRange;
  private final MeleeAttackRange meleeAttackRange;

  @Builder
  public FullPersonaTokenComponent(@NonNull BattleMapContext battleMapContext, @NonNull PersonaEntity personaEntity, @NonNull UiFactory uiFactory) {
    this.battleMapContext = battleMapContext;
    this.personaEntity = personaEntity;

    tokenCharacter = TokenCharacter.builder().uiFactory(uiFactory).personaEntity(personaEntity).build();
    rangeAttackRange = RangeAttackRange.builder().personaEntity(personaEntity).build();
    meleeAttackRange = MeleeAttackRange.builder().uiFactory(uiFactory).personaEntity(personaEntity).build();
    hideAttackRange();

    this.addActor(rangeAttackRange);
    this.addActor(meleeAttackRange);
    this.addActor(tokenCharacter);

    this.addListener(new PersonaListener());
  }

  public void setTargeted(boolean isTargeted) {
    tokenCharacter.setTargeted(isTargeted);
  }

  public boolean isTargeted() {
    return tokenCharacter.isTargeted();
  }

  public void showAttackRange() {
    switch (personaEntity.getAttack().getAttackType()) {
      case MELEE -> meleeAttackRange.setVisible(true);
      case RANGE, MAGIC -> rangeAttackRange.setVisible(true);
    }
  }

  public void hideAttackRange() {
    meleeAttackRange.setVisible(false);
    rangeAttackRange.setVisible(false);
  }

  @Override
  public void act(float delta) {
    boolean isActive = Objects.equals(personaEntity.getId(), this.battleMapContext.getCombatEntity().getCurrentCharacter().getId());

    boolean isAttacking = Objects.equals(battleMapContext.getUiState().getCurrentAction(), BattleMapUiState.Action.MELEE_ATTACK) ||
      Objects.equals(battleMapContext.getUiState().getCurrentAction(), BattleMapUiState.Action.RANGE_ATTACK);

    if (isActive && isAttacking) {
      showAttackRange();
    } else if (!isTargeted()) {
      hideAttackRange();
    }

    super.act(delta);
  }

  public void dispose() {
    rangeAttackRange.dispose();
  }

  private class PersonaListener extends ClickListener {

    @Override
    public void clicked(InputEvent event, float x, float y) {
      if (BattleMapUiState.Action.MELEE_ATTACK.equals(battleMapContext.getUiState().getCurrentAction())
        || BattleMapUiState.Action.RANGE_ATTACK.equals(battleMapContext.getUiState().getCurrentAction())) {
        setTargeted(false);
        battleMapContext.getGameEngine().submitAttack(battleMapContext.getCombatEntity().getCurrentCharacter(), getPersonaEntity());
        battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.IDLE);
      }
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
      if (BattleMapUiState.Action.MELEE_ATTACK.equals(battleMapContext.getUiState().getCurrentAction())
        || BattleMapUiState.Action.RANGE_ATTACK.equals(battleMapContext.getUiState().getCurrentAction())) {
        setTargeted(true);
      } else {
        showAttackRange();
      }
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
      if (isTargeted()) {
        setTargeted(false);
      }
      hideAttackRange();
    }
  }
}
