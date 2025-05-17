package net.alteiar.lendyr.ui.inventory.component;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import lombok.Builder;
import lombok.Setter;
import net.alteiar.lendyr.entity.PersonaInventoryEntity;
import net.alteiar.lendyr.ui.inventory.listener.ItemSlotListener;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.component.frame.SimpleFrame;

public class PersonaEquipped extends Group {
  // Left items
  private final ItemSlot leftHand;
  private final ItemSlot gloves;
  private final ItemSlot armor;
  private final ItemSlot boot;

  // Right items
  private final ItemSlot rightHand;
  private final ItemSlot belt;
  private final ItemSlot neckless;
  private final ItemSlot ring1;

  @Setter
  private PersonaInventoryEntity personaInventoryEntity;

  @Builder
  PersonaEquipped(UiFactory uiFactory) {
    // left menu
    leftHand = ItemSlot.builder().uiFactory(uiFactory).build();
    gloves = ItemSlot.builder().uiFactory(uiFactory).build();
    armor = ItemSlot.builder().uiFactory(uiFactory).build();
    boot = ItemSlot.builder().uiFactory(uiFactory).build();

    VerticalGroup verticalGroupLeft = new VerticalGroup();
    verticalGroupLeft.space(5);
    verticalGroupLeft.addActor(leftHand);
    verticalGroupLeft.addActor(gloves);
    verticalGroupLeft.addActor(armor);
    verticalGroupLeft.addActor(boot);

    // Right menu
    rightHand = ItemSlot.builder().uiFactory(uiFactory).build();
    belt = ItemSlot.builder().uiFactory(uiFactory).build();
    neckless = ItemSlot.builder().uiFactory(uiFactory).build();
    ring1 = ItemSlot.builder().uiFactory(uiFactory).build();

    VerticalGroup verticalGroupRight = new VerticalGroup();
    verticalGroupRight.space(5);
    verticalGroupRight.addActor(rightHand);
    verticalGroupRight.addActor(belt);
    verticalGroupRight.addActor(neckless);
    verticalGroupRight.addActor(ring1);

    PersonaIllustration personaFullIllustration = PersonaIllustration.builder().uiFactory(uiFactory).build();
    float illustrationWidth = 203.5714f;
    float illustrationHeight = 300f;
    personaFullIllustration.setSize(illustrationWidth, illustrationHeight);
    personaFullIllustration.setPosition(4, 4);

    SimpleFrame personaBackground = uiFactory.createSimpleFrame();
    personaBackground.setBorderThickness(4);
    personaBackground.addActor(personaFullIllustration);
    personaBackground.setSize(illustrationWidth + personaBackground.getBorderThickness() * 2, illustrationHeight + personaBackground.getBorderThickness() * 2);

    Table table = new Table();
    table.setFillParent(true);
    table.add(verticalGroupLeft).spaceRight(10);
    table.add(personaBackground).spaceRight(10);
    table.add(verticalGroupRight);

    float width = illustrationWidth + 40 + rightHand.getWidth() * 2;

    this.addActor(table);
    this.setSize(width, 360);
  }

  public void setItemSlotListener(ItemSlotListener itemSlotListener) {
    leftHand.setItemSlotListener(itemSlotListener);
    gloves.setItemSlotListener(itemSlotListener);
    armor.setItemSlotListener(itemSlotListener);
    boot.setItemSlotListener(itemSlotListener);

    rightHand.setItemSlotListener(itemSlotListener);
    belt.setItemSlotListener(itemSlotListener);
    neckless.setItemSlotListener(itemSlotListener);
    ring1.setItemSlotListener(itemSlotListener);
  }

  @Override
  public void act(float delta) {
    if (personaInventoryEntity != null) {
      if (!personaInventoryEntity.getTwoHanded().isEmpty()) {
        this.leftHand.setItemEntity(personaInventoryEntity.getTwoHanded());
        this.rightHand.setItemEntity(personaInventoryEntity.getTwoHanded());
      } else {
        this.leftHand.setItemEntity(personaInventoryEntity.getLeftHand());
        this.rightHand.setItemEntity(personaInventoryEntity.getRightHand());
      }
      super.act(delta);
    }
  }

  private static class PersonaIllustration extends Actor {

    private final Texture personaFullIllustration;

    @Builder
    PersonaIllustration(UiFactory uiFactory) {
      personaFullIllustration = uiFactory.getTexture("questjournal/illustrations/man-resized.png");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
      batch.draw(personaFullIllustration, getX(), getY(), getWidth(), getHeight());
    }
  }
}
