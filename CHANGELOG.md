# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Dynamic translation scanning and locale registration
  - Why: Enable server administrators to dynamically add translations for any arbitrary language (e.g., `messages_sv.properties`) without modifications to the plugin code.
- Configuration for custom wild-spin dimensions (`wild-spin-worlds`)
  - Why: Allow server administrators to define custom/modded dimensions that should exhibit wild clock spin behavior (like Nether/End) instead of normal time display.

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
