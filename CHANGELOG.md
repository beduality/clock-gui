# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.2.0] - 2026-07-05

### Added

#### Players

- Ported to the Fabric platform (supporting Minecraft 1.20.6)
  - Why: Expand platform availability to Fabric servers and single-player/client environments.

#### Server Administrators

- Dynamic translation scanning and locale registration
  - Why: Enable server administrators to dynamically add translations for any arbitrary language (e.g., `messages_sv.properties`) without modifications to the plugin code.
- Configuration for custom wild-spin dimensions (`wild-spin-worlds`)
  - Why: Allow server administrators to define custom/modded dimensions that should exhibit wild clock spin behavior (like Nether/End) instead of normal time display.

#### Developers

- Multi-module Gradle build restructure
  - Why: Cleanly isolate platform-agnostic domain logic (`clock-time-common`) from platform-specific infrastructure (`clock-time-paper`, `clock-time-fabric`) under a tidy `modules/` subdirectory structure to support multiple targets without code duplication.
- Compile-time dimension abstraction (`WorldInfo`)
  - Why: Abstract Minecraft's dimension state away from Bukkit API classes to enable cross-platform compatibility.
- Translation registry classpath fallback resilience
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
