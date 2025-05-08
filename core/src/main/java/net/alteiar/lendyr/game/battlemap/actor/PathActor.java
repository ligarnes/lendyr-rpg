package net.alteiar.lendyr.game.battlemap.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;
import net.alteiar.lendyr.entity.CharacterEntity;
import net.alteiar.lendyr.game.Constants;
import net.alteiar.lendyr.game.battlemap.CursorInfo;
import net.alteiar.lendyr.game.gui.UiFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PathActor extends Actor {
  private final CursorInfo cursorInfo;
  private final Texture textureOk;
  private final Texture textureNok;

  private final BitmapFont font;

  @Setter
  private CharacterEntity characterEntity;

  @Builder
  public PathActor(CursorInfo cursorInfo, @NonNull UiFactory uiFactory, CharacterEntity characterEntity) {
    this.characterEntity = characterEntity;
    this.cursorInfo = cursorInfo;

    font = uiFactory.getFont();
    textureOk = uiFactory.getTexture("encounter/move-overlay-ok.png");
    textureNok = uiFactory.getTexture("encounter/move-overlay-nok.png");

    this.setX(0);
    this.setY(0);
    this.setWidth(Constants.WORLD_WIDTH);
    this.setHeight(Constants.WORLD_HEIGHT);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Vector3 position = cursorInfo.getCursorPosition();
    position.x = roundPosition(position.x - characterEntity.getWidth() / 2);
    position.y = roundPosition(position.y - characterEntity.getHeight() / 2);

    List<Vector2> path = computePath(characterEntity.getPosition(), new Vector2(position.x, position.y));

    float speed = characterEntity.getSpeed() / 2f;
    float centerY = (characterEntity.getHeight() / 2f) + 0.1f;

    font.getData().setScale(0.015f);
    font.setColor(Color.BLACK);

    for (Vector2 pathPoint : path) {
      float dist = characterEntity.getPosition().dst(pathPoint);
      String text = dist >= 10 ? String.format("%.0f", dist) : String.format("%.1f", dist);
      if (dist <= speed) {
        batch.draw(textureOk, pathPoint.x, pathPoint.y, 1, 1);
        font.draw(batch, text, pathPoint.x + 0.2f, pathPoint.y + centerY);
      } else {
        batch.draw(textureNok, pathPoint.x, pathPoint.y, 1, 1);
        font.draw(batch, text, pathPoint.x + 0.2f, pathPoint.y + centerY);
      }
    }
    font.getData().setScale(1f);
  }

  public List<Vector2> computePath(Vector2 start, Vector2 end) {
    Vector2 currentPoint = start;
    List<Vector2> path = new ArrayList<>();

    while (!Objects.equals(currentPoint, end)) {
      currentPoint = nextPosition(currentPoint, end);
      path.add(currentPoint);
    }
    return path;
  }

  private Vector2 nextPosition(Vector2 current, Vector2 target) {
    if (current.x > target.x) {
      if (current.y > target.y) {
        float nextX = current.x - Math.min(1, current.x - target.x);
        float nextY = current.y - Math.min(1, current.y - target.y);
        return new Vector2(nextX, nextY);
      } else if (current.y < target.y) {
        float nextX = current.x - Math.min(1, current.x - target.x);
        float nextY = current.y + Math.min(1, target.y - current.y);
        return new Vector2(nextX, nextY);
      } else {
        float nextX = current.x - Math.min(1, current.x - target.x);
        return new Vector2(nextX, current.y);
      }
    } else if (current.x < target.x) {
      if (current.y > target.y) {
        float nextX = current.x + Math.min(1, target.x - current.x);
        float nextY = current.y - Math.min(1, current.y - target.y);
        return new Vector2(nextX, nextY);
      } else if (current.y < target.y) {
        float nextX = current.x + Math.min(1, target.x - current.x);
        float nextY = current.y + Math.min(1, target.y - current.y);
        return new Vector2(nextX, nextY);
      } else {
        float nextX = current.x + Math.min(1, target.x - current.x);
        return new Vector2(nextX, current.y);
      }
    } else {
      if (current.y > target.y) {
        float nextY = current.y - Math.min(1, current.y - target.y);
        return new Vector2(current.x, nextY);
      } else if (current.y < target.y) {
        float nextY = current.y + Math.min(1, target.y - current.y);
        return new Vector2(current.x, nextY);
      }
    }

    return target;
  }

  private float roundPosition(float position) {
    return Math.round(position * 4f) / 4f;
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
