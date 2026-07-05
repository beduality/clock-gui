# Permissions

ClockTime uses a single permission node to control access to the clock feature.

## Reference

| Permission | Description | Default |
|---|---|---|
| `clock_time.use` | Allows the player to right-click a clock to see the time | `true` (all players) |

## Behavior

- **Granted by default** — all players can use the clock without any permission plugin.
- **Deny to restrict** — use a permission manager like [LuckPerms](https://luckperms.net/) to revoke access for specific groups.

!!! example "LuckPerms example"

    Remove access for the `default` group:

    ```
    /lp group default permission set clock_time.use false
    ```
