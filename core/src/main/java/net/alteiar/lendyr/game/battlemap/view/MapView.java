package net.alteiar.lendyr.game.battlemap.view;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.alteiar.lendyr.game.Constants;
import net.alteiar.lendyr.game.battlemap.BattleMapContext;
import net.alteiar.lendyr.game.battlemap.CursorInfo;
import net.alteiar.lendyr.game.battlemap.actor.SquaredGrid;
import net.alteiar.lendyr.game.battlemap.actor.TokenCharacter;
import net.alteiar.lendyr.game.battlemap.actor.move.PathActor;
import net.alteiar.lendyr.game.gui.UiFactory;
import net.alteiar.lendyr.game.state.BattleMapUiState;

import java.util.Objects;

public class MapView extends ViewLayer {

  private BattleMapContext battleMapContext;

  private Sprite mapSprite;
  private SquaredGrid squaredGrid;
  private PathActor moveGroup;

  @Getter
  private OrthographicCamera camera;
  private CursorInfo cursorInfo;

  @Setter
  private MapListener mapListener;

  // cached object to avoid instantiation.
  private Vector3 point3d = new Vector3();

  @Builder
  public MapView(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext) {
    super(new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, new OrthographicCamera()));

    camera = (OrthographicCamera) this.stage.getCamera();
    cursorInfo = CursorInfo.builder().camera(camera).build();

    stage.setDebugAll(true);

    this.battleMapContext = battleMapContext;

    battleMapContext.getCombatEntity().getInitiativeOrder().forEach(state -> {
      TokenCharacter character = TokenCharacter.builder().characterEntity(state).uiFactory(uiFactory).build();
      character.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          mapListener.onCharacterClick(character);
        }

        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
          mapListener.onCharacterEnter(character);
        }

        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
          mapListener.onCharacterExit(character);
        }
      });
      stage.addActor(character);
    });

    mapSprite = new Sprite(uiFactory.getAssetManager().get("maps/map.jpg", Texture.class));
    mapSprite.setPosition(0, 0);
    mapSprite.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

    // Movement
    moveGroup = PathActor.builder().uiFactory(uiFactory).cursorInfo(cursorInfo).build();
    stage.addActor(moveGroup);

    squaredGrid = new SquaredGrid();
    squaredGrid.setVisible(false);
    stage.addActor(squaredGrid);
  }

  @Override
  public void dispose() {
    stage.dispose();
    squaredGrid.dispose();
  }

  @Override
  public void act(float delta) {
    stage.act(delta);

    moveGroup.setVisible(this.battleMapContext.getUiState().getCurrentAction() == BattleMapUiState.Action.MOVE);
    if (this.battleMapContext.getUiState().getCurrentAction() == BattleMapUiState.Action.MOVE) {
      moveGroup.setCharacterEntity(this.battleMapContext.getCombatEntity().getCurrentCharacter());
    }

    squaredGrid.setVisible(this.battleMapContext.getUiState().isGridVisible());
  }

  @Override
  public void draw() {
    ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
    camera.update();
    cursorInfo.update();


    stage.getBatch().begin();
    mapSprite.draw(stage.getBatch());
    stage.getBatch().end();

    stage.draw();
  }


  @Override
  public boolean keyTyped(char c) {
    if (Objects.equals("a".toCharArray()[0], c)) {
      if (camera.position.x > -1) {
        camera.translate(-3, 0, 0);
      }
    }
    if (Objects.equals("d".toCharArray()[0], c)) {
      if (camera.position.x < Constants.WORLD_WIDTH) {
        camera.translate(+3, 0, 0);
      }
    }
    if (Objects.equals("w".toCharArray()[0], c)) {
      if (camera.position.y < Constants.WORLD_HEIGHT) {
        camera.translate(0, 3, 0);
      }
    }
    if (Objects.equals("s".toCharArray()[0], c)) {
      if (camera.position.y > -1) {
        camera.translate(0, -3, 0);
      }
    }

    return false;
  }

  @Override
  public boolean scrolled(float direction, float amount) {
    float newZoom = camera.zoom + (amount * 0.2f);
    camera.zoom = Math.max(Math.min(newZoom, 4), 0.1f);
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    boolean isClicked = super.touchDown(screenX, screenY, pointer, button);
    if (isClicked) {
      return true;
    }

    // ignore if its not left mouse button or first touch pointer
    if (button != Input.Buttons.LEFT || pointer > 0) return false;

    Vector3 newPosition = camera.unproject(point3d.set(screenX, screenY, 0));
    return mapListener.onMapClick(newPosition);
  }
}
