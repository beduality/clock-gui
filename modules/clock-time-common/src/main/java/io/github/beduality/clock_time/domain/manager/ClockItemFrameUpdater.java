package io.github.beduality.clock_time.domain.manager;

import io.github.beduality.clock_time.domain.adapter.ClockItemFrameAdapter;
import io.github.beduality.clock_time.domain.model.WorldInfo;
import io.github.beduality.clock_time.domain.service.ClockMessageService;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;

/** Platform-agnostic task runner that updates name tags of clocks inside item frames. */
public class ClockItemFrameUpdater {

  private final ClockItemFrameRegistry registry;
  private final ClockMessageService messageService;
  private final Locale fallbackLocale;
  private final int updateInterval;
  private final Map<UUID, Long> lastMinutes = new ConcurrentHashMap<>();

  public ClockItemFrameUpdater(
      ClockItemFrameRegistry registry,
      ClockMessageService messageService,
      String fallbackLanguage,
      int updateInterval) {
    this.registry = registry;
    this.messageService = messageService;
    this.fallbackLocale = parseLocale(fallbackLanguage);
    this.updateInterval = updateInterval;
  }

  /** Performs a tick check and updates all tracked frames belonging to the given world. */
  public void tick(WorldInfo worldInfo) {
    if (worldInfo.getTime() % updateInterval != 0) {
      return;
    }

    for (ClockItemFrameAdapter frame : registry.getTrackedFrames()) {
      if (!frame.getWorldName().equals(worldInfo.getName())) {
        continue;
      }

      if (frame.isRemoved() || !frame.hasClock()) {
        registry.unregister(frame);
        lastMinutes.remove(frame.getUniqueId());
        continue;
      }

      long time = worldInfo.getTime();
      long currentMinute = (time * 60) / 1000;
      Long lastMin = lastMinutes.get(frame.getUniqueId());
      if (lastMin != null && lastMin == currentMinute) {
        continue;
      }

      updateFrame(frame, worldInfo);
      lastMinutes.put(frame.getUniqueId(), currentMinute);
    }
  }

  /** Immediately updates the custom name of the clock item inside the item frame. */
  public void updateFrame(ClockItemFrameAdapter frame, WorldInfo worldInfo) {
    if (frame == null || frame.isRemoved()) {
      return;
    }

    long time = worldInfo.getTime();
    Component timeComponent = messageService.getClockMessage(worldInfo, time, fallbackLocale);
    frame.setClockCustomName(timeComponent);
    lastMinutes.put(frame.getUniqueId(), (time * 60) / 1000);
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
