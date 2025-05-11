package net.alteiar.lendyr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import lombok.Builder;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.IOException;

@Builder
public class ResourceLoader {
  @NonNull
  private AssetManager assetManager;

  public void load() {
    try (BufferedReader reader = new BufferedReader(Gdx.files.internal("assets.txt").reader())) {
      while (reader.ready()) {
        String resource = reader.readLine();
        if (resource.endsWith(".png") || resource.endsWith(".jpeg") || resource.endsWith(".jpg") || resource.endsWith(".gif")) {
          assetManager.load(resource, Texture.class);
        } else if (resource.endsWith(".fnt")) {
          assetManager.load(resource, BitmapFont.class);
        } else {
          System.out.println("Ignore resource " + resource);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
