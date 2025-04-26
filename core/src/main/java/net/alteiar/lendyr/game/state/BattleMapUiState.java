package net.alteiar.lendyr.game.state;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BattleMapUiState {
  public enum Action {
    IDLE, MOVE, ATTACK
  }

  private Action currentAction;
  private boolean isGridVisible;
}
