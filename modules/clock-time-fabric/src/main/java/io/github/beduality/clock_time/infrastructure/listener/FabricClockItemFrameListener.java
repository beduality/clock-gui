package io.github.beduality.clock_time.infrastructure.listener;

import io.github.beduality.clock_time.infrastructure.manager.FabricClockItemFrameRegistry;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;

/** Monitors entity load, unload, and interactions in Fabric to register clock item frames. */
public class FabricClockItemFrameListener {

  private final FabricClockItemFrameRegistry registry;
  private Consumer<ItemFrameEntity> onRegisterCallback;

  public FabricClockItemFrameListener(FabricClockItemFrameRegistry registry) {
    this.registry = registry;
  }

  public void setOnRegisterCallback(Consumer<ItemFrameEntity> onRegisterCallback) {
    this.onRegisterCallback = onRegisterCallback;
  }

  private void registerFrame(ItemFrameEntity frame) {
    if (registry.register(frame)) {
      if (onRegisterCallback != null) {
        onRegisterCallback.accept(frame);
      }
    }
  }

  public void register() {
    ServerEntityEvents.ENTITY_LOAD.register(
        (entity, world) -> {
          if (entity instanceof ItemFrameEntity frame) {
            registerFrame(frame);
          }
        });

    ServerEntityEvents.ENTITY_UNLOAD.register(
        (entity, world) -> {
          if (entity instanceof ItemFrameEntity frame) {
            registry.unregister(frame);
          }
        });

    UseEntityCallback.EVENT.register(
        (player, world, hand, entity, hitResult) -> {
          if (!world.isClient() && entity instanceof ItemFrameEntity frame) {
            if (world.getServer() != null) {
              world.getServer().execute(() -> registerFrame(frame));
            }
          }
          return ActionResult.PASS;
        });
  }

  /** Scans loaded entities in the world to register existing clock item frames. */
  public void registerAlreadyLoadedFrames(ServerWorld world) {
    for (net.minecraft.entity.Entity entity : world.iterateEntities()) {
      if (entity instanceof ItemFrameEntity frame) {
        registerFrame(frame);
      }
    }
  }
}
