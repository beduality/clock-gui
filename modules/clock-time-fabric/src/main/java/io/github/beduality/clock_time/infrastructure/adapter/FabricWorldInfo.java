package io.github.beduality.clock_time.infrastructure.adapter;

import io.github.beduality.clock_time.domain.model.WorldInfo;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

/** Adapter implementing {@link WorldInfo} for Fabric {@link ServerWorld} instances. */
public class FabricWorldInfo implements WorldInfo {

  private final ServerWorld world;

  /**
   * Constructs a new FabricWorldInfo wrapping the given Fabric server world.
   *
   * @param world the Fabric server world to wrap
   */
  public FabricWorldInfo(ServerWorld world) {
    this.world = world;
  }

  @Override
  public long getTime() {
    return world != null ? world.getTime() : 0L;
  }

  @Override
  public String getName() {
    return world != null ? world.getRegistryKey().getValue().getPath() : "";
  }

  @Override
  public String getKey() {
    return world != null ? world.getRegistryKey().getValue().toString() : "";
  }

  @Override
  public boolean isNetherOrEnd() {
    if (world == null) {
      return false;
    }
    RegistryKey<World> key = world.getRegistryKey();
    return key == World.NETHER || key == World.END;
  }
}
