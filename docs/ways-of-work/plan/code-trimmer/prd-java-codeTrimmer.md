# Product Requirements Document: Code Trimmer

## 1. Feature Name

**Code Trimmer** - A Java Spring Shell file cleanup utility for maintaining consistent whitespace and formatting standards across codebases make it compatible with Java 17

## 2. Epic

Standalone feature - no parent Epic.

## 3. Goal

### Problem

Development teams frequently encounter inconsistent whitespace formatting across their codebases: trailing whitespace on lines, missing final newlines, excessive blank lines between code blocks, and inconsistent line endings. These formatting inconsistencies lead to:

- Noisy git diffs that obscure actual code changes
- Failed linting checks in CI/CD pipelines
- Reduced code readability and maintainability
- Wasted developer time manually fixing formatting issues

### Solution

Code Trimmer is a configurable bash script that automatically cleans up whitespace issues in text files across a directory tree. It provides:

- Automated trimming of trailing whitespace from all lines
- Enforcement of exactly one empty line at the end of each file
- Reduction of excessive blank lines (maximum 2 consecutive blank lines)
- Flexible file type filtering via inclusion/exclusion lists
- Safe operation with backup creation and dry-run mode
- Performance controls to prevent resource exhaustion

### Impact

- **Reduced Code Review Time:** Cleaner diffs mean faster code reviews (target: 20% reduction in review time)
- **Improved CI/CD Success Rate:** Fewer formatting-related pipeline failures (target: 30% reduction in formatting failures)
- **Enhanced Code Quality:** Consistent formatting standards across the codebase
- **Developer Productivity:** Automated cleanup eliminates manual formatting work (target: save 2-3 hours/developer/month)

## 4. User Personas

### Primary Personas

**1. Local Developer (Dev)**

