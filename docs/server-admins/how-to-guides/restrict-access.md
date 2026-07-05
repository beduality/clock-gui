# Restrict clock usage

Use this guide to control which players can check the time using a clock.

## Preconditions

- A Minecraft server with ClockTime installed and running.
- A permissions manager plugin such as [LuckPerms](https://luckperms.net/) installed and configured.

## Restricting Access

By default, all players have access to the clock feature. You can restrict access by setting the `clock_time.use` permission node.

### Revoke Permission

To prevent a group of players (for example, the `default` group) from checking the time:

```bash
/lp group default permission set clock_time.use false
```

### Grant Permission

To grant permission to a specific group (for example, `vip` or `moderators`):

```bash
/lp group vip permission set clock_time.use true
```

## Verification

To verify that the permission is correctly applied:

1. Log in as a player in the restricted group (e.g., `default`).
2. Right-click a clock.
3. Confirm that you do not see the in-game time.
4. Log in as a player in the allowed group (e.g., `vip`).
5. Right-click a clock and confirm you can see the time.
