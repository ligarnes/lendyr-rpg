package net.alteiar.lendyr.game.battlemap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;


public class CursorInfo {
  private final Camera camera;
  @Getter
  private final Vector3 cursorPosition = new Vector3();
  private final Vector3 actorPosition = new Vector3();

  @Builder
  public CursorInfo(@NonNull Camera camera) {
    this.camera = camera;
  }

  public void update() {
    camera.unproject(cursorPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0));
  }

  public float distanceToCursor(Actor actor) {
    actorPosition.set(actor.getX(), actor.getY(), 0);
    return cursorPosition.dst(actorPosition);
  }

  public float distanceToCursor(Vector2 actor) {
    actorPosition.set(actor.x, actor.y, 0);
    return cursorPosition.dst(actorPosition);
  }
}
