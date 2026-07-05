# How to Restrict Clock Usage

This guide explains how to control which players can check the time using a clock.

## Restricting Access with LuckPerms

By default, all players have access to the clock feature. You can restrict access using a permission manager such as [LuckPerms](https://luckperms.net/).

### Revoke Permission

To prevent a group of players (for example, the `default` group) from checking the time:

```bash
/lp group default permission set clock_time.use false
```

### Grant Permission

To grant permission back to a specific group (for example, `vip` or `moderators`):

```bash
/lp group vip permission set clock_time.use true
```
