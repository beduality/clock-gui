# API Reference

This page contains reference specifications for ClockTime's developer integration points.

## Javadoc API Reference

* You can read the full Javadocs online at the [Javadoc API Reference](/apidocs/).

---

## TimeFormatter Specifications

**Package:** `io.github.beduality.clock_time.domain.service.TimeFormatter`

### Minecraft Tick to Real-World Time Mapping

| Minecraft Tick | Real Time Representation | Core Event |
|---|---|---|
| `0` | 6:00 AM | Sunrise |
| `6000` | 12:00 PM | Noon |
| `12000` | 6:00 PM | Sunset |
| `18000` | 12:00 AM | Midnight |

---

## Adventure Translation Registry Keys

**Registry Namespace:** `clocktime:main`

| Key | Description | Arguments |
|---|---|---|
| `clock_time.message.time` | Regular time display message | `{0}` = Formatted locale time string |
| `clock_time.message.wild-spin` | Message shown in Nether or End | None |
