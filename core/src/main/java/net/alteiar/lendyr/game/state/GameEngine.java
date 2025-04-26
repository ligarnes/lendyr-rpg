package net.alteiar.lendyr.game.state;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import net.alteiar.lendyr.game.state.model.CharacterEntity;

import java.util.Objects;
import java.util.Random;


public class GameEngine {
  private final WorldState world;

  private final Random random;

  @Getter
  private int totalAction;
  @Getter
  private int remainingAction;

  @Builder
  public GameEngine(@NonNull WorldState world) {
    this.world = world;
    this.totalAction = 2;
    this.remainingAction = 2;
    random = new Random();
  }

  private void validateActions() {
    if (remainingAction <= 0) {
      throw new IllegalStateException("No action remaining");
    }
  }

  public void move(CharacterEntity character, Vector2 newPosition) {
    validateActions();
    if (!Objects.equals(world.getCurrentCharacter(), character)) {
      throw new IllegalStateException("Character move not allowed; it's not your turn");
    }
    character.setPosition(newPosition);
    this.remainingAction--;
  }

  public void attack(CharacterEntity from, CharacterEntity to) {
    validateActions();
    int damage = random.nextInt(6);
    to.setCurrentHp(to.getCurrentHp() - damage);
    this.remainingAction--;
  }

  public void endCharacterTurn() {
    int nextCharacterIdx = world.getCurrentCharacterIdx() + 1;
    if (nextCharacterIdx >= world.getCharacterEntities().size()) {
      newTurn();
    } else {
      world.setCurrentCharacterIdx(nextCharacterIdx);
    }
    this.remainingAction = 2;
  }

  private void newTurn() {
    world.setCurrentTurn(world.getCurrentTurn() + 1);
    world.setCurrentCharacterIdx(0);
  }
}
