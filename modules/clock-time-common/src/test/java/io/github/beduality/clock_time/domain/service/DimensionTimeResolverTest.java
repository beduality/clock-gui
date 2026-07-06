package io.github.beduality.clock_time.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.github.beduality.clock_time.domain.model.WorldInfo;
import java.util.List;
import org.junit.jupiter.api.Test;

class DimensionTimeResolverTest {

  private final DimensionTimeResolver resolver =
      new DimensionTimeResolver(List.of("custom_wild", "custom:space"));

  @Test
  void testNormalEnvironmentIsStandard() {
    WorldInfo world = mock(WorldInfo.class);
    when(world.isNetherOrEnd()).thenReturn(false);
    when(world.getName()).thenReturn("world");
    when(world.getKey()).thenReturn("minecraft:overworld");

    assertFalse(resolver.isWildSpinDimension(world));
  }

  @Test
  void testNetherEnvironmentIsWildSpin() {
    WorldInfo world = mock(WorldInfo.class);
    when(world.isNetherOrEnd()).thenReturn(true);
    when(world.getName()).thenReturn("world_nether");
    when(world.getKey()).thenReturn("minecraft:the_nether");

    assertTrue(resolver.isWildSpinDimension(world));
  }

  @Test
  void testEndEnvironmentIsWildSpin() {
    WorldInfo world = mock(WorldInfo.class);
    when(world.isNetherOrEnd()).thenReturn(true);
    when(world.getName()).thenReturn("world_the_end");
    when(world.getKey()).thenReturn("minecraft:the_end");

    assertTrue(resolver.isWildSpinDimension(world));
  }

  @Test
  void testCustomEnvironmentIsStandardByDefault() {
    WorldInfo world = mock(WorldInfo.class);
    when(world.isNetherOrEnd()).thenReturn(false);
    when(world.getName()).thenReturn("custom_standard");
    when(world.getKey()).thenReturn("custom:standard");

    assertFalse(resolver.isWildSpinDimension(world));
  }

  @Test
  void testCustomWorldByNameIsWildSpin() {
    WorldInfo world = mock(WorldInfo.class);
    when(world.isNetherOrEnd()).thenReturn(false);
    when(world.getName()).thenReturn("custom_wild");
    when(world.getKey()).thenReturn("custom:other");

    assertTrue(resolver.isWildSpinDimension(world));
  }

  @Test
  void testCustomWorldByKeyIsWildSpin() {
    WorldInfo world = mock(WorldInfo.class);
    when(world.isNetherOrEnd()).thenReturn(false);
    when(world.getName()).thenReturn("custom_other");
    when(world.getKey()).thenReturn("custom:space");

    assertTrue(resolver.isWildSpinDimension(world));
  }
}
