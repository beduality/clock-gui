package io.github.beduality.clock_time.domain.model;

/**
 * Platform-agnostic representation of a world or dimension. Allows core domain logic to determine
 * behavior without depending on Bukkit/Paper APIs.
 */
public interface WorldInfo {

  /**
   * Gets the name of the world.
   *
   * @return the world name
   */
  String getName();

  /**
   * Gets the unique identifier key for the dimension (e.g. "minecraft:overworld").
   *
   * @return the dimension key string
   */
  String getKey();

  /**
   * Determines whether the world is naturally a Nether or End environment.
   *
   * @return true if the environment is a wild spin dimension, false otherwise
   */
  boolean isNetherOrEnd();
}
