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
        frame.dropStack(frame.getHeldItemStack().copy(), 0.0f);
      }
      // Cancel the rest of the method (so the item frame itself does NOT drop!)
      ci.cancel();
    }
  }
}
