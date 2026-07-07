package io.github.beduality.clock_time.domain.adapter;

import java.util.UUID;
import net.kyori.adventure.text.Component;

/** Platform-agnostic adapter wrapping an Item Frame containing a clock. */
public interface ClockItemFrameAdapter {

  /** Returns the unique ID of the item frame. */
  UUID getUniqueId();

  /** Returns true if the entity has been removed from the world. */
  boolean isRemoved();

  /** Returns true if the item frame still contains a clock. */
  boolean hasClock();

  /** Updates the custom name of the clock item inside the frame. */
  void setClockCustomName(Component nameComponent);

  /** Returns the name of the world/dimension this frame is placed in. */
  String getWorldName();
}
