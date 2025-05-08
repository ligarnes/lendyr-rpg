package net.alteiar.lendyr.game.screen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.entity.CharacterEntity;
import net.alteiar.lendyr.game.Constants;
import net.alteiar.lendyr.game.battlemap.BattleMapContext;
import net.alteiar.lendyr.game.battlemap.actor.TokenCharacter;
import net.alteiar.lendyr.game.battlemap.view.ErrorDialog;
import net.alteiar.lendyr.game.battlemap.view.MapListener;
import net.alteiar.lendyr.game.state.BattleMapUiState;

public class MapListenerImpl implements MapListener {
  private final BattleMapContext battleMapContext;
  private final ErrorDialog errorDialog;
  private final Pool<Vector2> vector2Pool;

  @Builder
  public MapListenerImpl(@NonNull BattleMapContext battleMapContext, @NonNull ErrorDialog errorDialog) {
    this.battleMapContext = battleMapContext;
    this.errorDialog = errorDialog;
    this.vector2Pool = Pools.get(Vector2.class);
  }

  @Override
  public boolean onMapClick(Vector3 newPosition) {
    if (!BattleMapUiState.Action.MOVE.equals(battleMapContext.getUiState().getCurrentAction())) {
      return false;
    }

    Vector2 vector = vector2Pool.obtain();
    try {
      CharacterEntity character = battleMapContext.getCombatEntity().getCurrentCharacter();

      float newXpos = newPosition.x - (character.getWidth() / 2);
      float newYpos = newPosition.y - (character.getHeight() / 2);
      float xPosRounded = Math.round(newXpos * 4f) / 4f;
      float yPosRounded = Math.round(newYpos * 4f) / 4f;
      float xCentered = Math.max(0, Math.min(Constants.WORLD_WIDTH - character.getWidth(), xPosRounded));
      float yCentered = Math.max(0, Math.min(Constants.WORLD_HEIGHT - character.getHeight(), yPosRounded));

      battleMapContext.getGameEngine().move(character, vector.set(xCentered, yCentered));
      battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.IDLE);
    } catch (RuntimeException e) {
      errorDialog.setText(e.getMessage());
      errorDialog.show();
    } finally {
      vector2Pool.free(vector);
    }
    return true;
  }

  @Override
  public void onEscapeTyped() {
    battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.IDLE);
  }

  @Override
  public void onCharacterClick(TokenCharacter character) {
    if (BattleMapUiState.Action.ATTACK.equals(battleMapContext.getUiState().getCurrentAction())) {
      try {
        character.setTargeted(false);
        battleMapContext.getGameEngine().attack(battleMapContext.getCombatEntity().getCurrentCharacter(), character.getCharacterEntity());
        battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.IDLE);
      } catch (RuntimeException e) {
        errorDialog.setText(e.getMessage());
        errorDialog.show();
      }
    }
  }

  @Override
  public void onCharacterEnter(TokenCharacter character) {
    if (BattleMapUiState.Action.ATTACK.equals(battleMapContext.getUiState().getCurrentAction())) {
      character.setTargeted(true);
    }
  }

  @Override
  public void onCharacterExit(TokenCharacter character) {
    if (character.isTargeted()) {
      character.setTargeted(false);
    }
  }
}
