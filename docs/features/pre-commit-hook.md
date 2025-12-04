# Pre-commit Hook Generator

Automatically format code before commits using Git pre-commit hooks.

## Overview

Code Trimmer can generate Git pre-commit hooks that run automatically before each commit, ensuring all staged files meet whitespace standards.

## Generating Hooks

```bash
# Generate hook in current directory
java -jar code-trimmer.jar generate-hook

# Generate hook for specific directory
java -jar code-trimmer.jar generate-hook --directory /path/to/project

# Force overwrite existing hook
java -jar code-trimmer.jar generate-hook --force
```

## Generated Files

The command creates three hook files for cross-platform support:

| File | Platform | Location |
|------|----------|----------|
| `pre-commit` | Linux/macOS | `.git/hooks/pre-commit` |
| `pre-commit.bat` | Windows (CMD) | `.git/hooks/pre-commit.bat` |
| `pre-commit.ps1` | Windows (PowerShell) | `.git/hooks/pre-commit.ps1` |

## How It Works

1. Hook runs before each commit
2. Gets list of staged files
3. Runs Code Trimmer in dry-run mode
4. If files need trimming, commit is blocked
5. User fixes files and commits again

## Configuration

The hook looks for Code Trimmer JAR in these locations:

1. `CODE_TRIMMER_JAR` environment variable
2. `./target/code-trimmer-1.0.0.jar`
3. `./code-trimmer.jar`

### Setting JAR Location

```bash
# Linux/macOS
export CODE_TRIMMER_JAR=/path/to/code-trimmer.jar

# Windows (CMD)
set CODE_TRIMMER_JAR=C:\path\to\code-trimmer.jar

# Windows (PowerShell)
$env:CODE_TRIMMER_JAR = "C:\path\to\code-trimmer.jar"
```

## Usage Workflow

1. Generate hook:
   ```bash
   java -jar code-trimmer.jar generate-hook
   ```

2. Make changes to code

3. Stage changes:
   ```bash
   git add .
   ```

4. Commit:
   ```bash
   git commit -m "My changes"
   # Hook runs automatically
   ```

5. If files need trimming:
   ```
   Some files need whitespace cleanup.
   Run 'java -jar code-trimmer.jar trim .' to fix them.
   ```

6. Fix and re-commit:
   ```bash
   java -jar code-trimmer.jar trim .
   git add .
   git commit -m "My changes"
   ```

## Bypassing the Hook

For emergency commits that bypass the hook:

```bash
git commit --no-verify -m "Emergency fix"
```

**Warning:** Use sparingly. Bypassed commits may have whitespace issues.

## Troubleshooting

### Hook not running

```bash
# Check if hook is executable (Linux/macOS)
chmod +x .git/hooks/pre-commit

# Verify hook exists
ls -la .git/hooks/pre-commit
```

### JAR not found

```bash
# Set environment variable
export CODE_TRIMMER_JAR=/path/to/jar

# Or place JAR in project root
cp /path/to/code-trimmer.jar ./code-trimmer.jar
```

### Permission denied

```bash
# Fix hook permissions
chmod +x .git/hooks/pre-commit
```

## Integration with CI/CD

The pre-commit hook complements CI/CD:

1. **Pre-commit hook** - Catches issues before commit
2. **CI/CD pipeline** - Validates all code on push

See [Pipeline Support](./pipeline-support.md) for CI/CD integration.

## Removing Hooks

```bash
# Remove all hook files
rm .git/hooks/pre-commit
rm .git/hooks/pre-commit.bat
rm .git/hooks/pre-commit.ps1
```

---

**See Also:**
- [Configuration](./configuration.md)
- [Pipeline Support](./pipeline-support.md)
