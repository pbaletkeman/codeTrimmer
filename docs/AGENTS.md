# AGENTS.md

Documentation for autonomous agents and AI system interactions with Code Trimmer.

## Overview

This document describes how autonomous agents, AI systems, and automation tools can integrate with and utilize Code Trimmer for automated code formatting and cleanup operations.

Code Trimmer is available in two independent implementations:

- **Java 17 LTS** (reference implementation)
- **Python 3.12+** (parallel implementation)

See the appropriate section below for your implementation.

---

## Java Agent Integration

### REST API Integration

While Code Trimmer is primarily a CLI tool, it can be invoked programmatically:

```java
// Programmatic invocation
ProcessingStatistics stats = service.processDirectory("/project");
System.out.println("Files processed: " + stats.getTotalFiles());
System.out.println("Files modified: " + stats.getTotalModified());
```

### Command-Line Integration

Agents can invoke Code Trimmer as a subprocess:

```bash
#!/bin/bash
# CI/CD Agent Script

java -jar code-trimmer-1.0.0.jar \
  trim /project \
  --include "java,py" \
  --no-color \
  --quiet

exit $?  # Return exit code
```

### Exit Codes

```shell
0   - Success, all files processed
1   - Error during processing
2   - Invalid arguments
3   - File not found
4   - Permission denied
```

## Automation Scenarios

### Scenario 1: CI/CD Pipeline Enforcement

**Goal**: Ensure all committed code is properly formatted.

**Implementation**:

```yaml
# GitHub Actions Example
name: Code Quality
on: [pull_request, push]

jobs:
  trim:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Setup Java
        uses: actions/setup-java@v3
        with:
          java-version: "21"

      - name: Build Code Trimmer
        run: mvn clean package

      - name: Check Formatting
        run: |
          java -jar target/code-trimmer-1.0.0.jar trim . --quiet
          if git diff --exit-code; then
            echo "✓ Code formatting OK"
          else
            echo "✗ Code needs formatting"
            exit 1
          fi

      - name: Fix and Commit (if allowed)
        if: failure()
        run: |
          java -jar target/code-trimmer-1.0.0.jar trim . --quiet
          git config user.email "bot@example.com"
          git config user.name "Format Bot"
          git add -A
          git commit -m "chore: auto-format code"
          git push
```

### Scenario 2: Pre-Commit Hook

**Goal**: Automatically format code before commit.

**Implementation** (`.git/hooks/pre-commit`):

```bash
#!/bin/bash
# Auto-format code before commit

# Get path to JAR (adjust as needed)
TRIMMER="$(git rev-parse --show-toplevel)/code-trimmer-1.0.0.jar"

# Skip if JAR doesn't exist
if [ ! -f "$TRIMMER" ]; then
    exit 0
fi

# Run trimmer on staged files
java -jar "$TRIMMER" trim . --quiet

# Stage any changes made by trimmer
git add -A

exit 0
```

**Setup**:

```bash
chmod +x .git/hooks/pre-commit
```

### Scenario 3: Scheduled Maintenance

**Goal**: Periodically clean up entire repository.

**Implementation** (Cron job):

```bash
#!/bin/bash
# /opt/code-trimmer/cleanup.sh

LOG_DIR="/var/log/code-trimmer"
PROJECT_DIR="/var/projects/my-project"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

# Create log directory
mkdir -p "$LOG_DIR"

# Run trimmer
cd "$PROJECT_DIR" || exit 1

java -jar /opt/code-trimmer/code-trimmer-1.0.0.jar \
  trim . \
  --include "java,py,js,md" \
  --exclude "vendor,node_modules,dist" \
  --verbose \
  > "$LOG_DIR/cleanup_$TIMESTAMP.log" 2>&1

# Email report (if changes made)
if [ -s "$LOG_DIR/cleanup_$TIMESTAMP.log" ]; then
    mail -s "Code Trimmer Report - $TIMESTAMP" \
         admin@example.com < "$LOG_DIR/cleanup_$TIMESTAMP.log"
fi
```

**Cron schedule** (`crontab -e`):

```cron
# Run daily at 2 AM
0 2 * * * /opt/code-trimmer/cleanup.sh
```

### Scenario 4: Docker Integration

**Goal**: Run Code Trimmer in containerized environments.

**Dockerfile**:

```dockerfile
FROM eclipse-temurin:21-jre

# Install Code Trimmer
COPY code-trimmer-1.0.0.jar /opt/code-trimmer.jar

# Mount code volume
VOLUME ["/code"]

WORKDIR /code

# Default command
ENTRYPOINT ["java", "-jar", "/opt/code-trimmer.jar"]
CMD ["trim", ".", "--verbose"]
```

**Usage**:

