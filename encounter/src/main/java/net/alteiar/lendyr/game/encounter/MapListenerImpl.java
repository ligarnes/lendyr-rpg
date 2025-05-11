package net.alteiar.lendyr.game.encounter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pools;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.entity.CharacterEntity;
import net.alteiar.lendyr.entity.notification.NotificationMessageFactory;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.game.encounter.controller.BattleMapUiState;
import net.alteiar.lendyr.game.encounter.ui.component.FullPersonaToken;
import net.alteiar.lendyr.game.encounter.ui.layer.MapListener;

public class MapListenerImpl implements MapListener {
  private final BattleMapContext battleMapContext;

  @Builder
  public MapListenerImpl(@NonNull BattleMapContext battleMapContext) {
    this.battleMapContext = battleMapContext;
  }

  @Override
  public boolean onMapClick(Vector3 newPosition) {
    if (!BattleMapUiState.Action.MOVE.equals(battleMapContext.getUiState().getCurrentAction())) {
      return false;
    }

    Vector2 vector = Pools.obtain(Vector2.class);
    try {
      CharacterEntity character = battleMapContext.getCombatEntity().getCurrentCharacter();

      float worldWidth = battleMapContext.getCombatEntity().getWorldWidth();
      float worldHeigth = battleMapContext.getCombatEntity().getWorldHeight();

      float newXpos = newPosition.x - (character.getWidth() / 2);
      float newYpos = newPosition.y - (character.getHeight() / 2);
      float xPosRounded = Math.round(newXpos * 4f) / 4f;
      float yPosRounded = Math.round(newYpos * 4f) / 4f;
      float xCentered = Math.max(0, Math.min(worldWidth - character.getWidth(), xPosRounded));
      float yCentered = Math.max(0, Math.min(worldHeigth - character.getHeight(), yPosRounded));

      battleMapContext.getGameEngine().move(character, vector.set(xCentered, yCentered));
      battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.IDLE);
    } catch (RuntimeException e) {
      battleMapContext.getGameEngine().getEncounterController().pushNotification(NotificationMessageFactory.error(e.getMessage()));
    } finally {
      Pools.free(vector);
    }
    return true;
  }

  @Override
  public void onEscapeTyped() {
    battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.IDLE);
  }

  @Override
  public void onCharacterClick(FullPersonaToken character) {
    if (BattleMapUiState.Action.ATTACK.equals(battleMapContext.getUiState().getCurrentAction())) {
      try {
        character.setTargeted(false);
        battleMapContext.getGameEngine().attack(battleMapContext.getCombatEntity().getCurrentCharacter(), character.getCharacterEntity());
        battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.IDLE);
      } catch (RuntimeException e) {
        battleMapContext.getGameEngine().getEncounterController().pushNotification(NotificationMessageFactory.error(e.getMessage()));
      }
    }
  }

  @Override
  public void onCharacterEnter(FullPersonaToken character) {
    if (BattleMapUiState.Action.ATTACK.equals(battleMapContext.getUiState().getCurrentAction())) {
      character.setTargeted(true);
    }
    character.showAttackRange();
  }

  @Override
  public void onCharacterExit(FullPersonaToken character) {
    if (character.isTargeted()) {
      character.setTargeted(false);
    }
    character.hideAttackRange();
  }
}
