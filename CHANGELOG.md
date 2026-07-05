# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Support for 12-hour, 24-hour, and client-locale based time formatting.
- Resource bundle translation support dynamically extracted to the `languages/` folder.
- Clean architecture layout separating business domain rules from Bukkit adapters.
- Custom ClassLoader fallback resolution to allow server administrators to supply custom translations.
