package io.github.beduality.clock_time.infrastructure.manager;

import io.github.beduality.clock_time.domain.service.ClockMessageService;
import io.github.beduality.clock_time.infrastructure.adapter.PaperWorldInfo;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Task runner that periodically updates clocks placed in tracked item frames with the formatted
 * time.
 */
public class ClockItemFrameUpdater extends BukkitRunnable {
  private final ClockItemFrameRegistry registry;
  private final ClockMessageService messageService;
  private final Locale fallbackLocale;
  private final Map<java.util.UUID, Long> lastMinutes = new ConcurrentHashMap<>();

  public ClockItemFrameUpdater(
      ClockItemFrameRegistry registry,
      ClockMessageService messageService,
      String fallbackLanguage) {
    this.registry = registry;
    this.messageService = messageService;
    this.fallbackLocale = parseLocale(fallbackLanguage);
  }

  @Override
  public void run() {
    for (ItemFrame frame : registry.getTrackedFrames()) {
      if (!frame.isValid() || frame.getItem().getType() != Material.CLOCK) {
        registry.unregister(frame);
        lastMinutes.remove(frame.getUniqueId());
        continue;
      }

      long time = frame.getWorld().getTime();
      long currentMinute = (time * 60) / 1000;
      Long lastMin = lastMinutes.get(frame.getUniqueId());
      if (lastMin != null && lastMin == currentMinute) {
        continue;
      }

      updateFrame(frame);
      lastMinutes.put(frame.getUniqueId(), currentMinute);
    }
  }

  /** Immediately updates the display name of the clock in the item frame. */
  public void updateFrame(ItemFrame frame) {
    if (frame == null || !frame.isValid()) {
      return;
    }
    long time = frame.getWorld().getTime();
    var worldInfo = new PaperWorldInfo(frame.getWorld());
    net.kyori.adventure.text.Component timeComponent =
        messageService.getClockMessage(worldInfo, time, fallbackLocale);

    ItemStack clockItem = frame.getItem();
    if (clockItem.getType() == Material.CLOCK) {
      clockItem.editMeta(meta -> meta.displayName(timeComponent));
      frame.setItem(clockItem, false);
      lastMinutes.put(frame.getUniqueId(), (time * 60) / 1000);
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