```bash
# Build image
docker build -t code-trimmer:latest .

# Run container
docker run --rm \
  -v /path/to/project:/code \
  code-trimmer:latest \
  trim . --verbose
```

### Scenario 5: IDE Integration

**Goal**: Format files within integrated development environment.

**VS Code Extension** (simplified):

```typescript
import { execSync } from "child_process";

export function formatDocument() {
  const workspaceFolder = vscode.workspace.workspaceFolders?.[0].uri.fsPath;

  if (!workspaceFolder) return;

  try {
    execSync(
      `java -jar code-trimmer-1.0.0.jar trim "${workspaceFolder}" --quiet`
    );
    vscode.window.showInformationMessage("Code formatted successfully");
  } catch (error) {
    vscode.window.showErrorMessage("Formatting failed: " + error.message);
  }
}
```

## Agent Configuration

### Environment Variables for Agents

```bash
# Logging configuration
export LOG_LEVEL=INFO
export LOG_FILE=/var/log/trimmer.log

# Processing defaults
export TRIMMER_MAX_FILES=1000
export TRIMMER_MAX_SIZE=10485760

# Color and output
export NO_COLOR=true
export QUIET_MODE=true

# Custom configuration
export SPRING_CONFIG_LOCATION=/etc/code-trimmer/application.yml
```

### Configuration File for Agents

Create `/etc/code-trimmer/application.yml`:

```yaml
spring:
  application:
    name: code-trimmer

logging:
  level:
    root: INFO
    com.codetrimmer: INFO
  file:
    name: /var/log/trimmer.log
    max-size: 10MB

trimmer:
  defaults:
    maxFileSize: 10485760
    maxFiles: 1000
    includeHidden: false
    createBackups: true
    colorOutput: false
    verbose: false
```

## API for Agent Development

### Java API

Agents written in Java can use Code Trimmer programmatically:

```java
import com.codetrimmer.service.FileProcessingService;
import com.codetrimmer.model.TrimOptions;
import com.codetrimmer.model.ProcessingStatistics;

public class CodeFormattingAgent {

    public static void main(String[] args) {
        // Configure options
        TrimOptions options = TrimOptions.builder()
            .directory(args[0])
            .includeExtensions("java,py")
            .excludePatterns("*.min.js,vendor")
            .maxFileSize(5242880)
            .maxFiles(500)
            .dryRun(args.length > 1 && args[1].equals("--dry-run"))
            .verbose(true)
            .build();

        // Process
        FileProcessingService service = new FileProcessingService(...);
        ProcessingStatistics stats = service.processDirectory(options.getDirectory());

        // Report
        System.out.println("Processed: " + stats.getTotalFiles());
        System.out.println("Modified: " + stats.getTotalModified());

        System.exit(stats.getTotalErrors() > 0 ? 1 : 0);
    }
}
```

### Command-Line API

All agent integrations should follow this pattern:

```bash
java -jar code-trimmer-1.0.0.jar \
  trim <directory> \
  [--include <extensions>] \
  [--exclude <patterns>] \
  [--max-files <number>] \
  [--max-size <bytes>] \
  [--dry-run] \
  [--verbose] \
  [--quiet] \
  [--no-color] \
  [--include-hidden] \
  [--backup] \
  [--no-limits]
```

## Error Handling for Agents

### Expected Exit Codes

```bash
# Success
exit 0

# Generic error
exit 1

# Invalid arguments
exit 2

# Directory not found
exit 3

# Permission denied
exit 4
```

### Error Detection Script

````bash
#!/bin/bash

java -jar code-trimmer-1.0.0.jar trim /project --quiet
EXIT_CODE=$?

case $EXIT_CODE in
    0)
        echo "✓ Formatting successful"
        ;;
    1)
        echo "✗ Formatting error"
        exit 1
        ;;
    2)
        echo "✗ Invalid arguments"
        exit 2
        ;;
    3)
        echo "✗ Directory not found"
        exit 3
        ;;
    4)
        echo "✗ Permission denied"
        exit 4
        ;;
    *)
        echo "✗ Unknown error: $EXIT_CODE"
        exit $EXIT_CODE
        ;;
esac
```shell

## Monitoring and Logging

### Log Aggregation

Code Trimmer produces structured logs suitable for aggregation:

```shell
2025-01-15T10:30:45.123Z [main] INFO com.codetrimmer.service.FileProcessingService - Processing directory: /project
2025-01-15T10:30:45.234Z [main] INFO com.codetrimmer.service.FileProcessingService - Found 1234 files
2025-01-15T10:30:47.456Z [main] INFO com.codetrimmer.service.FileProcessingService - Processing complete
````

### Metrics Collection

