# Troubleshooting Guide

This guide covers common issues and solutions for Code Trimmer.

## Table of Contents

- [Error Codes Reference](#error-codes-reference)
- [Common Issues](#common-issues)
- [Configuration Problems](#configuration-problems)
- [Performance Issues](#performance-issues)
- [Platform-Specific Issues](#platform-specific-issues)

---

## Error Codes Reference

All Code Trimmer errors use the format `CT-XXXX`. Below is a complete reference:

### Configuration Errors (CT-0001 to CT-0009)

| Code | Title | Description | Solution |
|------|-------|-------------|----------|
| CT-0001 | Invalid configuration file | Configuration file format is invalid | Check YAML/JSON syntax |
| CT-0002 | Configuration file not found | Specified config file does not exist | Verify file path |
| CT-0003 | Invalid configuration value | Configuration value is out of range | Check value constraints |
| CT-0004 | Invalid rule definition | Custom rule is malformed | Review rule syntax |
| CT-0005 | Invalid regex pattern | Regex pattern in rule is invalid | Test regex pattern |

### File Operation Errors (CT-0010 to CT-0039)

| Code | Title | Description | Solution |
|------|-------|-------------|----------|
| CT-0010 | File not found | Specified file or directory does not exist | Verify path |
| CT-0011 | File read error | Cannot read file contents | Check permissions |
| CT-0012 | File write error | Cannot write to file | Check disk space |
| CT-0013 | Permission denied | Insufficient permissions | Run with proper access |
| CT-0014 | Directory not accessible | Cannot access directory | Check directory permissions |
| CT-0015 | File too large | File exceeds maximum size limit | Increase maxFileSize |
| CT-0016 | Binary file skipped | Binary file detected | Normal behavior |

### Backup/Restore Errors (CT-0040 to CT-0049)

| Code | Title | Description | Solution |
|------|-------|-------------|----------|
| CT-0040 | Backup creation failed | Failed to create backup file | Check disk space |
| CT-0041 | Restore failed | Failed to restore from backup | Check permissions |
| CT-0042 | Backup file missing | Backup file not found for restore | Verify backup exists |
| CT-0043 | Backup file corrupted | Backup file is corrupted | Use Git to restore |

### Hook Generation Errors (CT-0050 to CT-0059)

| Code | Title | Description | Solution |
|------|-------|-------------|----------|
| CT-0050 | Hook generation failed | Failed to generate pre-commit hook | Check .git folder |
| CT-0051 | Git directory not found | .git directory not found | Run from Git repo root |
| CT-0052 | Hook already exists | Pre-commit hook already exists | Use --force flag |

### Report Errors (CT-0060 to CT-0069)

| Code | Title | Description | Solution |
|------|-------|-------------|----------|
| CT-0060 | Report generation failed | Failed to generate report | Check output path |
| CT-0061 | Invalid report format | Unsupported report format | Use json, csv, or sqlite |
| CT-0062 | Report endpoint failed | Failed to send report | Check network/endpoint |
| CT-0063 | SQLite database error | Error writing to SQLite | Check disk space |

### General Errors (CT-0090 to CT-0099)

| Code | Title | Description | Solution |
|------|-------|-------------|----------|
| CT-0090 | Unknown error | An unexpected error occurred | Check logs |
| CT-0091 | Operation cancelled | Operation was cancelled | Retry operation |
| CT-0092 | Disk full | Insufficient disk space | Free disk space |

---

## Common Issues

### 1. "File not found" when running trim command

**Problem:** Error CT-0010 when trying to process a directory.

**Solution:**
```bash
# Verify the directory exists
ls -la /path/to/directory

# Use absolute path
java -jar code-trimmer.jar trim /absolute/path/to/project
```

### 2. "Permission denied" errors

**Problem:** Error CT-0013 when processing files.

**Solution:**
```bash
# Check file permissions
ls -la file.txt

# Fix permissions if needed
chmod 644 file.txt

# Or run with appropriate user
```

### 3. No files are being processed

**Problem:** "Files processed: 0" even though directory has files.

**Solutions:**
```bash
# Check include patterns
java -jar code-trimmer.jar trim . --include "java,py,js"

# Include hidden files
java -jar code-trimmer.jar trim . --include-hidden

# Disable limits
java -jar code-trimmer.jar trim . --no-limits
```

### 4. Files are being skipped as binary

**Problem:** Text files are being skipped as binary.

**Solution:**
- Check if files contain NULL bytes
- Verify file encoding is UTF-8
- Files with certain extensions are automatically skipped

### 5. Backup files not created

**Problem:** No .bak files appear after processing.

**Solution:**
```bash
# Ensure backup flag is set
java -jar code-trimmer.jar trim . --backup

# Check in configuration file
createBackups: true
```

---

## Configuration Problems

### Invalid YAML syntax

**Problem:** CT-0001 error when loading configuration.

**Solution:**
```yaml
# Check indentation (use spaces, not tabs)
rules:
  - name: "rule1"    # Correct
    pattern: "test"

# Avoid:
rules:
	- name: "rule1"  # Tab character - WRONG
```

### Invalid regex pattern

**Problem:** CT-0005 error for custom rules.

**Solution:**
```yaml
# Test your regex at https://regex101.com
rules:
  - name: "test"
    pattern: "\\s+$"  # Double escape backslashes in YAML
```

### Configuration not loading

**Problem:** Settings from .trimmerrc not being applied.

**Solution:**
```bash
# Validate configuration file
java -jar code-trimmer.jar validate-config --config-path .trimmerrc

# Explicitly specify config file
java -jar code-trimmer.jar trim-config . --config-file .trimmerrc
```

---

## Performance Issues

### Processing is slow

**Problem:** Processing takes too long for large projects.

**Solutions:**
```bash
# Limit file count
java -jar code-trimmer.jar trim . --max-files 100

# Limit file size
java -jar code-trimmer.jar trim . --max-size 1048576

# Target specific extensions
java -jar code-trimmer.jar trim . --include "java,py"

# Exclude heavy directories
java -jar code-trimmer.jar trim . --exclude "node_modules,build,dist"
```

### Out of memory errors

**Problem:** Java OutOfMemoryError during processing.

**Solution:**
```bash
# Increase JVM heap size
java -Xmx1g -jar code-trimmer.jar trim .

# Or limit file size
java -jar code-trimmer.jar trim . --max-size 2097152
```

---

## Platform-Specific Issues

### Windows: Line ending issues

**Problem:** Files show as modified after processing.

**Solution:**
```bash
# Configure Git to handle line endings
git config --global core.autocrlf true
```

### macOS/Linux: Permission issues with hooks

**Problem:** Pre-commit hook not executing.

**Solution:**
```bash
# Make hook executable
chmod +x .git/hooks/pre-commit

# Verify permissions
ls -la .git/hooks/pre-commit
```

### WSL: Path issues

**Problem:** Paths not resolving correctly in WSL.

**Solution:**
```bash
# Use WSL paths, not Windows paths
java -jar code-trimmer.jar trim /home/user/project

# Not: C:\Users\user\project
```

---

## Getting Help

If you encounter an issue not covered here:

1. **Check logs:** Run with `--verbose` flag for detailed output
2. **Search issues:** Check [GitHub Issues](https://github.com/pbaletkeman/codeTrimmer/issues)
3. **Open an issue:** Include error code, command used, and environment details

---

**Last Updated:** December 2025
