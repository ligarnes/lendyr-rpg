package net.alteiar.lendyr.game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import lombok.Builder;
import lombok.NonNull;

public class GaugeGroup extends Group {

  private final GaugeActor gauge;
  private final Label gaugeLabel;

  private float current;
  private float max;

  @Builder
  public GaugeGroup(@NonNull UiFactory uiFactory, float max, float current, @NonNull Color gaugeColor, @NonNull Color textColor, String innerTexture) {
    this.current = current;
    this.max = max;

    gauge = GaugeActor.builder().uiFactory(uiFactory).ratio(1 - current / max).color(gaugeColor)
      .innerTexture(innerTexture).build();

    gaugeLabel = uiFactory.createLabel("Text", textColor);
    gaugeLabel.setFontScale(0.6f);
    gaugeLabel.setAlignment(Align.center);

    this.addActor(gauge);
    this.addActor(gaugeLabel);

    this.setWidth(120);
    this.setHeight(120);
  }

  public void setInnerTexture(String texture) {
    gauge.setInnerTexture(texture);
  }

  public void setCurrent(float current) {
    this.current = current;
    gauge.setRatio(1 - current / max);
  }

  public void setMax(float max) {
    this.max = max;
    gauge.setRatio(1 - current / max);
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    gaugeLabel.setText(String.format("%.0f / %.0f", current, max));
  }

  @Override
  protected void sizeChanged() {
    gaugeLabel.setX(0);
    gaugeLabel.setY(getHeight() / 2f);
    gaugeLabel.setWidth(getWidth());

    gauge.setWidth(getWidth());
    gauge.setHeight(getHeight());
  }

  public void dispose() {
    gauge.dispose();
  }
}
