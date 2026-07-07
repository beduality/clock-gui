package io.github.beduality.clock_time.infrastructure.listener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.beduality.clock_time.ClockTimePlugin;
import io.github.beduality.clock_time.domain.service.ClockMessageService;
import io.github.beduality.clock_time.infrastructure.manager.ClockItemFrameRegistry;
import io.github.beduality.clock_time.infrastructure.manager.ClockItemFrameUpdater;
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
    return frame;
  }

  @Test
  void testRegistryRegisterOnlyClocks() {
    ItemFrame clockFrame = mockItemFrame(Material.CLOCK);
    ItemFrame regularFrame = mockItemFrame(Material.BOOK);

    registry.register(clockFrame);
    registry.register(regularFrame);

    assertTrue(registry.getTrackedFrames().contains(clockFrame));
    assertFalse(registry.getTrackedFrames().contains(regularFrame));

    registry.unregister(clockFrame);
    assertFalse(registry.getTrackedFrames().contains(clockFrame));
  }

  @Test
  void testChunkLoadUnload() {
    ItemFrame frame1 = mockItemFrame(Material.CLOCK);
    ItemFrame frame2 = mockItemFrame(Material.CLOCK);

    Chunk chunk = mock(Chunk.class);
    when(chunk.getEntities()).thenReturn(new Entity[] {frame1, frame2});

    ChunkLoadEvent loadEvent = new ChunkLoadEvent(chunk, false);
    listener.onChunkLoad(loadEvent);

    assertTrue(registry.getTrackedFrames().contains(frame1));
    assertTrue(registry.getTrackedFrames().contains(frame2));

    ChunkUnloadEvent unloadEvent = new ChunkUnloadEvent(chunk);
    listener.onChunkUnload(unloadEvent);

    assertFalse(registry.getTrackedFrames().contains(frame1));
    assertFalse(registry.getTrackedFrames().contains(frame2));
  }

  @Test
  void testPlayerInteractEntity() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);

    PlayerInteractEntityEvent event =
        new PlayerInteractEntityEvent(
            server.addPlayer(), frame, org.bukkit.inventory.EquipmentSlot.HAND);

    listener.onPlayerInteractEntity(event);

    server.getScheduler().performOneTick();

    assertTrue(registry.getTrackedFrames().contains(frame));
  }

  @Test
  void testHangingBreak() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);
    registry.register(frame);

    HangingBreakEvent event =
        new HangingBreakEvent(frame, HangingBreakEvent.RemoveCause.OBSTRUCTION);
    listener.onHangingBreak(event);

    assertFalse(registry.getTrackedFrames().contains(frame));
  }

  @Test
  void testEntityDamageKnockout() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);
    registry.register(frame);

    EntityDamageByEntityEvent event = mock(EntityDamageByEntityEvent.class);
    when(event.getEntity()).thenReturn(frame);

    when(frame.getItem()).thenReturn(new ItemStack(Material.AIR));

    listener.onEntityDamage(event);

    server.getScheduler().performOneTick();

    assertFalse(registry.getTrackedFrames().contains(frame));
  }

  @Test
  void testUpdaterUpdatesDisplayName() {
    ItemFrame frame = mockItemFrame(Material.CLOCK);

    World world = mock(World.class);
    when(frame.getWorld()).thenReturn(world);
    when(world.getTime()).thenReturn(6000L);
    when(world.getName()).thenReturn("world");
    when(world.getKey()).thenReturn(org.bukkit.NamespacedKey.minecraft("overworld"));
    when(world.getEnvironment()).thenReturn(World.Environment.NORMAL);

    registry.register(frame);

    ClockMessageService mockMessageService = mock(ClockMessageService.class);
    net.kyori.adventure.text.Component expectedComponent =
        net.kyori.adventure.text.Component.text("12:00 PM");
    when(mockMessageService.getClockMessage(any(), anyLong(), any())).thenReturn(expectedComponent);

    ClockItemFrameUpdater updater = new ClockItemFrameUpdater(registry, mockMessageService, "en");
    updater.run();

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
}
