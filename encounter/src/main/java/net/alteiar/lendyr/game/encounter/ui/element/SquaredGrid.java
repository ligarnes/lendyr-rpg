package net.alteiar.lendyr.game.encounter.ui.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.NonNull;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;

public class SquaredGrid extends Actor {
  private final BattleMapContext battleMapContext;
  private final ShapeRenderer shapeRenderer;

  public SquaredGrid(@NonNull BattleMapContext battleMapContext) {
    this.battleMapContext = battleMapContext;
    shapeRenderer = new ShapeRenderer();
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.end();
    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    // Draw grid
    shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.setColor(1, 0, 0, 0.2f);

    int squareWidthCount = (int) battleMapContext.getCombatEntity().getWorldWidth();
    int squareHeightCount = (int) battleMapContext.getCombatEntity().getWorldHeight();
    for (int x = 0; x < squareWidthCount; x++) {
      for (int y = 0; y < squareHeightCount; y++) {
        shapeRenderer.rect(x, y, 1, 1);
      }
    }
    shapeRenderer.end();
    batch.begin();
  }

  public void dispose() {
    shapeRenderer.dispose();
  }
}
