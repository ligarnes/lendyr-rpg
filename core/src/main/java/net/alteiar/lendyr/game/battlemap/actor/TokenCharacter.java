package net.alteiar.lendyr.game.battlemap.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import net.alteiar.lendyr.game.state.model.CharacterEntity;

public class TokenCharacter extends Actor {
  @Getter
  private final CharacterEntity characterEntity;
  private final Texture texture;

  @Builder
  public TokenCharacter(@NonNull CharacterEntity characterEntity, @NonNull AssetManager assetManager) {
    this.characterEntity = characterEntity;
    setName(characterEntity.getName());
    setPosition(characterEntity.getPosition().x, characterEntity.getPosition().y);
    setWidth(characterEntity.getWidth());
    setHeight(characterEntity.getHeight());

    // Load the sprite sheet as a Texture
    texture = assetManager.get(characterEntity.getToken(), Texture.class);
  }

  @Override
  public void act(float delta) {
    boolean newPosition = characterEntity.getPosition().x != getX() || characterEntity.getPosition().y != getY();
    if (newPosition && this.getActions().isEmpty()) {
      MoveToAction action = Actions.action(MoveToAction.class);
      action.setPosition(characterEntity.getPosition().x, characterEntity.getPosition().y);
      action.setDuration(1.0f);
      action.setInterpolation(Interpolation.linear);
      this.addAction(action);
    }
    super.act(delta);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    batch.draw(texture, getX(), getY(), getWidth(), getHeight());
  }
}
