package io.github.beduality.clock_time.infrastructure.manager;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;

/** Registry to track active item frames containing clocks in loaded chunks. */
public class ClockItemFrameRegistry {
  private final Set<ItemFrame> trackedFrames = ConcurrentHashMap.newKeySet();

  public boolean register(ItemFrame frame) {
    if (frame != null && frame.getItem().getType() == Material.CLOCK) {
      return trackedFrames.add(frame);
    }
    return false;
  }

  public void unregister(ItemFrame frame) {
    if (frame != null) {
      trackedFrames.remove(frame);
    }
  }

  public Set<ItemFrame> getTrackedFrames() {
    return trackedFrames;
  }
}
