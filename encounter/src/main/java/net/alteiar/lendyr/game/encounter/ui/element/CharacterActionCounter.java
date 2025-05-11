package net.alteiar.lendyr.game.encounter.ui.element;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class CharacterActionCounter extends Actor {
  @Setter
  @Getter
  private int progress;
  @Setter
  @Getter
  private int maxProgress;

  private final Texture border;
  private final Texture step;

  @Builder
  CharacterActionCounter(AssetManager assetManager) {
    this.progress = 2;
    this.maxProgress = 2;

    border = assetManager.get("black-crusader-ui/BlackCrusaderUI_el_06_01.png", Texture.class);
    step = assetManager.get("black-crusader-ui/BlackCrusaderUI_el_05.png", Texture.class);

    setWidth(250f);
    setHeight(31f);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    float borderWidth = 8;
    float spacing = 1;
    float allSpacing = borderWidth * 2 + spacing * maxProgress;
    float progressWidth = (getWidth() - allSpacing) / maxProgress;
    float xSpacing = progressWidth + spacing;
    for (int i = 0; i < progress; i++) {
      batch.draw(step, getX() + borderWidth + (i * xSpacing), getY() + 9, progressWidth, 12);
    }

    batch.draw(border,
      getX(), getY(),
      0, 0,
      20, getHeight(),
      1, 1,
      0,
      0, 0,
      80, border.getHeight(),
      false, false);

    batch.draw(border,
      getX() + 20, getY(),
      0, 0,
      getWidth() - 40, getHeight(),
      1, 1,
      0,
      80, 0,
      border.getWidth() - 160, border.getHeight(),
      false, false);

    batch.draw(border,
      getX() + getWidth() - 20, getY(),
      0, 0,
      20, getHeight(),
      1, 1,
      0,
      border.getWidth() - 80, 0,
      80, border.getHeight(),
      false, false);
  }
}
