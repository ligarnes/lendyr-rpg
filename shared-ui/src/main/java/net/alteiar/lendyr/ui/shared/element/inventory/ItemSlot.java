package net.alteiar.lendyr.ui.shared.element.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.alteiar.lendyr.entity.ItemEntity;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

import java.util.Objects;

public class ItemSlot extends Actor {
  private final UiFactory uiFactory;
  private final Texture background;
  private final Texture slot;
  @Getter
  @Setter
  private ItemEntity itemEntity;

  @Builder
  ItemSlot(UiFactory uiFactory) {
    this.uiFactory = uiFactory;
    background = uiFactory.getTexture("fantasy-gui/bg.png");
    slot = uiFactory.getTexture("fantasy-gui/slot_01.png");
    setWidth(64);
    setHeight(64);
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
