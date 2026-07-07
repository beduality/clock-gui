package io.github.beduality.clock_time.infrastructure.adapter;

import io.github.beduality.clock_time.domain.adapter.ClockItemFrameAdapter;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

/** Fabric adapter implementation for Item Frame Entities. */
public class FabricItemFrameAdapter implements ClockItemFrameAdapter {

  private final ItemFrameEntity itemFrame;

  public FabricItemFrameAdapter(ItemFrameEntity itemFrame) {
    this.itemFrame = itemFrame;
  }

  @Override
  public UUID getUniqueId() {
    return itemFrame.getUuid();
  }

  @Override
  public boolean isRemoved() {
    return itemFrame.isRemoved();
  }

  @Override
  public boolean hasClock() {
    return itemFrame.getHeldItemStack().isOf(Items.CLOCK);
  }

  @Override
  public void setClockCustomName(Component nameComponent) {
    ItemStack clockItem = itemFrame.getHeldItemStack();
    if (clockItem.isOf(Items.CLOCK)) {
      try {
        String json = GsonComponentSerializer.gson().serialize(nameComponent);
        Text text = Text.Serialization.fromJson(json, itemFrame.getWorld().getRegistryManager());

        ItemStack newStack = clockItem.copy();
        net.minecraft.component.type.NbtComponent nbtComponent =
            newStack.get(DataComponentTypes.CUSTOM_DATA);
        boolean alreadyHasOriginal =
            nbtComponent != null && nbtComponent.getNbt().contains("clock_time:original_name");

        if (!alreadyHasOriginal) {
          Text originalCustomName = newStack.get(DataComponentTypes.CUSTOM_NAME);
          String originalNameStr = "";
          if (originalCustomName != null) {
            originalNameStr =
                Text.Serialization.toJsonString(
                    originalCustomName, itemFrame.getWorld().getRegistryManager());
          }
          final String finalOriginalName = originalNameStr;
          net.minecraft.component.type.NbtComponent.set(
              DataComponentTypes.CUSTOM_DATA,
              newStack,
              nbt -> nbt.putString("clock_time:original_name", finalOriginalName));
        }

        newStack.set(DataComponentTypes.CUSTOM_NAME, text);
        itemFrame.setHeldItemStack(newStack, true);
      } catch (Exception e) {
        // Ignored
      }
    }
  }

  @Override
  public String getWorldName() {
    return itemFrame.getWorld() != null
        ? itemFrame.getWorld().getRegistryKey().getValue().getPath()
        : "";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FabricItemFrameAdapter that)) return false;
    return itemFrame.equals(that.itemFrame);
  }

  @Override
  public int hashCode() {
    return itemFrame.hashCode();
  }
}
