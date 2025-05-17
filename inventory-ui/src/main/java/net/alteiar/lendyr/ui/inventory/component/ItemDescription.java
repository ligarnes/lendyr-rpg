package net.alteiar.lendyr.ui.inventory.component;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import lombok.Builder;
import net.alteiar.lendyr.entity.ItemEntity;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.element.LargeTitle;
import net.alteiar.lendyr.ui.shared.element.MultilineText;

public class ItemDescription extends Group {

  private final UiFactory uiFactory;
  private ItemEntity item;

  //  graphical elements
  private final LargeTitle largeTitle;
  private final MultilineText description;
  private final Image image;

  @Builder
  ItemDescription(UiFactory uiFactory) {
    this.uiFactory = uiFactory;
    largeTitle = LargeTitle.builder().uiFactory(uiFactory).build();
    largeTitle.setText("");

    description = MultilineText.builder().uiFactory(uiFactory).build();
    description.setSize(250, 300);

    VerticalGroup verticalGroup = new VerticalGroup();
    verticalGroup.padTop(30);
    verticalGroup.align(Align.top);
    verticalGroup.addActor(largeTitle);
    verticalGroup.addActor(description);

    Table table = new Table();
    table.setFillParent(true);
    table.align(Align.top);
    table.add(verticalGroup);

    this.addActor(table);

    image = new Image();
    image.setSize(64, 64);

    this.addActor(image);

    this.setWidth(300);
    this.setHeight(500);
  }

  @Override
  protected void sizeChanged() {
    super.sizeChanged();
    image.setPosition(getX() + getWidth() - 84, getY() + getHeight() - 84);
  }

  @Override
  protected void positionChanged() {
    super.positionChanged();
    image.setPosition(getX() + getWidth() - 84, getY() + getHeight() - 84);
  }

  public void setItem(ItemEntity item) {
    if (this.item != item) {
      this.item = item;
      this.largeTitle.setText(item.getName());

      this.description.setText(createDescription());

      TextureRegionDrawable textureDrawable = new TextureRegionDrawable(uiFactory.getTexture(item.getIcon().replace("_b", "_t")));
      textureDrawable.setMinWidth(64);
      textureDrawable.setMinHeight(64);
      this.image.setDrawable(textureDrawable);
    }
  }

  private String createDescription() {
    if (item.isWeapon()) {
      return createWeaponDescription();
    }
    return item.getDescription();
  }

  private String createWeaponDescription() {
    return """
      Attack: %s (%s)
      Damage: %sd6+%s

      %s
      """.formatted(
      item.getAbility(), item.getFocus(),
      item.getDamageDice(), item.getFixedDamaged(),
      item.getDescription()
    );
  }
}
