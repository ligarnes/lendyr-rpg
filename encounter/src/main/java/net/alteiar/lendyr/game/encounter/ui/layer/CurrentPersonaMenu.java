package net.alteiar.lendyr.game.encounter.ui.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.ui.shared.component.*;
import net.alteiar.lendyr.ui.shared.component.frame.DecoratedFrame;
import net.alteiar.lendyr.ui.shared.element.LargeTitle;
import net.alteiar.lendyr.ui.shared.view.ViewLayer;

public class CurrentPersonaMenu extends ViewLayer {
  private final BattleMapContext battleMapContext;
  private final Table layout;
  private final DecoratedFrame frame;

  private final RadioButton majorAction;
  private final RadioButton minorAction;

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

    Label majorActionsTitle = uiFactory.createLabel("Major actions");
    Label minorActionsTitle = uiFactory.createLabel("Minor actions");

    majorAction = uiFactory.createRadioButton();
    minorAction = uiFactory.createRadioButton();

    attackMeleeSelector = uiFactory.createActionSelector(ActionSelectorFactory.Icon.MELEE_ATTACK);
    attackMeleeSelector.addClickListener(() -> menuListener.onMeleeAttackClick());
    attackRangeSelector = uiFactory.createActionSelector(ActionSelectorFactory.Icon.RANGE_ATTACK);
    attackRangeSelector.addClickListener(() -> menuListener.onRangeAttackClick());

    walkSelector = uiFactory.createActionSelector(ActionSelectorFactory.Icon.MOVE);
    walkSelector.addClickListener(() -> menuListener.onMoveClick());

    TextButtonGroup endTurn = uiFactory.createTextButton("End Turn");

    endTurn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        menuListener.onEndTurnClick();
        event.setCapture(true);
      }
    });

    HorizontalGroup majorActionTitleRow = new HorizontalGroup();
    majorActionTitleRow.space(10);
    majorActionTitleRow.addActor(majorActionsTitle);
    majorActionTitleRow.addActor(majorAction);

    HorizontalGroup majorActionRow = new HorizontalGroup();
    majorActionRow.space(5);
    majorActionRow.addActor(attackMeleeSelector);
    majorActionRow.addActor(attackRangeSelector);

    HorizontalGroup minorActionTitleRow = new HorizontalGroup();
    minorActionTitleRow.space(10);
    minorActionTitleRow.addActor(minorActionsTitle);
    minorActionTitleRow.addActor(minorAction);

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
    layout.add(majorActionTitleRow).space(5);
    layout.row();
    layout.add(majorActionRow).spaceBottom(10);
    layout.row();
    layout.add(minorActionTitleRow).space(5);
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

    attackMeleeSelector.setSelected(false);
    attackRangeSelector.setSelected(false);
    walkSelector.setSelected(false);

    switch (battleMapContext.getUiState().getCurrentAction()) {
      case MELEE_ATTACK -> attackMeleeSelector.setSelected(true);
      case RANGE_ATTACK -> attackRangeSelector.setSelected(true);
      case MOVE -> walkSelector.setSelected(true);
    }

    majorAction.setSelected(!battleMapContext.getCombatEntity().isMajorActionUsed());
    minorAction.setSelected(!battleMapContext.getCombatEntity().isMinorActionUsed());

    stage.act(delta);
  }

  public void resize(int width, int height) {
    super.resize(width, height);
    layout.setHeight(height);
    frame.setHeight(height);
  }
}
