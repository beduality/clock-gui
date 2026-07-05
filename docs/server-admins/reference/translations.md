# Translations Reference

This page contains reference schemas and properties tables for the ClockTime localization system.

## Built-in Locales

ClockTime maps standard ISO locale codes to Minecraft client-side language options:

| Code / Suffix | Language | Minecraft Client Locale |
|---|---|---|
| `en` (default / root) | English | `en_us`, `en_gb`, etc. |
| `es` | Spanish | `es_es`, `es_mx`, etc. |
| `pt` | Portuguese | `pt_pt`, `pt_br`, etc. |
| `de` | German | `de_de` |
| `fr` | French | `fr_fr`, `fr_ca`, etc. |
| `it` | Italian | `it_it` |
| `ja` | Japanese | `ja_jp` |
| `ko` | Korean | `ko_kr` |
| `nl` | Dutch | `nl_nl` |
| `pl` | Polish | `pl_pl` |
| `ru` | Russian | `ru_ru` |
| `tr` | Turkish | `tr_tr` |
| `uk` | Ukrainian | `uk_ua` |
| `zh_CN` | Chinese (Simplified) | `zh_cn` |
| `zh_TW` | Chinese (Traditional) | `zh_tw` |

## Directory Layout

Extracted translation assets are organized under the following schema within the plugin data directory:

```text
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

| Key | Description | Placeholder |
|---|---|---|
| `clock_time.message.time` | Displayed when players query the clock in standard time environments. | `{0}` = Formatted local time string |
| `clock_time.message.wild-spin` | Displayed in Nether or End dimensions. | None |
