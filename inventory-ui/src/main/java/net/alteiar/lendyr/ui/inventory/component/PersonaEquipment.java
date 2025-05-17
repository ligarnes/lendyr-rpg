package net.alteiar.lendyr.ui.inventory.component;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import lombok.Builder;
import lombok.Setter;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.ui.inventory.listener.ItemSlotListener;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.element.SmallTitle;

import java.util.Objects;

public class PersonaEquipment extends Group {

  private final ItemList itemList;
  private final PersonaEquipped personaEquipped;

  @Setter
  private PersonaEntity persona;

  @Builder
  PersonaEquipment(UiFactory uiFactory) {
    itemList = new ItemList(uiFactory);

    VerticalGroup backpack = new VerticalGroup();
    backpack.align(Align.center);
    backpack.space(10);
    backpack.addActor(SmallTitle.builder().uiFactory(uiFactory).title("Backpack").build());
    backpack.addActor(itemList);

    personaEquipped = PersonaEquipped.builder().uiFactory(uiFactory).build();

    Table table = new Table();
    table.setFillParent(true);
    table.add(personaEquipped).spaceRight(30);
    table.add(backpack);

    float height = Math.max(personaEquipped.getHeight(), backpack.getHeight());

    this.addActor(table);
    this.setWidth(780);
    this.setHeight(height);
  }

  public void setItemSlotListener(ItemSlotListener itemSlotListener) {
    itemList.setItemSlotListener(itemSlotListener);
    personaEquipped.setItemSlotListener(itemSlotListener);
  }

  @Override
  public void act(float delta) {
    if (Objects.nonNull(this.persona)) {
      itemList.setItems(persona.getInventory().getBackpack());
      personaEquipped.setPersonaInventoryEntity(persona.getInventory());

      super.act(delta);
    }
  }
}
