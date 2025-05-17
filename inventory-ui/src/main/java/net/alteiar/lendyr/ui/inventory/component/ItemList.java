package net.alteiar.lendyr.ui.inventory.component;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import net.alteiar.lendyr.entity.ItemEntity;
import net.alteiar.lendyr.ui.inventory.listener.ItemSlotListener;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.component.frame.SimpleFrame;

import java.util.ArrayList;
import java.util.List;

public class ItemList extends Group {

  private final List<ItemSlot> itemSlot;
  private final SimpleFrame simpleFrame;

  public ItemList(UiFactory uiFactory) {
    simpleFrame = uiFactory.createSimpleFrame();

    int rowCount = 4;
    int colCount = 5;

    itemSlot = new ArrayList<>();
    for (int i = 0; i < rowCount * colCount; i++) {
      ItemSlot entry = ItemSlot.builder().uiFactory(uiFactory).build();
      itemSlot.add(entry);
    }

    float tableWidth = itemSlot.getFirst().getWidth() * colCount;
    float tableHeight = itemSlot.getFirst().getHeight() * rowCount;
    Table table = new Table();
    table.setSize(tableWidth, tableHeight);
    table.align(Align.center);

    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < colCount; j++) {
        table.add(itemSlot.get(i * colCount + j));
      }
      table.row();
    }

    float borderThickness = 4;
    float frameWidth = tableWidth + borderThickness * 2;
    float frameHeight = tableHeight + borderThickness * 2;

    table.setPosition(borderThickness, borderThickness);

    simpleFrame.setBorderThickness(borderThickness);
    simpleFrame.setSize(frameWidth, frameHeight);
    simpleFrame.addActor(table);

    this.addActor(simpleFrame);

    this.setSize(frameWidth, frameHeight);
  }

  public void setItemSlotListener(ItemSlotListener itemSlotListener) {
    itemSlot.forEach(itemSlot -> {
      itemSlot.setItemSlotListener(itemSlotListener);
    });
  }

  public void addItem(ItemEntity itemEntity) {
    itemSlot.stream().filter(ItemSlot::isEmpty).findFirst()
      .ifPresentOrElse(itemSlot -> {
        itemSlot.setItemEntity(itemEntity);
      }, () -> {
        throw new IllegalArgumentException("Too many items");
      });
  }

  public void setItems(List<ItemEntity> itemEntities) {
    if (itemEntities.size() > itemSlot.size()) {
      throw new IllegalArgumentException("Too many items");
    }
    itemSlot.forEach(ItemSlot::empty);
    for (int i = 0; i < itemEntities.size(); i++) {
      itemSlot.get(i).setItemEntity(itemEntities.get(i));
    }
  }

  @Override
  protected void sizeChanged() {
    super.sizeChanged();
    simpleFrame.setPosition(getWidth() / 2 - simpleFrame.getWidth() / 2, getHeight() / 2 - simpleFrame.getHeight() / 2);
  }

  @Override
  protected void scaleChanged() {
    super.scaleChanged();
    simpleFrame.setPosition(getWidth() / 2 - simpleFrame.getWidth() / 2, getHeight() / 2 - simpleFrame.getHeight() / 2);
  }
}
