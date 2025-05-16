package net.alteiar.lendyr.game.encounter.ui.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pools;
import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PathActor extends Actor {
  private final Texture textureOk;
  private final Texture textureNok;
  private final BitmapFont font;
  private final GlyphLayout glyphLayout;

  @Setter
  private PersonaEntity personaEntity;

  @Builder
  public PathActor(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext, PersonaEntity personaEntity) {
    this.personaEntity = personaEntity;

    font = uiFactory.getFont();
    textureOk = uiFactory.getTexture("encounter/move-overlay-ok.png");
    textureNok = uiFactory.getTexture("encounter/move-overlay-nok.png");

    glyphLayout = new GlyphLayout();

    this.setX(0);
    this.setY(0);
    this.setWidth(battleMapContext.getCombatEntity().getWorldWidth());
    this.setHeight(battleMapContext.getCombatEntity().getWorldHeight());
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Vector3 position = Pools.get(Vector3.class).obtain();
    position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
    this.getStage().getCamera().unproject(position);
    position.x = roundPosition(position.x - personaEntity.getWidth() / 2);
    position.y = roundPosition(position.y - personaEntity.getHeight() / 2);

    List<Vector2> path = computePath(personaEntity.getPosition(), Pools.obtain(Vector2.class).set(position.x, position.y));
    Pools.free(position);

    float speed = personaEntity.getSpeed() / 2f;
    //float centerY = (characterEntity.getHeight() / 2f) + 0.1f;

    //font.getData().setScale(0.015f);
    font.setUseIntegerPositions(false);
    font.getData().setScale(0.02f);
    font.setColor(Color.BLACK);
    for (Vector2 pathPoint : path) {
      float dist = personaEntity.getPosition().dst(pathPoint);
      String text = dist >= 10 ? String.format("%.0f", dist) : String.format("%.1f", dist);
      glyphLayout.setText(font, text);
      float marginX = (1 - glyphLayout.width) / 2f;
      float marginY = (1 + glyphLayout.height) / 2f;

      if (dist <= speed) {
        batch.draw(textureOk, pathPoint.x, pathPoint.y, 1, 1);
        font.draw(batch, text, pathPoint.x + marginX, pathPoint.y + marginY);
      } else {
        batch.draw(textureNok, pathPoint.x, pathPoint.y, 1, 1);
        font.draw(batch, text, pathPoint.x + marginX, pathPoint.y + marginY);
      }
      Pools.free(pathPoint);
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
        return Pools.get(Vector2.class).obtain().set(nextX, nextY);
      } else if (current.y < target.y) {
        float nextX = current.x - Math.min(1, current.x - target.x);
        float nextY = current.y + Math.min(1, target.y - current.y);
        return Pools.get(Vector2.class).obtain().set(nextX, nextY);
      } else {
        float nextX = current.x - Math.min(1, current.x - target.x);
        return Pools.get(Vector2.class).obtain().set(nextX, current.y);
      }
    } else if (current.x < target.x) {
      if (current.y > target.y) {
        float nextX = current.x + Math.min(1, target.x - current.x);
        float nextY = current.y - Math.min(1, current.y - target.y);
        return Pools.get(Vector2.class).obtain().set(nextX, nextY);
      } else if (current.y < target.y) {
        float nextX = current.x + Math.min(1, target.x - current.x);
        float nextY = current.y + Math.min(1, target.y - current.y);
        return Pools.get(Vector2.class).obtain().set(nextX, nextY);
      } else {
        float nextX = current.x + Math.min(1, target.x - current.x);
        return Pools.get(Vector2.class).obtain().set(nextX, current.y);
      }
    } else {
      if (current.y > target.y) {
        float nextY = current.y - Math.min(1, current.y - target.y);
        return Pools.get(Vector2.class).obtain().set(current.x, nextY);
      } else if (current.y < target.y) {
        float nextY = current.y + Math.min(1, target.y - current.y);
        return Pools.get(Vector2.class).obtain().set(current.x, nextY);
      }
    }

    return target;
  }

  private float roundPosition(float position) {
    return Math.round(position * 4f) / 4f;
  }
}