- A software engineer working on feature development
- Wants to clean up code before committing
- Values speed and safety (doesn't want to lose work)
- May work with multiple programming languages

**2. DevOps Engineer (Ops)**

- Maintains CI/CD pipelines and automation
- Needs reliable, scriptable tools for automated workflows
- Requires detailed logging and error handling
- Values predictable behavior and exit codes

**3. Code Reviewer (Reviewer)**

- Reviews pull requests and enforces code standards
- Frustrated by whitespace-only changes cluttering diffs
- Wants team-wide consistency in formatting
- May run cleanup scripts on behalf of contributors

## 5. User Stories

### Core Functionality

**US-1: Directory Processing**

- **As a Dev**, I want to run Code Trimmer on a directory so that all text files are cleaned up in one operation.

**US-2: File Type Filtering (Inclusion)**

- **As a Dev**, I want to specify which file extensions to process (e.g., `.js`, `.py`, `.md`) so that I only clean up relevant files.

**US-3: File Type Filtering (Exclusion)**

- **As a Dev**, I want to exclude specific file extensions (e.g., `.min.js`, `.lock`) so that generated or minified files are not modified.

**US-4: Recursive Processing**

- **As a Dev**, I want the app to recursively process subdirectories so that I can clean an entire project tree at once.

### Whitespace Rules

**US-5: Trim Line Whitespace**

- **As a Reviewer**, I want all lines to have trailing whitespace removed so that diffs are clean and standards are enforced.

**US-6: Ensure Final Newline**

- **As a Ops**, I want every file to end with exactly one empty line so that POSIX standards are met and tools work correctly.

**US-7: Limit Consecutive Blank Lines**

- **As a Reviewer**, I want no more than 2 consecutive blank lines anywhere in a file so that code is compact and readable.

### Safety Features

**US-8: Backup Creation**

- **As a Dev**, I want automatic backups created before modification so that I can recover if something goes wrong.

**US-9: Dry-Run Mode**

- **As a Dev**, I want to preview what changes would be made without modifying files so that I can verify the app will do what I expect.

**US-10: Binary File Skipping**

- **As a Dev**, I want the app to automatically skip binary files so that I don't corrupt non-text files.

### Performance & Safety Limits

**US-11: File Size Limit**

- **As a Ops**, I want to skip files larger than a configurable size (default 5MB) so that the app doesn't hang on huge files.

**US-12: File Count Limit**

- **As a Dev**, I want to process at most a configurable number of files (default 50) so that the app completes in a reasonable time.

### Reporting

**US-13: Modified Files Report**

- **As a Dev**, I want to see which files were modified so that I know what changed.

**US-14: Statistics Report**

- **As a Reviewer**, I want to see statistics (lines trimmed, blank lines removed, files processed) so that I can quantify the cleanup impact.

**US-15: Verbose Mode**

- **As a Ops**, I want a verbose mode that shows detailed processing information so that I can debug issues.

**US-16: Quiet Mode**

- **As a Ops**, I want a quiet mode for CI/CD pipelines that only shows errors so that logs are not cluttered.

## 6. Requirements

### Functional Requirements

#### Core Processing

- **FR-1:** the app MUST process all files in the specified directory matching the filter criteria.
- **FR-2:** the app MUST support recursive directory traversal into all subdirectories.
- **FR-3:** the app MUST trim all trailing whitespace from every line in processed files.
- **FR-4:** the app MUST ensure every file ends with exactly one newline character (empty line).
- **FR-5:** the app MUST reduce any sequence of more than 2 consecutive blank lines to exactly 2 blank lines.
- **FR-6:** the app MUST preserve the original file permissions after modification.
- **FR-7:** the app MUST preserve the original file timestamps where possible.

#### File Filtering

- **FR-8:** the app MUST support an inclusion list of file extensions (e.g., `--include "js,py,md"`).
- **FR-9:** the app MUST support an exclusion list of file extensions (e.g., `--exclude "min.js,lock"`).
- **FR-10:** the app MUST process files with no extension when `*` or empty string is in the inclusion list.
- **FR-11:** When both inclusion and exclusion lists are provided, exclusion takes precedence.
- **FR-12:** the app MUST automatically skip binary files (detected by file content analysis).
- **FR-13:** the app MUST skip hidden files and directories (starting with `.`) by default.
- **FR-14:** the app MUST provide an option to include hidden files (`--include-hidden`).

#### Safety Features

- **FR-15:** the app MUST create a backup of each file before modification with a `.bak` extension.
- **FR-16:** the app MUST support a dry-run mode (`--dry-run`) that reports changes without modifying files.
- **FR-17:** the app MUST restore from backup if an error occurs during file processing.
- **FR-18:** the app MUST skip files without read permissions and log a warning.
- **FR-19:** the app MUST skip files without write permissions and log a warning.
- **FR-20:** the app MUST skip symbolic links by default.
- **FR-21:** the app MUST provide an option to follow symbolic links (`--follow-symlinks`).

#### Performance Limits

- **FR-22:** the app MUST skip files larger than a configurable size limit (default: 5MB).
- **FR-23:** the app MUST stop processing after a configurable file count limit (default: 50 files).
- **FR-24:** the app MUST provide options to configure both limits: `--max-size <bytes>` and `--max-files <count>`.
- **FR-25:** the app MUST provide an option to disable limits: `--no-limits`.

#### Reporting & Output

- **FR-26:** the app MUST display a summary report showing:
  - Total files scanned
  - Total files modified
  - Total files skipped (with reasons: binary, size, permissions, etc.)
  - Total lines trimmed
  - Total blank lines removed
  - Execution time
- **FR-27:** the app MUST list all modified files by default.
- **FR-28:** the app MUST support a verbose mode (`--verbose` or `-v`) showing detailed processing for each file.
- **FR-29:** the app MUST support a quiet mode (`--quiet` or `-q`) showing only errors and final summary.
- **FR-30:** the app MUST use color-coded output (green for success, yellow for warnings, red for errors) when outputting to a terminal.
- **FR-31:** the app MUST disable color output when piped or redirected, or when `--no-color` is specified.

#### Error Handling

- **FR-32:** the app MUST return exit code 0 on successful completion.
- **FR-33:** the app MUST return exit code 1 for invalid arguments or configuration errors.
- **FR-34:** the app MUST return exit code 2 for file processing errors.
- **FR-35:** the app MUST log all errors to stderr.
- **FR-36:** the app MUST continue processing remaining files after a single file error.
- **FR-37:** the app MUST provide an option to stop on first error (`--fail-fast`).

#### Configuration

- **FR-38:** the app MUST accept all configuration via command-line arguments.
- **FR-39:** the app MUST support a configuration file (`--config <file>`) for persistent settings.
- **FR-40:** the app MUST support environment variables for common settings (e.g., `CODE_TRIMMER_MAX_SIZE`).
- **FR-41:** Command-line arguments MUST override configuration file settings.
- **FR-42:** Configuration file settings MUST override environment variables.

#### Help & Documentation

- **FR-43:** the app MUST provide a help message (`--help` or `-h`) documenting all options.
- **FR-44:** the app MUST provide a version flag (`--version`) showing the app version.
- **FR-45:** the app MUST provide usage examples in the help output.

### Non-Functional Requirements

#### Performance

- **NFR-1:** the app MUST process at least 100 small files (<10KB) per second on modern hardware.
- **NFR-2:** the app MUST not consume more than 100MB of memory during normal operation.
- **NFR-3:** the app MUST be interruptible (Ctrl+C) and clean up partial backups.

#### Compatibility

- **NFR-4:** the app MUST be compatible with Bash 4.0 or later.
- **NFR-5:** the app MUST run on Linux, macOS, and Windows (via Git Bash, WSL, or Cygwin).
- **NFR-6:** the app MUST use only POSIX-compliant utilities where possible.
- **NFR-7:** the app MUST document any non-POSIX dependencies (e.g., GNU-specific flags).

#### Security

- **NFR-8:** the app MUST NOT follow symbolic links outside the target directory tree by default.
- **NFR-9:** the app MUST validate all user inputs to prevent command injection.
- **NFR-10:** the app MUST handle filenames with special characters (spaces, quotes, newlines) safely.

#### Maintainability

- **NFR-11:** the app MUST include inline comments explaining complex logic.
- **NFR-12:** the app MUST follow consistent coding style (e.g., Google Shell Style Guide).
- **NFR-13:** the app MUST be modular with clearly defined functions.

#### Usability

- **NFR-14:** the app MUST provide clear, actionable error messages.
- **NFR-15:** the app MUST validate required arguments and show usage on error.
- **NFR-16:** the app MUST provide progress indication for long-running operations.

#### Reliability

- **NFR-17:** the app MUST be idempotent (running multiple times produces the same result).
- **NFR-18:** the app MUST handle unexpected termination gracefully (no corrupted files).
- **NFR-19:** the app MUST restore original files from backup on any processing error.

## 7. Acceptance Criteria

### AC-1: Basic Directory Processing

**Given** a directory with 5 text files containing trailing whitespace
**When** I run `code-trimmer.jar /path/to/dir`
**Then** all 5 files should have trailing whitespace removed
**And** each file should end with exactly one blank line
**And** the summary should report 5 files modified

### AC-2: File Extension Inclusion

**Given** a directory with .js, .py, and .md files
**When** I run `code-trimmer.jar --include "js,py" /path/to/dir`
**Then** only .js and .py files should be processed
**And** .md files should be skipped
**And** the summary should show correct counts

### AC-3: File Extension Exclusion

**Given** a directory with .js and .min.js files
**When** I run `code-trimmer.jar --include "js" --exclude "min.js" /path/to/dir`
**Then** only .js files (not .min.js) should be processed
**And** the summary should show .min.js files as skipped

### AC-4: Recursive Processing

**Given** a directory tree with files in subdirectories
**When** I run `code-trimmer.jar /path/to/dir`
**Then** all files in all subdirectories should be processed
**And** the directory structure should remain unchanged

### AC-5: Trailing Whitespace Removal

**Given** a file with lines ending in spaces and tabs
**When** the file is processed
**Then** all trailing spaces and tabs should be removed
**And** line content should be preserved
**And** the summary should report lines trimmed

### AC-6: Final Newline Enforcement

**Given** a file with no trailing newline
**When** the file is processed
**Then** exactly one newline should be added at the end
**Given** a file with multiple trailing newlines
**When** the file is processed
**Then** all but one trailing newline should be removed

### AC-7: Blank Line Reduction

**Given** a file with 5 consecutive blank lines
**When** the file is processed
**Then** only 2 consecutive blank lines should remain
**And** the summary should report blank lines removed
**Given** a file with 2 or fewer consecutive blank lines
**When** the file is processed
**Then** blank lines should remain unchanged

### AC-8: Backup Creation

**Given** any file being processed
**When** the app runs in normal mode
**Then** a backup file with `.bak` extension should be created
**And** the backup should contain the original content
**And** the backup should be created before modification

### AC-9: Dry-Run Mode

**Given** files that need cleaning
**When** I run `code-trimmer.jar --dry-run /path/to/dir`
**Then** no files should be modified
**And** the report should show what would be changed
**And** no backup files should be created

### AC-10: Binary File Skipping

**Given** a directory with text and binary files (.jpg, .exe, .pdf)
**When** the app runs
**Then** binary files should be automatically skipped
**And** the summary should report them as skipped

### AC-11: File Size Limit

**Given** files of various sizes including one 10MB file
**When** I run with default settings (5MB limit)
**Then** the 10MB file should be skipped
**And** the summary should report it as "skipped (size)"
**When** I run `code-trimmer.jar --max-size 20000000 /path/to/dir`
**Then** the 10MB file should be processed

### AC-12: File Count Limit

**Given** a directory with 100 files
**When** I run with default settings (50 file limit)
**Then** only 50 files should be processed
**And** the summary should indicate the limit was reached
**When** I run `code-trimmer.jar --max-files 100 /path/to/dir`
**Then** all 100 files should be processed

### AC-13: Modified Files Report

**Given** 3 files were modified and 2 were skipped
**When** the app completes
**Then** the output should list the 3 modified file paths
**And** should not list skipped files (unless verbose mode)

### AC-14: Statistics Report

**Given** a completed run
**When** the app finishes
**Then** the summary should display:

- Total files scanned
- Files modified count
- Files skipped count with breakdown by reason
- Lines trimmed count
- Blank lines removed count
- Execution time in seconds

### AC-15: Verbose Mode

**Given** verbose mode is enabled
**When** the app runs
**Then** each file should be logged as it's processed
**And** detailed statistics should be shown per file
**And** skipped files should be listed with reasons

### AC-16: Quiet Mode

**Given** quiet mode is enabled
**When** the app runs successfully
**Then** only the final summary should be displayed
**And** individual file changes should not be shown
**When** an error occurs
**Then** the error should be displayed even in quiet mode

### AC-17: Permission Handling

**Given** a file without read permission
**When** the app encounters it
**Then** it should log a warning and skip the file
**And** should continue processing other files
**Given** a file without write permission
**When** the app encounters it
**Then** it should log a warning and skip the file

### AC-18: Error Recovery

**Given** a processing error occurs on one file
**When** the app continues
**Then** the original file should be restored from backup
**And** other files should continue to be processed
**And** the exit code should indicate an error occurred

### AC-19: Hidden Files Handling

**Given** a directory with hidden files (.gitignore, .env)
**When** I run with default settings
**Then** hidden files should be skipped
**When** I run `code-trimmer.jar --include-hidden /path/to/dir`
**Then** hidden files matching the include filter should be processed

### AC-20: Idempotency

**Given** files that have already been processed once
**When** I run the app again on the same files
**Then** no modifications should be made
**And** the summary should show 0 files modified
**And** the app should return exit code 0

### AC-21: Help and Version

**When** I run `code-trimmer.jar --help`
**Then** usage information and all options should be displayed
**And** examples should be shown
**When** I run `code-trimmer.jar --version`
**Then** the app version should be displayed

### AC-22: Configuration File

**Given** a config file with settings
**When** I run `code-trimmer.jar --config myconfig.conf /path/to/dir`
**Then** settings from the config file should be applied
**When** I also provide command-line arguments
**Then** command-line arguments should override config file settings

### AC-23: Color Output

**Given** the app runs in a terminal
**When** outputting results
**Then** success messages should be green
**And** warnings should be yellow
**And** errors should be red
**When** output is piped or `--no-color` is used
**Then** no color codes should be in the output

### AC-24: Interrupt Handling

**Given** the app is processing files
**When** I press Ctrl+C
**Then** the app should stop gracefully
**And** should clean up any partial backups
**And** should not leave corrupted files

## 8. Out of Scope

The following are explicitly **not** included in this feature to maintain focus and limit scope:

- **Content Transformation:** No code reformatting, linting, or style enforcement beyond whitespace
- **Language-Specific Rules:** No language-aware processing (e.g., Python indentation fixes, JavaScript semicolon insertion)
- **Git Integration:** No automatic git staging, committing, or diff generation
- **Encoding Conversion:** No character encoding detection or conversion (assumes UTF-8)
- **Line Ending Normalization:** No CRLF/LF conversion (preserves existing line endings)
- **GUI or Web Interface:** Command-line only, no graphical interface
- **Undo Functionality:** No built-in undo beyond manual restoration from `.bak` files
- **Cloud Integration:** No integration with cloud storage or remote repositories
- **Real-time Watching:** No file watching or automatic processing on file changes
- **Parallel Processing:** Single-threaded processing only (no multi-core optimization)
- **Compression:** No automatic compression of backup files
- **Advanced Reporting:** No HTML, JSON, or XML report formats (plain text only)
- **Plugin System:** No extensibility or plugin architecture
- **Whitespace Standardization:** No tab-to-space or space-to-tab conversion
- **Comment Preservation:** No special handling of comments vs. code
- **Configuration Migration:** No automatic upgrade of config files between versions

---

**Document Version:** 1.0
**Last Updated:** November 30, 2025
**Status:** Draft - Pending Technical Specification
