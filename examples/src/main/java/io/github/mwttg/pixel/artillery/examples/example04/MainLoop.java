package io.github.mwttg.pixel.artillery.examples.example04;

import io.github.mwttg.pixel.artillery.framework.entity.Entity;
import io.github.mwttg.pixel.artillery.framework.entity.drawable.Sprite;
import io.github.mwttg.pixel.artillery.framework.entity.drawable.SpriteAnimation;
import io.github.mwttg.pixel.artillery.framework.entity.position.Position;
import io.github.mwttg.pixel.artillery.framework.window.Configuration;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL40;

public class MainLoop {

  private final Matrix4f viewMatrix;
  private final Matrix4f projectionMatrix;
  private final Entity player;
  private final Entity level;

  public MainLoop(final Configuration configuration) {
    final var playerIdleJsonFile = "files/player/blob/player-idle.json";
    final var playerIdleTextureFile = "files/player/blob/player-idle.png";
    final var playerIdleDrawable = new SpriteAnimation(playerIdleJsonFile, playerIdleTextureFile);
    final var playerLeftJsonFile = "files/player/blob/player-left.json";
    final var playerLeftTextureFile = "files/player/blob/player-left.png";
    final var playerLeftDrawable = new SpriteAnimation(playerLeftJsonFile, playerLeftTextureFile);
    final var playerRightJsonFile = "files/player/blob/player-right.json";
    final var playerRightTextureFile = "files/player/blob/player-right.png";
    final var playerRightDrawable =
        new SpriteAnimation(playerRightJsonFile, playerRightTextureFile);
    final var playerModelMatrix = new Matrix4f().translate(10, 7, 0);
    final var playerPosition = new Position(playerModelMatrix, 2.0f);
    this.player = new Entity.EntityBuilder()
        .addDrawable("idle", playerIdleDrawable)
        .addDrawable("left", playerLeftDrawable)
        .addDrawable("right", playerRightDrawable)
        .setDrawableName("idle")
        .addPosition(playerPosition)
        .build();

    final var levelJsonFile = "files/level/level2/level.json";
    final var levelTextureFile = "files/level/texture-atlas.png";
    final var levelDrawable = new Sprite(levelJsonFile, levelTextureFile);
    final var levelModelMatrix = new Matrix4f().translate(0, 0, -1);
    final var levelPosition = new Position(levelModelMatrix, 80.0f);
    this.level = new Entity.EntityBuilder()
        .addDrawable(levelDrawable)
        .addPosition(levelPosition)
        .build();

    this.viewMatrix = createViewMatrix();
    this.projectionMatrix = createOrtho2DMatrix(configuration);
  }

  public void loop(final long gameWindowId) {

    while (!GLFW.glfwWindowShouldClose(gameWindowId)) {
      // logic
      playerMovement(gameWindowId);

      // clear
      GL40.glClear(GL40.GL_COLOR_BUFFER_BIT | GL40.GL_DEPTH_BUFFER_BIT);

      // render
      level.draw(viewMatrix, projectionMatrix);
      player.draw(viewMatrix, projectionMatrix);

      GLFW.glfwPollEvents();
      GLFW.glfwSwapBuffers(gameWindowId);
    }
  }

  private Matrix4f createOrtho2DMatrix(final Configuration configuration) {
    final var near = configuration.viewPortConfiguration().nearPlane();
    final var far = configuration.viewPortConfiguration().farPlane();
    return new Matrix4f().setOrtho(0.0f, 20.0f, 0.0f, 15.0f, near, far);
  }

  private Matrix4f createViewMatrix() {
    return new Matrix4f().setLookAt(0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
  }

  // method manipulates player entity (mutable)
  private void playerMovement(final long gameWindowId) {
    if (GLFW.glfwGetKey(gameWindowId, GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS) {
      viewMatrix.translate(0.08f, 0f, 0f, viewMatrix);
      player.setDrawableName("left");
      player.getPosition().translate(-0.08f, 0f, 0f);
      // return new MoveTuple("left", playerModelMatrix.translate(-0.08f, 0f, 0f, playerModelMatrix));
    } else if (GLFW.glfwGetKey(gameWindowId, GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS) {
      viewMatrix.translate(-0.08f, 0f, 0f, viewMatrix);
      player.setDrawableName("right");
      player.getPosition().translate(0.08f, 0f, 0f);
      // return new MoveTuple("right", playerModelMatrix.translate(0.08f, 0f, 0f, playerModelMatrix));
    } else {
      player.setDrawableName("idle");
      // return new MoveTuple("idle", playerModelMatrix);
    }
  }
}
