package net.alteiar.lendyr.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.game.Constants;
import net.alteiar.lendyr.game.battlemap.BattleMapContext;
import net.alteiar.lendyr.game.battlemap.actor.TokenCharacter;
import net.alteiar.lendyr.game.battlemap.menu.*;
import net.alteiar.lendyr.game.gui.UiFactory;
import net.alteiar.lendyr.game.state.BattleMapUiState;
import net.alteiar.lendyr.game.state.GameEngine;
import net.alteiar.lendyr.game.state.WorldState;
import net.alteiar.lendyr.game.state.model.CharacterEntity;

import java.util.ArrayList;
import java.util.List;

public class BattlemapScreen extends ScreenAdapter {
  private final UiFactory uiFactory;
  private final BattleMapContext battleMapContext;

  private MapView mapView;
  private BattleMenu menu;
  private ErrorDialog errorDialog;

  private List<ViewLayer> views;


  @Builder
  public BattlemapScreen(@NonNull WorldState worldState, @NonNull AssetManager assetManager) {
    this.uiFactory = UiFactory.builder().assetManager(assetManager).build();

    BattleMapUiState uiState = BattleMapUiState.builder().currentAction(BattleMapUiState.Action.IDLE).isGridVisible(false).build();
    GameEngine gameEngine = GameEngine.builder().world(worldState).build();

    battleMapContext = BattleMapContext.builder().worldState(worldState).uiState(uiState).gameEngine(gameEngine).build();
    views = new ArrayList<>();
  }

  @Override
  public void show() {
    mapView = MapView.builder().battleMapContext(battleMapContext).uiFactory(uiFactory).build();
    mapView.setMapListener(new MapListener() {

      @Override
      public void onCharacterClick(TokenCharacter character) {
        if (BattleMapUiState.Action.ATTACK.equals(battleMapContext.getUiState().getCurrentAction())) {
          try {
            battleMapContext.getGameEngine().attack(battleMapContext.getWorldState().getCurrentCharacter(), character.getCharacterEntity());
          } catch (RuntimeException e) {
            errorDialog.setText(e.getMessage());
            errorDialog.show();
          }
        }
      }

      @Override
      public void onCharacterEnter(TokenCharacter character) {
        if (BattleMapUiState.Action.ATTACK.equals(battleMapContext.getUiState().getCurrentAction())) {
        }
      }

      @Override
      public void onCharacterExit(TokenCharacter character) {

      }

      @Override
      public boolean onMapClick(Vector3 newPosition) {
        if (!BattleMapUiState.Action.MOVE.equals(battleMapContext.getUiState().getCurrentAction())) {
          return false;
        }

        try {
          CharacterEntity character = battleMapContext.getWorldState().getCurrentCharacter();

          float xCentered = Math.max(0, Math.min(Constants.WORLD_WIDTH - character.getWidth(), newPosition.x - (character.getWidth() / 2)));
          float yCentered = Math.max(0, Math.min(Constants.WORLD_HEIGHT - character.getHeight(), newPosition.y - (character.getHeight() / 2)));

          battleMapContext.getGameEngine().move(character, new Vector2(xCentered, yCentered));
        } catch (RuntimeException e) {
          errorDialog.setText(e.getMessage());
          errorDialog.show();
        }
        return true;
      }
    });

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
    errorDialog.show();
    errorDialog.addButtonListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        errorDialog.hide();
      }
    });

    InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(errorDialog);
    multiplexer.addProcessor(menu);
    multiplexer.addProcessor(mapView);
    // multiplexer.addProcessor(PlayerInputProcessor.builder().battleMapContext(battleMapContext).camera(mapView.getCamera()).build());
    Gdx.input.setInputProcessor(multiplexer);

    views.add(mapView);
    views.add(menu);
    views.add(errorDialog);
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
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1, true);

    views.forEach(v -> v.act(delta));
    views.forEach(ViewLayer::draw);
  }

  @Override
  public void hide() {
    Gdx.input.setInputProcessor(null);
  }
}
