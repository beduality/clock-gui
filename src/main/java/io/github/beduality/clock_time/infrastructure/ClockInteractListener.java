package io.github.beduality.clock_time.infrastructure;

import io.github.beduality.clock_time.application.TranslationService;
import io.github.beduality.clock_time.domain.TimeFormatter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.time.LocalTime;
import java.util.Locale;

/**
 * Infrastructure-level event listener for player interactions.
 * Listens for right-clicks with clocks in-hand, validates permissions,
 * and delegates formatting and translation to pure Java service layers.
 */
public class ClockInteractListener implements Listener {

    private final TimeFormatter timeFormatter;
    private final TranslationService translationService;

    /**
     * Constructs a new ClockInteractListener.
     *
     * @param timeFormatter      the domain service used to compute LocalTime from ticks
     * @param translationService the application service used to resolve localized messages
     */
    public ClockInteractListener(TimeFormatter timeFormatter, TranslationService translationService) {
        this.timeFormatter = timeFormatter;
        this.translationService = translationService;
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

        // Handle dimensions where time does not spin normally
        if (world.getEnvironment() == org.bukkit.World.Environment.NETHER || 
            world.getEnvironment() == org.bukkit.World.Environment.THE_END) {
            String pattern = translationService.getMessage("clock_time.message.wild-spin", locale);
            player.sendMessage(MiniMessage.miniMessage().deserialize(pattern));
            return;
        }

        long time = world.getTime();
        LocalTime localTime = timeFormatter.formatTicks(time);
        String message = translationService.getFormattedTimeMessage(localTime, locale);
        player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }
}
