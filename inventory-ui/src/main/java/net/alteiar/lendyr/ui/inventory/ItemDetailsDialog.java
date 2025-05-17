package net.alteiar.lendyr.ui.inventory;

import com.badlogic.gdx.Gdx;
import lombok.Builder;
import net.alteiar.lendyr.entity.ItemEntity;
import net.alteiar.lendyr.ui.inventory.component.ItemDescription;
import net.alteiar.lendyr.ui.shared.component.Dialog;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class ItemDetailsDialog extends Dialog<ItemDescription> {

  @Builder
  public ItemDetailsDialog(UiFactory uiFactory) {
    super(uiFactory);

    ItemDescription itemDescription = ItemDescription.builder().uiFactory(uiFactory).build();
    this.setContent(itemDescription);
    this.setPosition(Gdx.graphics.getWidth() / 2f - getWidth() / 2f, Gdx.graphics.getHeight() / 2f - getHeight() / 2f);
    this.setVisible(false);
  }

  public void setItem(ItemEntity item) {
    setDialogTitle("%s".formatted(item.getName()));
    this.getContent().setItem(item);
  }

}
