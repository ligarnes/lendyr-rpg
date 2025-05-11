package net.alteiar.lendyr.entity;

import lombok.Builder;
import lombok.Data;
import net.alteiar.lendyr.grpc.model.v1.encounter.LendyrCurrentInitiative;

@Data
@Builder
public class ActivePersona {
  private int idx;
  private boolean majorActionUsed;
  private boolean minorActionUsed;

  public void update(LendyrCurrentInitiative currentInitiative) {
    this.idx = currentInitiative.getActivePersonaIdx();
    this.majorActionUsed = currentInitiative.getMajorActionUsed();
    this.minorActionUsed = currentInitiative.getMinorActionUsed();
  }
}
