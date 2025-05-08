package net.alteiar.lendyr.game.battlemap;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Timer;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.entity.model.AttackResult;
import net.alteiar.lendyr.game.battlemap.attack.Defense;
import net.alteiar.lendyr.game.battlemap.attack.DiceGroup;
import net.alteiar.lendyr.game.battlemap.attack.WoundResult;
import net.alteiar.lendyr.game.gui.UiFactory;
import net.alteiar.lendyr.game.gui.Window;

import java.util.Optional;

public class AttackLayer extends Group {
  private static final int WIDTH = 400;
  private static final int HEIGHT = 300;

  private final Window background;
  private final DiceGroup diceGroup;
  private final Defense defense;
  private final WoundResult woundResult;

  private final BattleMapContext battleMapContext;

  @Builder
  AttackLayer(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext) {
    this.battleMapContext = battleMapContext;
    background = uiFactory.createWindow();
    background.setWidth(WIDTH);
    background.setHeight(HEIGHT);
    this.addActor(background);

    float topY = HEIGHT - (120 + 50);
    float remaining = (HEIGHT - 50) - topY;

    diceGroup = DiceGroup.builder().uiFactory(uiFactory).build();
    diceGroup.setX(50);
    float diceGroupY = topY + ((remaining - diceGroup.getHeight()) / 2f);
    diceGroup.setY(diceGroupY);
    this.addActor(diceGroup);

    defense = Defense.builder().uiFactory(uiFactory).build();
    defense.setX(WIDTH - (defense.getWidth() + 50));
    defense.setY(topY);
    defense.setDefense(12);
    this.addActor(defense);

    woundResult = WoundResult.builder().uiFactory(uiFactory).build();
    woundResult.setX(0);
    woundResult.setY(50);
    woundResult.setWidth(WIDTH);

    this.addActor(woundResult);

    this.setWidth(WIDTH);
    this.setHeight(HEIGHT);
    this.setVisible(false);
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    Optional<AttackResult> result = this.battleMapContext.getGameEngine().getEncounterController().popDamage();
    if (result.isPresent()) {
      Timer.schedule(new Timer.Task() {
        @Override
        public void run() {
          hide();
        }
      }, 2.0f);
      this.setAttackResult(result.get());
    }
  }

  public void setAttackResult(AttackResult result) {
    this.setVisible(true);
    this.diceGroup.setResult(result.getAttackResult());
    this.defense.setDefense(battleMapContext.getGameEngine().getCombatEntity().getCharacter(result.getTarget()).getDefense());

    if (result.getMitigatedDamage() > 0) {
      this.woundResult.setDamage(result.getMitigatedDamage());
    } else if (result.getDamage() > 0) {
      // Mitigated damage
    } else {
      // attack miss
    }
  }

  public void hide() {
    this.setVisible(false);
  }
}
