package io.github.beduality.clock_time.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.junit.jupiter.api.Test;

class DimensionTimeResolverTest {

  private final DimensionTimeResolver resolver =
      new DimensionTimeResolver(List.of("custom_wild", "custom:space"));

  @Test
  void testNormalEnvironmentIsStandard() {
    World world = mock(World.class);
    when(world.getEnvironment()).thenReturn(World.Environment.NORMAL);
    when(world.getName()).thenReturn("world");
    when(world.getKey()).thenReturn(NamespacedKey.minecraft("overworld"));

    assertFalse(resolver.isWildSpinDimension(world));
  }

  @Test
  void testNetherEnvironmentIsWildSpin() {
    World world = mock(World.class);
    when(world.getEnvironment()).thenReturn(World.Environment.NETHER);
    when(world.getName()).thenReturn("world_nether");
    when(world.getKey()).thenReturn(NamespacedKey.minecraft("the_nether"));

    assertTrue(resolver.isWildSpinDimension(world));
  }

  @Test
  void testEndEnvironmentIsWildSpin() {
    World world = mock(World.class);
    when(world.getEnvironment()).thenReturn(World.Environment.THE_END);
    when(world.getName()).thenReturn("world_the_end");
    when(world.getKey()).thenReturn(NamespacedKey.minecraft("the_end"));

    assertTrue(resolver.isWildSpinDimension(world));
  }

  @Test
  void testCustomEnvironmentIsStandardByDefault() {
    World world = mock(World.class);
    when(world.getEnvironment()).thenReturn(World.Environment.CUSTOM);
    when(world.getName()).thenReturn("custom_standard");
    when(world.getKey()).thenReturn(new NamespacedKey("custom", "standard"));

    assertFalse(resolver.isWildSpinDimension(world));
  }

  @Test
  void testCustomWorldByNameIsWildSpin() {
    World world = mock(World.class);
    when(world.getEnvironment()).thenReturn(World.Environment.CUSTOM);
    when(world.getName()).thenReturn("custom_wild");
    when(world.getKey()).thenReturn(new NamespacedKey("custom", "other"));

    assertTrue(resolver.isWildSpinDimension(world));
  }

  @Test
  void testCustomWorldByKeyIsWildSpin() {
    World world = mock(World.class);
    when(world.getEnvironment()).thenReturn(World.Environment.CUSTOM);
    when(world.getName()).thenReturn("custom_other");
    when(world.getKey()).thenReturn(new NamespacedKey("custom", "space"));

    assertTrue(resolver.isWildSpinDimension(world));
  }
}
