package net.alteiar.lendyr.game.gui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Builder;

public class Separator extends Actor {

  private final Texture separator;

  @Builder
  private Separator(AssetManager assetManager) {
    separator = assetManager.get("black-crusader-ui/BlackCrusaderUI_el_27.png", Texture.class);
    setWidth(251f);
    setHeight(41.5f);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.draw(separator, getX(), getY(), getOriginX(), getOriginY(),
      getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0, separator.getWidth(), separator.getHeight(), false, false);
  }
}
