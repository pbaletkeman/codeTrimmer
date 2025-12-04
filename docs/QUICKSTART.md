# Quick Start Guide

Get Code Trimmer up and running in 5 minutes.

## 1. Prerequisites

- Java 17 or higher (`java -version`)
- Maven 3.6.0+ (`mvn -version`)
- Git (optional, for cloning)

## 2. Clone and Build

```bash
# Clone repository
git clone https://github.com/yourusername/code-trimmer.git
cd code-trimmer

# Build project (takes ~1-2 minutes)
mvn clean package
```

## 3. Launch Application

```bash
java -jar target/code-trimmer-1.0.0.jar
```

You should see:

```shell
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| ._ _| |_|_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 v1.0.0

code-trimmer>
```

## 4. Try Your First Command

### Basic: Process Current Directory

```bash
code-trimmer> trim .
```

Output:

```shell
Processing directory: /home/user/code-trimmer
Files found: 547
Files processed: 234
Files modified: 5
Execution time: 1.234 seconds
```

### Preview: Dry-run with Details

```bash
code-trimmer> trim . --dry-run --verbose
```

This shows what WOULD be changed without actually modifying files.

### Specific: Process Java Files Only

```bash
code-trimmer> trim src --include "java"
```

### Safe: Create Backups (Enabled by Default)

```bash
code-trimmer> trim . --verbose
```

All modified files get `.bak` backups automatically.

## 5. Common Use Cases

### Case 1: Clean Up Project Before Commit

```bash
code-trimmer> trim . --dry-run --verbose
# Review changes carefully...
code-trimmer> trim . --verbose
# Changes applied!
```

### Case 2: Process Multiple File Types

```bash
code-trimmer> trim /project \
  --include "java,py,js,md" \
  --verbose
```

### Case 3: Exclude Vendor Code

```bash
code-trimmer> trim . \
  --exclude "node_modules,vendor,dist" \
  --verbose
```

### Case 4: Limit Processing

```bash
code-trimmer> trim /large-project \
  --max-files 1000 \
  --max-size 5242880
```

### Case 5: Quiet Mode for Automation

```bash
code-trimmer> trim . --quiet
```

## 6. Help Commands

### Get Help on Trim Command

```bash
code-trimmer> help-trim
```

Shows detailed help with all available options.

### Show Version

```bash
code-trimmer> version
```

## 7. Common Options

| Option        | Purpose                    | Example                |
| ------------- | -------------------------- | ---------------------- |
| `--dry-run`   | Preview only, don't modify | `trim . --dry-run`     |
| `--verbose`   | Show detailed output       | `trim . --verbose`     |
| `--include`   | Only process these types   | `--include "java,py"`  |
| `--exclude`   | Skip these patterns        | `--exclude "*.min.js"` |
| `--quiet`     | Suppress output            | `trim . --quiet`       |
| `--no-color`  | Disable colors (CI/CD)     | `--no-color`           |
| `--no-limits` | Remove file limits         | `--no-limits`          |

## 8. Exit Application

```bash
code-trimmer> exit
```

Or press `Ctrl+C`

## 10. Next Steps

## Troubleshooting

### No files processed?

```bash
code-trimmer> trim . --no-limits --verbose --dry-run
```

### Permission denied?

Make sure you have write permission to the directory.

### Out of memory?

Exit and restart with more memory:

```bash
java -Xmx1g -jar target/code-trimmer-1.0.0.jar
```

**Ready to go!** 🚀 Start with `trim .` and review the output.
