package net.alteiar.lendyr.game.state;

import com.badlogic.gdx.math.Vector2;
import net.alteiar.lendyr.game.state.model.CharacterEntity;

import java.util.List;

public class WorldFactory {

  public static WorldState createSimpleWorld() {
    CharacterEntity ulfrik = CharacterEntity.builder()
      .name("Ulfrik")
      .token("token/ulfrik-token.png")
      .maxHp(40)
      .currentHp(20)
      .position(new Vector2(10f, 10f))
      .speed(7)
      .width(1).height(1)
      .build();

    CharacterEntity cyrilla = CharacterEntity.builder()
      .name("Cyrilla")
      .token("token/cyrilla-token.png")
      .maxHp(40)
      .currentHp(36)
      .position(new Vector2(20f, 20f))
      .speed(9)
      .width(1).height(1)
      .build();

    return WorldState.builder()
      .currentCharacterIdx(0)
      .currentTurn(1)
      .characterEntities(List.of(ulfrik, cyrilla))
      .build();
  }
}
