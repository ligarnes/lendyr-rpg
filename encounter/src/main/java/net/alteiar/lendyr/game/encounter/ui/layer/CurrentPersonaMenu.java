package net.alteiar.lendyr.game.encounter.ui.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.game.encounter.controller.BattleMapUiState;
import net.alteiar.lendyr.grpc.model.v1.persona.AttackType;
import net.alteiar.lendyr.ui.shared.component.*;
import net.alteiar.lendyr.ui.shared.component.frame.DecoratedFrame;
import net.alteiar.lendyr.ui.shared.element.LargeTitle;
import net.alteiar.lendyr.ui.shared.element.SmallTitle;
import net.alteiar.lendyr.ui.shared.element.button.TextButtonLarge;
import net.alteiar.lendyr.ui.shared.view.ViewLayer;

public class CurrentPersonaMenu extends ViewLayer {
  private final BattleMapContext battleMapContext;
  private final Table layout;
  private final DecoratedFrame frame;

  private final ActionTitle majorActionTitle;
  private final ActionTitle minorActionTitle;

  private final ActionSelector attackMeleeSelector;
  private final ActionSelector attackRangeSelector;
  private final ActionSelector walkSelector;

  @Setter
  private MenuListener menuListener;

  private final LargeTitle nameLabel;
  private final GaugeGroup healthGauge;

  @Builder
  CurrentPersonaMenu(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext) {
    super(new ScreenViewport());

    this.battleMapContext = battleMapContext;

    nameLabel = LargeTitle.builder().uiFactory(uiFactory).build();

    healthGauge = GaugeGroup.builder().uiFactory(uiFactory)
      .max(100).current(100)
      .gaugeColor(Color.valueOf("#a61f16"))
      .textColor(Color.BLACK)
      .build();

    majorActionTitle = ActionTitle.builder().uiFactory(uiFactory).title("Major actions").build();
    minorActionTitle = ActionTitle.builder().uiFactory(uiFactory).title("Minor actions").build();

    attackMeleeSelector = uiFactory.createActionSelector(ActionSelectorFactory.Icon.MELEE_ATTACK);
    attackMeleeSelector.addClickListener(() -> menuListener.onMeleeAttackClick());
    attackRangeSelector = uiFactory.createActionSelector(ActionSelectorFactory.Icon.RANGE_ATTACK);
    attackRangeSelector.addClickListener(() -> menuListener.onRangeAttackClick());

    walkSelector = uiFactory.createActionSelector(ActionSelectorFactory.Icon.MOVE);
    walkSelector.addClickListener(() -> menuListener.onMoveClick());

    TextButtonLarge endTurn = TextButtonLarge.builder().uiFactory(uiFactory).text("End Turn").build();
    endTurn.setSize(200, 60);

    endTurn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        menuListener.onEndTurnClick();
        event.setCapture(true);
      }
    });

    HorizontalGroup majorActionRow = new HorizontalGroup();
    majorActionRow.space(5);
    majorActionRow.addActor(attackMeleeSelector);
    majorActionRow.addActor(attackRangeSelector);

    HorizontalGroup minorActionRow = new HorizontalGroup();
    minorActionRow.addActor(walkSelector);

    layout = new Table();
    layout.setSize(300, Gdx.graphics.getHeight());
    layout.setPosition(0, 0);
    layout.padTop(30);
    layout.align(Align.top);

    layout.add(nameLabel);
    layout.row();
    layout.add(healthGauge).space(10);
    layout.row();
    layout.add(majorActionTitle).center().space(5);
    layout.row();
    layout.add(majorActionRow).spaceBottom(10);
    layout.row();
    layout.add(minorActionTitle).center().space(5);
    layout.row();
    layout.add(minorActionRow).spaceBottom(10);
    layout.row();
    layout.add(endTurn);

    frame = uiFactory.createDecoratedFrame();
    frame.setWidth(300);
    frame.setHeight(Gdx.graphics.getHeight());
    frame.addActor(layout);

    stage.addActor(frame);
  }

  public void act(float delta) {
    PersonaEntity currentCharacter = battleMapContext.getCombatEntity().getCurrentCharacter();
    nameLabel.setText(currentCharacter.getName());
    healthGauge.setCurrent(currentCharacter.getCurrentHp());
    healthGauge.setMax(currentCharacter.getMaxHp());
    healthGauge.setInnerTexture(currentCharacter.getToken());

    // Major actions
    boolean isMeleeDisabled = battleMapContext.getCombatEntity().isMajorActionUsed() || currentCharacter.getAttack().getAttackType() != AttackType.MELEE;
    boolean isMeleeSelected = battleMapContext.getUiState().getCurrentAction() == BattleMapUiState.Action.MELEE_ATTACK && !isMeleeDisabled;
    attackMeleeSelector.setSelected(isMeleeSelected);
    attackMeleeSelector.setDisabled(isMeleeDisabled);

    boolean isRangeDisabled = battleMapContext.getCombatEntity().isMajorActionUsed() || currentCharacter.getAttack().getAttackType() != AttackType.RANGE;
    boolean isRangeSelected = battleMapContext.getUiState().getCurrentAction() == BattleMapUiState.Action.RANGE_ATTACK && !isRangeDisabled;
    attackRangeSelector.setSelected(isRangeSelected);
    attackRangeSelector.setDisabled(isRangeDisabled);

    // Minor actions
    boolean isMoveDisabled = battleMapContext.getCombatEntity().isMinorActionUsed();
    boolean isMoveSelected = battleMapContext.getUiState().getCurrentAction() == BattleMapUiState.Action.MOVE && !isMoveDisabled;
    walkSelector.setSelected(isMoveSelected);
    walkSelector.setDisabled(isMoveDisabled);

    majorActionTitle.setSelected(!battleMapContext.getCombatEntity().isMajorActionUsed());
    minorActionTitle.setSelected(!battleMapContext.getCombatEntity().isMinorActionUsed());

    stage.act(delta);
  }

  public void resize(int width, int height) {
    super.resize(width, height);
    layout.setHeight(height);
    frame.setHeight(height);
  }

  private static class ActionTitle extends WidgetGroup {
    private final RadioButton actived;

    @Builder
    ActionTitle(UiFactory uiFactory, String title) {
      SmallTitle titleLabel = SmallTitle.builder().uiFactory(uiFactory).title(title).build();
      actived = uiFactory.createRadioButton();
      actived.setPosition(titleLabel.getWidth() - 50, titleLabel.getHeight() / 2f - actived.getHeight() / 2f);

      this.addActor(titleLabel);
      this.addActor(actived);

      this.setWidth(titleLabel.getWidth());
      this.setHeight(titleLabel.getHeight());
    }

    public float getPrefWidth() {
      return getWidth();
    }

    public float getPrefHeight() {
      return getHeight();
    }

    public float getMaxWidth() {
      return getWidth();
    }

    public float getMaxHeight() {
      return getHeight();
    }

    public void setSelected(boolean isSelected) {
      this.actived.setSelected(isSelected);
    }
  }
}
