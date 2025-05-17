package net.alteiar.lendyr.entity;

import lombok.Builder;
import lombok.Data;
import net.alteiar.lendyr.grpc.model.v1.item.LendyrItem;

@Data
@Builder
public class ItemEntity {
  private String name;
  private String icon;
  private String description;
  private int cost;
  private int encumbrance;

  private boolean isWeapon;

  // Damage specific
  private String weaponGroup;
  private int minStr;
  private String ability;
  private String focus;
  private int fixedDamaged;
  private int damageDice;


  public boolean isEmpty() {
    return this.name == null || this.name.isBlank();
  }

  public void update(LendyrItem item) {
    this.name = item.getName();
    this.icon = item.getIcon();

    this.cost = item.getCost();
    this.encumbrance = item.getEncumbrance();
    this.description = item.getDescription();

    this.isWeapon = item.getPropertiesCase() == LendyrItem.PropertiesCase.WEAPON;

    if (isWeapon) {
      fixedDamaged = item.getWeapon().getDamageFixed();
      damageDice = item.getWeapon().getDamageDice();
      minStr = item.getWeapon().getMinStr();
      ability = item.getWeapon().getAbility().name();
      focus = item.getWeapon().getFocus().name();
      weaponGroup = item.getWeapon().getWeaponGroup();
    }
  }
}
