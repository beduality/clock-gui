package io.github.beduality.clock_time.infrastructure.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameEntity.class)
public class ItemFrameEntityMixin {

  @Inject(method = "dropHeldStack", at = @At("HEAD"), cancellable = true)
  private void onDropHeldStack(Entity breaker, boolean dropSelf, CallbackInfo ci) {
    ItemFrameEntity frame = (ItemFrameEntity) (Object) this;
    if (frame.isInvisible() && frame.getHeldItemStack().isOf(Items.CLOCK)) {
      // If broken by a creative player, don't drop anything.
      // Otherwise, drop only the clock.
      boolean creative =
          breaker instanceof net.minecraft.entity.player.PlayerEntity player
              && player.getAbilities().creativeMode;
      if (!creative) {
        net.minecraft.item.ItemStack clockStack = frame.getHeldItemStack().copy();
        net.minecraft.component.type.NbtComponent nbtComp =
            clockStack.get(net.minecraft.component.DataComponentTypes.CUSTOM_DATA);
        if (nbtComp != null && nbtComp.getNbt().contains("clock_time:original_name")) {
          String originalNameJson = nbtComp.getNbt().getString("clock_time:original_name");
          net.minecraft.component.type.NbtComponent.set(
              net.minecraft.component.DataComponentTypes.CUSTOM_DATA,
              clockStack,
              nbt -> nbt.remove("clock_time:original_name"));
          if (originalNameJson == null || originalNameJson.isEmpty()) {
            clockStack.remove(net.minecraft.component.DataComponentTypes.CUSTOM_NAME);
          } else {
            try {
              net.minecraft.text.Text originalText =
                  net.minecraft.text.Text.Serialization.fromJson(
                      originalNameJson, frame.getWorld().getRegistryManager());
              clockStack.set(net.minecraft.component.DataComponentTypes.CUSTOM_NAME, originalText);
            } catch (Exception e) {
              clockStack.remove(net.minecraft.component.DataComponentTypes.CUSTOM_NAME);
            }
          }
        }
        frame.dropStack(clockStack, 0.0f);
      }
      // Cancel the rest of the method (so the item frame itself does NOT drop!)
      ci.cancel();
    }
  }
}
