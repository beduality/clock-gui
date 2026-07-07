package io.github.beduality.clock_time.domain.adapter;

/** Shared constants for clock item frame name persistence across platforms. */
public final class ClockItemFrameConstants {

  private ClockItemFrameConstants() {}

  /**
   * The Paper NamespacedKey namespace for the original name tag. Used with {@code new
   * NamespacedKey("clock-time", ORIGINAL_NAME_PDC_KEY)}.
   */
  public static final String PAPER_NAMESPACE = "clock-time";

  /** The Paper NamespacedKey key part for the original name tag. */
  public static final String PAPER_ORIGINAL_NAME_KEY = "original-name";

  /** The Fabric/NBT raw key for storing the original custom name in CUSTOM_DATA. */
  public static final String FABRIC_ORIGINAL_NAME_KEY = "clock_time:original_name";
}
