package net.alteiar.lendyr.game.encounter.ui.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;
import net.alteiar.lendyr.entity.CharacterEntity;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.game.encounter.controller.BattleMapUiState;
import net.alteiar.lendyr.game.encounter.ui.element.menu.Title;
import net.alteiar.lendyr.ui.shared.ViewLayer;
import net.alteiar.lendyr.ui.shared.component.*;

public class ActionMenuAlternate extends ViewLayer {
  private final BattleMapContext battleMapContext;
  private final VerticalGroup layout;
  private final Window leftMenuBackground;

  @Setter
  private MenuListener menuListener;

  private final Title nameLabel;
  private final GaugeGroup healthGauge;

  @Builder
  ActionMenuAlternate(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext) {
    super(new ScreenViewport());

    this.battleMapContext = battleMapContext;

    nameLabel = new Title(uiFactory);

    healthGauge = GaugeGroup.builder().uiFactory(uiFactory)
      .max(100).current(100)
      .gaugeColor(Color.valueOf("#a61f16"))
      .textColor(Color.BLACK)
      .build();

    Label majorActionsTitle = uiFactory.createLabel("Major actions");
    Label minorActionsTitle = uiFactory.createLabel("Minor actions");
    IconButtonActor attackIcon = uiFactory.createIconButton(IconButtonActor.ButtonType.SWORD);
    IconButtonActor walkIcon = uiFactory.createIconButton(IconButtonActor.ButtonType.WALK);

    TextButtonGroup endTurn = uiFactory.createTextButton("End Turn");

    walkIcon.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        event.setCapture(true);
        menuListener.onMoveClick();
      }
    });

    attackIcon.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.ATTACK);
        menuListener.onAttackClick();
      }
    });

    endTurn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        menuListener.onEndTurnClick();
        event.setCapture(true);
      }
    });

    layout = new VerticalGroup();
    layout.setSize(300, Gdx.graphics.getHeight());
    layout.setPosition(0, 0);
    layout.pad(10);
    layout.space(10);

    layout.addActor(nameLabel);
    layout.addActor(healthGauge);
    layout.addActor(majorActionsTitle);
    layout.addActor(attackIcon);
    layout.addActor(minorActionsTitle);
    layout.addActor(walkIcon);
    layout.addActor(endTurn);

    leftMenuBackground = uiFactory.createWindow();
    leftMenuBackground.setWidth(300);
    leftMenuBackground.setHeight(Gdx.graphics.getHeight());

    stage.addActor(leftMenuBackground);
    stage.addActor(layout);
  }

  public void act(float delta) {
    CharacterEntity currentCharacter = battleMapContext.getCombatEntity().getCurrentCharacter();
    nameLabel.setText(currentCharacter.getName());
    healthGauge.setCurrent(currentCharacter.getCurrentHp());
    healthGauge.setMax(currentCharacter.getMaxHp());
    healthGauge.setInnerTexture(currentCharacter.getToken());
    stage.act(delta);
  }

  public void resize(int width, int height) {
    super.resize(width, height);
    layout.setHeight(height);
    leftMenuBackground.setHeight(height);
  }
}
