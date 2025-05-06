package net.alteiar.lendyr.game.battlemap.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.game.battlemap.BattleMapContext;
import net.alteiar.lendyr.game.battlemap.actor.InitiativeTracker;
import net.alteiar.lendyr.game.gui.UiFactory;

public class MapOverlay extends ViewLayer {
  public static final int TRACKER_TOP_SPACING = InitiativeTracker.PORTRAIT_HEIGHT + 10;

  private final InitiativeTracker initiativeTracker;
  private final int trackerWidth;

  @Builder
  public MapOverlay(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext) {
    super(new ScreenViewport());

    trackerWidth = (InitiativeTracker.PORTRAIT_WIDTH + InitiativeTracker.SPACING) * battleMapContext.getCombatEntity().getInitiativeOrder().size();

    initiativeTracker = InitiativeTracker.builder().battleMapContext(battleMapContext).uiFactory(uiFactory).build();
    initiativeTracker.setPosition(Gdx.graphics.getWidth() / 2f - trackerWidth, Gdx.graphics.getHeight() - TRACKER_TOP_SPACING);

    stage.addActor(initiativeTracker);
  }

  @Override
  public void dispose() {
    initiativeTracker.dispose();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    initiativeTracker.setPosition(width / 2f - trackerWidth, height - TRACKER_TOP_SPACING);
  }
}
