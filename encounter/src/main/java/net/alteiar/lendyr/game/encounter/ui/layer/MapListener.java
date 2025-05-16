package net.alteiar.lendyr.game.encounter.ui.layer;

import com.badlogic.gdx.math.Vector3;

public interface MapListener {

  boolean onMapClick(Vector3 mapPosition);

  void onEscapeTyped();
}
