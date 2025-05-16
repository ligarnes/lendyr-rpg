package net.alteiar.lendyr.game.encounter;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.ScreenUtils;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.game.encounter.controller.BattleMapUiState;
import net.alteiar.lendyr.game.encounter.ui.layer.*;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.screen.GameOverScreen;
import net.alteiar.lendyr.ui.shared.view.ViewLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BattlemapScreen extends ScreenAdapter {
  private final Game game;
  private final UiFactory uiFactory;
  private final BattleMapContext battleMapContext;

  private BattleMapUiState.Action currentAction;

  private Cursor walkCursor;
  private Cursor attackCursor;

  private final List<ViewLayer> views;

  @Builder
  public BattlemapScreen(@NonNull Game game, @NonNull GameEngine gameEngine, @NonNull AssetManager assetManager) {
    this.game = game;
    this.uiFactory = UiFactory.builder().assetManager(assetManager).build();

    BattleMapUiState uiState = BattleMapUiState.builder().currentAction(BattleMapUiState.Action.IDLE).isGridVisible(false).build();

    battleMapContext = BattleMapContext.builder().uiState(uiState).gameEngine(gameEngine).build();
    views = new ArrayList<>();
  }

  @Override
  public void show() {
    MapView mapView = MapView.builder().battleMapContext(battleMapContext).uiFactory(uiFactory).build();

    CurrentPersonaMenu menu = CurrentPersonaMenu.builder().uiFactory(uiFactory).battleMapContext(battleMapContext).build();

    // BattleMenu menu = BattleMenu.builder().uiFactory(uiFactory).battleMapContext(battleMapContext).build();
    menu.setMenuListener(new MenuListener() {
      @Override
      public void onMoveClick() {
        if (battleMapContext.getUiState().getCurrentAction() == BattleMapUiState.Action.MOVE) {
          battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.IDLE);
        } else {
          battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.MOVE);
        }
      }

      @Override
      public void onMeleeAttackClick() {
        if (battleMapContext.getUiState().getCurrentAction() == BattleMapUiState.Action.MELEE_ATTACK) {
          battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.IDLE);
        } else {
          battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.MELEE_ATTACK);
        }
      }

      @Override
      public void onRangeAttackClick() {
        if (battleMapContext.getUiState().getCurrentAction() == BattleMapUiState.Action.RANGE_ATTACK) {
          battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.IDLE);
        } else {
          battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.RANGE_ATTACK);
        }
      }

      @Override
      public void onEndTurnClick() {
        battleMapContext.getGameEngine().endCharacterTurn();
      }
    });

    //NotificationDialog notificationDialog = NotificationDialog.builder().uiFactory(uiFactory).battleMapContext(battleMapContext).build();
    MapOverlay mapOverlay = MapOverlay.builder().battleMapContext(battleMapContext).uiFactory(uiFactory).build();

    NotificationLayer notificationLayer = NotificationLayer.builder().uiFactory(uiFactory).battleMapContext(battleMapContext).build();

    InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(notificationLayer);
    multiplexer.addProcessor(menu);
    multiplexer.addProcessor(mapView);
    multiplexer.addProcessor(new InputAdapter() {

      @Override
      public boolean keyTyped(char character) {
        if (Objects.equals("a".toCharArray()[0], character)) {
          mapView.moveLeft();
          return true;
        }
        if (Objects.equals("d".toCharArray()[0], character)) {
          mapView.moveRight();
          return true;
        }
        if (Objects.equals("w".toCharArray()[0], character)) {
          mapView.moveTop();
          return true;
        }
        if (Objects.equals("s".toCharArray()[0], character)) {
          mapView.moveBottom();
          return true;
        }
        if (Objects.equals("i".toCharArray()[0], character)) {
          notificationLayer.showHideInventory();
          return true;
        }
        return false;
      }
    });
    Gdx.input.setInputProcessor(multiplexer);

    views.add(mapView);
    views.add(mapOverlay);
    views.add(menu);
    views.add(notificationLayer);

    walkCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("encounter/cursor/walk.png")), 16, 16);
    attackCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("encounter/cursor/attack.png")), 16, 16);

    mapView.setMapListener(MapListenerImpl.builder().battleMapContext(battleMapContext).build());
  }

  @Override
  public void dispose() {
    views.forEach(ViewLayer::dispose);
  }

  @Override
  public void resize(int width, int height) {
    views.forEach(v -> v.resize(width, height));
  }

  @Override
  public void render(float delta) {
    if (this.battleMapContext.getGameEngine().isGameOver()) {
      game.setScreen(GameOverScreen.builder().game(game).assetManager(uiFactory.getAssetManager()).build());
    }
    if (this.battleMapContext.getGameEngine().isGameWon()) {
      //game.setScreen(BattlemapScreen.builder().assetManager(assetManager).worldState(worldState).build());
    }

    if (!Objects.equals(currentAction, battleMapContext.getUiState().getCurrentAction())) {
      currentAction = battleMapContext.getUiState().getCurrentAction();
      switch (currentAction) {
        case MELEE_ATTACK -> Gdx.graphics.setCursor(attackCursor);
        case MOVE -> Gdx.graphics.setCursor(walkCursor);
        default -> Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
      }
    }

    ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

    views.forEach(v -> v.act(delta));
    views.forEach(ViewLayer::draw);
  }

  @Override
  public void hide() {
    Gdx.input.setInputProcessor(null);
  }
}
