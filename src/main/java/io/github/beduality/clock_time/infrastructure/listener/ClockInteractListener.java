package io.github.beduality.clock_time.infrastructure.listener;

import io.github.beduality.clock_time.domain.service.TimeFormatter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.GlobalTranslator;
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

    /**
     * Constructs a new ClockInteractListener.
     *
     * @param timeFormatter the domain service used to compute LocalTime from ticks
     */
    public ClockInteractListener(TimeFormatter timeFormatter) {
        this.timeFormatter = timeFormatter;
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
            String message = translate("clock_time.message.wild-spin", locale);
            player.sendMessage(MiniMessage.miniMessage().deserialize(message));
            return;
        }

        long time = world.getTime();
        LocalTime localTime = timeFormatter.formatTicks(time);

        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter
                .ofLocalizedTime(java.time.format.FormatStyle.SHORT)
                .withLocale(locale);
        String formattedTime = localTime.format(dtf);

        String message = translate("clock_time.message.time", locale, formattedTime);
        player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    private String translate(String key, Locale locale, Object... args) {
        var format = GlobalTranslator.translator().translate(key, locale);
        if (format == null) {
            format = GlobalTranslator.translator().translate(key, Locale.ROOT);
        }
        return format != null ? format.format(args) : key;
    }
}
