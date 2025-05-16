package net.alteiar.lendyr.ui.shared.component.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import lombok.Builder;
import lombok.Setter;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.entity.PersonaInventoryEntity;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.component.frame.SimpleFrame;
import net.alteiar.lendyr.ui.shared.element.SmallTitle;
import net.alteiar.lendyr.ui.shared.element.inventory.ItemSlot;

import java.util.Objects;

public class PersonaEquipment extends Group {

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

  private final ItemList itemList;

  @Setter
  private PersonaEntity persona;

  @Builder
  PersonaEquipment(UiFactory uiFactory) {
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

    itemList = new ItemList(uiFactory);

    VerticalGroup backpack = new VerticalGroup();
    backpack.align(Align.center);
    backpack.space(10);
    backpack.addActor(SmallTitle.builder().uiFactory(uiFactory).title("Backpack").build());
    backpack.addActor(itemList);

    Table table = new Table();
    table.setFillParent(true);
    table.add(verticalGroupLeft).spaceRight(10);
    table.add(personaBackground).spaceRight(10);
    table.add(verticalGroupRight).spaceRight(30);
    table.add(backpack);

    this.addActor(table);
    this.setWidth(790);
    this.setHeight(360);
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    itemList.setItems(persona.getInventory().getBackpack());

    if (Objects.nonNull(this.persona)) {
      PersonaInventoryEntity personaInventoryEntity = persona.getInventory();
      if (!personaInventoryEntity.getTwoHanded().isEmpty()) {
        this.leftHand.setItemEntity(personaInventoryEntity.getTwoHanded());
        this.rightHand.setItemEntity(personaInventoryEntity.getTwoHanded());
      } else {
        this.leftHand.setItemEntity(personaInventoryEntity.getLeftHand());
        this.rightHand.setItemEntity(personaInventoryEntity.getRightHand());
      }
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
