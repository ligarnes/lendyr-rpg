package net.alteiar.lendyr.entity;

import lombok.Data;
import net.alteiar.lendyr.grpc.model.v1.persona.LendyrPersonaInventory;

import java.util.ArrayList;
import java.util.List;

@Data
public class PersonaInventoryEntity {
  private ItemEntity leftHand = ItemEntity.builder().build();
  private ItemEntity rightHand = ItemEntity.builder().build();
  private ItemEntity twoHanded = ItemEntity.builder().build();

  private ItemEntity gloves = ItemEntity.builder().build();
  private ItemEntity armor = ItemEntity.builder().build();
  private ItemEntity boot = ItemEntity.builder().build();
  private ItemEntity belt = ItemEntity.builder().build();
  private ItemEntity neckless = ItemEntity.builder().build();
  private ItemEntity ring1 = ItemEntity.builder().build();

  private List<ItemEntity> backpack = new ArrayList<>();

  public void update(LendyrPersonaInventory item) {
    leftHand.update(item.getLeftHand());
    rightHand.update(item.getRightHand());
    twoHanded.update(item.getTwoHanded());

    gloves.update(item.getGloves());
    armor.update(item.getArmor());
    boot.update(item.getShoes());
    belt.update(item.getBelt());
    neckless.update(item.getNeckless());
    ring1.update(item.getRing1());

    while (backpack.size() < item.getBackpackCount()) {
      backpack.add(ItemEntity.builder().build());
    }
    while (backpack.size() > item.getBackpackCount()) {
      backpack.removeLast();
    }
    for (int i = 0; i < item.getBackpackCount(); i++) {
      backpack.get(i).update(item.getBackpack(i));
    }
  }
}
