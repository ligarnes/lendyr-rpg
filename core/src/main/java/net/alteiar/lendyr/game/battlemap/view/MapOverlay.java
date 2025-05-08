package net.alteiar.lendyr.game.battlemap.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.game.battlemap.AttackLayer;
import net.alteiar.lendyr.game.battlemap.BattleMapContext;
import net.alteiar.lendyr.game.battlemap.InitiativeTrackerLayer;
import net.alteiar.lendyr.game.gui.UiFactory;

public class MapOverlay extends ViewLayer {

  private final InitiativeTrackerLayer initiativeTracker;
  private final AttackLayer attackLayer;

  @Builder
  public MapOverlay(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext) {
    super(new ScreenViewport());

    initiativeTracker = InitiativeTrackerLayer.builder().battleMapContext(battleMapContext).uiFactory(uiFactory).build();
    attackLayer = AttackLayer.builder().uiFactory(uiFactory).battleMapContext(battleMapContext).build();

    attackLayer.setX(Gdx.graphics.getWidth() / 2f - attackLayer.getWidth() / 2f);
    attackLayer.setY(Gdx.graphics.getHeight() / 2f + attackLayer.getHeight());

    stage.addActor(initiativeTracker);
    stage.addActor(attackLayer);
  }

  @Override
  public void dispose() {
    initiativeTracker.dispose();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);

    attackLayer.setX(Gdx.graphics.getWidth() / 2f - attackLayer.getWidth() / 2f);
    attackLayer.setY(Gdx.graphics.getHeight() / 2f - attackLayer.getHeight() / 2f);

    initiativeTracker.resize(width, height);
  }
}
