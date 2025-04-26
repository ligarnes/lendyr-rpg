package net.alteiar.lendyr.game.battlemap.actor.move;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Builder;
import lombok.Setter;
import net.alteiar.lendyr.game.Constants;
import net.alteiar.lendyr.game.battlemap.CursorInfo;
import net.alteiar.lendyr.game.state.model.CharacterEntity;

public class PathActor extends Actor {
  private final CursorInfo cursorInfo;
  private final ShapeRenderer shapeRenderer = new ShapeRenderer();

  @Setter
  private CharacterEntity characterEntity;

  @Builder
  public PathActor(CursorInfo cursorInfo, CharacterEntity characterEntity) {
    this.characterEntity = characterEntity;
    this.cursorInfo = cursorInfo;
    this.setX(0);
    this.setY(0);
    this.setWidth(Constants.WORLD_WIDTH);
    this.setHeight(Constants.WORLD_HEIGHT);
  }


  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.end();
    Vector3 position = cursorInfo.getCursorPosition();
    position.x -= characterEntity.getWidth() / 2;
    position.y -= characterEntity.getHeight() / 2;

    shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
    shapeRenderer.setColor(getColor());
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.rect(position.x, position.y, characterEntity.getWidth(), characterEntity.getHeight());
    shapeRenderer.end();

    shapeRenderer.setColor(getColor());
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.polygon(computePolygon(position));
    shapeRenderer.end();
    batch.begin();
  }

  private float[] computePolygon(Vector3 targetPosition) {
    if (targetPosition.x < characterEntity.getPosition().x) {
      // target is on the left
      if (targetPosition.y > characterEntity.getPosition().y) {
        return computePolygonTopLeftOnly(targetPosition);
      } else if (targetPosition.y < characterEntity.getPosition().y) {
        return computePolygonBottomLeftOnly(targetPosition);
      } else {
        return computePolygonLeftOnly(targetPosition);
      }

    } else if (targetPosition.x > characterEntity.getPosition().x) {
      //  target is on the right
      if (targetPosition.y > characterEntity.getPosition().y) {
        return computePolygonTopRightOnly(targetPosition);
      } else if (targetPosition.y < characterEntity.getPosition().y) {
        return computePolygonBottomRightOnly(targetPosition);
      } else {
        return computePolygonRightOnly(targetPosition);
      }
    } else {
      // Target is aligned on x
      if (targetPosition.y > characterEntity.getPosition().y) {
        return computePolygonTopOnly(targetPosition);
      } else if (targetPosition.y < characterEntity.getPosition().y) {
        return computePolygonBottomOnly(targetPosition);
      }
    }

    return computePolygonTopRightOnly(targetPosition);
  }

  private float[] computePolygonLeftOnly(Vector3 targetPosition) {
    return new float[]{
      characterEntity.getPosition().x, characterEntity.getPosition().y,
      characterEntity.getPosition().x, characterEntity.getPosition().y + characterEntity.getHeight(),
      targetPosition.x + characterEntity.getWidth(), targetPosition.y + characterEntity.getHeight(),
      targetPosition.x + characterEntity.getWidth(), targetPosition.y,
    };
  }

  private float[] computePolygonRightOnly(Vector3 targetPosition) {
    return new float[]{
      characterEntity.getPosition().x + characterEntity.getWidth(), characterEntity.getPosition().y,
      characterEntity.getPosition().x + characterEntity.getWidth(), characterEntity.getPosition().y + characterEntity.getHeight(),
      targetPosition.x, targetPosition.y + characterEntity.getHeight(),
      targetPosition.x, targetPosition.y,
    };
  }

  private float[] computePolygonTopOnly(Vector3 targetPosition) {
    return new float[]{
      characterEntity.getPosition().x, characterEntity.getPosition().y + characterEntity.getHeight(),
      characterEntity.getPosition().x + characterEntity.getWidth(), characterEntity.getPosition().y + characterEntity.getHeight(),
      targetPosition.x + characterEntity.getWidth(), targetPosition.y,
      targetPosition.x, targetPosition.y,
    };
  }

  private float[] computePolygonBottomOnly(Vector3 targetPosition) {
    return new float[]{
      characterEntity.getPosition().x, characterEntity.getPosition().y,
      characterEntity.getPosition().x + characterEntity.getWidth(), characterEntity.getPosition().y,
      targetPosition.x + characterEntity.getWidth(), targetPosition.y + characterEntity.getHeight(),
      targetPosition.x, targetPosition.y + characterEntity.getHeight(),
    };
  }

  private float[] computePolygonTopRightOnly(Vector3 targetPosition) {
    return new float[]{
      characterEntity.getPosition().x, characterEntity.getPosition().y + characterEntity.getHeight(),
      characterEntity.getPosition().x + characterEntity.getWidth(), characterEntity.getPosition().y + characterEntity.getHeight(),
      characterEntity.getPosition().x + characterEntity.getWidth(), characterEntity.getPosition().y,
      targetPosition.x + characterEntity.getWidth(), targetPosition.y,
      targetPosition.x, targetPosition.y,
      targetPosition.x, targetPosition.y + characterEntity.getHeight(),
    };
  }

  private float[] computePolygonTopLeftOnly(Vector3 targetPosition) {
    return new float[]{
      characterEntity.getPosition().x, characterEntity.getPosition().y,
      characterEntity.getPosition().x, characterEntity.getPosition().y + characterEntity.getHeight(),
      characterEntity.getPosition().x + characterEntity.getWidth(), characterEntity.getPosition().y + characterEntity.getHeight(),
      targetPosition.x + characterEntity.getWidth(), targetPosition.y + characterEntity.getHeight(),
      targetPosition.x + characterEntity.getWidth(), targetPosition.y,
      targetPosition.x, targetPosition.y,
    };
  }

  private float[] computePolygonBottomRightOnly(Vector3 targetPosition) {
    return new float[]{
      characterEntity.getPosition().x, characterEntity.getPosition().y,
      characterEntity.getPosition().x + characterEntity.getWidth(), characterEntity.getPosition().y,
      characterEntity.getPosition().x + characterEntity.getWidth(), characterEntity.getPosition().y + characterEntity.getHeight(),
      targetPosition.x + characterEntity.getWidth(), targetPosition.y + characterEntity.getHeight(),
      targetPosition.x, targetPosition.y + characterEntity.getHeight(),
      targetPosition.x, targetPosition.y,
    };
  }

  private float[] computePolygonBottomLeftOnly(Vector3 targetPosition) {
    return new float[]{
      characterEntity.getPosition().x + characterEntity.getWidth(), characterEntity.getPosition().y,
      characterEntity.getPosition().x, characterEntity.getPosition().y,
      characterEntity.getPosition().x, characterEntity.getPosition().y + characterEntity.getHeight(),
      targetPosition.x, targetPosition.y + characterEntity.getHeight(),
      targetPosition.x + characterEntity.getWidth(), targetPosition.y + characterEntity.getHeight(),
      targetPosition.x + characterEntity.getWidth(), targetPosition.y,
    };
  }
}
