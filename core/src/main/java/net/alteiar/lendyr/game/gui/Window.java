package net.alteiar.lendyr.game.gui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Builder;

public class Window extends Actor {

  private final Texture background;
  private final Texture borderAngleOverlay;
  private final Texture borderStraight;

  @Builder
  private Window(AssetManager assetManager) {
    background = assetManager.get("black-crusader-ui/BlackCrusaderUI_el_31.png", Texture.class);
    borderAngleOverlay = assetManager.get("black-crusader-ui/BlackCrusaderUI_el_43.png", Texture.class);
    borderStraight = assetManager.get("black-crusader-ui/BlackCrusaderUI_el_46.png", Texture.class);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.draw(background, getX(), getY(), getWidth(), getHeight());

    batch.draw(borderStraight, getX(), getY() + getHeight() - 6f, getWidth(), 6f);
    batch.draw(borderStraight, getX(), getY(), getWidth(), 6f, 0, 0, 284, 30, false, true);

    batch.draw(borderStraight, getX() + 6, getY(), 0, 0, getHeight(), 6f, 1, 1, 90f, 0, 0, 284, 30, false, false);
    batch.draw(borderStraight, getX() + getWidth(), getY(), 0, 0, getHeight(), 6f, 1, 1, 90f, 0, 0, 284, 30, false, true);

    batch.draw(borderAngleOverlay, getX(), getY() + getHeight() - 56.8f, 56.8f, 56.8f);
    batch.draw(borderAngleOverlay, getX() + getWidth() - 56.8f, getY() + getHeight() - 56.8f, 56.8f, 56.8f, 0, 0, 284, 284, true, false);
    batch.draw(borderAngleOverlay, getX() + getWidth() - 56.8f, getY(), 56.8f, 56.8f, 0, 0, 284, 284, true, true);
    batch.draw(borderAngleOverlay, getX(), getY(), 56.8f, 56.8f, 0, 0, 284, 284, false, true);
  }
}
