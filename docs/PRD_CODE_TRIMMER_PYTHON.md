# Product Requirements Document (PRD): Code Trimmer Python 3.14+ Reproduction

This PRD defines comprehensive requirements to reproduce the Code Trimmer application in Python 3.14+ based on the Java 17 reference implementation. The Python version must implement all features, configuration options, error handling, documentation standards, and achieve >85% unit test coverage matching the original.

---

## PROJECT INDEPENDENCE ⚠️

**CRITICAL:** This is a **NEW, INDEPENDENT PROJECT**. NOT a fork of the Java version.

- **Separate Repository:** `github.com/pbaletkeman/code-trimmer-python`
- **No Java Code:** Zero Java dependencies or imports
- **Independent Releases:** Separate versioning (Python 1.0.0 ≠ Java 1.0.0)
- **No Overwrites:** Java codebase (`/src/main/java/`, `/src/test/java/`, `pom.xml`) remains completely untouched
- **Documentation:** Separate `/python-docs/` directory (NOT `/docs/`)
- **Reference Only:** Java implementation is used as a feature/behavior reference only
- **Language-Idiomatic:** Python implementation follows Python 3.14+ best practices, NOT direct Java translation
- **Parallel Development:** Both versions maintained independently

---

## FOR COPILOT/AGENTS: STRICT BOUNDARIES 🚨

### MANDATORY INSTRUCTIONS FOR ALL AGENTS

**CRITICAL CONSTRAINTS - MUST BE FOLLOWED:**

1. ✅ **ONLY modify** files in `/code-trimmer-python/` directory
2. ❌ **NEVER touch** Java files in `/src/main/java/` or `/src/test/java/`
3. ❌ **NEVER modify** `pom.xml`, `pom.xml.bak`, or Java configuration files
4. ❌ **NEVER delete or rename** the Java version directory
5. ✅ **ALWAYS use Python 3.14+ idioms** - do NOT translate Java code directly
6. ✅ **ALWAYS test** in isolated Python environment (`venv` or Docker)
7. ✅ **ALWAYS commit** with message prefix: `[PYTHON]` to distinguish from Java changes
8. ✅ **ALWAYS validate** all changes stay in `/code-trimmer-python/` before committing
9. ❌ **NEVER import** from Java code or reference Java source files
10. ✅ **ALWAYS update** ONLY Python dependencies in `code-trimmer-python/requirements.txt`

### Pre-Commit Verification Checklist

**Before EVERY commit, verify ALL of these:**

- [ ] Changed files are ONLY in `/code-trimmer-python/` directory
- [ ] NO Java files modified (check git diff)
- [ ] Python tests pass (>85% coverage required)
- [ ] NO imports from Java code
- [ ] `requirements.txt` updated (Python deps only)
- [ ] Commit message starts with `[PYTHON]`
- [ ] NO files deleted from `/src/` directory
- [ ] NO modifications to `pom.xml` or Java configs

---

## Table of Contents

