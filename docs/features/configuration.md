# Configuration File Support

Code Trimmer supports persistent configuration through `.trimmerrc` files in YAML or JSON format.

## Overview

Configuration files allow teams to share consistent trimming settings across projects.

## File Locations

Code Trimmer searches for configuration in this order:

1. Path specified with `--config-file` option
2. `.trimmerrc` in the target directory
3. `.trimmerrc.yaml` in the target directory
4. `.trimmerrc.json` in the target directory

## Usage

### Using Default Configuration

```bash
# Auto-loads .trimmerrc from project directory
java -jar code-trimmer.jar trim /path/to/project
```

### Specifying Configuration File

```bash
# Use specific config file
java -jar code-trimmer.jar trim-config /path/to/project --config-file /path/to/.trimmerrc
```

### Validating Configuration

```bash
# Check if config file is valid
java -jar code-trimmer.jar validate-config --config-path .trimmerrc
```

## Configuration Options

### File Filtering

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `include` | String | `*` | File extensions to include |
| `exclude` | String | `""` | File extensions to exclude |
| `includeHidden` | Boolean | `false` | Process hidden files |
| `followSymlinks` | Boolean | `false` | Follow symbolic links |

### Whitespace Rules

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `maxConsecutiveBlankLines` | Integer | `2` | Maximum blank lines |
| `ensureFinalNewline` | Boolean | `true` | Ensure file ends with newline |
| `trimTrailingWhitespace` | Boolean | `true` | Remove trailing whitespace |

### Performance Limits

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `maxFileSize` | Long | `5242880` | Maximum file size (bytes) |
| `maxFiles` | Integer | `50` | Maximum files to process |
| `noLimits` | Boolean | `false` | Disable all limits |

### Operation Modes

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `dryRun` | Boolean | `false` | Preview without modifying |
| `createBackups` | Boolean | `true` | Create .bak files |
| `failFast` | Boolean | `false` | Stop on first error |

### Output Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `verbose` | Boolean | `false` | Detailed output |
| `quiet` | Boolean | `false` | Suppress output |
| `noColor` | Boolean | `false` | Disable colors |

## Sample Files

See [docs/samples/](../samples/) for complete examples:

- [.trimmerrc.yaml](../samples/.trimmerrc.yaml) - YAML format with comments
- [.trimmerrc.json](../samples/.trimmerrc.json) - JSON format

## Best Practices

1. **Commit .trimmerrc** to version control for team consistency
2. **Use YAML** format for better readability and comments
3. **Set `createBackups: true`** for safety
4. **Use `dryRun: true`** when testing new configurations

## Error Handling

| Error Code | Description | Solution |
|------------|-------------|----------|
| CT-0001 | Invalid format | Check YAML/JSON syntax |
| CT-0002 | File not found | Verify file path |
| CT-0003 | Invalid value | Check value constraints |

---

**See Also:**
- [Custom Rules](./custom-rules.md)
- [CLI Reference](./cli-reference.md)
