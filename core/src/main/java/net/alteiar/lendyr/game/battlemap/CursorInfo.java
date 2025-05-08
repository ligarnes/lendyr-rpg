package net.alteiar.lendyr.game.battlemap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;


public class CursorInfo {
  private final Camera camera;
  @Getter
  private final Vector3 cursorPosition = new Vector3();

  @Builder
  public CursorInfo(@NonNull Camera camera) {
    this.camera = camera;
  }

  public void update() {
    camera.unproject(cursorPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0));
  }
}
