package net.alteiar.lendyr.game;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import net.alteiar.lendyr.app.grpc.GrpcServer;
import net.alteiar.lendyr.engine.GameContext;

@Log4j2
@Builder
public class GameServer {
  private final int port;

  public void start() {
    // Load game
    Thread.ofPlatform().name("Game-Server").start(this::run);
  }

  public void run() {
    GameContext gameContext = GameContext.builder().build();
    gameContext.load("dummy-test-1");

    GrpcServer grpcServer = GrpcServer.builder().gameContext(gameContext).build();

    try {
      grpcServer.start(port);
    } catch (Exception e) {
      log.error("Game server crashed", e);
    }
  }
}
