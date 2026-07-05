package io.github.beduality.clock_time.domain.service;

import org.bukkit.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DimensionTimeResolverTest {

    private final DimensionTimeResolver resolver = new DimensionTimeResolver();

    @Test
    void testNormalEnvironmentIsStandard() {
        assertFalse(resolver.isWildSpinDimension(World.Environment.NORMAL));
    }

    @Test
    void testNetherEnvironmentIsWildSpin() {
        assertTrue(resolver.isWildSpinDimension(World.Environment.NETHER));
    }

    @Test
    void testEndEnvironmentIsWildSpin() {
        assertTrue(resolver.isWildSpinDimension(World.Environment.THE_END));
    }

    @Test
    void testCustomEnvironmentIsStandard() {
        assertFalse(resolver.isWildSpinDimension(World.Environment.CUSTOM));
    }
}
