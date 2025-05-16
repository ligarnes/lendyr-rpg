package net.alteiar.lendyr.game.encounter.controller;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BattleMapUiState {
  public enum Action {
    IDLE, MOVE, MELEE_ATTACK, RANGE_ATTACK
  }

  private Action currentAction;
  private boolean isGridVisible;
}
