package net.alteiar.lendyr.entity;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Getter;
import net.alteiar.lendyr.entity.mapper.GenericMapper;
import net.alteiar.lendyr.grpc.model.v1.LendyrGameState;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CombatEntity {
  private final Lock lock = new ReentrantLock();
  @Getter
  private boolean loaded;

  @Getter
  private int currentTurn;
  private final ActivePersona activePersona;
  private final List<UUID> initiativeOrder;
  private final Map<UUID, CharacterEntity> characterEntities;

  private final List<CharacterEntity> cachedInitiativeOrder;

  @Builder
  CombatEntity() {
    initiativeOrder = new ArrayList<>();
    cachedInitiativeOrder = new ArrayList<>();
    characterEntities = new HashMap<>();
    this.activePersona = ActivePersona.builder().build();
    this.loaded = false;
  }

  public CharacterEntity getCurrentCharacter() {
    if (activePersona.getIdx() >= characterEntities.size()) {
      throw new IllegalStateException("No current character found");
    }
    return cachedInitiativeOrder.get(activePersona.getIdx());
  }

  public List<CharacterEntity> getInitiativeOrder() {
    return cachedInitiativeOrder;
  }

  public void newState(LendyrGameState newState) {
    try {
      if (lock.tryLock(100, TimeUnit.MILLISECONDS)) {
        try {
          safeNewState(newState);
        } finally {
          lock.unlock();
        }
      }
    } catch (InterruptedException e) {
    }
  }

  private void safeNewState(LendyrGameState newState) {
    this.currentTurn = newState.getCombat().getCurrentTurn();
    this.activePersona.update(newState.getCombat().getActive());

    if (initiativeOrder.isEmpty()) {
      newState.getCombat().getInitiativeOrderList().stream().map(GenericMapper.INSTANCE::convertBytesToUUID).forEach(initiativeOrder::add);
    }

    newState.getPersonaList().forEach(persona -> {
      UUID personaId = GenericMapper.INSTANCE.convertBytesToUUID(persona.getId());
      this.characterEntities.compute(personaId, (k, entity) -> {
        if (entity == null) {
          entity = CharacterEntity.builder()
            .id(personaId)
            .name(persona.getName())
            .portrait(persona.getPortraitPath())
            .token(persona.getTokenPath())
            .position(new Vector2(persona.getPosition().getX(), persona.getPosition().getY()))
            .build();
        }
        entity.update(persona);
        return entity;
      });
    });

    initiativeOrder.stream().map(characterEntities::get).forEach(cachedInitiativeOrder::add);
    printDebug();
    loaded = true;
  }

  public void printDebug() {
    System.out.printf("Turn: %s | activeIdx: %s | major action: %s | minor action: %s%n",
      this.currentTurn, this.activePersona.getIdx(), this.activePersona.isMajorActionUsed(), this.activePersona.isMinorActionUsed());
  }
}
