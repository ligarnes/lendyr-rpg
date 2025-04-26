package net.alteiar.lendyr.game.state.model;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CharacterEntity {
  private String name;
  private String token;
  private int maxHp;
  private int currentHp;

  private Vector2 position;
  private float width;
  private float height;
  private float speed;
}
