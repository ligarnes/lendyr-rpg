package net.alteiar.lendyr.ui.shared.element.button;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import lombok.Builder;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class TextButtonLarge extends Group {

  private final TextButton button;

  @Builder
  TextButtonLarge(UiFactory uiFactory, String text) {
    Texture texture = uiFactory.getTexture("fantasy-gui-customized/button_normal_small.png");
    NinePatch ninePatch = new NinePatch(texture, 40, 40, 13, 18);
    NinePatchDrawable patch = new NinePatchDrawable(ninePatch);

    TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
    style.font = uiFactory.getFont16();
    style.fontColor = Color.BLACK;
    style.up = patch;
    style.over = patch.tint(new Color(0.663f, 0.663f, 0.663f, 0.9f));
    style.down = new OffsetNinePatchDrawable(ninePatch, 0, -2f);
    style.pressedOffsetY = -2;
    style.disabled = patch.tint(UiFactory.DARK_GRAY_OVERLAY_COLOR);

    button = new TextButton(text, style);
    this.addActor(button);
  }

  public void setText(String text) {
    button.setText(text);
  }

  @Override
  protected void sizeChanged() {
    button.setSize(getWidth(), getHeight());
  }

  // Custom Drawable that offsets the drawing of the NinePatch
  private static class OffsetNinePatchDrawable extends NinePatchDrawable {
    private final float offsetX;
    private final float offsetY;

    public OffsetNinePatchDrawable(NinePatch patch, float offsetX, float offsetY) {
      super(patch);
      this.offsetX = offsetX;
      this.offsetY = offsetY;
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
      super.draw(batch, x + offsetX, y + offsetY, width, height);
    }
  }
}
