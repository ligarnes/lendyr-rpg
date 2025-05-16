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
import net.alteiar.lendyr.entity.PersonaEntity;
import net.alteiar.lendyr.ui.shared.component.UiFactory;

public class TokenCharacter extends Actor {
  @Getter
  private final PersonaEntity personaEntity;
  private final Texture texture;
  private final Texture targetedMeleeOverlay;
  private final Texture targetedRangedOverlay;
  @Getter
  @Setter
  private boolean targeted;

  @Builder
  public TokenCharacter(@NonNull PersonaEntity personaEntity, @NonNull UiFactory uiFactory) {
    this.personaEntity = personaEntity;
    setName(personaEntity.getName());
    setPosition(personaEntity.getPosition().x, personaEntity.getPosition().y);
    setWidth(personaEntity.getWidth());
    setHeight(personaEntity.getHeight());

    // Load the sprite sheet as a Texture
    texture = uiFactory.getTexture(personaEntity.getToken());
    targetedMeleeOverlay = uiFactory.getTexture("icon/TabletopBadges_47_overlay.png");
    targetedRangedOverlay = uiFactory.getTexture("icon/TabletopBadges_29_overlay.png");

    this.targeted = false;
  }

  @Override
  public void act(float delta) {
    boolean newPosition = personaEntity.getPosition().x != getX() || personaEntity.getPosition().y != getY();
    if (newPosition && this.getActions().isEmpty()) {
      MoveToAction action = Actions.action(MoveToAction.class);
      action.setPosition(personaEntity.getPosition().x, personaEntity.getPosition().y);
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
      batch.draw(targetedMeleeOverlay, getX(), getY(), getWidth(), getHeight());
    }
  }
}
