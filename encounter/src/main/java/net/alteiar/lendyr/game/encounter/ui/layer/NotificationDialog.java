package net.alteiar.lendyr.game.encounter.ui.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.game.encounter.ui.component.OkDialog;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.view.ViewLayer;

public class NotificationDialog extends ViewLayer {
  private final OkDialog dialog;
  private final BattleMapContext battleMapContext;

  @Builder
  public NotificationDialog(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext) {
    super(new ScreenViewport());

    this.battleMapContext = battleMapContext;
    dialog = OkDialog.builder().uiFactory(uiFactory).build();
    dialog.setSize(300, 300);
    dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight()) / 2);
    dialog.addButtonListener(this::hide);

    hide();
    stage.addActor(dialog);
  }

  public void show() {
    this.dialog.setVisible(true);
  }

  public void hide() {
    this.dialog.setVisible(false);
  }

  @Override
  public void act(float delta) {
    battleMapContext.getGameEngine().getEncounterController().popNotification()
      .ifPresent(notificationMessage -> {
        dialog.setText(notificationMessage.getMessage());
        show();
      });

    super.act(delta);
  }

  public void resize(int width, int height) {
    super.resize(width, height);
    dialog.setPosition((Gdx.graphics.getWidth() - dialog.getWidth()) / 2, (Gdx.graphics.getHeight() - dialog.getHeight()) / 2);
  }
}
