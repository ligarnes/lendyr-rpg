package net.alteiar.lendyr.ui.shared.element;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import lombok.Builder;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.component.frame.SimpleFrame;

public class MultilineText extends Group {

  private final SimpleFrame simpleFrame;
  private final Label label;

  @Builder
  MultilineText(UiFactory uiFactory) {
    simpleFrame = SimpleFrame.builder().uiFactory(uiFactory).build();
    simpleFrame.setBorderThickness(2);

    label = uiFactory.createLabel(uiFactory.getFont14(), Color.BLACK);
    label.setWrap(true);
    label.setAlignment(Align.topLeft);

    this.addActor(label);
  }

  @Override
  protected void sizeChanged() {
    label.setSize(getWidth() - 40, getHeight() - 40);
    simpleFrame.setSize(getWidth(), getHeight());
  }

  public void setText(String text) {
    this.label.setText(text);
  }
}
