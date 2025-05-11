package net.alteiar.lendyr.game.encounter.ui.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.game.encounter.ui.component.AttackDialog;
import net.alteiar.lendyr.game.encounter.ui.component.InitiativeTrackerLayer;
import net.alteiar.lendyr.ui.shared.ViewLayer;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class MapOverlay extends ViewLayer {

  private final InitiativeTrackerLayer initiativeTracker;
  private final AttackDialog attackDialog;

  @Builder
  public MapOverlay(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext) {
    super(new ScreenViewport());

    initiativeTracker = InitiativeTrackerLayer.builder().battleMapContext(battleMapContext).uiFactory(uiFactory).build();
    attackDialog = AttackDialog.builder().uiFactory(uiFactory).battleMapContext(battleMapContext).build();

    attackDialog.setX(Gdx.graphics.getWidth() / 2f - attackDialog.getWidth() / 2f);
    attackDialog.setY(Gdx.graphics.getHeight() / 2f + attackDialog.getHeight());

    stage.addActor(initiativeTracker);
    stage.addActor(attackDialog);
  }

  @Override
  public void dispose() {
    initiativeTracker.dispose();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);

    attackDialog.setX(Gdx.graphics.getWidth() / 2f - attackDialog.getWidth() / 2f);
    attackDialog.setY(Gdx.graphics.getHeight() / 2f - attackDialog.getHeight() / 2f);

    initiativeTracker.resize(width - 300, height);
  }
}
