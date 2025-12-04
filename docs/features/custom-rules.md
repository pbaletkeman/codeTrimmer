# Custom Trimming Rules

Define custom regex-based trimming rules to extend Code Trimmer's functionality.

## Overview

Custom rules allow you to define project-specific formatting transformations using regular expressions.

## Defining Rules

Rules are defined in the `rules` section of your `.trimmerrc` file:

```yaml
rules:
  - name: "rule-name"
    description: "What this rule does"
    pattern: "regex-pattern"
    replacement: "replacement-text"
    action: "replace"
    enabled: true
```

## Rule Properties

| Property | Required | Type | Description |
|----------|----------|------|-------------|
| `name` | Yes | String | Unique rule identifier |
| `description` | No | String | Human-readable description |
| `pattern` | Yes | String | Regular expression pattern |
| `replacement` | No | String | Replacement text |
| `action` | No | String | Action type: "replace" |
| `enabled` | No | Boolean | Enable/disable rule |

## Examples

### Remove Trailing Whitespace

```yaml
- name: "trailing-whitespace"
  description: "Remove trailing whitespace from all lines"
  pattern: "[ \\t]+$"
  replacement: ""
  enabled: true
```

### Normalize Blank Lines

```yaml
- name: "normalize-blanks"
  description: "Reduce multiple blank lines to maximum of 2"
  pattern: "(\\n\\s*){3,}"
  replacement: "\\n\\n"
  enabled: true
```

### Remove Debugging Comments

```yaml
- name: "remove-debug"
  description: "Remove DEBUG comments"
  pattern: "//\\s*DEBUG:.*$"
  replacement: ""
  enabled: true
```

### Fix TODO Format

```yaml
- name: "standardize-todo"
  description: "Standardize TODO format"
  pattern: "(TODO|FIXME|HACK)\\s*:"
  replacement: "TODO:"
  enabled: true
```

### Remove Console.log (JavaScript)

```yaml
- name: "remove-console"
  description: "Remove console.log statements"
  pattern: "console\\.log\\([^)]*\\);?"
  replacement: ""
  enabled: false  # Disabled by default
```

## Regex Escaping

In YAML, backslashes must be double-escaped:

```yaml
# Match whitespace
pattern: "\\s+"     # Correct
pattern: "\s+"      # Won't work

# Match literal dot
pattern: "\\."      # Correct
pattern: "."        # Matches any character
```

## Testing Rules

1. Test your regex at [regex101.com](https://regex101.com/)
2. Use dry-run mode to preview changes:

```bash
java -jar code-trimmer.jar trim . --dry-run --verbose
```

## Built-in Rules

Code Trimmer applies these rules by default (configurable):

1. **Trailing Whitespace Removal** - Removes spaces/tabs at line ends
2. **Blank Line Normalization** - Limits consecutive blank lines
3. **Final Newline** - Ensures file ends with single newline

## Validation

Invalid rules produce error CT-0004 or CT-0005:

```bash
# Validate rules in config file
java -jar code-trimmer.jar validate-config --config-path .trimmerrc
```

## Best Practices

1. **Test First** - Always test rules with `--dry-run`
2. **Be Specific** - Use precise patterns to avoid unintended matches
3. **Document** - Include clear descriptions
4. **Disable Carefully** - Some rules may conflict

---

**See Also:**
- [Configuration](./configuration.md)
- [Troubleshooting](../TROUBLESHOOTING.md)
