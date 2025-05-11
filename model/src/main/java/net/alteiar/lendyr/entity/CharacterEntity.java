package net.alteiar.lendyr.entity;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Data;
import net.alteiar.lendyr.grpc.model.v1.persona.LendyrAttack;
import net.alteiar.lendyr.grpc.model.v1.persona.LendyrPersona;

import java.util.UUID;

@Data
@Builder
public class CharacterEntity {
  private UUID id;
  private String name;
  private String portrait;
  private String token;
  private int maxHp;
  private int currentHp;

  private int defense;

  private Vector2 position;
  private float width;
  private float height;
  private float speed;

  private LendyrAttack attack;


  public void update(LendyrPersona persona) {
    this.maxHp = persona.getHealthPoint();
    this.currentHp = persona.getCurrentHealthPoint();
    this.position.set(persona.getPosition().getX(), persona.getPosition().getY());
    this.width = persona.getSize().getWidth();
    this.height = persona.getSize().getHeight();
    this.speed = persona.getSpeed();
    this.defense = persona.getDefense();
    this.attack = persona.getAttack();

    //persona.getAttack()
  }
}
