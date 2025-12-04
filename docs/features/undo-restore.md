# Undo/Restore Functionality

Restore files from backups after Code Trimmer processing.

## Overview

Code Trimmer creates `.bak` backup files before modifying any file. The undo feature allows you to restore files from these backups.

## Creating Backups

Backups are created automatically when processing files (unless disabled):

```bash
# Process with backups (default)
java -jar code-trimmer.jar trim .

# Explicitly enable backups
java -jar code-trimmer.jar trim . --backup
```

**Important:** Without backups, undo is not possible. Use `--backup` (default) or use Git for version control.

## Undo Commands

### Restore All Backups in Directory

```bash
# Restore all .bak files in current directory
java -jar code-trimmer.jar undo

# Restore in specific directory
java -jar code-trimmer.jar undo --directory /path/to/project
```

### Recursive Restore

```bash
# Restore all .bak files recursively
java -jar code-trimmer.jar undo --recursive
```

### Selective Restore by Pattern

```bash
# Restore only Java files
java -jar code-trimmer.jar undo --pattern ".*\\.java"

# Restore files matching pattern
java -jar code-trimmer.jar undo --pattern "Test.*"
```

## List Available Backups

Before restoring, you can list available backup files:

```bash
# List backups in current directory
java -jar code-trimmer.jar list-backups

# List backups recursively
java -jar code-trimmer.jar list-backups --recursive

# List in specific directory
java -jar code-trimmer.jar list-backups --directory /path/to/project
```

## How Restore Works

1. Finds all `.bak` files in the specified directory
2. For each backup:
   - Copies backup content to original file
   - Deletes the `.bak` file
3. Reports success/failure for each file

## Output Example

```
=== Restore Summary ===
Backup files found: 5
Files restored: 4
Files failed: 1
  ✗ /project/readonly.java (Permission denied)

Some files could not be restored.
```

## Exit Codes

| Code | Meaning |
|------|---------|
| 0 | All files restored successfully |
| 1 | Partial success (some files failed) |
| 2 | All files failed to restore |

## Error Handling

### CT-0041: Restore Failed

**Problem:** Cannot restore file from backup.

**Solutions:**
- Check file permissions
- Verify disk space
- Check if file is locked

### CT-0042: Backup File Missing

**Problem:** No backup file exists.

**Solutions:**
- Verify backups were created (`--backup` flag)
- Use Git to restore: `git checkout -- file.txt`

### CT-0043: Backup File Corrupted

**Problem:** Backup file is corrupted.

**Solutions:**
- Use Git to restore
- Restore from version control

## Best Practices

1. **Always create backups** - Keep `--backup` enabled (default)
2. **Use Git** - Git provides better version control
3. **Test first** - Use `--dry-run` before actual processing
4. **Verify after restore** - Check files after restoring

## Alternative Recovery

### Using Git

If backups weren't created or are corrupted:

```bash
# Restore single file
git checkout -- file.txt

# Restore all changes
git checkout -- .

# Restore to specific commit
git checkout abc123 -- file.txt
```

### Manual Restore

If you have `.bak` files but the command fails:

```bash
# Manual restore (Linux/macOS)
for f in *.bak; do
  cp "$f" "${f%.bak}"
  rm "$f"
done

# Manual restore (Windows)
for %f in (*.bak) do copy "%f" "%~nf" && del "%f"
```

## Backup Retention

- Backups are stored in the same directory as the original file
- Backups are overwritten on each run (unless `--no-overwrite-backup`)
- Clean up backups manually when no longer needed:

```bash
# Remove all .bak files
find . -name "*.bak" -delete

# List before deleting
find . -name "*.bak"
```

---

**See Also:**
- [Configuration](./configuration.md)
- [Troubleshooting](../TROUBLESHOOTING.md)
