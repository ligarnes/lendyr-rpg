package net.alteiar.lendyr.game.encounter.ui.element.persona;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.alteiar.lendyr.entity.CharacterEntity;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class TokenCharacter extends Actor {
  @Getter
  private final CharacterEntity characterEntity;
  private final Texture texture;
  private final Texture targetedOverlay;
  @Getter
  @Setter
  private boolean targeted;

  @Builder
  public TokenCharacter(@NonNull CharacterEntity characterEntity, @NonNull UiFactory uiFactory) {
    this.characterEntity = characterEntity;
    setName(characterEntity.getName());
    setPosition(characterEntity.getPosition().x, characterEntity.getPosition().y);
    setWidth(characterEntity.getWidth());
    setHeight(characterEntity.getHeight());

    // Load the sprite sheet as a Texture
    texture = uiFactory.getTexture(characterEntity.getToken());
    targetedOverlay = uiFactory.getTexture("icon/TabletopBadges_47_overlay.png");

    this.targeted = false;
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
    if (targeted) {
      batch.draw(targetedOverlay, getX(), getY(), getWidth(), getHeight());
    }
  }
}
