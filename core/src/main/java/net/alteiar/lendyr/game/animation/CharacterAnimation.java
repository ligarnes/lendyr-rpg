package net.alteiar.lendyr.game.animation;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Builder;
import lombok.NonNull;
import net.alteiar.lendyr.game.state.model.CharacterEntity;

import java.util.Objects;

public class CharacterAnimation extends Actor {
  // Constant rows and columns of the sprite sheet
  private static final int FRAME_COLS = 9, FRAME_ROWS = 7;
  public static final int MOVE_SPEED = 50;

  static enum Activity {
    IDLE, ATTACK, MOVE
  }

  Texture walkSheet;

  Animation<TextureRegion> idleAnimation;
  Animation<TextureRegion> walkAnimation;
  Animation<TextureRegion> attackAnimation;

  // Real state
  CharacterEntity characterEntity;
  // Animation statuses
  Activity currentActivity;
  float animationTime;
  Camera camera;

  @Builder
  public CharacterAnimation(@NonNull CharacterEntity characterEntity, @NonNull Camera camera, @NonNull AssetManager assetManager) {
    this.camera = camera;

    this.characterEntity = characterEntity;
    setName("Character Animation");
    setPosition(characterEntity.getPosition().x, characterEntity.getPosition().y);
    setWidth(32);
    setHeight(32);

    this.currentActivity = Activity.IDLE;
    animationTime = 0f;

    // Load the sprite sheet as a Texture
    walkSheet = assetManager.get("assets/soldier-orcs/Characters/Soldier/Soldier/Soldier.png");

    // Use the split utility method to create a 2D array of TextureRegions. This is
    // possible because this sprite sheet contains frames of equal size and they are
    // all aligned.
    TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);

    TextureRegion[] idleFrames = new TextureRegion[6];
    System.arraycopy(tmp[0], 0, idleFrames, 0, 6);

    TextureRegion[] walkFrames = new TextureRegion[8];
    System.arraycopy(tmp[1], 0, walkFrames, 0, 8);

    TextureRegion[] attackFrames = new TextureRegion[6];
    System.arraycopy(tmp[2], 0, attackFrames, 0, 6);


    // Initialize the Animation with the frame interval and array of frames
    idleAnimation = new Animation<>(0.1f, idleFrames);
    walkAnimation = new Animation<>(0.1f, walkFrames);
    attackAnimation = new Animation<>(0.1f, attackFrames);
  }


  @Override
  public void act(float delta) {
    animationTime += delta;

    if (currentActivity == Activity.MOVE) {
      computeNewPosition(delta);
    }

    // Change activity
    Vector2 currentPosition = new Vector2(getX(), getY());
    if (!Objects.equals(currentPosition, characterEntity.getPosition()) && currentActivity != Activity.MOVE) {
      currentActivity = Activity.MOVE;
      animationTime = 0f;
    }
    if (Objects.equals(currentPosition, characterEntity.getPosition()) && currentActivity == Activity.MOVE) {
      currentActivity = Activity.IDLE;
      animationTime = 0f;
    }
  }

  private void computeNewPosition(float elapsedTime) {
    float maxMoveDistance = elapsedTime * MOVE_SPEED;
    float moveX = 0;
    if (characterEntity.getPosition().x != getX()) {
      float remainingXDistance = characterEntity.getPosition().x - getX();
      if (remainingXDistance > 0) {
        moveX = Math.min(remainingXDistance, maxMoveDistance);
      } else if (remainingXDistance < 0) {
        moveX = Math.max(remainingXDistance, -maxMoveDistance);
      }
    }

    float moveY = 0;
    if (characterEntity.getPosition().y != getY()) {
      float remainingYDistance = characterEntity.getPosition().y - getY();
      if (remainingYDistance > 0) {
        moveY = Math.min(remainingYDistance, maxMoveDistance);
      } else if (remainingYDistance < 0) {
        moveY = Math.max(remainingYDistance, -maxMoveDistance);
      }
    }

    moveBy(moveX, moveY);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    TextureRegion currentFrame = getAnimation().getKeyFrame(animationTime, true);

    batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(),
      getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
  }

  private Animation<TextureRegion> getAnimation() {
    return switch (currentActivity) {
      case IDLE -> idleAnimation;
      case MOVE -> walkAnimation;
      case ATTACK -> attackAnimation;
    };
  }
}
