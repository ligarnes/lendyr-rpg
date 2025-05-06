package net.alteiar.lendyr.client.grpc.v1;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import lombok.Builder;
import lombok.Getter;

public class GameService {

  private final String serverUrl;
  private ManagedChannel channel;

  @Getter
  private GameClient gameClient;

  @Builder
  public GameService(String host, int port) {
    serverUrl = String.format("%s:%d", host, port);
  }

  public void start() {
    channel = Grpc.newChannelBuilder(serverUrl, InsecureChannelCredentials.create())
      .maxRetryAttempts(3)
      .build();

    gameClient = new GameClient(channel);
  }

  public void stop() {
    channel.shutdown();
  }
}
