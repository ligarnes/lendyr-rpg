package net.alteiar.lendyr.game.battlemap.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.alteiar.lendyr.entity.CharacterEntity;
import net.alteiar.lendyr.game.gui.UiFactory;

public class TokenCharacter extends Actor {
  @Getter
  private final CharacterEntity characterEntity;
  private final Texture texture;
  private final Texture targetedOverlay;
  private boolean isTextVisible;
  private final StringBuilder text;
  private final BitmapFont font;
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
    this.font = uiFactory.getFont();
    font.setUseIntegerPositions(false);
    this.text = new StringBuilder();

    this.targeted = false;
  }

  public void showText(String text) {
    this.text.setLength(0);
    this.text.append(text);
    this.isTextVisible = true;
  }

  public void hideText() {
    this.isTextVisible = false;
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

    if (isTextVisible) {
      font.setColor(Color.RED);
      font.getData().setScale(0.01f);
      font.draw(batch, text, getX() + 1.5f, getY() + 1f);
      font.getData().setScale(1f);
    }
  }
}
