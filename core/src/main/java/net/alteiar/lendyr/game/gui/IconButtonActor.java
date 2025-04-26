package net.alteiar.lendyr.game.gui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Builder;

public class IconButtonActor extends Actor {

  public static enum ButtonType {
    SWORD, SHIELD, LOCK, WALK, NEXT, PREVIOUS
  }

  private final ButtonType type;
  private final Texture button;
  private final Texture icon;

  @Builder
  private IconButtonActor(AssetManager assetManager, ButtonType buttonType) {
    this.type = buttonType;
    button = assetManager.get("black-crusader-ui/BlackCrusaderUI_el_01_01.png", Texture.class);
    icon = loadIcon(assetManager, buttonType);

    setWidth(50f);
    setHeight(50f);
  }

  private Texture loadIcon(AssetManager assetManager, ButtonType type) {
    return switch (type) {
      case LOCK -> assetManager.get("black-crusader-ui/BlackCrusaderUI_el_35.png", Texture.class);
      case SHIELD -> assetManager.get("black-crusader-ui/BlackCrusaderUI_el_33.png", Texture.class);
      case SWORD -> assetManager.get("black-crusader-ui/BlackCrusaderUI_el_17.png", Texture.class);
      case NEXT, PREVIOUS -> assetManager.get("black-crusader-ui/BlackCrusaderUI_el_02_03.png", Texture.class);
      case WALK -> assetManager.get("icon/TabletopBadges_15.PNG", Texture.class);
    };
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.draw(button, getX(), getY(), getWidth(), getHeight());

    if (type == ButtonType.SWORD) {
      batch.draw(icon, getX(), getY() + 10, 0, 0, 15.6f, 52.6f, 1, 1, -45f, 0, 0, icon.getWidth(), icon.getHeight(), false, false);
    } else {
      boolean flipX = false;
      if (type == ButtonType.NEXT) {
        flipX = true;
      }
      batch.draw(icon, getX() + 5, getY() + 5, getWidth() - 10, getHeight() - 10, 0, 0, icon.getWidth(), icon.getHeight(), flipX, false);
    }
  }
}
