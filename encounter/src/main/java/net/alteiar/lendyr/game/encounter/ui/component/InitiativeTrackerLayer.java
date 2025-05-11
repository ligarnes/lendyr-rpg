package net.alteiar.lendyr.game.encounter.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.game.encounter.ui.element.InitiativeTracker;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class InitiativeTrackerLayer extends Group {
  public static final int TRACKER_TOP_SPACING = InitiativeTracker.PORTRAIT_HEIGHT + 10;

  private final InitiativeTracker initiativeTracker;

  @Builder
  InitiativeTrackerLayer(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext) {
    initiativeTracker = InitiativeTracker.builder().battleMapContext(battleMapContext).uiFactory(uiFactory).build();
    initiativeTracker.setPosition(0, 0);

    this.setPosition(computeX(Gdx.graphics.getWidth()), computeY(Gdx.graphics.getHeight()));
    this.setWidth(initiativeTracker.getWidth());
    this.setHeight(initiativeTracker.getHeight());
    
    this.addActor(initiativeTracker);
  }

  private float computeX(int width) {
    return Math.max(310f, 300 + ((width - (300 + initiativeTracker.getWidth())) / 2f));
  }

  private float computeY(int height) {
    return height - TRACKER_TOP_SPACING;
  }

  public void dispose() {
    initiativeTracker.dispose();
  }

  public void resize(int width, int height) {
    this.setX(computeX(width));
    this.setY(computeY(height));
  }
}
