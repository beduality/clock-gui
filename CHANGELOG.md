# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.2.0](https://github.com/beduality/clock-time/compare/v0.1.0...v0.2.0) (2026-07-05)


### Features

* add clock gui ([f3de437](https://github.com/beduality/clock-time/commit/f3de437dc185a4e11bcbb206e8e2b97f9464aad6))
* add file concatenation script and prevent overwriting existing translation properties ([9df3cab](https://github.com/beduality/clock-time/commit/9df3cab2883f55583fcdb81624b8ff9968efea93))
* add file concatenation script and update README documentation links ([e5c53c8](https://github.com/beduality/clock-time/commit/e5c53c8bb0987a8cae59546cfa4f66726525969b))
* add project logo and update color palette in documentation theme ([41c1649](https://github.com/beduality/clock-time/commit/41c1649cbe716ed165be539352d033dfecdf1336))
* implement locale-aware time formatting and dimension-specific clock messaging services ([28a17c4](https://github.com/beduality/clock-time/commit/28a17c49798fbc488f57f00eda0984214f5de41e))


### Bug Fixes

* open gui only on right-click ([c1d0ea1](https://github.com/beduality/clock-time/commit/c1d0ea1029b5b50a3856da4c55664b50cc669eec))

## [Unreleased]

### Added

- Support for 12-hour, 24-hour, and client-locale based time formatting.
- Resource bundle translation support dynamically extracted to the `languages/` folder.
- Clean architecture layout separating business domain rules from Bukkit adapters.
- Custom ClassLoader fallback resolution to allow server administrators to supply custom translations.
