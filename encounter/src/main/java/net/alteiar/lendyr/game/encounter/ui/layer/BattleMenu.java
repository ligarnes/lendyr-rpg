package net.alteiar.lendyr.game.encounter.ui.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;
import net.alteiar.lendyr.entity.CharacterEntity;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.game.encounter.controller.BattleMapUiState;
import net.alteiar.lendyr.game.encounter.ui.element.CharacterActionCounter;
import net.alteiar.lendyr.ui.shared.ViewLayer;
import net.alteiar.lendyr.ui.shared.component.*;

public class BattleMenu extends ViewLayer {
  private final VerticalGroup layout;
  private final GaugeGroup healthGauge;
  private final CharacterActionCounter characterActionCounter;

  private final Window leftMenuBackground;

  private final BattleMapContext battleMapContext;

  @Setter
  private MenuListener menuListener;

  @Builder
  BattleMenu(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext) {
    super(new ScreenViewport());

    this.battleMapContext = battleMapContext;

    healthGauge = GaugeGroup.builder().uiFactory(uiFactory)
      .max(100).current(100)
      .gaugeColor(Color.valueOf("#a61f16"))
      .textColor(Color.BLACK)
      .build();

    IconButtonActor attackIcon = IconButtonActor.builder().assetManager(uiFactory.getAssetManager()).buttonType(IconButtonActor.ButtonType.SWORD).build();
    IconButtonActor walkIcon = IconButtonActor.builder().assetManager(uiFactory.getAssetManager()).buttonType(IconButtonActor.ButtonType.WALK).build();

    TextButtonGroup moveTextButton = uiFactory.createTextButton("Move");
    TextButtonGroup attackTextButton = uiFactory.createTextButton("Attack !");

    TextButtonGroup endTurn = uiFactory.createTextButton("End Turn");

    characterActionCounter = CharacterActionCounter.builder().assetManager(uiFactory.getAssetManager()).build();

    moveTextButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        event.setCapture(true);
        menuListener.onMoveClick();
      }
    });

    attackTextButton.addListener(new ClickListener() {
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

    HorizontalGroup paginationGroup = new HorizontalGroup();
    paginationGroup.setSize(250, Gdx.graphics.getHeight());
    paginationGroup.space(10);

    HorizontalGroup attackGroup = new HorizontalGroup();
    attackGroup.space(10);
    attackGroup.addActor(attackIcon);
    attackGroup.addActor(attackTextButton);

    HorizontalGroup moveGroup = new HorizontalGroup();
    moveGroup.space(10);
    moveGroup.addActor(walkIcon);
    moveGroup.addActor(moveTextButton);

    layout.addActor(healthGauge);
    layout.addActor(moveGroup);
    layout.addActor(attackGroup);
    layout.addActor(endTurn);


    layout.addActor(characterActionCounter);

    leftMenuBackground = uiFactory.createWindow();
    leftMenuBackground.setWidth(300);
    leftMenuBackground.setHeight(Gdx.graphics.getHeight());

    stage.addActor(leftMenuBackground);
    stage.addActor(layout);
  }

  public void act(float delta) {
    CharacterEntity currentCharacter = battleMapContext.getCombatEntity().getCurrentCharacter();
    healthGauge.setCurrent(currentCharacter.getCurrentHp());
    healthGauge.setMax(currentCharacter.getMaxHp());
    healthGauge.setInnerTexture(currentCharacter.getToken());

    characterActionCounter.setProgress(2);
    characterActionCounter.setMaxProgress(2);

    stage.act(delta);
  }

  public void resize(int width, int height) {
    super.resize(width, height);
    layout.setHeight(height);
    leftMenuBackground.setHeight(height);
  }

  public void dispose() {
    stage.dispose();
    healthGauge.dispose();
  }
}
