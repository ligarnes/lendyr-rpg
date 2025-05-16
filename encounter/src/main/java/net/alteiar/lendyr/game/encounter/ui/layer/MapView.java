package net.alteiar.lendyr.game.encounter.ui.layer;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.alteiar.lendyr.game.encounter.controller.BattleMapContext;
import net.alteiar.lendyr.game.encounter.controller.BattleMapUiState;
import net.alteiar.lendyr.game.encounter.ui.component.FullPersonaTokenComponent;
import net.alteiar.lendyr.game.encounter.ui.element.Map;
import net.alteiar.lendyr.game.encounter.ui.element.PathActor;
import net.alteiar.lendyr.game.encounter.ui.element.SquaredGrid;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.view.ViewLayer;

public class MapView extends ViewLayer {

  private final BattleMapContext battleMapContext;

  private final SquaredGrid squaredGrid;
  private final PathActor moveGroup;

  @Getter
  private final OrthographicCamera camera;

  @Setter
  private MapListener mapListener;

  // cached object to avoid instantiation.
  private final Vector3 point3d = new Vector3();

  @Builder
  public MapView(@NonNull UiFactory uiFactory, @NonNull BattleMapContext battleMapContext) {
    super(new ExtendViewport(
      battleMapContext.getCombatEntity().getWorldWidth(),
      battleMapContext.getCombatEntity().getWorldHeight(),
      new OrthographicCamera()));

    camera = (OrthographicCamera) this.stage.getCamera();
    camera.zoom = 15 / battleMapContext.getCombatEntity().getWorldWidth();

    this.battleMapContext = battleMapContext;

    Map map = Map.builder()
      .mapName(battleMapContext.getCombatEntity().getMapPath())
      .width(battleMapContext.getCombatEntity().getWorldWidth())
      .height(battleMapContext.getCombatEntity().getWorldHeight())
      .uiFactory(uiFactory)
      .build();
    stage.addActor(map);

    battleMapContext.getCombatEntity().getInitiativeOrder().forEach(state -> {
      FullPersonaTokenComponent character = FullPersonaTokenComponent.builder().personaEntity(state).uiFactory(uiFactory)
        .battleMapContext(battleMapContext).build();
      stage.addActor(character);
    });

    // Movement
    moveGroup = PathActor.builder().uiFactory(uiFactory).battleMapContext(battleMapContext).build();
    stage.addActor(moveGroup);

    battleMapContext.getUiState().setGridVisible(false);
    squaredGrid = new SquaredGrid(battleMapContext);
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
      moveGroup.setPersonaEntity(this.battleMapContext.getCombatEntity().getCurrentCharacter());
    }

    squaredGrid.setVisible(this.battleMapContext.getUiState().isGridVisible());
  }

  public void zoomIn() {

  }

  public void zoomOut() {

  }

  public void moveLeft() {
    if (camera.position.x > -1) {
      camera.translate(-3, 0, 0);
    }
  }

  public void moveRight() {
    if (camera.position.x < battleMapContext.getCombatEntity().getWorldWidth()) {
      camera.translate(+3, 0, 0);
    }
  }

  public void moveTop() {
    if (camera.position.y < battleMapContext.getCombatEntity().getWorldHeight()) {
      camera.translate(0, 3, 0);
    }
  }

  public void moveBottom() {
    if (camera.position.y < battleMapContext.getCombatEntity().getWorldHeight()) {
      camera.translate(0, -3, 0);
    }
  }

  @Override
  public boolean keyDown(int keycode) {
    if (Input.Keys.ESCAPE == keycode) {
      battleMapContext.getUiState().setCurrentAction(BattleMapUiState.Action.IDLE);
      return true;
    }

    return stage.keyDown(keycode);
  }

  @Override
  public boolean scrolled(float direction, float amount) {
    float newZoom = camera.zoom + (amount * 0.2f);
    float boundedZoom = Math.max(Math.min(newZoom, 4), 0.1f);

    if (boundedZoom != camera.zoom) {
      camera.zoom = boundedZoom;
    }

    return true;
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
