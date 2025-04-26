package net.alteiar.lendyr.game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import lombok.Builder;

public class GaugeGroup extends Group {

  private final GaugeActor gauge;
  private final Label gaugeLabel;

  private float current;
  private float max;

  @Builder
  public GaugeGroup(UiFactory uiFactory, float max, float current, Color gaugeColor, Color textColor) {
    this.current = current;
    this.max = max;

    gauge = GaugeActor.builder().uiFactory(uiFactory).ratio(current / max).color(gaugeColor).build();

    gaugeLabel = uiFactory.createLabel(textColor);
    gaugeLabel.setPosition(0, 0);
    gaugeLabel.setWidth(120);
    gaugeLabel.setHeight(120);
    gaugeLabel.setAlignment(Align.center);
    gaugeLabel.setVisible(true);

    this.addActor(gauge);
    this.addActor(gaugeLabel);

    this.setWidth(120);
    this.setHeight(120);
  }

  public void setCurrent(float current) {
    this.current = current;
    gauge.setRatio(current / max);
  }

  public void setMax(float max) {
    this.max = max;
    gauge.setRatio(current / max);
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    gaugeLabel.setText(String.format("%.0f / %.0f", current, max));
  }

  @Override
  protected void sizeChanged() {
    gaugeLabel.setWidth(getWidth());
    //gaugeLabel.setPosition(0, (getHeight() / 2f) - 10);

    gauge.setWidth(getWidth());
    gauge.setHeight(getHeight());
  }

  public void dispose() {
    gauge.dispose();
  }
}
