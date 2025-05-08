package net.alteiar.lendyr.entity.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillResult {
  private int dice1;
  private int dice2;
  private int stunDice;
  private int modifier;
}
