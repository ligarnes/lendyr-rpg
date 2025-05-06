package net.alteiar.lendyr.game.battlemap;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import net.alteiar.lendyr.entity.CombatEntity;
import net.alteiar.lendyr.game.state.BattleMapUiState;
import net.alteiar.lendyr.game.state.GameEngine;

@Data
@Builder
public class BattleMapContext {
  private final BattleMapUiState uiState;
  @Getter
  private final GameEngine gameEngine;

  public CombatEntity getCombatEntity() {
    return gameEngine.getCombatEntity();
  }
}
