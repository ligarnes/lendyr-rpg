package net.alteiar.lendyr.entity.mapper;

import com.badlogic.gdx.math.Vector2;
import com.google.protobuf.ByteString;
import net.alteiar.lendyr.grpc.model.v1.generic.LendyrPosition;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.UUID;

public class GenericMapper {

  public static GenericMapper INSTANCE = new GenericMapper();

  public ByteString convertUUIDToBytes(UUID uuid) {
    if (Objects.isNull(uuid)) {
      return null;
    }

    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    return ByteString.copyFrom(bb.array());
  }

  public UUID convertBytesToUUID(ByteString bytes) {
    if (Objects.isNull(bytes) || bytes.isEmpty()) {
      return null;
    }
    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes.toByteArray());
    long high = byteBuffer.getLong();
    long low = byteBuffer.getLong();
    return new UUID(high, low);
  }

  public LendyrPosition convertPosition(Vector2 position) {
    if (Objects.isNull(position)) {
      return null;
    }

    return LendyrPosition.newBuilder().setX(position.x).setY(position.y).build();
  }
}
