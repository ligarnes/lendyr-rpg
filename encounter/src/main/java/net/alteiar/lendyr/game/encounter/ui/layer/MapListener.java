package net.alteiar.lendyr.game.encounter.ui.layer;

import com.badlogic.gdx.math.Vector3;
import net.alteiar.lendyr.game.encounter.ui.component.FullPersonaToken;

public interface MapListener {

  void onCharacterClick(FullPersonaToken character);

  void onCharacterEnter(FullPersonaToken character);

  void onCharacterExit(FullPersonaToken character);

  boolean onMapClick(Vector3 mapPosition);

  void onEscapeTyped();
}
