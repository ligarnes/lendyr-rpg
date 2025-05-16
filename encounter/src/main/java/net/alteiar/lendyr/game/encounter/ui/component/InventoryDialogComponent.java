package net.alteiar.lendyr.game.encounter.ui.component;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.component.frame.DecoratedFrame;
import net.alteiar.lendyr.ui.shared.component.inventory.ItemList;

public class InventoryDialogComponent extends Group {
  private static final int WIDTH = 400;

  private final DecoratedFrame background;
  private final ItemList itemList;

  private final BattleMapContext battleMapContext;

  @Builder
  InventoryDialogComponent(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext) {
    float borderPad = 20;
    float spacing = 10;
    this.battleMapContext = battleMapContext;

    background = uiFactory.createDecoratedFrame();
    this.addActor(background);

    itemList = new ItemList(uiFactory);
    itemList.setWidth(WIDTH);

    Group itemListGroup = new Group();
    itemListGroup.setWidth(WIDTH);
    itemListGroup.setHeight(itemList.getHeight());
    itemListGroup.addActor(itemList);

    Label label = uiFactory.createLabel("Inventory");

    float verticalHeight = borderPad * 2 + spacing + itemList.getHeight() + label.getHeight();

    VerticalGroup verticalGroup = new VerticalGroup();
    verticalGroup.setWidth(WIDTH);
    verticalGroup.setHeight(verticalHeight);
    verticalGroup.padTop(borderPad);
    verticalGroup.space(spacing);
    verticalGroup.addActor(label);
    verticalGroup.addActor(itemListGroup);

    background.addActor(verticalGroup);

    background.setWidth(WIDTH);
    background.setHeight(verticalGroup.getHeight());

    this.setWidth(WIDTH);
    this.setHeight(verticalGroup.getHeight());
    this.setVisible(false);

    this.addListener(new DragListener() {
      @Override
      public void drag(InputEvent event, float x, float y, int pointer) {
        super.drag(event, x, y, pointer);
        background.setPosition(x - background.getWidth() / 2, y - background.getHeight());
      }
    });
  }

  @Override
  public void act(float delta) {
    //itemList.setItems(battleMapContext.getCombatEntity().getCurrentCharacter().getInventory().getRightHand());
    super.act(delta);
  }
}