1. [Overview](#overview)
2. [Application Structure & Core Components](#application-structure--core-components)
3. [CLI Commands & Options](#cli-commands--options)
4. [Configuration System](#configuration-system)
5. [Core Features Implementation](#core-features-implementation)
6. [Error Handling & Codes](#error-handling--codes)
7. [File Processing Pipeline](#file-processing-pipeline)
8. [Services & Utilities](#services--utilities)
9. [Testing Strategy](#testing-strategy)
10. [Documentation Requirements](#documentation-requirements)
11. [Security & Privacy](#security--privacy)
12. [Release & Deployment](#release--deployment)
13. [Performance Requirements](#performance-requirements)
14. [Project Dependencies](#project-dependencies)
15. [Implementation Timeline](#implementation-timeline)
16. [Repository Structure Diagram](#repository-structure-diagram)

---

## Overview

Code Trimmer is a file formatting and whitespace normalization utility that processes source code to remove unnecessary whitespace, enforce consistent formatting rules, and optionally create backups. The Python implementation must provide:

- **Multi-format support** for YAML/JSON configuration
- **Regex-based custom rules** for extensible formatting
- **Binary file detection** and intelligent skipping
- **Comprehensive error codes** (45+ error types) with actionable messages
- **Advanced reporting** (JSON/CSV/SQLite/HTTP endpoint support)
- **Git integration** via pre-commit hooks
- **Undo/restore capability** with backup management
- **Rich CLI experience** with color, progress, and dry-run modes
- **Production-grade quality** (>85% test coverage, flake8 compliance)

**Reference:** Java implementation: 21 source classes, 22 test classes, 712 passing unit tests, >85% coverage, 4,529 lines of documentation.

---

## Application Structure & Core Components

### Directory Layout

**PYTHON PROJECT LOCATION:** All files in `/code-trimmer-python/` directory

```shell
code-trimmer-python/
codetrimmer/
├── __init__.py
├── __main__.py                 # CLI entry point
├── app.py                      # Application bootstrap
├── config/
│   ├── __init__.py
│   ├── loader.py              # ConfigurationLoader
│   ├── models.py              # Config dataclasses
│   └── defaults.py            # Default values
├── cli/
│   ├── __init__.py
│   ├── commands.py            # CLI commands
│   ├── options.py             # TrimOptions
│   └── output.py              # ColorOutput
├── error/
│   ├── __init__.py
│   ├── codes.py               # ErrorCode enum
│   └── exceptions.py          # CodeTrimmerException
├── model/
│   ├── __init__.py
│   ├── binary_detector.py     # BinaryFileDetector
│   ├── file_result.py         # FileProcessingResult
│   └── statistics.py          # ProcessingStatistics
├── service/
│   ├── __init__.py
│   ├── file_processor.py      # FileProcessingService
│   ├── trimmer.py             # FileTrimmer
│   ├── diff_generator.py      # DiffGenerator
│   ├── hook_generator.py      # HookGenerator
│   ├── undo_service.py        # UndoService
│   └── report_generator.py    # ReportGenerator
├── util/
│   ├── __init__.py
│   ├── constants.py           # AppConstants
│   └── color.py               # ANSI colors
├── tests/
│   ├── conftest.py
│   ├── unit/
│   │   ├── test_config_loader.py
│   │   ├── test_file_trimmer.py
│   │   └── [20 more test files]
│   └── integration/
│       └── test_cli_commands.py
├── pyproject.toml
├── requirements.txt
├── Dockerfile
├── README.md                   # Python-specific README
└── python-docs/
    ├── QUICKSTART.md
    ├── ARCHITECTURE.md
    ├── CONFIGURATION.md
    ├── FEATURES.md
    ├── ERROR_CODES.md
    ├── CLI_REFERENCE.md
    ├── TROUBLESHOOTING.md
    └── samples/
        ├── .codetrimmer.yaml
        ├── .codetrimmer.json
        └── sample-rules.yaml
```

**IMPORTANT:** All Python documentation goes in `code-trimmer-python/python-docs/`, NOT in the shared `/docs/` directory.

---

## CLI Commands & Options

### Main Command: `codetrimmer trim`

**All 24 options from Java version required:**

```shell
codetrimmer trim [OPTIONS] [DIRECTORY]
```

| Option                          | Type   | Default                   | Description                       |
| ------------------------------- | ------ | ------------------------- | --------------------------------- |
| `--include`                     | str    | `*`                       | File glob pattern to include      |
| `--exclude`                     | str    | ``                        | File glob pattern to exclude      |
| `--include-hidden`              | bool   | False                     | Include hidden files              |
| `--follow-symlinks`             | bool   | False                     | Follow symbolic links             |
| `--max-consecutive-blank-lines` | int    | 2                         | Maximum consecutive blank lines   |
| `--ensure-final-newline`        | bool   | True                      | Ensure file ends with newline     |
| `--trim-trailing-whitespace`    | bool   | True                      | Trim trailing whitespace          |
| `--max-file-size`               | int    | 5242880                   | Max file size in bytes            |
| `--max-files`                   | int    | 50                        | Max files to process              |
| `--no-limits`                   | bool   | False                     | Disable all limits                |
| `--dry-run`                     | bool   | False                     | Preview changes without modifying |
| `--create-backups`              | bool   | True                      | Create `.bak` backup files        |
| `--fail-fast`                   | bool   | False                     | Stop on first error               |
| `--verbose`                     | bool   | False                     | Detailed output                   |
| `--quiet`                       | bool   | False                     | Minimal output                    |
| `--no-color`                    | bool   | False                     | Disable colored output            |
| `--disable-color-for-pipe`      | bool   | True                      | Disable colors when piped         |
| `--config`                      | str    | `.codetrimmer.yaml`       | Config file path                  |
| `--report`                      | choice | None                      | Report format: json/csv/sqlite    |
| `--report-output`               | str    | `codetrimmer-report.json` | Report file path                  |
| `--report-endpoint`             | str    | None                      | HTTP endpoint for reports         |
| `--diff`                        | bool   | False                     | Show diff in dry-run              |

### Auxiliary Commands

- `codetrimmer generate-hook` - Create pre-commit hook
- `codetrimmer undo [--undo-dir PATH]` - Restore from backups

---

## Configuration System

### Config File Formats

**Supported:** `.codetrimmer.yaml`, `.codetrimmer.json`, environment variables

**Priority:** CLI > Environment > Config file > Defaults

**Example `.codetrimmer.yaml`:**

```yaml
codetrimmer:
  include: "*.{java,py,js,md}"
  exclude: "*.min.{js,css},node_modules/**"
  max-consecutive-blank-lines: 2
  ensure-final-newline: true
  trim-trailing-whitespace: true
  create-backups: true
  rules:
    - name: "remove-trailing-spaces"
      pattern: '\s+$'
      replacement: ""
      description: "Remove trailing whitespace"
```

### ConfigurationLoader Class

**Responsibilities:**

- Load YAML/JSON from file
- Parse environment variables (CODETRIMMER\_\*)
- Validate config values
- Merge configs with CLI options
- Provide defaults

**Methods:**

- `load_config_file(path: str) -> dict`
- `load_env_vars() -> dict`
- `merge_configs(...) -> TrimmerConfig`
- `validate_config(config: dict) -> list[str]`

---

## Core Features Implementation

### Feature Requirements

**1. Configuration File Support** (Phase 1)

- Auto-load `.codetrimmer.yaml` from project root
- CLI `--config` flag overrides location
- Invalid config → error code CT-0001 with suggestions
- All options documented

**2. Custom Trimming Rules** (Phase 1)

- Define rules in config via `rules` section
- Fields: `name`, `pattern` (regex), `replacement`, `description`
- Invalid rules → error code CT-0004 or CT-0005

**3. Pre-commit Hook Generator** (Phase 2)

- Command `codetrimmer generate-hook`
- Creates `.git/hooks/pre-commit` (bash/batch)
- Works on Windows and Unix

**4. Pipeline/CI/CD Support** (Phase 2)

- Dockerfile provided
- Works with GitHub Actions, GitLab CI, Jenkins
- Sample `docker-compose.yml`

**5. Detailed Error Reporting** (Phase 3)

- 45+ error codes (CT-0001 to CT-0092)
- Messages include: code, title, description, cause, suggestion
- Logged to stderr

**6. Advanced Statistics/Reporting** (Phase 3)

- CLI: `--report json|csv|sqlite`
- Export: files processed, modified, skipped, errors, bytes trimmed
- HTTP endpoint support

**7. Undo/Restore Functionality** (Phase 4)

- Command `codetrimmer undo`
- Restores `.bak` files
- Logs all operations

**8. Dry-run Diff Output** (Phase 4)

- Option: `--dry-run --diff`
- Unified diff with line numbers
- Colorized, 3-line context

---

## Error Handling & Codes

### ErrorCode Enum (45 codes)

**Configuration (CT-0001 to CT-0005):**

- CT-0001: Invalid configuration file
- CT-0002: Configuration file not found
- CT-0003: Invalid configuration value
- CT-0004: Invalid rule definition
- CT-0005: Invalid regex pattern

**File Operations (CT-0010 to CT-0016):**

- CT-0010: File not found
- CT-0011: File read error
- CT-0012: File write error
- CT-0013: Permission denied
- CT-0014: Directory not accessible
- CT-0015: File too large
- CT-0016: Binary file skipped

**Backup/Restore (CT-0040 to CT-0043):**

- CT-0040: Backup creation failed
- CT-0041: Restore failed
- CT-0042: Backup file missing
- CT-0043: Backup file corrupted

**Hook Generation (CT-0050 to CT-0052):**

- CT-0050: Hook generation failed
- CT-0051: Git directory not found
- CT-0052: Hook already exists

**Reporting (CT-0060 to CT-0063):**

- CT-0060: Report generation failed
- CT-0061: Invalid report format
- CT-0062: Report endpoint failed
- CT-0063: SQLite database error

**General (CT-0090 to CT-0092):**

- CT-0090: Unknown error
- CT-0091: Operation cancelled
- CT-0092: Disk full

### CodeTrimmerException Class

```python
class CodeTrimmerException(Exception):
    def __init__(self, error_code: ErrorCode, cause: str = None, suggestion: str = None):
        self.error_code = error_code
        self.cause = cause
        self.suggestion = suggestion

    def __str__(self) -> str:
        # Format error message with code, title, cause, suggestion
```

---

## File Processing Pipeline

### FileProcessingService (Main Orchestrator)

**Flow:**

1. Load config → ConfigurationLoader
2. Parse CLI options → TrimOptions
3. Discover files → directory walk with include/exclude
4. Detect binary files → BinaryFileDetector
5. Process each file → FileTrimmer.trim_file()
6. Create backups → if create-backups=true
7. Generate diffs → if --diff flag
8. Collect stats → ProcessingStatistics
9. Export report → ReportGenerator
10. Display output → ColorOutput

### FileTrimmer (Core Logic)

**Operations:**

- Remove trailing whitespace
- Collapse excessive blank lines
- Ensure final newline
- Apply custom rules (regex)
- Detect binary files
- Preserve encoding

**Methods:**

- `trim_file(path: str, options: TrimOptions) -> FileProcessingResult`
- `trim_line(line: str) -> str`
- `apply_custom_rules(content: str, rules: list) -> str`

### BinaryFileDetector

**Detection:**

- File extension check
- Null byte detection
- Magic number checking

### FileProcessingResult (Dataclass)

```python
@dataclass
class FileProcessingResult:
    file_path: str
    was_modified: bool
    bytes_modified: int
    error: Optional[ErrorCode] = None
    error_message: str = ""
    backup_path: Optional[str] = None
    processing_time_ms: int = 0
```

### ProcessingStatistics (Dataclass)

```python
@dataclass
class ProcessingStatistics:
    files_processed: int = 0
    files_modified: int = 0
    files_skipped: int = 0
    files_with_errors: int = 0
    total_bytes_trimmed: int = 0
    total_execution_time_ms: int = 0
    errors: dict[ErrorCode, int] = field(default_factory=dict)
```

---

## Services & Utilities

### DiffGenerator Service

```python
def generate_diff(original: str, modified: str, context_lines: int = 3) -> str:
    """Generate unified diff with line numbers and context."""
```

### HookGenerator Service

```python
def generate_hook(target_dir: str, hook_type: str = "bash") -> str:
    """Create pre-commit hook (bash or batch)."""

def create_hook_file(path: str, content: str, executable: bool = True) -> None:
    """Write hook to file system."""
```

### UndoService

```python
def restore_directory(dir_path: str) -> dict[str, bool]:
    """Restore all .bak files in directory."""

def restore_file(file_path: str) -> bool:
    """Restore single file from backup."""
```

### ReportGenerator Service

```python
def export_json(stats: ProcessingStatistics) -> str:
def export_csv(stats: ProcessingStatistics) -> str:
def export_sqlite(stats: ProcessingStatistics, db_path: str) -> None:
def send_http_endpoint(stats: ProcessingStatistics, url: str) -> None:
```

### ColorOutput Utility

```python
def red(text: str) -> str:
def green(text: str) -> str:
def yellow(text: str) -> str:
def cyan(text: str) -> str:
def bold(text: str) -> str:
def strip_colors(text: str) -> str:
```

---

## Testing Strategy

### Unit Test Coverage: >85%

**22+ test modules required:**

1. `test_config_loader.py` - YAML/JSON loading, validation
2. `test_trim_options.py` - Builder pattern, CLI parsing
3. `test_binary_detector.py` - Binary detection, edge cases
4. `test_file_trimmer.py` - Trimming logic, rules
5. `test_file_processor.py` - Orchestration, error handling
6. `test_diff_generator.py` - Diff generation
7. `test_hook_generator.py` - Hook creation (Windows/Unix)
8. `test_undo_service.py` - Backup restore
9. `test_report_generator.py` - JSON/CSV/SQLite export
10. `test_error_codes.py` - Error enum and handling
11. `test_color_output.py` - ANSI colors
12. `test_cli_commands.py` - CLI integration
13. `test_full_pipeline.py` - End-to-end scenarios

**Test Types:**

- Happy path: Valid inputs, standard operations
- Edge cases: Empty files, large files, special chars, encodings
- Errors: Missing files, permissions, invalid config
- Cross-platform: Windows/macOS/Linux
- Performance: Large file sets, memory usage

### Code Quality Gates

- **Linting:** flake8 (no violations in main/tests)
- **Formatting:** isort consistency
- **Coverage:** >85% via coverage.py
- **Type hints:** mypy compliance (optional)

### CI/CD Pipeline

```yaml
steps:
  - Set up Python 3.12
  - Install dependencies
  - Run linting (flake8, isort)
  - Run tests (pytest)
  - Generate coverage report
  - Check coverage gate (>85%)
  - Build documentation
  - Create artifacts
```

---

## Documentation Requirements

### Root `code-trimmer-python/README.md`

**Location:** `code-trimmer-python/README.md` (NOT shared `/docs/README.md`)

- Length: <300 lines
- Links to `./python-docs/` for details
- Quick overview and key features
- Clear statement: "This is the Python 3.14+ implementation. See `./python-docs/` for details."

### `code-trimmer-python/python-docs/` Directory Files

- `QUICKSTART.md` - Build (5 steps), Run (5 steps)
- `TROUBLESHOOTING.md` - 15+ FAQs
- `ARCHITECTURE.md` - Design, components, data flow
- `CONFIGURATION.md` - Config options, examples
- `FEATURES.md` - Each of 8 features with usage
- `ERROR_CODES.md` - All error codes with solutions
- `CLI_REFERENCE.md` - Command options, examples

### Sample Files in `code-trimmer-python/python-docs/samples/`

- `.codetrimmer.yaml` - YAML config with all options
- `.codetrimmer.json` - JSON config equivalent
- `sample-rules.yaml` - Custom rule examples

### Diagrams (Mermaid)

- Architecture diagram
- File processing flow
- Error handling state machine
- Configuration loading flow

### Standards

- Markdown linting (markdownlint)
- TOC for files >100 lines
- All links validated
- Code blocks with language tags
- Plain language, clear examples
- Unicode verification (Windows/macOS/Linux)

---

## Security & Privacy

**Backup Files:**

- Created with `.bak` extension
- Same directory as original
- Same permissions
- No encryption (local file system only)

**Configuration:**

- No sensitive data in `.codetrimmer.yaml`
- Document best practices
- Never store API keys/passwords

**Error Logs:**

- No file content in error messages
- Exclude system paths
- Verbose logging requires opt-in
- Local logging only

**Temporary Files:**

- All temp files cleaned up
- Standard system temp directory
- Cleanup on normal and error exit

**Dependencies:**

- Scan for vulnerabilities (pip-audit)
- Block high/critical in CI
- Monitor advisories

---

## Release & Deployment

**Versioning:** Semantic versioning (MAJOR.MINOR.PATCH)

**Process:**

1. Create release branch
2. Update version in setup.py/pyproject.toml
3. Run tests, linting, coverage gates
4. Create git tag
5. GitHub Actions builds and publishes

**Artifacts:**

- PyPI package (pip install codetrimmer)
- Docker Hub image
- GitHub Releases (wheel, sdist)
- SHA-256 checksums

**Documentation:**

- Release notes
- Migration guide (if breaking changes)
- Upgrade instructions
- Known issues

---

## Performance Requirements

- **Speed:** 1,000 files in <10 seconds
- **Memory:** <512MB for 10,000 files
- **Scalability:** Support >100,000 files
- **Startup:** <2 seconds

---

## Project Dependencies

### Core

```shell
click>=8.0.0              # CLI framework
pyyaml>=6.0              # YAML config
pydantic>=2.0.0          # Data validation
colorama>=0.4.6          # Cross-platform colors
```

### Optional

```shell
aiohttp>=3.8.0           # HTTP endpoint support
```

### Dev

```shell
pytest>=7.0.0
pytest-cov>=4.0.0
coverage>=7.0.0
flake8>=6.0.0
black>=23.0.0
isort>=5.12.0
pip-audit>=2.0.0
mypy>=1.0.0
```

### Requirements

- **Python:** 3.14+
- **OS:** Windows 10+, macOS 10.14+, Linux
- **File systems:** NTFS, APFS, ext4

---

## Implementation Timeline

- **Phase 1**: Infrastructure, config, rules
- **Phase 2**: File processing, backups
- **Phase 3**: Error reporting, statistics
- **Phase 4**: Hooks, undo, CLI
- **Phase 5**: Quality, docs, Docker
- **Phase 6**: Release, PyPI, Docker Hub

---

## REPOSITORY STRUCTURE DIAGRAM

```
github.com/pbaletkeman/codeTrimmer (main repository)
│
├── /src/main/java/              ← JAVA CODE (PROTECTED - DO NOT TOUCH)
├── /src/test/java/              ← JAVA TESTS (PROTECTED - DO NOT TOUCH)
├── pom.xml                       ← JAVA BUILD (PROTECTED - DO NOT TOUCH)
├── /docs/                        ← JAVA DOCS (PROTECTED - DO NOT TOUCH)
│
└── /code-trimmer-python/         ← PYTHON CODE (THIS PROJECT)
    ├── codetrimmer/              ← Source code
    ├── tests/                    ← Test modules
    ├── python-docs/              ← Python-specific documentation
    ├── requirements.txt
    ├── pyproject.toml
    ├── README.md                 ← Python-specific README
    └── Dockerfile
```

**This strict separation ensures ZERO risk of Java code being overwritten.**

---

**Document Version:** 2.0
**Last Updated:** December 3, 2025
**Status:** Ready for Python 3.14+ Implementation
**Reference:** Code Trimmer Java (712 unit tests, >85% coverage)
**Project Type:** INDEPENDENT - Separate from Java version
**Repository:** github.com/pbaletkeman/code-trimmer-python
