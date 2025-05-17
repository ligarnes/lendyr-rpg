package net.alteiar.lendyr.ui.shared.element.button;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import lombok.Builder;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.listener.ButtonClickListener;

public class TextButtonGreen extends Group {
  private final Label label;

  @Builder
  private TextButtonGreen(UiFactory uiFactory, String text) {
    ButtonIcon icon = new ButtonIcon(uiFactory);
    label = uiFactory.createLabel(text, uiFactory.getFont16(), Color.BLACK);
    label.setWidth(icon.getWidth() - 10);
    label.setHeight(icon.getHeight() - 10);
    label.setX(5);
    label.setY(5);
    label.setAlignment(Align.center);

    //setWidth(icon.getWidth());
    //setHeight(icon.getHeight());

    Texture texture = uiFactory.getTexture("fantasy-gui-customized/button_normal_small.png");
    NinePatch ninePatch = new NinePatch(texture, 40, 40, 35, 35);
    NinePatchDrawable patch = new NinePatchDrawable(ninePatch);

    TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
    style.font = uiFactory.getFont16();
    style.fontColor = Color.BLACK;
    style.up = patch;
    style.over = patch;
    style.down = patch;

    style.disabled = patch.tint(UiFactory.DARK_GRAY_OVERLAY_COLOR);

    // Instantiate the Button itself.
    TextButton button = new TextButton("hello world", style);
    button.setSize(210, 80);

    this.setSize(210, 80);

    this.addActor(button);
  }

  public void addClickListener(ButtonClickListener listener) {
    this.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        listener.buttonClicked();
      }
    });
  }

  public void setText(String text) {
    this.label.setText(text);
  }

  private static class ButtonIcon extends Actor {
    private final NinePatch button;

    @Builder
    private ButtonIcon(UiFactory assetManager) {
      Texture texture = assetManager.getTexture("fantasy-gui-customized/button_green.9.png");
      button = new NinePatch(texture);

      setWidth(200f);
      setHeight(50f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
      button.draw(batch, getX(), getY(), getWidth(), getHeight());
    }
  }
}
