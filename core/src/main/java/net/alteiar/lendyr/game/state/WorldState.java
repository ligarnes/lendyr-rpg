package net.alteiar.lendyr.game.state;

import lombok.Builder;
import lombok.Data;
import net.alteiar.lendyr.game.state.model.CharacterEntity;

import java.util.List;

@Data
@Builder
public class WorldState {
  private int currentTurn;
  private int currentCharacterIdx;
  private List<CharacterEntity> characterEntities;

  public CharacterEntity getCurrentCharacter() {
    return characterEntities.get(currentCharacterIdx);
  }

}
