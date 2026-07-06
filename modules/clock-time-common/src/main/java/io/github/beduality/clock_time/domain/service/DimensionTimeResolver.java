package io.github.beduality.clock_time.domain.service;

import io.github.beduality.clock_time.domain.model.WorldInfo;
import java.util.Collection;
import java.util.Set;

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
   * @param world the world representation
   * @return true if time spins wildly, false otherwise
   */
  public boolean isWildSpinDimension(WorldInfo world) {
    if (world == null) {
      return false;
    }
    if (world.isNetherOrEnd()) {
      return true;
    }
    String name = world.getName();
    String key = world.getKey();
    return wildSpinWorlds.contains(name) || (key != null && wildSpinWorlds.contains(key));
  }
}
