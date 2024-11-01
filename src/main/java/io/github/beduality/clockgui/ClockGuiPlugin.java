package io.github.beduality.clockgui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ClockGuiPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Clock-GUI Plugin Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Clock-GUI Plugin Disabled");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Check if the player is right-clicking with a clock
        if (event.getItem() != null && event.getItem().getType() == Material.CLOCK) {
            openClockGui(event.getPlayer());
        }
    }

    private void openClockGui(Player player) {
        // Get the in-game time from the player's current world
        var world = player.getWorld();
        var time = world.getTime();

        // Convert the in-game time to a 12-hour format
        var hour = (int) ((time / 1000 + 6) % 24); // +6 to adjust to Minecraft's "day start" at 6:00 AM
        var minute = (int) ((time % 1000) * 60 / 1000); // Convert the remaining time to minutes
        var twelveHour = hour % 12 == 0 ? 12 : hour % 12; // Convert 24-hour to 12-hour, with 0 as 12
        var period = hour < 12 ? "AM" : "PM";

        // Display the formatted time as the GUI title
        var title = String.format("Current Time: %02d:%02d %s", twelveHour, minute, period);

        // Create an empty GUI with the time in the title
        var gui = Bukkit.createInventory(null, 9, title);

        // Open the GUI for the player
        player.openInventory(gui);
    }
}