# Product Requirements Document (PRD) for Code Trimmer: GitHub Enhancement Features

---

## Table of Contents

1. [Overview](#overview)
2. [Goals](#goals)
3. [User Stories](#user-stories)
4. [Features](#features)
5. [Acceptance Criteria](#acceptance-criteria-by-feature)
6. [Test Coverage & Code Quality](#test-coverage--code-quality)
7. [Documentation Requirements](#documentation-requirements)
8. [Security & Privacy](#security--privacy)
9. [Release & Deployment Plan](#release--deployment-plan)
10. [Performance Requirements](#performance-requirements)
11. [Accessibility](#accessibility)
12. [Rollback & Failure Handling](#rollback--failure-handling)
13. [Metrics Collection](#metrics-collection)
14. [Implementation Phases](#implementation-phases)
15. [Non-Functional Requirements](#non-functional-requirements)
16. [References](#references)

---

## Overview

This PRD defines new features to enhance Code Trimmer's usability, configurability, integration, and reporting for professional development workflows. These features address persistent configuration, rule customization, automation, containerization, error handling, reporting, undo/restore, and improved dry-run feedback.

---

## Goals

- Enable persistent and shareable configuration via `.trimmerrc` (YAML/JSON)
- Allow users to define custom trimming rules
- Automate code formatting in Git workflows via pre-commit hooks
- Provide support for CI/CD
- Improve error reporting and troubleshooting
- Offer advanced statistics and export capabilities
- Support undo/restore of changes
- Show line-by-line diffs in dry-run mode

---

## User Stories

**User Story 1:** As a team lead, I want to define and share Code Trimmer settings across my project so that all developers apply consistent formatting rules.

**User Story 2:** As a DevOps engineer, I want to run Code Trimmer in Docker containers so that I can integrate it into CI/CD pipelines without managing Java installations.

**User Story 3:** As a developer, I want to see line-by-line diffs of proposed changes before applying them so that I can review modifications in detail.

**User Story 4:** As a developer, I want to undo changes made by Code Trimmer so that I can recover if something goes wrong.

**User Story 5:** As a project maintainer, I want an option to export processing statistics to JSON/CSV so that I can track code quality metrics over time.

**User Story 6:** As a project maintainer, I want an option to export processing statistics to SQLite so that I can track code quality metrics over time.

---

## Features

### 1. Configuration File Support

- Support `.trimmerrc` (YAML/JSON) for persistent, project-level configuration.
- Auto-load config file from project root; allow override via CLI.
- Configurable options: include/exclude patterns, limits, backup, output mode, custom rules.
- Sample config files must be provided with all switch/option combinations.

### 2. Customizable Trimming Rules

- Allow users to define custom whitespace/formatting rules in config.
- Support regex-based rules, blank line policies, indentation, file ending, etc.

### 3. Pre-commit Hook Generator

- Provide a command/script to auto-generate a Git pre-commit hook.
- Hook runs Code Trimmer on staged files before commit.

### 4. Pipelines

- Support all CLI features in container.
- Document usage for CI/CD pipelines.

### 5. Detailed Error Reporting

- Implement granular error codes and messages.
- Provide troubleshooting output for common failures (permissions, file access, config errors).

### 6. Advanced Statistics/Reporting

- Export processing statistics to JSON/CSV.
- Optionally send stats to external systems (HTTP endpoint, file, etc.).

### 7. Undo/Restore Functionality

- Allow users to undo the last trim operation or restore from backup files in bulk.

### 8. Dry-run Diff Output

- In dry-run mode, show line-by-line diffs for proposed changes.

---

## Acceptance Criteria by Feature

### Configuration File Support

- Config file `.trimmerrc` (YAML or JSON format) loads automatically from project root.
- CLI `--config` flag allows override of default config file location.
- Invalid config raises error with clear message and suggests fixes.
- All config options are documented with examples.

### Customizable Trimming Rules

- Config file allows definition of custom rules via `rules` section.
- Each rule specifies name, description, regex pattern, and action.
- Invalid rules are caught during validation with actionable error messages.
- At least 3 sample rules provided (blank line handling, trailing space, file ending).

### Pre-commit Hook Generator

- Command `code-trimmer --generate-hook` creates `.git/hooks/pre-commit` script.
- Hook is executable and passes shellcheck validation (if available).
- Hook works on Windows (batch), Windows (PowerShell), macOS/Linux (bash).
- Documentation includes setup and troubleshooting steps.

### Docker Support

- Dockerfile provided and builds without errors.
- Container image works with all CLI options.
- Sample `docker-compose.yml` provided for CI/CD integration.

### Detailed Error Reporting

- All errors have unique error code (format: `CT-XXXX`).
- Error message includes: code, description, cause, and suggested fix.
- Errors logged to stderr with proper formatting.
- Documentation lists all error codes and their resolutions.

### Advanced Statistics/Reporting

- CLI option `--report json` exports stats to JSON format.
- CLI option `--report csv` exports stats to CSV format.
- Stats include: files processed, modified, skipped, errors, execution time, bytes trimmed.
- Stats can be sent to HTTP endpoint via `--report-endpoint <URL>`.

### Undo/Restore Functionality

- Command `code-trimmer --undo` restores all `.bak` files in a directory.
- Option `--undo-dir <path>` allows selective restore.
- Undo logs what was restored and confirms success.
- Documentation covers backup file location and retention.

### Dry-run Diff Output

- Option `--dry-run --diff` shows unified diff for each file.
- Diff output is colorized (can be disabled with `--no-color`).
- Diff shows line numbers and context (3 lines before/after change).
- Works with all file types.

---

## Test Coverage & Code Quality

- **Unit Test Coverage:** Minimum 85% overall coverage (verified via JaCoCo).
- **Integration Tests:** Cover all features end-to-end.
- **Checkstyle Compliance:** All code (main and tests) must pass checkstyle validation.
  - always do a `mvn clear` before running `mvn test` or `mvn test jacoco:report`
- **Test Execution:** `mvn test` runs all tests; `mvn test jacoco:report` generates coverage report.

---

## Documentation Requirements

### Structure

- **Root README.md:** < 300 lines, links to `/docs/` for detailed docs.
- **All Other Docs:** In `/docs/` directory.
- **Table of Contents:** Required for all files > 100 lines.
- **Link Validation:** All links must be verified; create files if referenced but missing.

### Content

- **Sample Config Files:** Provide `.trimmerrc.yaml` and `.trimmerrc.json` with all options and combinations.
- **Mermaid Diagrams:** Update architecture, workflow, and feature interaction diagrams.
- **Quick Start - Build:** 5 steps or less to build from source.
- **Quick Start - Use:** 5 steps or less to run first command.
- **Troubleshooting:** FAQs with 10-15 common issues and solutions.
- **Unicode Verification:** Test all icons/characters on Windows, macOS, and Linux terminals before release.

---

## Security & Privacy

- **Backup Files:** Stored locally with `.bak` extension; users control retention.
- **Config Files:** Can contain patterns or rules; no sensitive data should be stored; document best practices.
- **Error Logs:** Do not include file content or system paths in error logs by default; verbose logs require explicit opt-in.
- **Temporary Files:** Any temporary files are cleaned up after processing.

---

## Release & Deployment Plan

- **Versioning:** Follow semantic versioning (e.g., 2.0.0 for major feature release).
- **Release Notes:** Document new features, bug fixes, breaking changes.
- **Migration Guide:** If features change CLI behavior, provide migration guide for existing users.
- **Rollout:** Release to GitHub Releases, Docker Hub, Maven Central (if packaged).

---

## Performance Requirements

- **Speed:** Process 1,000 files in < 10 seconds on standard hardware (SSD, 8GB RAM).
- **Memory:** Consume < 512MB for projects with 10,000 files.
- **Scalability:** Support projects with > 100,000 files (with appropriate limits set).
- **Parallelization:** For future enhancement; current single-threaded performance acceptable.

---

## Accessibility

- **Error Messages:** Clear, concise language without jargon; provide context.
- **Documentation:** Use plain language and clear structure; provide examples.

---

## Rollback & Failure Handling

- **Backup Integrity:** If `.bak` file is missing or corrupted, undo operation fails with clear error message.
  - override existing `.bak` files
- **Partial Restore:** If restore fails for some files, operation continues; report which files failed.
- **Recovery Options:** Document manual restore steps if automated undo fails.
- **Logging:** All undo operations logged for audit trail.
  - each run needs a different log file

---

## Metrics Collection

- **Reporting:**
  - Monthly usage report (if opt-in telemetry implemented).
  - Quarterly feature adoption analysis.
  - Annual roadmap planning based on metrics.

---

## Implementation Phases

- **Phase 1:** Configuration file support + customizable rules
- **Phase 2:** Pre-commit hook generator + Docker support
- **Phase 3:** Error reporting + statistics/reporting
- **Phase 4:** Undo/restore + dry-run diff
- **Phase 5:** Testing, documentation, and quality assurance
- **Phase 6:** Release and deployment

---

## Non-Functional Requirements

- Must comply with checkstyle for both main and tests (validation via `mvn checkstyle:check`).
- Must end up with overall unit test coverage > 85% (validation via `mvn test jacoco:report`).
- Must update all documentation per documented standards (see Documentation Requirements section).

---

## Out of Scope

- GUI application
- IDE plugins (covered in separate roadmap)

---

## Success Metrics

- Increased adoption in CI/CD and team workflows
- Fewer support requests for configuration and error handling
- Positive feedback on reporting and undo features

---

## References

- [GitHub Pre-commit Hook Docs](https://git-scm.com/book/en/v2/Customizing-Git-Git-Hooks)
- [Unified Diff Format](https://en.wikipedia.org/wiki/Diff_utility)
- [Semantic Versioning](https://semver.org/)
- [Checkstyle Documentation](https://checkstyle.sourceforge.io/)
- [JaCoCo Coverage](https://www.jacoco.org/jacoco/)

---

**Document Version:** 1.1
**Last Updated:** December 2, 2025
**Status:** Ready for Implementation Review
