package net.alteiar.lendyr.entity.mapper;

import net.alteiar.lendyr.entity.model.AttackResult;
import net.alteiar.lendyr.entity.model.SkillResult;
import net.alteiar.lendyr.grpc.model.v1.combat.LendyrAttackActionResult;
import net.alteiar.lendyr.grpc.model.v1.combat.LendyrSkillResult;

import java.util.UUID;

public class AttackMapper {
  public static AttackMapper INSTANCE = new AttackMapper();

  public AttackResult convertToBusiness(UUID source, UUID target, LendyrAttackActionResult result) {
    return AttackResult.builder()
      .attacker(source)
      .target(target)
      .damage(result.getRawDamage())
      .mitigatedDamage(result.getMitigatedDamage())
      .attackResult(convertToBusiness(result.getAttackResult()))
      .build();
  }

  public SkillResult convertToBusiness(LendyrSkillResult skillResult) {
    return SkillResult.builder()
      .dice1(skillResult.getDice1())
      .dice2(skillResult.getDice2())
      .stunDice(skillResult.getStunDie())
      .modifier(skillResult.getBonus())
      .build();
  }
}
