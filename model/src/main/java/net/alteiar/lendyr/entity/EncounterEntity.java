package net.alteiar.lendyr.entity;

import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.entity.mapper.GenericMapper;
import net.alteiar.lendyr.grpc.model.v1.game.LendyrGameState;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2
public class EncounterEntity {
  private final Lock lock = new ReentrantLock();
  @Getter
  private boolean loaded;

  @Getter
  public String mapPath;
  public int widthInPixel;
  public int heightInPixel;
  @Getter
  public float pixelPerMeter;

  @Getter
  private int currentTurn;
  private final ActivePersona activePersona;
  private final List<UUID> initiativeOrder;
  private final Map<UUID, CharacterEntity> characterEntities;

  private final List<CharacterEntity> cachedInitiativeOrder;

  @Builder
  EncounterEntity() {
    initiativeOrder = new ArrayList<>();
    cachedInitiativeOrder = new ArrayList<>();
    characterEntities = new HashMap<>();
    this.activePersona = ActivePersona.builder().build();
    this.loaded = false;
  }

  public float getWorldWidth() {
    return this.widthInPixel / this.pixelPerMeter;
  }

  public float getWorldHeight() {
    return this.heightInPixel / this.pixelPerMeter;
  }

  public CharacterEntity getCharacter(UUID id) {
    return characterEntities.get(id);
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
      } else {
        log.warn("Unable to acquire lock");
      }
    } catch (InterruptedException e) {
      log.warn("Interrupted while waiting for lock", e);
    }
  }

  private void safeNewState(LendyrGameState newState) {
    this.mapPath = newState.getEncounter().getMap().getMapPath();
    this.widthInPixel = newState.getEncounter().getMap().getWidthInPixel();
    this.heightInPixel = newState.getEncounter().getMap().getHeightInPixel();
    this.pixelPerMeter = newState.getEncounter().getMap().getPixelPerMeter();

    this.currentTurn = newState.getEncounter().getCurrentState().getCurrentTurn();
    this.activePersona.update(newState.getEncounter().getCurrentState().getActive());

    if (initiativeOrder.isEmpty()) {
      newState.getEncounter().getCurrentState().getInitiativeOrderList().stream().map(GenericMapper.INSTANCE::convertBytesToUUID).forEach(initiativeOrder::add);
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

    cachedInitiativeOrder.clear();
    initiativeOrder.stream().map(characterEntities::get).forEach(cachedInitiativeOrder::add);
    printDebug();
    loaded = true;
  }

  public void printDebug() {
    log.info("Turn: {} | activeIdx: {} | major action: {} | minor action: {} | Entities: {}",
      this.currentTurn, this.activePersona.getIdx(), this.activePersona.isMajorActionUsed(), this.activePersona.isMinorActionUsed(),
      this.initiativeOrder.size());
  }
}
