package net.alteiar.lendyr.game.encounter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.ScreenUtils;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.game.encounter.controller.BattleMapUiState;
import net.alteiar.lendyr.game.encounter.ui.layer.*;
import net.alteiar.lendyr.ui.shared.ViewLayer;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.screen.GameOverScreen;

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

    ActionMenuAlternate menu = ActionMenuAlternate.builder().uiFactory(uiFactory).battleMapContext(battleMapContext).build();

    // BattleMenu menu = BattleMenu.builder().uiFactory(uiFactory).battleMapContext(battleMapContext).build();
    menu.setMenuListener(new MenuListener() {
      @Override
      public void onMoveClick() {
        battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.MOVE);
      }

      @Override
      public void onAttackClick() {
        battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.ATTACK);
      }

      @Override
      public void onEndTurnClick() {
        battleMapContext.getGameEngine().endCharacterTurn();
      }
    });

    NotificationDialog notificationDialog = NotificationDialog.builder().uiFactory(uiFactory).battleMapContext(battleMapContext).build();
    MapOverlay mapOverlay = MapOverlay.builder().battleMapContext(battleMapContext).uiFactory(uiFactory).build();

    InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(notificationDialog);
    multiplexer.addProcessor(menu);
    multiplexer.addProcessor(mapView);
    Gdx.input.setInputProcessor(multiplexer);

    views.add(mapView);
    views.add(mapOverlay);
    views.add(menu);
    views.add(notificationDialog);

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
        case ATTACK -> Gdx.graphics.setCursor(attackCursor);
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
