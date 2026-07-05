package io.github.beduality.clock_time.infrastructure.listener;

import static org.junit.jupiter.api.Assertions.*;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.github.beduality.clock_time.ClockTimePlugin;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
  void testRightClickWithClockUS() {
    PlayerMock player = server.addPlayer();
    player.setLocale(Locale.US);
    player.addAttachment(plugin, "clock_time.use", true);

    // Set time to Sunrise (0 ticks)
    player.getWorld().setTime(0);

    ItemStack clock = new ItemStack(Material.CLOCK);
    player.getInventory().setItemInMainHand(clock);

    var event =
        new org.bukkit.event.player.PlayerInteractEvent(
            player, Action.RIGHT_CLICK_AIR, clock, null, null, EquipmentSlot.HAND);
    server.getPluginManager().callEvent(event);

    String message = player.nextMessage();
    assertNotNull(message, "Player should have received a message");
    // US locale prints AM/PM format (contains narrow/standard spaces depending on JDK, so we assert
    // with startsWith/contains)
    assertTrue(message.startsWith("§6Current Time:§r §b"), "Message was: " + message);
    assertTrue(
        message.contains("6:00")
            && (message.contains("AM") || message.contains("a.m.") || message.contains("a. m.")),
        "Message was: " + message);
  }

  @Test
  void testRightClickWithClockDE() {
    PlayerMock player = server.addPlayer();
    player.setLocale(Locale.GERMANY);
    player.addAttachment(plugin, "clock_time.use", true);

    // Set time to Noon (6000 ticks)
    player.getWorld().setTime(6000);

    ItemStack clock = new ItemStack(Material.CLOCK);
    player.getInventory().setItemInMainHand(clock);

    var event =
        new org.bukkit.event.player.PlayerInteractEvent(
            player, Action.RIGHT_CLICK_AIR, clock, null, null, EquipmentSlot.HAND);
    server.getPluginManager().callEvent(event);

    String message = player.nextMessage();
    assertNotNull(message, "Player should have received a message");
    assertEquals("§6Aktuelle Uhrzeit:§r §b12:00", message);
  }

  @Test
  void testRightClickWithClockES() {
    PlayerMock player = server.addPlayer();
    player.setLocale(new Locale("es"));
    player.addAttachment(plugin, "clock_time.use", true);

    // Set time to Midnight (18000 ticks)
    player.getWorld().setTime(18000);

    ItemStack clock = new ItemStack(Material.CLOCK);
    player.getInventory().setItemInMainHand(clock);

    var event =
        new org.bukkit.event.player.PlayerInteractEvent(
            player, Action.RIGHT_CLICK_AIR, clock, null, null, EquipmentSlot.HAND);
    server.getPluginManager().callEvent(event);

    String message = player.nextMessage();
    assertNotNull(message, "Player should have received a message");
    assertTrue(message.startsWith("§6Hora Actual:§r §b"), "Message was: " + message);
  }

  @Test
  void testRightClickWithClockNether() {
    PlayerMock player = server.addPlayer();
    player.setLocale(Locale.US);
    player.addAttachment(plugin, "clock_time.use", true);

    // Create Nether world and teleport player
    World nether =
        server.createWorld(new WorldCreator("world_nether").environment(World.Environment.NETHER));
    player.teleport(nether.getSpawnLocation());

    ItemStack clock = new ItemStack(Material.CLOCK);
    player.getInventory().setItemInMainHand(clock);

    var event =
        new org.bukkit.event.player.PlayerInteractEvent(
            player, Action.RIGHT_CLICK_AIR, clock, null, null, EquipmentSlot.HAND);
    server.getPluginManager().callEvent(event);

    String message = player.nextMessage();
    assertNotNull(message, "Player should have received a message");
    assertEquals("§cThe clock spins wildly... Time has no meaning here.", message);
  }

  @Test
  void testRightClickWithClockEnd() {
    PlayerMock player = server.addPlayer();
    player.setLocale(Locale.GERMANY);
    player.addAttachment(plugin, "clock_time.use", true);

    // Create End world and teleport player
    World end =
        server.createWorld(
            new WorldCreator("world_the_end").environment(World.Environment.THE_END));
    player.teleport(end.getSpawnLocation());

    ItemStack clock = new ItemStack(Material.CLOCK);
    player.getInventory().setItemInMainHand(clock);

    var event =
        new org.bukkit.event.player.PlayerInteractEvent(
            player, Action.RIGHT_CLICK_AIR, clock, null, null, EquipmentSlot.HAND);
    server.getPluginManager().callEvent(event);

    String message = player.nextMessage();
    assertNotNull(message, "Player should have received a message");
    assertEquals("§cDie Uhr dreht sich wild... Zeit hat hier keine Bedeutung.", message);
  }

  @Test
  void testNoPermission() {
    PlayerMock player = server.addPlayer();
    player.setLocale(Locale.US);

    // Revoke the default permission node
    player.addAttachment(plugin, "clock_time.use", false);

    ItemStack clock = new ItemStack(Material.CLOCK);
    player.getInventory().setItemInMainHand(clock);

    var event =
        new org.bukkit.event.player.PlayerInteractEvent(
            player, Action.RIGHT_CLICK_AIR, clock, null, null, EquipmentSlot.HAND);
    server.getPluginManager().callEvent(event);

    assertNull(player.nextMessage(), "Player without permission should not receive a message");
  }

  @Test
  void testCancelledEvent() {
    PlayerMock player = server.addPlayer();
    player.setLocale(Locale.US);
    player.addAttachment(plugin, "clock_time.use", true);

    ItemStack clock = new ItemStack(Material.CLOCK);
    player.getInventory().setItemInMainHand(clock);

    var event =
        new org.bukkit.event.player.PlayerInteractEvent(
            player, Action.RIGHT_CLICK_AIR, clock, null, null, EquipmentSlot.HAND);
    event.setUseItemInHand(Result.DENY); // Cancel item use
    server.getPluginManager().callEvent(event);

    assertNull(
        player.nextMessage(),
        "Player should not receive a message if the interact event is cancelled");
  }

  @Test
  void testOffHandInteraction() {
    PlayerMock player = server.addPlayer();
    player.setLocale(Locale.US);
    player.addAttachment(plugin, "clock_time.use", true);

    ItemStack clock = new ItemStack(Material.CLOCK);
    player.getInventory().setItemInMainHand(clock);

    var event =
        new org.bukkit.event.player.PlayerInteractEvent(
            player,
            Action.RIGHT_CLICK_AIR,
            clock,
            null,
            null,
            EquipmentSlot.OFF_HAND // Off-hand slot
            );
    server.getPluginManager().callEvent(event);

    assertNull(player.nextMessage(), "Off hand interaction should be ignored");
  }

  @Test
  void testEmptyHand() {
    PlayerMock player = server.addPlayer();
    player.setLocale(Locale.US);
    player.addAttachment(plugin, "clock_time.use", true);

    // Empty main hand

    var event =
        new org.bukkit.event.player.PlayerInteractEvent(
            player, Action.RIGHT_CLICK_AIR, null, null, null, EquipmentSlot.HAND);
    server.getPluginManager().callEvent(event);

    assertNull(
        player.nextMessage(), "Player interacting with an empty hand should not get a message");
  }

  @Test
  void testNonClockItem() {
    PlayerMock player = server.addPlayer();
    player.setLocale(Locale.US);
    player.addAttachment(plugin, "clock_time.use", true);

    ItemStack feather = new ItemStack(Material.FEATHER);
    player.getInventory().setItemInMainHand(feather);

    var event =
        new org.bukkit.event.player.PlayerInteractEvent(
            player, Action.RIGHT_CLICK_AIR, feather, null, null, EquipmentSlot.HAND);
    server.getPluginManager().callEvent(event);

    assertNull(
        player.nextMessage(), "Player interacting with a non-clock item should not get a message");
  }
}
