package io.github.beduality.clock_time.infrastructure.listener;

import io.github.beduality.clock_time.ClockTimePlugin;
import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.block.Action;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClockInteractListenerTest {

    private ServerMock server;
    private ClockTimePlugin plugin;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(ClockTimePlugin.class);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testRightClickWithClock() {
        PlayerMock player = server.addPlayer();
        player.setLocale(java.util.Locale.US);
        
        // Give permission
        player.addAttachment(plugin, "clock_time.use", true);
        
        // Give player clock in hand
        ItemStack clock = new ItemStack(Material.CLOCK);
        player.getInventory().setItemInMainHand(clock);

        // Perform Right Click interaction
        var event = new org.bukkit.event.player.PlayerInteractEvent(
            player,
            Action.RIGHT_CLICK_AIR,
            clock,
            null,
            null,
            org.bukkit.inventory.EquipmentSlot.HAND
        );
        server.getPluginManager().callEvent(event);

        // We expect the player to get a message about the time.
        String nextMessage = player.nextMessage();
        assertNotNull(nextMessage, "Player should have received a message");
        assertTrue(nextMessage.contains("time") || nextMessage.contains("spin") || nextMessage.contains("Time") || nextMessage.contains("currently"), 
            "Message was: " + nextMessage);
    }
}
