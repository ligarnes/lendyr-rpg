package net.alteiar.lendyr.game.encounter.ui.attack;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class SkillResult extends Group {
  public static final int DICE_WIDTH = 30;
  public static final int DICE_HEIGHT = 30;
  public static final int SPACING = 5;

  private final DiceResult dice1;
  private final DiceResult dice2;
  private final DiceResult dice3;
  private final Label result;


  @Builder
  SkillResult(@NonNull UiFactory uiFactory) {
    this.setWidth(DICE_WIDTH * 3 + SPACING * 4);
    this.setHeight(90);

    DiceBackground background = new DiceBackground(uiFactory);
    background.setColor(Color.BLUE);
    background.setWidth(this.getWidth());
    background.setHeight(this.getHeight());
    this.addActor(background);

    dice1 = DiceResult.builder().uiFactory(uiFactory).build();
    dice2 = DiceResult.builder().uiFactory(uiFactory).build();
    dice3 = DiceResult.builder().uiFactory(uiFactory).build();

    dice1.setWidth(DICE_WIDTH);
    dice1.setHeight(DICE_HEIGHT);
    dice1.setX(SPACING);
    dice1.setY(this.getHeight() - (DICE_HEIGHT + SPACING));

    dice2.setWidth(DICE_WIDTH);
    dice2.setHeight(DICE_HEIGHT);
    dice2.setX(DICE_WIDTH + SPACING * 2);
    dice2.setY(this.getHeight() - (DICE_HEIGHT + SPACING));

    dice3.setWidth(DICE_WIDTH);
    dice3.setHeight(DICE_HEIGHT);
    dice3.setX(DICE_WIDTH * 2 + SPACING * 3);
    dice3.setY(this.getHeight() - (DICE_HEIGHT + SPACING));

    this.addActor(dice1);
    this.addActor(dice2);
    this.addActor(dice3);

    result = uiFactory.createLabel();
    result.setColor(Color.RED);
    result.setScale(1);
    result.setAlignment(Align.center);
    result.setWidth(DICE_WIDTH * 3 + SPACING * 4);
    result.setY(30);

    this.addActor(result);
  }

  public void setResult(net.alteiar.lendyr.entity.model.SkillResult result) {
    dice1.setResult(result.getDice1());
    dice2.setResult(result.getDice2());
    dice3.setResult(result.getStunDice());
    int total = result.getDice1() + result.getDice2() + result.getStunDice() + result.getModifier();
    this.result.setText(total);
  }

  private static class DiceBackground extends Actor {
    private final Texture background;

    public DiceBackground(UiFactory uiFactory) {
      background = uiFactory.getTexture("black.png");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
      batch.draw(background, getX(), getY(), getWidth(), getHeight());
    }
  }
}
