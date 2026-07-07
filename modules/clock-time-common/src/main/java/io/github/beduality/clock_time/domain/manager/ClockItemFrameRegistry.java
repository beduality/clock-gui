package io.github.beduality.clock_time.domain.manager;

import io.github.beduality.clock_time.domain.adapter.ClockItemFrameAdapter;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** Generic, thread-safe registry to track active item frames containing clocks. */
public class ClockItemFrameRegistry {

  private final Set<ClockItemFrameAdapter> trackedFrames = ConcurrentHashMap.newKeySet();

  /** Registers a clock item frame adapter. */
  public boolean register(ClockItemFrameAdapter frame) {
    if (frame != null && frame.hasClock()) {
      return trackedFrames.add(frame);
    }
    return false;
  }

  /** Unregisters a clock item frame adapter. */
  public void unregister(ClockItemFrameAdapter frame) {
    if (frame != null) {
      trackedFrames.remove(frame);
    }
  }

  /** Returns all tracked clock item frame adapters. */
  public Set<ClockItemFrameAdapter> getTrackedFrames() {
    return trackedFrames;
  }
}
