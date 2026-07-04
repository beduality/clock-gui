package io.github.beduality.clock_time;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ClockTimePlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("ClockTime Plugin Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("ClockTime Plugin Disabled");
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
        if (event.getHand() != org.bukkit.inventory.EquipmentSlot.HAND) {
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

        // Load the bundle for the player's locale, fallback to English
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle.getBundle("messages", locale, this.getClass().getClassLoader());
        } catch (MissingResourceException e) {
            bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH, this.getClass().getClassLoader());
        }

        // Handle dimensions where time does not make sense (Nether and End)
        if (world.getEnvironment() == org.bukkit.World.Environment.NETHER || 
            world.getEnvironment() == org.bukkit.World.Environment.THE_END) {
            String pattern = bundle.getString("clock_time.message.wild-spin");
            player.sendMessage(net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(pattern));
            return;
        }

        // Get the in-game time from the player's current world
        var time = world.getTime();

        // Convert the in-game time to a 12-hour format
        var hour = (int) ((time / 1000 + 6) % 24); // +6 to adjust to Minecraft's "day start" at 6:00 AM
        var minute = (int) ((time % 1000) * 60 / 1000); // Convert the remaining time to minutes
        var twelveHour = hour % 12 == 0 ? 12 : hour % 12; // Convert 24-hour to 12-hour, with 0 as 12
        var period = hour < 12 ? "AM" : "PM";

        // Display the formatted time as a colorful chat message
        String pattern = bundle.getString("clock_time.message.time");
        String formatted = MessageFormat.format(pattern, String.format("%02d", twelveHour), String.format("%02d", minute), period);
        player.sendMessage(net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(formatted));
    }
}
