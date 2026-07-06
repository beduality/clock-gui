# Feature Proposal: Realtime Clock Display in Item Frames

This proposal outlines the design and implementation details for a new feature in `clock-time` that allows clocks placed in (invisible) item frames to show the current world time dynamically when hovered or looked at by players.

## 1. Goal & Overview

When a player places a Clock into an Item Frame (particularly an invisible one), the clock item should have its custom display name updated automatically in real-time to match the world's current time. 

In Minecraft, if an item in an item frame has a custom name, looking at the frame displays the name tag floating above/near it. By updating this name tag in real-time, players can create custom "wall clocks" that display the in-game time without requiring any client-side mods.

---

## 2. Requirements

- **Paper/Spigot Compatibility:** Implement via standard Bukkit/Paper API methods.
- **Dynamic Updates:** The display name should update periodically (e.g., every 20 ticks / 1 second, or every Minecraft minute) to reflect the progression of time.
- **Robust Tracking:** Correctly identify when a clock is placed, rotated, or removed from an item frame.
- **Chunk Safety:** Properly register frames as chunks load and unregister them when chunks unload to avoid memory leaks or accessing unloaded entities.
- **Performance-First Design:** Avoid iterating over all world entities or chunks. Use an event-driven registry to track only the frames containing clocks.

---

## 3. Technical Design

### A. Registry System
We will maintain a registry class, `ClockItemFrameRegistry`, to track active item frames containing clocks in loaded chunks:

```java
public class ClockItemFrameRegistry {
    private final Set<ItemFrame> trackedFrames = ConcurrentHashMap.newKeySet();

    public void register(ItemFrame frame) {
        if (frame.getItem().getType() == Material.CLOCK) {
            trackedFrames.add(frame);
        }
    }

    public void unregister(ItemFrame frame) {
        trackedFrames.remove(frame);
    }

    public Set<ItemFrame> getTrackedFrames() {
        return trackedFrames;
    }
}
```

### B. Event Listeners
We need to monitor item frame contents and chunk loading state to populate and clean up the registry:

1. **Player Interaction (`PlayerInteractEntityEvent`):**
   Triggers when a player places a clock into an item frame or rotates it.
2. **Entity Damage (`EntityDamageByEntityEvent` / `HangingBreakEvent`):**
   Triggers when the item frame is broken or the clock is removed.
3. **Chunk Loading/Unloading (`ChunkLoadEvent` / `ChunkUnloadEvent`):**
   - On Chunk Load: Scan the chunk's entities for `ItemFrame` instances containing clocks and register them.
   - On Chunk Unload: Unregister any tracked item frames belonging to that chunk.
4. **Entity Lifecycle (`EntitySpawnEvent` / `EntityRemoveEvent`):**
   Detect when item frames are spawned or removed under other circumstances.

### C. Updater Task (Scheduler)
A repeating task (e.g., running every 20 ticks / 1 second) will run on the main server thread to update the name of the clocks:

```java
public class ClockItemFrameUpdater implements Runnable {
    private final ClockItemFrameRegistry registry;
    private final ClockMessageService messageService;
    private final Locale fallbackLocale;

    @Override
    public void run() {
        for (ItemFrame frame : registry.getTrackedFrames()) {
            if (!frame.isValid() || frame.getItem().getType() != Material.CLOCK) {
                registry.unregister(frame);
                continue;
            }

            long time = frame.getWorld().getTime();
            var worldInfo = new PaperWorldInfo(frame.getWorld());
            
            // Format time based on configuration / fallback locale
            net.kyori.adventure.text.Component timeComponent = 
                messageService.getClockMessage(worldInfo, time, fallbackLocale);

            ItemStack clockItem = frame.getItem();
            clockItem.editMeta(meta -> meta.displayName(timeComponent));
            frame.setItem(clockItem, false); // false to avoid playing the placement sound again
        }
    }
}
```

---

## 4. Performance Considerations

To keep the performance footprint close to zero:
- **No Global Scans:** The updater only iterates over `trackedFrames`, which represents a small subset of item frames currently loaded on the server.
- **Suppressed Sound Packets:** By setting `frame.setItem(item, false)`, we update the item state without spamming the item placement sound to nearby clients.
- **Configurable Update Interval:** The update frequency can be made configurable (e.g., `update-interval-ticks: 20`), allowing server administrators to choose between high precision (every second) or lower CPU overhead (every 10–30 seconds).

---

## 5. Configuration Options

We will add a new configuration section to `config.conf` / `ClockTimePluginConfig`:

```hocon
item-frame-clocks {
  # Enable dynamic time updates for clocks placed in item frames
  enabled = true
  # How often to update the clock time (in ticks, 20 ticks = 1 second)
  update-interval = 20
}
```

---

## 6. Multi-Platform Support (Fabric & Paper)

Because this project is structured as a multi-module Gradle project:
- **Common Service Layer:** Formatting and messages will continue to leverage `ClockMessageService` in `clock-time-common`.
- **Platform-Specific Listeners:**
  - `clock-time-paper` will implement this using the Bukkit events and Paper scheduler.
  - `clock-time-fabric` can implement equivalent logic using Fabric's block/entity tick events or ServerEntityEvents if requested in the future.
