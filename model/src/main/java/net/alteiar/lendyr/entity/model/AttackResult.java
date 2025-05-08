package net.alteiar.lendyr.entity.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AttackResult {
  private UUID attacker;
  private UUID target;

  private SkillResult attackResult;
  private int damage;
  private int mitigatedDamage;
}
