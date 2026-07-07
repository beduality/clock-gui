package io.github.beduality.clock_time.infrastructure.manager;

import io.github.beduality.clock_time.domain.service.ClockMessageService;
import io.github.beduality.clock_time.infrastructure.adapter.FabricWorldInfo;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

/** Task runner that updates clock entities inside item frames on Fabric. */
public class FabricClockItemFrameUpdater {

  private final FabricClockItemFrameRegistry registry;
  private final ClockMessageService messageService;
  private final Locale fallbackLocale;
  private final int updateInterval;
  private final Map<UUID, Long> lastMinutes = new ConcurrentHashMap<>();

  public FabricClockItemFrameUpdater(
      FabricClockItemFrameRegistry registry,
      ClockMessageService messageService,
      String fallbackLanguage,
      int updateInterval) {
    this.registry = registry;
    this.messageService = messageService;
    this.fallbackLocale = parseLocale(fallbackLanguage);
    this.updateInterval = updateInterval;
  }

  public void tick(ServerWorld world) {
    if (world.getTime() % updateInterval != 0) {
      return;
    }

    for (ItemFrameEntity frame : registry.getTrackedFrames()) {
      if (frame.getWorld() != world) {
        continue;
      }

      if (frame.isRemoved() || !frame.getHeldItemStack().isOf(Items.CLOCK)) {
        registry.unregister(frame);
        lastMinutes.remove(frame.getUuid());
        continue;
      }

      long time = world.getTime();
      long currentMinute = (time * 60) / 1000;
      Long lastMin = lastMinutes.get(frame.getUuid());
      if (lastMin != null && lastMin == currentMinute) {
        continue;
      }

      updateFrame(frame);
      lastMinutes.put(frame.getUuid(), currentMinute);
    }
  }

  /** Immediately updates the custom name of the clock item inside the item frame. */
  public void updateFrame(ItemFrameEntity frame) {
    if (frame == null
        || frame.isRemoved()
        || !(frame.getWorld() instanceof ServerWorld serverWorld)) {
      return;
    }

    long time = serverWorld.getTime();
    var worldInfo = new FabricWorldInfo(serverWorld);
    Component timeComponent = messageService.getClockMessage(worldInfo, time, fallbackLocale);

    ItemStack clockItem = frame.getHeldItemStack();
    if (clockItem.isOf(Items.CLOCK)) {
      try {
        String json = GsonComponentSerializer.gson().serialize(timeComponent);
        Text text = Text.Serialization.fromJson(json, serverWorld.getRegistryManager());

        ItemStack newStack = clockItem.copy();
        newStack.set(DataComponentTypes.CUSTOM_NAME, text);
        frame.setHeldItemStack(newStack, true);

        lastMinutes.put(frame.getUuid(), (time * 60) / 1000);
      } catch (Exception e) {
        // Ignored
      }
    }
  }

  private Locale parseLocale(String localeStr) {
    if (localeStr == null || localeStr.isEmpty() || localeStr.equalsIgnoreCase("root")) {
      return Locale.ROOT;
    }
    String[] parts = localeStr.split("_");
    if (parts.length == 1) {
      return Locale.of(parts[0]);
    } else if (parts.length == 2) {
      return Locale.of(parts[0], parts[1]);
    } else if (parts.length >= 3) {
      return Locale.of(parts[0], parts[1], parts[2]);
    }
    return Locale.of(localeStr);
  }
}
