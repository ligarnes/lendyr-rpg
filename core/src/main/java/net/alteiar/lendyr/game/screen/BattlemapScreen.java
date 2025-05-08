package net.alteiar.lendyr.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.entity.notification.NotificationMessage;
import net.alteiar.lendyr.game.battlemap.BattleMapContext;
import net.alteiar.lendyr.game.battlemap.view.*;
import net.alteiar.lendyr.game.gui.UiFactory;
import net.alteiar.lendyr.game.state.BattleMapUiState;
import net.alteiar.lendyr.game.state.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BattlemapScreen extends ScreenAdapter {
  private final Game game;
  private final UiFactory uiFactory;
  private final BattleMapContext battleMapContext;

  private MapView mapView;
  private BattleMenu menu;
  private ErrorDialog errorDialog;
  private MapOverlay mapOverlay;

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
    mapView = MapView.builder().battleMapContext(battleMapContext).uiFactory(uiFactory).build();

    menu = BattleMenu.builder().uiFactory(uiFactory).battleMapContext(battleMapContext).build();
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

    errorDialog = ErrorDialog.builder().uiFactory(uiFactory).errorMessage("Too many action").build();
    errorDialog.hide();
    errorDialog.addButtonListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        errorDialog.hide();
      }
    });

    mapOverlay = MapOverlay.builder().battleMapContext(battleMapContext).uiFactory(uiFactory).build();

    InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(errorDialog);
    multiplexer.addProcessor(menu);
    multiplexer.addProcessor(mapView);
    Gdx.input.setInputProcessor(multiplexer);

    views.add(mapView);
    views.add(mapOverlay);
    views.add(menu);
    views.add(errorDialog);

    walkCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("encounter/cursor/walk.png")), 16, 16);
    attackCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("encounter/cursor/attack.png")), 16, 16);

    mapView.setMapListener(MapListenerImpl.builder().battleMapContext(battleMapContext).errorDialog(errorDialog).build());
  }


  @Override
  public void dispose() {
    views.forEach(ViewLayer::dispose);
  }

  @Override
  public void resize(int width, int height) {
    mapView.resize(width, height);
    menu.resize(width, height);
    errorDialog.resize(width, height);
    mapOverlay.resize(width, height);
  }

  private BattleMapUiState.Action currentAction;

  private Cursor walkCursor;
  private Cursor attackCursor;


  @Override
  public void render(float delta) {
    if (this.battleMapContext.getGameEngine().isGameOver()) {
      game.setScreen(GameOverScreen.builder().game(game).assetManager(uiFactory.getAssetManager()).build());
    }
    if (this.battleMapContext.getGameEngine().isGameWon()) {
      //game.setScreen(BattlemapScreen.builder().assetManager(assetManager).worldState(worldState).build());
    }

    if (!errorDialog.isVisible()) {
      Optional<NotificationMessage> message = battleMapContext.getGameEngine().getEncounterController().popNotification();
      if (message.isPresent()) {
        errorDialog.setText(message.get().getMessage());
        errorDialog.show();
      }
    }

    if (!Objects.equals(currentAction, battleMapContext.getUiState().getCurrentAction())) {
      currentAction = battleMapContext.getUiState().getCurrentAction();
      switch (currentAction) {
        case ATTACK -> Gdx.graphics.setCursor(attackCursor);
        case MOVE -> Gdx.graphics.setCursor(walkCursor);
        default -> Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
      }
    }

    ScreenUtils.clear(0, 0, 0, 1, true);

    views.forEach(v -> v.act(delta));
    views.forEach(ViewLayer::draw);
  }

  @Override
  public void hide() {
    Gdx.input.setInputProcessor(null);
  }
}
