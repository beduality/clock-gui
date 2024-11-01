package io.github.beduality.clockgui;

import org.bukkit.plugin.java.JavaPlugin;

public class ClockGuiPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        getLogger().info("Clock-GUI Plugin Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Clock-GUI Plugin Disabled");
    }
}
