package io.github.beduality.clock_time.infrastructure.listener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.github.beduality.clock_time.ClockTimePlugin;
import io.github.beduality.clock_time.domain.manager.ClockItemFrameRegistry;
import io.github.beduality.clock_time.domain.manager.ClockItemFrameUpdater;
import io.github.beduality.clock_time.domain.service.ClockMessageService;
import io.github.beduality.clock_time.infrastructure.adapter.PaperItemFrameAdapter;
import io.github.beduality.clock_time.infrastructure.adapter.PaperWorldInfo;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClockItemFrameListenerTest {

  private ServerMock server;
  private ClockTimePlugin plugin;
  private ClockItemFrameRegistry registry;
  private ClockItemFrameListener listener;

  @BeforeEach
  void setUp() {
    server = MockBukkit.mock();
    plugin = MockBukkit.load(ClockTimePlugin.class);
    registry = plugin.getClockItemFrameRegistry();
    listener = new ClockItemFrameListener(plugin, registry);
  }

  @AfterEach
  void tearDown() {
    MockBukkit.unmock();
  }

  private ItemFrame mockItemFrame(Material material) {
    ItemFrame frame = mock(ItemFrame.class);
    when(frame.getUniqueId()).thenReturn(UUID.randomUUID());
    when(frame.getItem()).thenReturn(new ItemStack(material));
    when(frame.isValid()).thenReturn(true);
    when(frame.isVisible()).thenReturn(true);
    return frame;
  }

  @Test
  void testRegistryRegisterOnlyClocks() {
    ItemFrame clockFrame = mockItemFrame(Material.CLOCK);
    ItemFrame regularFrame = mockItemFrame(Material.BOOK);

    registry.register(new PaperItemFrameAdapter(clockFrame));
    registry.register(new PaperItemFrameAdapter(regularFrame));

    assertTrue(registry.getTrackedFrames().contains(new PaperItemFrameAdapter(clockFrame)));
    assertFalse(registry.getTrackedFrames().contains(new PaperItemFrameAdapter(regularFrame)));

    registry.unregister(new PaperItemFrameAdapter(clockFrame));
    assertFalse(registry.getTrackedFrames().contains(new PaperItemFrameAdapter(clockFrame)));
  }

  @Test
  void testChunkLoadUnload() {
    ItemFrame frame1 = mockItemFrame(Material.CLOCK);
    ItemFrame frame2 = mockItemFrame(Material.CLOCK);

    Chunk chunk = mock(Chunk.class);
    when(chunk.getEntities()).thenReturn(new Entity[] {frame1, frame2});

    ChunkLoadEvent loadEvent = new ChunkLoadEvent(chunk, false);
    listener.onChunkLoad(loadEvent);

    assertTrue(registry.getTrackedFrames().contains(new PaperItemFrameAdapter(frame1)));
    assertTrue(registry.getTrackedFrames().contains(new PaperItemFrameAdapter(frame2)));

    ChunkUnloadEvent unloadEvent = new ChunkUnloadEvent(chunk);
    listener.onChunkUnload(unloadEvent);

    assertFalse(registry.getTrackedFrames().contains(new PaperItemFrameAdapter(frame1)));
    assertFalse(registry.getTrackedFrames().contains(new PaperItemFrameAdapter(frame2)));
  }

  @Test
  void testPlayerInteractEntity() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);

    PlayerInteractEntityEvent event =
        new PlayerInteractEntityEvent(
            server.addPlayer(), frame, org.bukkit.inventory.EquipmentSlot.HAND);

    listener.onPlayerInteractEntity(event);

    server.getScheduler().performOneTick();

    assertTrue(registry.getTrackedFrames().contains(new PaperItemFrameAdapter(frame)));
  }

  @Test
  void testHangingBreak() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);
    registry.register(new PaperItemFrameAdapter(frame));

    HangingBreakEvent event =
        new HangingBreakEvent(frame, HangingBreakEvent.RemoveCause.OBSTRUCTION);
    listener.onHangingBreak(event);

    assertFalse(registry.getTrackedFrames().contains(new PaperItemFrameAdapter(frame)));
  }

  @Test
  void testEntityDamageKnockout() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);
    registry.register(new PaperItemFrameAdapter(frame));

    EntityDamageByEntityEvent event = mock(EntityDamageByEntityEvent.class);
    when(event.getEntity()).thenReturn(frame);

    when(frame.getItem()).thenReturn(new ItemStack(Material.AIR));

    listener.onEntityDamage(event);

    server.getScheduler().performOneTick();

    assertFalse(registry.getTrackedFrames().contains(new PaperItemFrameAdapter(frame)));
  }

  @Test
  void testUpdaterUpdatesDisplayName() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);

    World world = mock(World.class);
    when(frame.getWorld()).thenReturn(world);
    when(world.getTime()).thenReturn(16L); // aligned to update interval
    when(world.getName()).thenReturn("world");
    when(world.getKey()).thenReturn(org.bukkit.NamespacedKey.minecraft("overworld"));
    when(world.getEnvironment()).thenReturn(World.Environment.NORMAL);

    registry.register(new PaperItemFrameAdapter(frame));

    ClockMessageService mockMessageService = mock(ClockMessageService.class);
    net.kyori.adventure.text.Component expectedComponent =
        net.kyori.adventure.text.Component.text("12:00 PM");
    when(mockMessageService.getFormattedTimeOnly(any(), anyLong(), any(), any()))
        .thenReturn(expectedComponent);

    ClockItemFrameUpdater updater =
        new ClockItemFrameUpdater(registry, mockMessageService, "en", 16, "🌀");
    updater.tick(new PaperWorldInfo(world));

    verify(frame)
        .setItem(
            argThat(
                item -> {
                  ItemMeta meta = item.getItemMeta();
                  return meta != null && expectedComponent.equals(meta.displayName());
                }),
            eq(false));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testOnRegisterCallbackInvoked() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);
    java.util.function.Consumer<ItemFrame> callback = mock(java.util.function.Consumer.class);
    listener.setOnRegisterCallback(callback);

    Chunk chunk = mock(Chunk.class);
    when(chunk.getEntities()).thenReturn(new Entity[] {frame});
    listener.onChunkLoad(new ChunkLoadEvent(chunk, false));

    verify(callback).accept(frame);
  }

  @Test
  void testNoItemFrameDropOnHangingBreak() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);
    when(frame.isVisible()).thenReturn(false);

    World world = mock(World.class);
    when(frame.getWorld()).thenReturn(world);
    org.bukkit.Location loc = new org.bukkit.Location(world, 0, 64, 0);
    when(frame.getLocation()).thenReturn(loc);

    HangingBreakEvent event =
        new HangingBreakEvent(frame, HangingBreakEvent.RemoveCause.OBSTRUCTION);
    listener.onHangingBreak(event);

    assertTrue(
        event.isCancelled(), "HangingBreakEvent should be cancelled to prevent default drops");
    verify(frame).remove();
    verify(world).dropItemNaturally(eq(loc), argThat(item -> item.getType() == Material.CLOCK));
  }

  @Test
  void testNoItemFrameDropOnEntityDamage() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);
    when(frame.isVisible()).thenReturn(false);

    World world = mock(World.class);
    when(frame.getWorld()).thenReturn(world);
    org.bukkit.Location loc = new org.bukkit.Location(world, 0, 64, 0);
    when(frame.getLocation()).thenReturn(loc);

    PlayerMock player = server.addPlayer();
    player.setGameMode(org.bukkit.GameMode.SURVIVAL);
    player.addAttachment(plugin, "clock_time.break", true);

    EntityDamageByEntityEvent event =
        new EntityDamageByEntityEvent(
            player,
            frame,
            org.bukkit.event.entity.EntityDamageEvent.DamageCause.ENTITY_ATTACK,
            1.0);
    listener.onEntityDamage(event);

    assertTrue(
        event.isCancelled(),
        "EntityDamageByEntityEvent should be cancelled to prevent default drops");
    verify(frame).remove();
    verify(world).dropItemNaturally(eq(loc), argThat(item -> item.getType() == Material.CLOCK));
  }

  @Test
  void testNoItemFrameDropOnEntityDamageNoPermission() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);
    when(frame.isVisible()).thenReturn(false);

    World world = mock(World.class);
    when(frame.getWorld()).thenReturn(world);
    org.bukkit.Location loc = new org.bukkit.Location(world, 0, 64, 0);
    when(frame.getLocation()).thenReturn(loc);

    PlayerMock player = server.addPlayer();
    player.setGameMode(org.bukkit.GameMode.SURVIVAL);
    player.addAttachment(plugin, "clock_time.break", false);

    EntityDamageByEntityEvent event =
        new EntityDamageByEntityEvent(
            player,
            frame,
            org.bukkit.event.entity.EntityDamageEvent.DamageCause.ENTITY_ATTACK,
            1.0);
    listener.onEntityDamage(event);

    assertTrue(
        event.isCancelled(),
        "EntityDamageByEntityEvent should be cancelled to protect the clock wall");
    verify(frame, never()).remove();
    verify(world, never()).dropItemNaturally(any(), any());
  }

  @Test
  void testOriginalNamePersistedAndRestoredOnHangingBreak() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);
    when(frame.isVisible()).thenReturn(false);

    ItemStack item = frame.getItem();
    ItemMeta meta = item.getItemMeta();
    org.bukkit.NamespacedKey key = new org.bukkit.NamespacedKey("clock-time", "original-name");
    meta.getPersistentDataContainer()
        .set(key, org.bukkit.persistence.PersistentDataType.STRING, "My Special Clock");
    item.setItemMeta(meta);

    World world = mock(World.class);
    when(frame.getWorld()).thenReturn(world);
    org.bukkit.Location loc = new org.bukkit.Location(world, 0, 64, 0);
    when(frame.getLocation()).thenReturn(loc);

    HangingBreakEvent event =
        new HangingBreakEvent(frame, HangingBreakEvent.RemoveCause.OBSTRUCTION);
    listener.onHangingBreak(event);

    assertTrue(event.isCancelled());
    verify(frame).remove();
    verify(world)
        .dropItemNaturally(
            eq(loc),
            argThat(
                dropped -> {
                  ItemMeta droppedMeta = dropped.getItemMeta();
                  return droppedMeta != null
                      && "My Special Clock".equals(droppedMeta.getDisplayName())
                      && !droppedMeta
                          .getPersistentDataContainer()
                          .has(key, org.bukkit.persistence.PersistentDataType.STRING);
                }));
  }

  @Test
  void testOriginalNameRestoredOnEntityDamage() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);
    when(frame.isVisible()).thenReturn(false);

    ItemStack item = frame.getItem();
    ItemMeta meta = item.getItemMeta();
    org.bukkit.NamespacedKey key = new org.bukkit.NamespacedKey("clock-time", "original-name");
    meta.getPersistentDataContainer()
        .set(key, org.bukkit.persistence.PersistentDataType.STRING, "Anvil Renamed Clock");
    meta.displayName(net.kyori.adventure.text.Component.text("12:00 PM"));
    item.setItemMeta(meta);

    World world = mock(World.class);
    when(frame.getWorld()).thenReturn(world);
    org.bukkit.Location loc = new org.bukkit.Location(world, 0, 64, 0);
    when(frame.getLocation()).thenReturn(loc);

    PlayerMock player = server.addPlayer();
    player.setGameMode(org.bukkit.GameMode.SURVIVAL);
    player.addAttachment(plugin, "clock_time.break", true);

    EntityDamageByEntityEvent event =
        new EntityDamageByEntityEvent(
            player,
            frame,
            org.bukkit.event.entity.EntityDamageEvent.DamageCause.ENTITY_ATTACK,
            1.0);
    listener.onEntityDamage(event);

    assertTrue(event.isCancelled());
    verify(frame).remove();
    verify(world)
        .dropItemNaturally(
            eq(loc),
            argThat(
                dropped -> {
                  ItemMeta droppedMeta = dropped.getItemMeta();
                  return droppedMeta != null
                      && "Anvil Renamed Clock".equals(droppedMeta.getDisplayName())
                      && !droppedMeta
                          .getPersistentDataContainer()
                          .has(key, org.bukkit.persistence.PersistentDataType.STRING);
                }));
  }

  @Test
  void testCreativeModeNoDropOnEntityDamage() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);
    when(frame.isVisible()).thenReturn(false);

    World world = mock(World.class);
    when(frame.getWorld()).thenReturn(world);
    org.bukkit.Location loc = new org.bukkit.Location(world, 0, 64, 0);
    when(frame.getLocation()).thenReturn(loc);

    PlayerMock player = server.addPlayer();
    player.setGameMode(org.bukkit.GameMode.CREATIVE);
    player.addAttachment(plugin, "clock_time.break", true);

    EntityDamageByEntityEvent event =
        new EntityDamageByEntityEvent(
            player,
            frame,
            org.bukkit.event.entity.EntityDamageEvent.DamageCause.ENTITY_ATTACK,
            1.0);
    listener.onEntityDamage(event);

    assertTrue(event.isCancelled());
    verify(frame).remove();
    verify(world, never()).dropItemNaturally(any(), any());
  }

  @Test
  void testEmptyOriginalNameClearedOnDrop() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);
    when(frame.isVisible()).thenReturn(false);

    ItemStack item = frame.getItem();
    ItemMeta meta = item.getItemMeta();
    org.bukkit.NamespacedKey key = new org.bukkit.NamespacedKey("clock-time", "original-name");
    meta.getPersistentDataContainer()
        .set(key, org.bukkit.persistence.PersistentDataType.STRING, "");
    meta.displayName(net.kyori.adventure.text.Component.text("3:00 PM"));
    item.setItemMeta(meta);

    World world = mock(World.class);
    when(frame.getWorld()).thenReturn(world);
    org.bukkit.Location loc = new org.bukkit.Location(world, 0, 64, 0);
    when(frame.getLocation()).thenReturn(loc);

    HangingBreakEvent event =
        new HangingBreakEvent(frame, HangingBreakEvent.RemoveCause.OBSTRUCTION);
    listener.onHangingBreak(event);

    assertTrue(event.isCancelled());
    verify(frame).remove();
    verify(world)
        .dropItemNaturally(
            eq(loc),
            argThat(
                dropped -> {
                  ItemMeta droppedMeta = dropped.getItemMeta();
                  return droppedMeta != null
                      && !droppedMeta.hasDisplayName()
                      && !droppedMeta
                          .getPersistentDataContainer()
                          .has(key, org.bukkit.persistence.PersistentDataType.STRING);
                }));
  }
}
