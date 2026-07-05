package io.github.beduality.clock_time.infrastructure.listener;

import io.github.beduality.clock_time.domain.service.ClockMessageService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Locale;

/**
 * Infrastructure-level event listener for player interactions.
 * Listens for right-clicks with clocks in-hand, validates permissions,
 * and delegates formatting and translation to pure Java service layers.
 */
public class ClockInteractListener implements Listener {

    private final ClockMessageService clockMessageService;

    /**
     * Constructs a new ClockInteractListener.
     *
     * @param clockMessageService the service used to resolve and format clock messages
     */
    public ClockInteractListener(ClockMessageService clockMessageService) {
        this.clockMessageService = clockMessageService;
    }

    /**
     * Handles the PlayerInteractEvent.
     * Verifies that the player is right-clicking with a clock, has the appropriate permission,
     * and is in a valid environment before showing the time.
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

        sendClockTimeMessage(player);
    }

    private void sendClockTimeMessage(Player player) {
        var world = player.getWorld();
        Locale locale = player.locale();
        long time = world.getTime();

        net.kyori.adventure.text.Component messageComponent = clockMessageService.getClockMessage(
                world.getEnvironment(),
                time,
                locale
        );

        player.sendMessage(messageComponent);
    }
}
