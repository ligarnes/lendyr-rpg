package net.alteiar.lendyr.client.grpc.v1;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

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
      .keepAliveTime(10, TimeUnit.SECONDS)
      .keepAliveTimeout(1, TimeUnit.SECONDS)
      .keepAliveWithoutCalls(true)
      .maxRetryAttempts(3)
      .build();

    gameClient = new GameClient(channel);
  }

  public void stop() {
    gameClient.terminate();
    channel.shutdownNow();

    try {
      channel.awaitTermination(1, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      System.out.println("Interrupted while waiting for channel to shut down");
    }
    System.out.printf("Channel is shut down %s%n", channel.isTerminated());
  }
}
