package net.alteiar.lendyr.game.battlemap.view;

import com.badlogic.gdx.math.Vector3;
import net.alteiar.lendyr.game.battlemap.actor.TokenCharacter;

public interface MapListener {

  void onCharacterClick(TokenCharacter character);

  void onCharacterEnter(TokenCharacter character);

  void onCharacterExit(TokenCharacter character);

  boolean onMapClick(Vector3 mapPosition);

  void onEscapeTyped();
}
