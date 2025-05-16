package net.alteiar.lendyr.game.encounter.ui.element.persona;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Builder;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class RangeAttackRange extends Actor {
  private final PersonaEntity personaEntity;
  private final ShapeRenderer shapeRenderer;

  @Builder
  RangeAttackRange(PersonaEntity personaEntity) {
    shapeRenderer = new ShapeRenderer();
    this.personaEntity = personaEntity;
  }

  public void dispose() {
    shapeRenderer.dispose();
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Vector2 position = personaEntity.getPosition();
    float range = personaEntity.getAttack().getRange();
    float x = position.x + (personaEntity.getWidth() / 2);
    float y = position.y + (personaEntity.getHeight() / 2);

    int segments = 200;

    batch.end();
    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.setColor(UiFactory.DARK_GRAY_OVERLAY_COLOR);
    shapeRenderer.circle(x, y, range, segments);
    shapeRenderer.setColor(UiFactory.GRAY_OVERLAY_COLOR);
    shapeRenderer.circle(x, y, range * 2, segments);
    shapeRenderer.end();

    Gdx.gl.glLineWidth(4);
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.setColor(UiFactory.GREEN_SELECTION_COLOR);
    shapeRenderer.circle(x, y, range, segments);
    shapeRenderer.end();

    Gdx.gl.glLineWidth(4);
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.setColor(UiFactory.WARN_SELECTION_COLOR);
    shapeRenderer.circle(x, y, range * 2, segments);
    shapeRenderer.end();

    batch.begin();
  }
}
