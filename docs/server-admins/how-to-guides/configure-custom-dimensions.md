# Configure Custom Dimensions

This guide explains how to configure custom or modded dimensions where clocks should spin wildly (like in the Nether and The End) instead of displaying standard time.

## Preconditions

- A Minecraft server with ClockTime installed.
- Access to the server's configuration file under `plugins/ClockTime/config.yml`.

## Step-by-Step Configuration

1. **Identify the World Name or Dimension Key**:
   - For custom worlds, find their folder name in the server directory (e.g., `custom_nether`, `space_world`).
   - For datapack-generated or modded dimensions, locate their namespaced key (e.g., `custom:space`, `modded:twilight_forest`).
2. **Open the Config File**:
   - Open `plugins/ClockTime/config.yml` in a text editor.
3. **Add to the `wild-spin-worlds` List**:
   - Locate the `wild-spin-worlds` option.
   - Add the world name or namespaced key to the list.
   
   ```yaml title="config.yml"
   # A list of custom world names or dimension keys (e.g. 'custom_world' or 'custom:space')
   # that should be treated as wild-spin dimensions.
   wild-spin-worlds:
     - "custom_nether"
     - "custom:space"
   ```
4. **Save the File**.
5. **Restart the Server** (or reload the plugin) to apply the changes.

## Verification

1. Log into the server.
2. Teleport to the configured custom dimension (e.g., `/execute in custom:space run tp @s ~ ~ ~`).
3. Hold a clock in your main hand and **right-click**.
4. Verify that you receive the wild spin message:
   `The clock spins wildly... Time has no meaning here.`
