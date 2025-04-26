package net.alteiar.lendyr.game.battlemap.actor.move;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Builder;
import lombok.Getter;
import net.alteiar.lendyr.game.battlemap.CursorInfo;
import net.alteiar.lendyr.game.gui.UiFactory;
import net.alteiar.lendyr.game.state.model.CharacterEntity;

import java.util.Objects;

public class MoveGroup extends Actor {
  @Getter
  private CharacterEntity characterEntity;

  private final CursorInfo cursorInfo;
  private final PathActor path;
  private final BitmapFont font;
  private String text;

  @Builder
  public MoveGroup(UiFactory uiFactory, CharacterEntity characterEntity, CursorInfo cursorInfo) {
    this.characterEntity = characterEntity;
    this.cursorInfo = cursorInfo;

    this.text = "";
    font = uiFactory.getFont();
    font.getData().setScale(0.05f);
    font.setUseIntegerPositions(false);

    path = PathActor.builder().characterEntity(characterEntity).cursorInfo(cursorInfo).build();
  }

  public void setCharacterEntity(CharacterEntity characterEntity) {
    this.characterEntity = characterEntity;
    path.setCharacterEntity(characterEntity);
  }

  @Override
  public void act(float delta) {
    if (Objects.nonNull(characterEntity)) {
      float dist = cursorInfo.distanceToCursor(characterEntity.getPosition());
      Color color = computeColor(dist);
      font.setColor(color);
      this.text = String.format("%.1f m", dist);

      path.act(delta);
      path.setColor(color);
    }
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    path.draw(batch, parentAlpha);

    Vector3 cursorPosition = cursorInfo.getCursorPosition();
    font.draw(batch, text, cursorPosition.x + 1.5f, cursorPosition.y + 1f);
  }

  private Color computeColor(float dist) {
    if (dist > characterEntity.getSpeed()) {
      return Color.RED;
    } else if (dist < characterEntity.getSpeed() / 2) {
      return Color.GREEN;
    } else if (dist < characterEntity.getSpeed()) {
      return Color.YELLOW;
    }

    return Color.DARK_GRAY;
  }

}
