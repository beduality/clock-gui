package io.github.beduality.clock_time.domain.service;

import org.bukkit.World;

/** Domain service responsible for determining if a dimension has standard or wild time behavior. */
public class DimensionTimeResolver {

  /**
   * Determines whether the given environment is a dimension where the clock spins wildly and
   * standard time has no meaning (i.e. Nether and End).
   *
   * @param environment the world environment/dimension
   * @return true if time spins wildly, false otherwise
   */
  public boolean isWildSpinDimension(World.Environment environment) {
    return environment == World.Environment.NETHER || environment == World.Environment.THE_END;
  }
}
