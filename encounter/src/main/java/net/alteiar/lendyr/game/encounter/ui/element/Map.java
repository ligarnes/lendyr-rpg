package net.alteiar.lendyr.game.encounter.ui.element;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class Map extends Actor {

  private final Texture background;

  @Builder
  public Map(@NonNull UiFactory uiFactory, @NonNull String mapName, float width, float height) {
    background = uiFactory.getAssetManager().get(String.format("maps/%s", mapName), Texture.class);
    this.setWidth(width);
    this.setHeight(height);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.draw(background, 0, 0, getWidth(), getHeight());
  }
}
