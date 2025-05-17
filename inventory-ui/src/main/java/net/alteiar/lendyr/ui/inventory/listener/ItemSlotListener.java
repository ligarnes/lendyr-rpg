package net.alteiar.lendyr.ui.inventory.listener;


import net.alteiar.lendyr.ui.inventory.component.ItemSlot;

public interface ItemSlotListener {
  default void onItemSlotRightClick(ItemSlot itemSlot) {
  }
}
