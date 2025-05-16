package net.alteiar.lendyr.game.encounter.ui.layer;

import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.game.encounter.ui.component.InitiativeTrackerComponent;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.view.ViewLayer;

public class MapOverlay extends ViewLayer {

  private final InitiativeTrackerComponent initiativeTracker;

  @Builder
  public MapOverlay(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext) {
    super(new ScreenViewport());
    initiativeTracker = InitiativeTrackerComponent.builder().battleMapContext(battleMapContext).uiFactory(uiFactory).build();
    stage.addActor(initiativeTracker);
  }

  @Override
  public void dispose() {
    initiativeTracker.dispose();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
    initiativeTracker.resize(width - 300, height);
  }
}
