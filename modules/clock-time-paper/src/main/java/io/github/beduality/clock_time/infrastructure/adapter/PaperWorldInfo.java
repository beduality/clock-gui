package io.github.beduality.clock_time.infrastructure.adapter;

import io.github.beduality.clock_time.domain.model.WorldInfo;
import org.bukkit.World;

/** Adapter implementing {@link WorldInfo} for Bukkit/Paper {@link World} instances. */
public class PaperWorldInfo implements WorldInfo {

  private final World world;

  /**
   * Constructs a new PaperWorldInfo wrapping the given Bukkit world.
   *
   * @param world the Bukkit world to wrap
   */
  public PaperWorldInfo(World world) {
    this.world = world;
  }

  @Override
  public String getName() {
    return world != null ? world.getName() : "";
  }

  @Override
  public String getKey() {
    return world != null ? world.getKey().toString() : "";
  }

  @Override
  public boolean isNetherOrEnd() {
    if (world == null) {
      return false;
    }
    World.Environment env = world.getEnvironment();
    return env == World.Environment.NETHER || env == World.Environment.THE_END;
  }
}
