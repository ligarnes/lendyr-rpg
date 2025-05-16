package net.alteiar.lendyr.ui.shared;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Builder;
import lombok.Singular;
import net.alteiar.lendyr.entity.ItemEntity;
import net.alteiar.lendyr.ui.shared.component.UiFactory;
import net.alteiar.lendyr.ui.shared.component.frame.DecoratedFrame;
import net.alteiar.lendyr.ui.shared.component.inventory.ItemList;

import java.util.ArrayList;
import java.util.List;

public class UiTestScreen implements Screen {

  private final UiFactory uiFactory;
  private Stage stage;
  private OrthographicCamera camera;

  @Builder
  public UiTestScreen(UiFactory uiFactory) {
    this.uiFactory = uiFactory;
  }

  @Override
  public void show() {
    camera = new OrthographicCamera();
    stage = new Stage(new ScreenViewport(camera));


    //table.row();

    //table.setFillParent(true);

    // stage.addActor(table);

    float windowWidth = Gdx.graphics.getWidth() / 2f;
    float windowHeight = Gdx.graphics.getHeight() / 2f;
    DecoratedFrame alternate = DecoratedFrame.builder().uiFactory(uiFactory).build();
    alternate.setX(Gdx.graphics.getWidth() / 2f - windowWidth / 2f);
    alternate.setY(Gdx.graphics.getHeight() / 2f - windowHeight / 2f);
    alternate.setWidth(windowWidth);
    alternate.setHeight(windowHeight);

    ItemList itemList = new ItemList(uiFactory);
    itemList.setWidth(windowWidth);
    itemList.setY(windowHeight);

    itemList.addItem(ItemEntity.builder().icon("").build());

    alternate.addActor(itemList);
    stage.addActor(alternate);

    Gdx.input.setInputProcessor(new InputProcessor() {
      @Override
      public boolean keyDown(int keycode) {
        return false;
      }

      @Override
      public boolean keyUp(int keycode) {
        return false;
      }

      @Override
      public boolean keyTyped(char character) {
        return false;
      }

      @Override
      public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
      }

      @Override
      public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
      }

      @Override
      public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
      }

      @Override
      public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
      }

      @Override
      public boolean mouseMoved(int screenX, int screenY) {
        return false;
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
    });
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
  }

  @Override
  public void hide() {
  }

  @Override
  public void dispose() {
    stage.dispose();
  }

  private static class TextureActor extends Actor {
    private final List<Texture> textures;

    @Builder
    public TextureActor(UiFactory uiFactory, @Singular List<String> textures) {
      this.textures = new ArrayList<>();
      for (String texture : textures) {
        this.textures.add(uiFactory.getTexture(texture));
      }
      this.setWidth(64);
      this.setHeight(64);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
      for (Texture texture : this.textures) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
      }
    }

    @Override
    public void act(float delta) {
      super.act(delta);
    }
  }
}
