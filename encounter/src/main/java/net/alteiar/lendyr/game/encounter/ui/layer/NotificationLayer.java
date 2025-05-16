package net.alteiar.lendyr.game.encounter.ui.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.game.encounter.ui.component.AttackDialog;
import net.alteiar.lendyr.game.encounter.ui.component.OkDialog;
import net.alteiar.lendyr.ui.shared.component.PersonaEquipmentDialog;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.view.ViewLayer;

public class NotificationLayer extends ViewLayer {

  private final OkDialog notificationDialog;
  private final AttackDialog attackDialog;
  private final PersonaEquipmentDialog inventoryDialog;

  private final BattleMapContext battleMapContext;

  @Builder
  public NotificationLayer(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext) {
    super(new ScreenViewport());

    this.battleMapContext = battleMapContext;

    notificationDialog = OkDialog.builder().uiFactory(uiFactory).build();
    notificationDialog.setSize(300, 300);
    notificationDialog.setPosition(centerX(notificationDialog), centerY(notificationDialog));
    notificationDialog.addButtonListener(() -> this.notificationDialog.setVisible(false));
    notificationDialog.setVisible(false);

    attackDialog = AttackDialog.builder().uiFactory(uiFactory).battleMapContext(battleMapContext).build();
    attackDialog.setPosition(centerX(attackDialog), centerY(attackDialog));

    inventoryDialog = PersonaEquipmentDialog.builder().uiFactory(uiFactory).build();
    inventoryDialog.setPosition(centerX(inventoryDialog), centerY(inventoryDialog));

    stage.addActor(attackDialog);
    stage.addActor(inventoryDialog);
    stage.addActor(notificationDialog);
  }

  public void showHideInventory() {
    this.inventoryDialog.setVisible(!this.inventoryDialog.isVisible());
  }

  @Override
  public void act(float delta) {
    battleMapContext.getGameEngine().getEncounterController().popNotification()
      .ifPresent(notificationMessage -> {
        notificationDialog.setText(notificationMessage.getMessage());
        notificationDialog.setVisible(true);
      });

    inventoryDialog.setPersona(battleMapContext.getCombatEntity().getCurrentCharacter());

    super.act(delta);
  }

  public void resize(int width, int height) {
    super.resize(width, height);
    notificationDialog.setPosition(centerX(notificationDialog), centerY(notificationDialog));
    inventoryDialog.setPosition(centerX(inventoryDialog), centerY(inventoryDialog));

    attackDialog.setPosition(centerX(attackDialog), centerY(attackDialog));
  }

  private float centerX(Actor actor) {
    return Gdx.graphics.getWidth() / 2f - actor.getWidth() / 2f;
  }

  private float centerY(Actor actor) {
    return Gdx.graphics.getHeight() / 2f - actor.getHeight() / 2f;
  }

}
