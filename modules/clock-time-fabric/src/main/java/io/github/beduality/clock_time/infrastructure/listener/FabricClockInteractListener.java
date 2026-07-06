package io.github.beduality.clock_time.infrastructure.listener;

import io.github.beduality.clock_time.domain.service.ClockMessageService;
import io.github.beduality.clock_time.infrastructure.adapter.FabricWorldInfo;
import java.util.Locale;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricClockInteractListener {

  private static final Logger LOGGER = LoggerFactory.getLogger("clock-time");

  private final ClockMessageService clockMessageService;

  public FabricClockInteractListener(ClockMessageService clockMessageService) {
    this.clockMessageService = clockMessageService;
  }

  public void register() {
    UseItemCallback.EVENT.register(
        (player, world, hand) -> {
          if (world.isClient() || hand != Hand.MAIN_HAND) {
            return TypedActionResult.pass(ItemStack.EMPTY);
          }

          ItemStack stack = player.getStackInHand(hand);
          if (stack.isOf(Items.CLOCK)) {
            sendClockTimeMessage((ServerPlayerEntity) player, (ServerWorld) world);
            return TypedActionResult.success(stack);
          }

          return TypedActionResult.pass(stack);
        });
  }

  private void sendClockTimeMessage(ServerPlayerEntity player, ServerWorld world) {
    long time = world.getTime();
    Locale locale = parseLocale(player.getClientOptions().language());

    var worldInfo = new FabricWorldInfo(world);
    Component messageComponent = clockMessageService.getClockMessage(worldInfo, time, locale);

    try {
      String json = GsonComponentSerializer.gson().serialize(messageComponent);
      Text text = Text.Serialization.fromJson(json, player.getRegistryManager());
      player.sendMessage(text, false);
    } catch (Exception e) {
      LOGGER.error("Failed to format and send clock time message to player", e);
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
