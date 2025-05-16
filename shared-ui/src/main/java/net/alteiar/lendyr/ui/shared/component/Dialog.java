package net.alteiar.lendyr.ui.shared.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import lombok.Getter;
import net.alteiar.lendyr.ui.shared.component.frame.SimpleFrame;

public class Dialog<E extends Actor> extends Group {

  private final SimpleFrame topFrame;
  private final SimpleFrame frame;
  private final Table table;
  private final Label dialogTitle;
  @Getter
  private E content;

  public Dialog(UiFactory uiFactory) {
    CloseButton closeButton = new CloseButton(uiFactory);
    closeButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        setVisible(false);
      }
    });

    dialogTitle = uiFactory.createLabel(Color.WHITE);
    dialogTitle.setFontScale(0.5f);

    table = new Table();
    table.setFillParent(true);
    table.padLeft(20);
    table.padRight(2);
    table.add(dialogTitle).left().expand();
    table.add(closeButton).right();

    topFrame = SimpleFrame.builder().uiFactory(uiFactory).backgroundTexture("fantasy-gui/bg.png").build();
    topFrame.setHeight(28);
    topFrame.setBorderThickness(2);
    topFrame.addActor(table);

    topFrame.addListener(new DragListener() {
      @Override
      public void drag(InputEvent event, float x, float y, int pointer) {
        setPosition(event.getStageX() - getWidth() / 2f, event.getStageY() - (getHeight() - 28));
      }
    });

    frame = SimpleFrame.builder().uiFactory(uiFactory).build();
    frame.setBorderThickness(2);
    this.addActor(frame);
    this.addActor(topFrame);
  }

  public void setDialogTitle(String title) {
    this.dialogTitle.setText(title);
  }

  protected void setContent(E content) {
    this.content = content;
    // Add it to the middle
    this.addActorAt(1, this.content);

    frame.setSize(content.getWidth() + frame.getBorderThickness() * 2, content.getHeight() + frame.getBorderThickness() * 2);

    topFrame.setPosition(0, frame.getHeight() - 24f);
    topFrame.setWidth(frame.getWidth());

    table.invalidate();

    this.setSize(frame.getWidth(), frame.getHeight() + topFrame.getHeight());
    this.setPosition(Gdx.graphics.getWidth() / 2f - getWidth() / 2f, Gdx.graphics.getHeight() / 2f - getHeight() / 2f);
  }

  private static class CloseButton extends Actor {
    private final Texture frame;
    private final Texture button;
    private final Texture icon;

    private float ratio;

    private float buttonX;
    private float buttonY;

    private float iconX;
    private float iconY;

    CloseButton(UiFactory uiFactory) {
      frame = uiFactory.getTexture("fantasy-gui/button_09_frame.png");
      button = uiFactory.getTexture("fantasy-gui/button_09.png");
      icon = uiFactory.getTexture("fantasy-gui/button_09_x.png");

      this.setSize(24, 24);
    }

    @Override
    protected void positionChanged() {
      iconX = getX() + (getWidth() - (icon.getWidth() * ratio)) / 2f;
      iconY = getY() + (getHeight() - (icon.getHeight() * ratio)) / 2f;

      buttonX = getX() + (getWidth() - (button.getWidth() * ratio)) / 2f;
      buttonY = getY() + (getHeight() - (button.getHeight() * ratio)) / 2f;
    }

    @Override
    protected void sizeChanged() {
      ratio = getWidth() / frame.getWidth();
      positionChanged();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
      batch.draw(button, buttonX, buttonY, button.getWidth() * ratio, button.getHeight() * ratio);
      batch.draw(frame, getX(), getY(), frame.getWidth() * ratio, frame.getHeight() * ratio);
      batch.draw(icon, iconX, iconY, icon.getWidth() * ratio, icon.getHeight() * ratio);
    }
  }
}