```bash
#!/bin/bash
# Extract metrics from trimmer output

OUTPUT=$(java -jar code-trimmer-1.0.0.jar trim /project --verbose 2>&1)

TOTAL_FILES=$(echo "$OUTPUT" | grep -oP 'Files found: \K\d+')
MODIFIED_FILES=$(echo "$OUTPUT" | grep -oP 'Files modified: \K\d+')
EXECUTION_TIME=$(echo "$OUTPUT" | grep -oP 'Execution time: \K[0-9.]+')

# Send to monitoring system
curl -X POST "http://metrics.example.com/api/metrics" \
  -H "Content-Type: application/json" \
  -d '{
    "service": "code-trimmer",
    "total_files": '$TOTAL_FILES',
    "modified_files": '$MODIFIED_FILES',
    "execution_time_seconds": '$EXECUTION_TIME',
    "timestamp": "'$(date -u +%Y-%m-%dT%H:%M:%SZ)'"
  }'
```

## Best Practices for Agents

1. **Always use `--dry-run` first**: Verify changes before applying
2. **Implement retry logic**: Handle transient errors gracefully
3. **Log all operations**: For audit and debugging
4. **Limit resource usage**: Use `--max-files` and `--max-size`
5. **Handle errors appropriately**: Check exit codes
6. **Monitor performance**: Track execution time and resource usage
7. **Backup before processing**: Especially for critical systems
8. **Use `--quiet` mode**: For CI/CD and automated systems
9. **Add timeouts**: Prevent hanging processes
10. **Respect version control**: Check git status before/after

## Troubleshooting Agent Integration

### Issue: Agent hangs

**Solution**:

- Add timeout: `timeout 60 java -jar code-trimmer-1.0.0.jar trim . --quiet`
- Reduce `--max-files` limit
- Use `--dry-run` mode first

### Issue: Permission denied

**Solution**:

- Ensure agent runs with appropriate permissions
- Check file/directory ownership
- Verify read/write access

### Issue: Memory errors

**Solution**:

- Increase heap: `java -Xmx1g -jar code-trimmer-1.0.0.jar ...`
- Reduce scope with `--max-files` and `--max-size`
- Process in batches

## Support

For agent integration issues:

1. Check exit codes and error messages
2. Run with `--verbose` flag for debugging
3. Check application logs
4. Consult [README.md](./README.md) for detailed information

---

## Python Agent Integration

The Python 3.12+ implementation provides the same functionality as the Java version but uses idiomatic Python patterns and the Click CLI framework.

**Location:** `/code-trimmer-python/` directory (separate from Java implementation)

### Project Structure

```
code-trimmer-python/
├── codetrimmer/
│   ├── cli/commands.py           # Click CLI commands
│   ├── service/file_processor.py # Core processing service
│   ├── config/loader.py          # Configuration loading
│   └── ...
├── tests/                        # Comprehensive test suite (>85% coverage)
├── pyproject.toml               # Python project config
├── requirements.txt             # Dependencies
└── python-docs/                 # Python-specific documentation
```

### Command-Line Integration

Agents can invoke Code Trimmer as a subprocess:

```bash
#!/bin/bash
# Python agent invocation

python -m codetrimmer trim /project \
  --include "*.py,*.md" \
  --exclude "venv,dist" \
  --no-color \
  --quiet

exit $?  # Return exit code
```

### Python API for Agent Development

Agents written in Python can use Code Trimmer programmatically:

```python
from codetrimmer.service.file_processor import FileProcessingService
from codetrimmer.cli.options import TrimOptions
from codetrimmer.config.models import TrimmerConfig

class CodeFormattingAgent:
    def __init__(self):
        self.service = FileProcessingService()

    def format_project(self, directory, extensions):
        config = TrimmerConfig(
            directory=directory,
            include=extensions,
            exclude="venv,dist,.git",
            max_file_size=5242880,
            max_files=500,
            create_backups=True,
            verbose=True
        )

        stats = self.service.process_directory(config)

        print(f"Processed: {stats.files_processed}")
        print(f"Modified: {stats.files_modified}")
        print(f"Errors: {stats.files_with_errors}")

        return 0 if stats.files_with_errors == 0 else 1

# Usage
agent = CodeFormattingAgent()
exit_code = agent.format_project("/project", "*.py,*.md")
exit(exit_code)
```

### Exit Codes (Python Version)

Same as Java version for compatibility:

```shell
0   - Success, all files processed
1   - Error during processing
2   - Invalid arguments
3   - File not found
4   - Permission denied
```

### Environment Variables (Python)

```bash
# Configuration
export CODETRIMMER_CONFIG=/etc/codetrimmer/.codetrimmer.yaml
export CODETRIMMER_INCLUDE="*.py,*.md"
export CODETRIMMER_EXCLUDE="venv,dist"

# Output control
export CODETRIMMER_NO_COLOR=1
export CODETRIMMER_QUIET=1
export CODETRIMMER_VERBOSE=1

# Processing limits
export CODETRIMMER_MAX_FILES=1000
export CODETRIMMER_MAX_FILE_SIZE=5242880
```

