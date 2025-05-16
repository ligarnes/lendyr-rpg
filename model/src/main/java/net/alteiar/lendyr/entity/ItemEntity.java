package net.alteiar.lendyr.entity;

import lombok.Builder;
import lombok.Data;
import net.alteiar.lendyr.grpc.model.v1.item.LendyrItem;

@Data
@Builder
public class ItemEntity {
  private String name;
  private String icon;

  public boolean isEmpty() {
    return this.name == null || this.name.isBlank();
  }

  public void update(LendyrItem item) {
    this.name = item.getName();
    this.icon = item.getIcon();
  }
}
