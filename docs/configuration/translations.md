# Translations

ClockTime ships with **16 built-in languages** and supports adding custom translations without modifying the JAR.

## Built-in Languages

| Code | Language |
|---|---|
| `en` | English (default) |
| `es` | Spanish |
| `pt` | Portuguese |
| `de` | German |
| `fr` | French |
| `it` | Italian |
| `ja` | Japanese |
| `ko` | Korean |
| `nl` | Dutch |
| `pl` | Polish |
| `ru` | Russian |
| `tr` | Turkish |
| `uk` | Ukrainian |
| `zh_CN` | Chinese (Simplified) |
| `zh_TW` | Chinese (Traditional) |

## Translation Files

On first startup, the plugin extracts all translation files into:

```
plugins/ClockTime/languages/
├── messages.properties         # English (root fallback)
├── messages_de.properties
├── messages_es.properties
├── messages_fr.properties
├── messages_it.properties
├── messages_ja.properties
├── messages_ko.properties
├── messages_nl.properties
├── messages_pl.properties
├── messages_pt.properties
├── messages_ru.properties
├── messages_tr.properties
├── messages_uk.properties
├── messages_zh_CN.properties
└── messages_zh_TW.properties
```

## Translation Keys

Each file contains two message keys:

| Key | Description | Placeholder |
|---|---|---|
| `clock_time.message.time` | Shown when a player checks the time in the Overworld | `{0}` = formatted time |
| `clock_time.message.wild-spin` | Shown in the Nether or End | — |

## Editing Messages

Open any file in `plugins/ClockTime/languages/` and edit the values. Messages support [MiniMessage](https://docs.advntr.dev/minimessage/format.html) formatting.

```properties title="messages.properties (English)"
clock_time.message.time=<gold>Current Time:</gold> <aqua>{0}</aqua>
clock_time.message.wild-spin=<red>The clock spins wildly... Time has no meaning here.</red>
```

!!! tip

    Changes take effect after a server restart. No `/reload` command is needed.

## Adding a New Language

To add support for a language that isn't bundled (e.g., Swedish):

1. Create `messages_sv.properties` in `plugins/ClockTime/languages/`.
2. Add translations for both keys:

    ```properties title="messages_sv.properties"
    clock_time.message.time=<gold>Aktuell tid:</gold> <aqua>{0}</aqua>
    clock_time.message.wild-spin=<red>Klockan snurrar vilt... Tiden har ingen betydelse här.</red>
    ```

3. Restart the server.

Any player whose Minecraft client is set to Swedish (`sv`) will automatically see these messages.
