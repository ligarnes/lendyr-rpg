package net.alteiar.lendyr.game.battlemap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.game.battlemap.actor.InitiativeTracker;
import net.alteiar.lendyr.game.gui.UiFactory;

public class InitiativeTrackerLayer extends Group {
  public static final int TRACKER_TOP_SPACING = InitiativeTracker.PORTRAIT_HEIGHT + 10;

  private final net.alteiar.lendyr.game.battlemap.actor.InitiativeTracker initiativeTracker;
  private final int trackerWidth;

  @Builder
  InitiativeTrackerLayer(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext) {
    trackerWidth = (net.alteiar.lendyr.game.battlemap.actor.InitiativeTracker.PORTRAIT_WIDTH + net.alteiar.lendyr.game.battlemap.actor.InitiativeTracker.SPACING) * battleMapContext.getCombatEntity().getInitiativeOrder().size();

    initiativeTracker = net.alteiar.lendyr.game.battlemap.actor.InitiativeTracker.builder().battleMapContext(battleMapContext).uiFactory(uiFactory).build();
    initiativeTracker.setPosition(Gdx.graphics.getWidth() / 2f - trackerWidth, Gdx.graphics.getHeight() - TRACKER_TOP_SPACING);
    this.setPosition(0, 0);
    this.setWidth(Gdx.graphics.getWidth());
    this.setHeight(Gdx.graphics.getHeight());
    this.addActor(initiativeTracker);
  }

  public void dispose() {
    initiativeTracker.dispose();
  }

  public void resize(int width, int height) {
    this.setWidth(width);
    this.setHeight(height);
    initiativeTracker.setPosition(width / 2f - trackerWidth, height - TRACKER_TOP_SPACING);
  }
}
