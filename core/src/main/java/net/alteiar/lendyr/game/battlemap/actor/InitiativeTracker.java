package net.alteiar.lendyr.game.battlemap.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import lombok.Builder;
import net.alteiar.lendyr.entity.CharacterEntity;
import net.alteiar.lendyr.game.battlemap.BattleMapContext;
import net.alteiar.lendyr.game.gui.UiFactory;

import java.util.ArrayList;
import java.util.List;

public class InitiativeTracker extends Group {
  public static final int PORTRAIT_WIDTH = 70;
  public static final int PORTRAIT_HEIGHT = 70;
  public static final int SPACING = 5;

  private final List<InitiativePortrait> portaits;

  @Builder
  InitiativeTracker(UiFactory uiFactory, BattleMapContext battleMapContext) {
    List<CharacterEntity> characterEntities = battleMapContext.getCombatEntity().getInitiativeOrder();
    portaits = new ArrayList<>(characterEntities.size());

    for (int i = 0; i < characterEntities.size(); i++) {
      InitiativePortrait initiativePortrait = new InitiativePortrait(uiFactory, characterEntities.get(i), battleMapContext);
      initiativePortrait.setX((PORTRAIT_WIDTH + SPACING) * i);
      initiativePortrait.setY(0);
      initiativePortrait.setWidth(PORTRAIT_WIDTH);
      initiativePortrait.setHeight(PORTRAIT_HEIGHT);

      portaits.add(initiativePortrait);
      this.addActor(initiativePortrait);
    }
  }

  public void dispose() {
    this.portaits.forEach(InitiativePortrait::dispose);
  }

  private static class InitiativePortrait extends Actor {
    private final Texture portrait;
    private final BattleMapContext battleMapContext;
    private final CharacterEntity entity;
    private final Texture overlayGreen;
    private final Texture overlayRed;
    private final ShapeRenderer shapeRenderer;
    private final Color color;
    private float healthOverlayHeight;

    public InitiativePortrait(UiFactory uiFactory, CharacterEntity character, BattleMapContext battleMapContext) {
      portrait = uiFactory.getTexture(character.getPortrait());
      this.entity = character;
      this.battleMapContext = battleMapContext;
      overlayGreen = uiFactory.getTexture("portrait/overlay-green.png");
      overlayRed = uiFactory.getTexture("portrait/overlay-red.png");

      shapeRenderer = new ShapeRenderer();
      this.shapeRenderer.setAutoShapeType(true);

      color = UiFactory.HEALTH_COLOR;
    }

    @Override
    public void act(float delta) {
      float ratio = Math.min(1, 1 - this.entity.getCurrentHp() / (float) this.entity.getMaxHp());
      healthOverlayHeight = getHeight() * ratio;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
      Texture current = overlayRed;
      if (battleMapContext.getCombatEntity().getCurrentCharacter().equals(entity)) {
        current = overlayGreen;
      }

      batch.draw(portrait, getX(), getY(), getWidth(), getHeight());
      batch.end();

      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

      shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
      shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
      shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
      shapeRenderer.setColor(color);
      shapeRenderer.rect(getX(), getY(), getWidth(), healthOverlayHeight);
      shapeRenderer.end();

      batch.begin();
      batch.draw(current, getX(), getY(), getWidth(), getHeight());
    }

    public void dispose() {
      shapeRenderer.dispose();
    }
  }
}
