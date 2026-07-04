package io.github.beduality.clock_time.infrastructure;

import io.github.beduality.clock_time.application.TranslationService;
import io.github.beduality.clock_time.domain.FormattedTime;
import io.github.beduality.clock_time.domain.TimeFormatter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Locale;

public class ClockInteractListener implements Listener {

    private final TimeFormatter timeFormatter;
    private final TranslationService translationService;

    public ClockInteractListener(TimeFormatter timeFormatter, TranslationService translationService) {
        this.timeFormatter = timeFormatter;
        this.translationService = translationService;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Ignore cancelled interactions (e.g. protected regions)
        if (event.useItemInHand() == org.bukkit.event.Event.Result.DENY) {
            return;
        }

        // Check if the player is right-clicking with an item
        if (!event.getAction().name().contains("RIGHT_CLICK") || !event.hasItem()) {
            return;
        }

        // Prevent double execution from off-hand interact events
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        // Check if the player is holding a clock
        if (event.getItem().getType() != Material.CLOCK) {
            return;
        }

        // Permission check
        Player player = event.getPlayer();
        if (!player.hasPermission("clock_time.use")) {
            return;
        }

        sendClockTimeMessage(player);
    }

    private void sendClockTimeMessage(Player player) {
        var world = player.getWorld();
        Locale locale = player.locale();

        // Handle dimensions where time does not make sense (Nether and End)
        if (world.getEnvironment() == org.bukkit.World.Environment.NETHER || 
            world.getEnvironment() == org.bukkit.World.Environment.THE_END) {
            String pattern = translationService.getMessage("clock_time.message.wild-spin", locale);
            player.sendMessage(MiniMessage.miniMessage().deserialize(pattern));
            return;
        }

        // Get the in-game time from the player's current world
        long time = world.getTime();

        // Format the time using the domain formatter
        FormattedTime formattedTime = timeFormatter.formatTicks(time);

        // Display the formatted time as a colorful chat message
        String message = translationService.getMessage(
            "clock_time.message.time",
            locale,
            formattedTime.getFormattedHour(),
            formattedTime.getFormattedMinute(),
            formattedTime.period()
        );
        player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }
}
