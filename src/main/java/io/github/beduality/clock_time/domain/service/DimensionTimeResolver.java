package io.github.beduality.clock_time.domain.service;

import java.util.Collection;
import java.util.Set;
import org.bukkit.World;

/* Domain service responsible for determining if a dimension has standard or wild time behavior. */
public class DimensionTimeResolver {

  private final Set<String> wildSpinWorlds;

  public DimensionTimeResolver(Collection<String> wildSpinWorlds) {
    this.wildSpinWorlds = Set.copyOf(wildSpinWorlds);
  }

  /*
   * Determines whether the given world is a dimension where the clock spins wildly and
   * standard time has no meaning (i.e. Nether, End, or a custom configured world).
   *
   * @param world the world
   * @return true if time spins wildly, false otherwise
   */
  public boolean isWildSpinDimension(World world) {
    if (world == null) {
      return false;
    }
    World.Environment environment = world.getEnvironment();
    if (environment == World.Environment.NETHER || environment == World.Environment.THE_END) {
      return true;
    }
    String name = world.getName();
    String key = world.getKey().toString();
    return wildSpinWorlds.contains(name) || wildSpinWorlds.contains(key);
  }
}
