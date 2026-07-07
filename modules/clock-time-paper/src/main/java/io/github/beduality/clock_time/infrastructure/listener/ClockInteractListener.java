package io.github.beduality.clock_time.infrastructure.listener;

import io.github.beduality.clock_time.ClockTimePlugin;
import io.github.beduality.clock_time.domain.manager.ClockItemFrameRegistry;
import io.github.beduality.clock_time.domain.manager.ClockItemFrameUpdater;
import io.github.beduality.clock_time.domain.service.ClockMessageService;
import io.github.beduality.clock_time.infrastructure.adapter.PaperItemFrameAdapter;
import io.github.beduality.clock_time.infrastructure.adapter.PaperWorldInfo;
import io.github.beduality.clock_time.infrastructure.config.ClockTimePluginConfig;
import java.util.Locale;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/*
 * Infrastructure-level event listener for player interactions. Listens for right-clicks with clocks
 * in-hand, validates permissions, and delegates formatting and translation to pure Java service
 * layers. Also supports placing clocks on walls/floors/ceilings as invisible item frames.
 */
public class ClockInteractListener implements Listener {

  private final ClockTimePlugin plugin;
  private final ClockTimePluginConfig config;
  private final ClockMessageService clockMessageService;
  private final ClockItemFrameRegistry registry;
  private final ClockItemFrameUpdater updater;

  /*
   * Constructs a new ClockInteractListener.
   *
   * @param plugin the plugin instance
   * @param config the configuration instance
   * @param clockMessageService the service used to resolve and format clock messages
   * @param registry the clock registry
   * @param updater the clock updater
   */
  public ClockInteractListener(
      ClockTimePlugin plugin,
      ClockTimePluginConfig config,
      ClockMessageService clockMessageService,
      ClockItemFrameRegistry registry,
      ClockItemFrameUpdater updater) {
    this.plugin = plugin;
    this.config = config;
    this.clockMessageService = clockMessageService;
    this.registry = registry;
    this.updater = updater;
  }

  /*
   * Handles the PlayerInteractEvent. Verifies that the player is right-clicking with a clock, has
   * the appropriate permission, and either displays the time or places a wall/floor/ceiling clock.
   *
   * @param event the Paper interact event being processed
   */
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (event.useItemInHand() == org.bukkit.event.Event.Result.DENY) {
      return;
    }

    if (!event.getAction().name().contains("RIGHT_CLICK") || !event.hasItem()) {
      return;
    }

    // Prevent double execution from off-hand interact events
    if (event.getHand() != EquipmentSlot.HAND) {
      return;
    }

    if (event.getItem().getType() != Material.CLOCK) {
      return;
    }

    Player player = event.getPlayer();
    if (!player.hasPermission("clock_time.use")) {
      return;
    }

    if (event.getAction() == Action.RIGHT_CLICK_BLOCK && config.getWallClocks().isEnabled()) {
      Block clickedBlock = event.getClickedBlock();
      if (clickedBlock != null) {
        // If block is interactable and player is not sneaking, let block interaction handle it
        if (clickedBlock.getType().isInteractable() && !player.isSneaking()) {
          return;
        }

        if (!player.hasPermission("clock_time.place")) {
          sendClockTimeMessage(player);
          return;
        }

        BlockFace face = event.getBlockFace();
        try {
          ItemStack itemToPlace = event.getItem().clone();
          itemToPlace.setAmount(1);

          long time = clickedBlock.getWorld().getTime();
          Locale playerLocale = Locale.forLanguageTag(player.getLocale().replace('_', '-'));
          Component timeName =
              clockMessageService.getFormattedTimeOnly(
                  new PaperWorldInfo(clickedBlock.getWorld()),
                  time,
                  playerLocale,
                  config.getItemFrameClocks().getWildSpinSymbol());

          org.bukkit.inventory.meta.ItemMeta meta = itemToPlace.getItemMeta();
          if (meta != null) {
            org.bukkit.NamespacedKey key =
                new org.bukkit.NamespacedKey("clock-time", "original-name");
            if (!meta.getPersistentDataContainer()
                .has(key, org.bukkit.persistence.PersistentDataType.STRING)) {
              String originalName = meta.hasDisplayName() ? meta.getDisplayName() : "";
              meta.getPersistentDataContainer()
                  .set(key, org.bukkit.persistence.PersistentDataType.STRING, originalName);
            }
            meta.displayName(timeName);
            itemToPlace.setItemMeta(meta);
          }

          Block targetBlock = clickedBlock.getRelative(face);
          ItemFrame frame =
              targetBlock
                  .getWorld()
                  .spawn(
                      targetBlock.getLocation(),
                      ItemFrame.class,
                      f -> {
                        f.setFacingDirection(face);
                        f.setVisible(false);
                        f.setItem(itemToPlace, false);
                      });

          if (registry != null && updater != null) {
            var adapter = new PaperItemFrameAdapter(frame);
            registry.register(adapter);
            updater.updateFrame(adapter, new PaperWorldInfo(frame.getWorld()));
          }

          if (player.getGameMode() != org.bukkit.GameMode.CREATIVE) {
            event.getItem().setAmount(event.getItem().getAmount() - 1);
          }
          event.setCancelled(true);
          return;
        } catch (Exception e) {
          // Fallback to checking time if placement fails
        }
      }
    }

    sendClockTimeMessage(player);
  }

  private void sendClockTimeMessage(Player player) {
    var world = player.getWorld();
    Locale locale = player.locale();
    long time = world.getTime();

    var worldInfo = new PaperWorldInfo(world);
    net.kyori.adventure.text.Component messageComponent =
        clockMessageService.getClockMessage(worldInfo, time, locale);

    player.sendMessage(messageComponent);
  }
}
