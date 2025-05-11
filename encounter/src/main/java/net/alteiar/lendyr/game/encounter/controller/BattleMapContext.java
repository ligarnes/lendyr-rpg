package net.alteiar.lendyr.game.encounter.controller;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import net.alteiar.lendyr.entity.EncounterEntity;
import net.alteiar.lendyr.game.encounter.GameEngine;

@Data
@Builder
public class BattleMapContext {
  private final BattleMapUiState uiState;
  @Getter
  private final GameEngine gameEngine;

  public EncounterEntity getCombatEntity() {
    return gameEngine.getEncounterEntity();
  }
}
