package io.github.beduality.clock_time.infrastructure.listener;

import io.github.beduality.clock_time.infrastructure.manager.ClockItemFrameRegistry;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;

/**
 * Monitors chunk loading, unloading, player interaction, and entity damage to register and
 * unregister clock item frames.
 */
public class ClockItemFrameListener implements Listener {
  private final Plugin plugin;
  private final ClockItemFrameRegistry registry;
  private Consumer<ItemFrame> onRegisterCallback;

  public ClockItemFrameListener(Plugin plugin, ClockItemFrameRegistry registry) {
    this.plugin = plugin;
    this.registry = registry;
  }

  public void setOnRegisterCallback(Consumer<ItemFrame> onRegisterCallback) {
    this.onRegisterCallback = onRegisterCallback;
  }

  private void registerFrame(ItemFrame frame) {
    if (registry.register(frame)) {
      if (onRegisterCallback != null) {
        onRegisterCallback.accept(frame);
      }
    }
  }

  /** Scans all loaded chunks in all worlds to register already loaded clock item frames. */
  public void registerAlreadyLoadedFrames() {
    for (World world : Bukkit.getWorlds()) {
      for (Chunk chunk : world.getLoadedChunks()) {
        for (Entity entity : chunk.getEntities()) {
          if (entity instanceof ItemFrame frame) {
            registerFrame(frame);
          }
        }
      }
    }
  }

  @EventHandler
  public void onChunkLoad(ChunkLoadEvent event) {
    for (Entity entity : event.getChunk().getEntities()) {
      if (entity instanceof ItemFrame frame) {
        registerFrame(frame);
      }
    }
  }

  @EventHandler
  public void onChunkUnload(ChunkUnloadEvent event) {
    for (Entity entity : event.getChunk().getEntities()) {
      if (entity instanceof ItemFrame frame) {
        registry.unregister(frame);
      }
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
    if (event.getRightClicked() instanceof ItemFrame frame) {
      // Run 1 tick later to allow the item to be placed or updated in the frame
      Bukkit.getScheduler().runTask(plugin, () -> registerFrame(frame));
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onHangingBreak(HangingBreakEvent event) {
    if (event.getEntity() instanceof ItemFrame frame) {
      registry.unregister(frame);
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onEntityDamage(EntityDamageByEntityEvent event) {
    if (event.getEntity() instanceof ItemFrame frame) {
      // Run 1 tick later to see if the item was knocked out
      Bukkit.getScheduler()
          .runTask(
              plugin,
              () -> {
                if (!frame.isValid() || frame.getItem().getType() != org.bukkit.Material.CLOCK) {
                  registry.unregister(frame);
                }
              });
    }
  }
}