### Docker Integration (Python)

**Dockerfile**:

```dockerfile
FROM python:3.12-slim

WORKDIR /app

# Copy application
COPY code-trimmer-python/ /app/

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Mount code volume
VOLUME ["/code"]

WORKDIR /code

# Default command
ENTRYPOINT ["python", "-m", "codetrimmer"]
CMD ["trim", ".", "--verbose"]
```

**Usage**:

```bash
# Build image
docker build -f Dockerfile.python -t code-trimmer-python:latest .

# Run container
docker run --rm \
  -v /path/to/project:/code \
  code-trimmer-python:latest \
  trim . --verbose
```

### CI/CD Pipeline (Python)

**GitHub Actions Example**:

```yaml
name: Python Code Quality
on: [pull_request, push]

jobs:
  trim:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Setup Python 3.12
        uses: actions/setup-python@v4
        with:
          python-version: "3.12"

      - name: Install dependencies
        run: |
          cd code-trimmer-python
          pip install -r requirements.txt

      - name: Check Formatting
        run: |
          cd code-trimmer-python
          python -m codetrimmer trim . --dry-run --quiet
          if git diff --exit-code; then
            echo "✓ Code formatting OK"
          else
            echo "✗ Code needs formatting"
            exit 1
          fi

      - name: Fix and Commit (if allowed)
        if: failure()
        run: |
          cd code-trimmer-python
          python -m codetrimmer trim .
          git config user.email "bot@example.com"
          git config user.name "Format Bot"
          git add -A
          git commit -m "[PYTHON] chore: auto-format code"
          git push
```

### Testing Integration (Python)

```bash
#!/bin/bash
# Run Python tests with coverage

cd code-trimmer-python

# Install test dependencies
pip install pytest pytest-cov flake8 black isort

# Run tests with coverage
pytest tests/ --cov=codetrimmer --cov-report=term-missing

# Code quality checks
flake8 codetrimmer tests/
black --check codetrimmer tests/
isort --check codetrimmer tests/
```

### Important: Agent Boundaries

**CRITICAL:** Python agent must respect strict project boundaries:

✅ **DO:**

- Modify files only in `/code-trimmer-python/` directory
- Use Python 3.12+ idioms and best practices
- Test in isolated Python environment (`venv` or Docker)
- Commit with prefix `[PYTHON]` to distinguish from Java changes

❌ **DON'T:**

- Touch Java files in `/src/main/java/` or `/src/test/java/`
- Modify `pom.xml` or Java configuration files
- Import or reference Java code
- Delete or rename the Java version directory

### Best Practices for Python Agents

1. **Always use `--dry-run` first**: Verify changes before applying
2. **Test in `venv`**: Use isolated Python environment for testing
3. **Run linting**: Execute flake8, black, isort before committing
4. **Check coverage**: Maintain >85% test coverage requirement
5. **Validate git status**: Ensure only Python files are modified
6. **Use `--quiet` mode**: For CI/CD and automated systems
7. **Add timeouts**: Prevent hanging processes
8. **Handle errors appropriately**: Check exit codes
9. **Monitor performance**: Track execution time and resource usage
10. **Respect boundaries**: Never modify Java code or shared configs

### Troubleshooting Python Agent Integration

**Issue: Module not found**

**Solution**:

- Ensure Python 3.12+ is installed
- Install dependencies: `pip install -r requirements.txt`
- Set PYTHONPATH: `export PYTHONPATH=/code-trimmer-python:$PYTHONPATH`

**Issue: Permission denied**

**Solution**:

- Run with appropriate user permissions
- Check file/directory ownership
- Verify read/write access in `/code-trimmer-python/`

**Issue: Memory errors**

**Solution**:

- Reduce scope with `--max-files` and `--max-file-size`
- Process in batches
- Use `--quiet` mode to reduce memory overhead

**Issue: Test failures**

**Solution**:

- Run tests with verbose output: `pytest -v tests/`
- Check coverage: `pytest --cov=codetrimmer --cov-report=term-missing`
- Verify dependencies: `pip install -r requirements.txt`

### Reference

For comprehensive Python implementation details, see:

- `code-trimmer-python/python-docs/ARCHITECTURE.md` - Design and structure
- `code-trimmer-python/python-docs/CLI_REFERENCE.md` - All CLI options
- `code-trimmer-python/python-docs/ERROR_CODES.md` - Error handling
- `/docs/PRD_CODE_TRIMMER_PYTHON.md` - Complete specification

---

**Version**: 2.0 | **Last Updated**: December 2025 | **Java & Python Implementations**
