# Code Trimmer

A sophisticated Java/Spring Boot application that automatically cleans up whitespace and formatting in source code files. Code Trimmer removes trailing whitespace, normalizes blank lines, and optimizes formatting across multiple file types.

**[📚 Full Documentation](./docs/README.md)** | **[🚀 Quick Start](./docs/QUICKSTART.md)** | **[🤝 Contributing](./CONTRIBUTING.md)** | **[📋 License](./LICENSE)**

## Features

- **Batch File Processing**: Process entire directories recursively
- **Selective Filtering**: Include/exclude specific file types with pattern matching
- **Dry-Run Mode**: Preview changes without modifying files
- **Backup Creation**: Automatic backup files with `.bak` extension
- **Verbose Output**: Detailed processing information and statistics
- **Performance**: Configurable file size and count limits
- **Error Recovery**: Built-in error handling and backup restoration
- **Spring Shell CLI**: Interactive command-line interface
- **Comprehensive Logging**: Detailed operation logging with SLF4J

## Quick Install

```bash
# Clone repository
git clone https://github.com/yourusername/code-trimmer.git
cd code-trimmer

# Build project
mvn clean package

# Run JAR
java -jar target/code-trimmer-1.0.0.jar
```

## Usage Examples

### Basic Usage

```bash
trim /path/to/project
```

### Include Specific File Types

```bash
trim /path/to/project --include "java,py,js"
```

### Exclude Patterns

```bash
trim /path/to/project --exclude "*.min.js,*.lock,node_modules"
```

### Dry-Run Mode (Preview Changes)

```bash
trim /path/to/project --dry-run --verbose
```

### All Options

```bash
trim /path/to/project \
  --include "java,py,md" \
  --exclude "*.min.js" \
  --max-size 5242880 \
  --max-files 1000 \
  --dry-run \
  --verbose \
  --backup \
  --include-hidden \
  --no-color
```

## Command Options

| Option             | Type    | Default  | Description                                              |
| ------------------ | ------- | -------- | -------------------------------------------------------- |
| `directory`        | String  | Required | Directory path to process                                |
| `--include`        | String  | `*`      | Comma-separated extensions to include (e.g., `js,py,md`) |
| `--exclude`        | String  | Empty    | Comma-separated extensions to exclude (e.g., `*.min.js`) |
| `--max-size`       | Long    | 5242880  | Maximum file size in bytes (5MB default)                 |
| `--max-files`      | Integer | 50       | Maximum number of files to process                       |
| `--dry-run`        | Flag    | False    | Preview changes without modifying                        |
| `--verbose`        | Flag    | False    | Show detailed processing information                     |
| `--quiet`          | Flag    | False    | Suppress non-error output                                |
| `--no-color`       | Flag    | False    | Disable colored terminal output                          |
| `--include-hidden` | Flag    | False    | Include hidden files (starting with `.`)                 |
| `--backup`         | Flag    | True     | Create backup files (`.bak` extension)                   |
| `--no-limits`      | Flag    | False    | Disable file size and count limits                       |

## Commands

### `trim` - Process Files

```bash
trim [directory] [options]

# Example: Process all Java files in src/ with verbose output
trim src --include "java" --verbose
```

### `version` - Show Version

```bash
version
# Output: Code Trimmer v1.0.0
```

### `help-trim` - Show Command Help

```bash
help-trim
# Displays detailed help for trim command
```

## Processing Rules

Code Trimmer applies the following transformations:

1. **Trailing Whitespace**: Removes spaces and tabs at the end of lines
2. **Blank Line Normalization**: Consolidates multiple consecutive blank lines (configurable)
3. **Leading Indentation**: Preserves intentional indentation
4. **File Ending**: Ensures proper line ending

## Architecture

See [docs/ARCHITECTURE.md](./docs/ARCHITECTURE.md) for detailed architecture and design information.

## Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=FileProcessingServiceTest

# Run with coverage
mvn test jacoco:report
```

Current test coverage: **592 tests** across the codebase.

## Requirements

- **Java**: 17+
- **Maven**: 3.6.0+
- **Spring Boot**: 3.1.5+
- **OS**: Windows, macOS, Linux

## Configuration

See [docs/CONFIGURATION.md](./docs/CONFIGURATION.md) for environment variables and advanced configuration options.

## Troubleshooting

See [docs/TROUBLESHOOTING.md](./docs/TROUBLESHOOTING.md) for common issues and solutions.

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](./CONTRIBUTING.md) for guidelines.

## License

This project is licensed under the MIT License - see [LICENSE](./LICENSE) file for details.

## Support

- **Issues**: [GitHub Issues](https://github.com/yourusername/code-trimmer/issues)
- **Documentation**: [docs/](./docs/)
- **Quick Start**: [docs/QUICKSTART.md](./docs/QUICKSTART.md)

## Roadmap

- [ ] GUI Application
- [ ] IDE Plugins (VS Code, IntelliJ)
- [ ] Git Pre-commit Hook Support
- [ ] Docker Support
- [ ] Configuration File Support (`.trimmerrc`)

---

**Version**: 1.0.0 | **Last Updated**: December 2025 | **Status**: Active Development
