package net.alteiar.lendyr.game.battlemap;

import lombok.Builder;
import lombok.Data;
import net.alteiar.lendyr.game.state.BattleMapUiState;
import net.alteiar.lendyr.game.state.GameEngine;
import net.alteiar.lendyr.game.state.WorldState;

@Data
@Builder
public class BattleMapContext {
  private final BattleMapUiState uiState;
  private final WorldState worldState;
  private final GameEngine gameEngine;
}
