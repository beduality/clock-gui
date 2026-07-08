# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.4.0] - 2026-07-07

### Added

#### Server Administrators

- **[Paper & Fabric]** `item-frame-clocks.encode-spaces` configuration option (default: `true`)
  - Why: Bedrock clients (e.g. via Geyser) treat regular spaces (`U+0020`) as word-break boundaries in item `CUSTOM_NAME` fields, causing item frame clock names like `"3:45 PM"` to split or truncate. When enabled, regular spaces in item frame display names are replaced with non-breaking spaces (`U+00A0`). Affects only the item frame / wall clock name path — chat messages are untouched. Disable if running a Java-only server and non-breaking spaces cause unexpected rendering.

### Fixed

#### Players

- **[Paper & Fabric]** Prevent players from rotating or interacting with clocks placed in item frames.
  - Why: The item frame is used as a workaround to display the clock in vanilla, so allowing player interaction or rotation is a visual bug.

## [0.3.0] - 2026-07-06

### Added

#### Players

- **[Paper & Fabric]** Realtime clocks in item frames
  - Why: Allow players to place clocks in item frames to serve as visual wall clocks that display in-game time in real-time when looked at.

#### Server Administrators

- **[Paper & Fabric]** Configuration options for item frame clocks
  - Why: Enable configuration (`item-frame-clocks` / `itemFrameClocks`) to toggle the feature and set custom update intervals (defaulting to 16 ticks).
- **[Paper]** Configuration schema version 2 migration path
  - Why: Automatically upgrade older server config files to version 2 while preserving existing settings.

#### Developers

- **[Paper & Fabric]** Optimized item frame clock tracking and updates
  - Why: Track loaded clock-containing frames via an event-driven registry and cache Minecraft minute values to minimize processing overhead.

## [0.2.0] - 2026-07-05

### Added

#### Players

- **[Fabric]** Ported to the Fabric platform (supporting Minecraft 1.20.6)
  - Why: Expand platform availability to Fabric servers and single-player/client environments.
 
#### Server Administrators
 
- **[Paper]** Dynamic translation scanning and locale registration
  - Why: Enable server administrators to dynamically add translations for any arbitrary language (e.g., `messages_sv.properties`) without modifications to the plugin code.
- **[Paper]** Configuration for custom wild-spin dimensions (`wild-spin-worlds`)
  - Why: Allow server administrators to define custom/modded dimensions that should exhibit wild clock spin behavior (like Nether/End) instead of normal time display.
 
#### Developers
 
- **[Common]** Multi-module Gradle build restructure
  - Why: Cleanly isolate platform-agnostic domain logic (`clock-time-common`) from platform-specific infrastructure (`clock-time-paper`, `clock-time-fabric`) under a tidy `modules/` subdirectory structure to support multiple targets without code duplication.
- **[Common]** Compile-time dimension abstraction (`WorldInfo`)
  - Why: Abstract Minecraft's dimension state away from Bukkit API classes to enable cross-platform compatibility.
- **[Common]** Translation registry classpath fallback resilience
  - Why: Enable default language properties to load from resource classpath bundles when JAR-file zip extraction fails or is skipped (e.g. in MockBukkit test and dev environments).

## [0.1.0] - 2026-07-05

### Added

#### Players

- Client-locale and 12/24-hour time formatting
  - Why: Support heterogeneous client environments and user preferences without manual server configuration.
- Dimension-aware time display behavior for the Nether and The End
  - Why: Avoid displaying invalid or static time readouts where standard day/night cycles do not apply.

#### Server Administrators

- Dynamic extraction of default translations to the `languages/` folder
  - Why: Allow server administrators to customize language strings directly without modifying the plugin jar.
- Programmatic configuration generation using Configurate
  - Why: Ensure configuration schema validation and automatic updates for default values.
- Automated release workflows to Hangar and Modrinth
  - Why: Simplify artifact distribution for operators and maintainers.
- Local translation overwrite prevention
  - Why: Ensure custom edits made by server administrators are preserved during updates or restarts.

#### Developers

- Custom ClassLoader fallback translation resolution
  - Why: Prevent translation loading failures in environments with non-standard classloaders.
- JitPack support and Maven publishing tasks
  - Why: Enable downstream developers to integrate the plugin as a dependency in their own builds.
