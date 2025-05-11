package net.alteiar.lendyr.game.encounter.ui.element.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class Title extends Group {

  private final Label nameLabel;

  public Title(UiFactory uiFactory) {
    Background background = new Background(uiFactory);
    background.setPosition(0, 0);
    float scale = 250f / background.getWidth();
    background.setWidth(scale * background.getWidth());
    background.setHeight(scale * background.getHeight());

    nameLabel = uiFactory.createLabel();
    nameLabel.setColor(Color.BLACK);

    GlyphLayout layout = nameLabel.getGlyphLayout();
    layout.setText(nameLabel.getStyle().font, "some text");
    float x = 0f;
    float y = background.getHeight() - (layout.height + 3);
    nameLabel.setPosition(x, y);
    nameLabel.setWidth(background.getWidth());
    nameLabel.setHeight(layout.height);
    nameLabel.setAlignment(Align.center);
    nameLabel.setFontScale(0.8f);

    this.addActor(background);
    this.addActor(nameLabel);

    this.setX(0f);
    this.setY(0f);
    this.setWidth(background.getWidth());
    this.setHeight(background.getHeight());
  }

  public void setText(String text) {
    nameLabel.setText(text);
  }

  private static class Background extends Actor {
    private final Texture texture;

    public Background(UiFactory uiFactory) {
      this.texture = uiFactory.getTexture("fantasy-gui/text_bg_03.png");
      this.setWidth(texture.getWidth());
      this.setHeight(texture.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
      batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }
  }
}
