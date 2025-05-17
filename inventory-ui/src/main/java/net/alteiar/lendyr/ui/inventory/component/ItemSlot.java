package net.alteiar.lendyr.ui.inventory.component;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.alteiar.lendyr.entity.ItemEntity;
import net.alteiar.lendyr.ui.inventory.listener.ItemSlotListener;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

import java.util.Objects;

public class ItemSlot extends Actor {
  private final UiFactory uiFactory;
  private final Texture background;
  private final Texture slot;
  @Getter
  @Setter
  private ItemEntity itemEntity;

  @Setter
  private ItemSlotListener itemSlotListener;

  @Builder
  ItemSlot(UiFactory uiFactory) {
    this.uiFactory = uiFactory;
    background = uiFactory.getTexture("fantasy-gui/bg.png");
    slot = uiFactory.getTexture("fantasy-gui/slot_01.png");
    setWidth(64);
    setHeight(64);

    this.addListener(new ClickListener(Input.Buttons.RIGHT) {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (itemSlotListener != null) {
          itemSlotListener.onItemSlotRightClick(ItemSlot.this);
        }
      }
    });
  }

  public boolean isEmpty() {
    return Objects.isNull(itemEntity);
  }

  public void empty() {
    this.itemEntity = null;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.draw(background, getX(), getY(), getWidth(), getHeight());
    batch.draw(slot, getX(), getY(), getWidth(), getHeight());
    if (itemEntity != null && itemEntity.getName() != null && !itemEntity.getName().isBlank()) {
      Texture itemTexture = uiFactory.getTexture(itemEntity.getIcon());
      batch.draw(itemTexture, getX() + 5, getY() + 5, getWidth() - 10, getHeight() - 10);
    }
  }
}
