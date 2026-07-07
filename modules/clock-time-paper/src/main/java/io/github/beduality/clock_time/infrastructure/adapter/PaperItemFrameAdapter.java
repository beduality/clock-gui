package io.github.beduality.clock_time.infrastructure.adapter;

import io.github.beduality.clock_time.domain.adapter.ClockItemFrameAdapter;
import io.github.beduality.clock_time.domain.adapter.ClockItemFrameConstants;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/** Paper adapter implementation for Item Frames. */
public class PaperItemFrameAdapter implements ClockItemFrameAdapter {

  private final ItemFrame itemFrame;

  public PaperItemFrameAdapter(ItemFrame itemFrame) {
    this.itemFrame = itemFrame;
  }

  @Override
  public UUID getUniqueId() {
    return itemFrame.getUniqueId();
  }

  @Override
  public boolean isRemoved() {
    return !itemFrame.isValid();
  }

  @Override
  public boolean hasClock() {
    ItemStack item = itemFrame.getItem();
    return item != null && item.getType() == Material.CLOCK;
  }

  @Override
  public void setClockCustomName(Component nameComponent) {
    ItemStack item = itemFrame.getItem();
    if (item != null && item.getType() == Material.CLOCK) {
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
        org.bukkit.NamespacedKey key =
            new org.bukkit.NamespacedKey(
                ClockItemFrameConstants.PAPER_NAMESPACE,
                ClockItemFrameConstants.PAPER_ORIGINAL_NAME_KEY);
        if (!meta.getPersistentDataContainer()
            .has(key, org.bukkit.persistence.PersistentDataType.STRING)) {
          String originalName = meta.hasDisplayName() ? meta.getDisplayName() : "";
          meta.getPersistentDataContainer()
              .set(key, org.bukkit.persistence.PersistentDataType.STRING, originalName);
        }
        meta.displayName(nameComponent);
        item.setItemMeta(meta);
        itemFrame.setItem(item, false);
      }
    }
  }

  @Override
  public String getWorldName() {
    return itemFrame.getWorld().getName();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PaperItemFrameAdapter that)) return false;
    return itemFrame.equals(that.itemFrame);
  }

  @Override
  public int hashCode() {
    return itemFrame.hashCode();
  }
}
