package io.github.beduality.clock_time.infrastructure.manager;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Items;

/** Registry to track active item frames containing clocks in loaded worlds/chunks on Fabric. */
public class FabricClockItemFrameRegistry {
  private final Set<ItemFrameEntity> trackedFrames = ConcurrentHashMap.newKeySet();

  public boolean register(ItemFrameEntity frame) {
    if (frame != null && frame.getHeldItemStack().isOf(Items.CLOCK)) {
      return trackedFrames.add(frame);
    }
    return false;
  }

  public void unregister(ItemFrameEntity frame) {
    if (frame != null) {
      trackedFrames.remove(frame);
    }
  }

  public Set<ItemFrameEntity> getTrackedFrames() {
    return trackedFrames;
  }
}
