# Code Quality Standards

This document outlines the code quality standards and tools used in Code Trimmer.

## Table of Contents

- [Checkstyle Configuration](#checkstyle-configuration)
- [Test Coverage Requirements](#test-coverage-requirements)
- [Build Commands](#build-commands)
- [Continuous Integration](#continuous-integration)

---

## Checkstyle Configuration

Code Trimmer uses Checkstyle for static code analysis. The configuration is in `checkstyle.xml`.

### Active Rules

| Category | Rule | Configuration |
|----------|------|---------------|
| File Length | FileLength | max 500 lines |
| Line Length | LineLength | max 120 characters |
| Tabs | FileTabCharacter | Disallowed |
| Naming | PackageName, TypeName, MethodName | Standard Java |
| Naming | ConstantName, LocalVariableName | Standard Java |
| Imports | AvoidStarImport | Required |
| Imports | UnusedImports | Remove unused |
| Complexity | CyclomaticComplexity | max 10 |
| Method Length | MethodLength | max 100 lines |
| Parameters | ParameterNumber | max 12 |

### Running Checkstyle

```bash
# Check all code
mvn clean checkstyle:check

# Check and generate report
mvn clean checkstyle:checkstyle
# Report: target/site/checkstyle.html
```

### Suppressing Violations

For exceptional cases, use comments:

```java
// CHECKSTYLE:OFF - [reason]
// code that needs exception
// CHECKSTYLE:ON
```

---

## Test Coverage Requirements

### Minimum Coverage

- **Overall Coverage:** 85%
- **New Code:** 90%

### Coverage Tool

JaCoCo is used for code coverage measurement.

### Running Coverage Reports

```bash
# Run tests with coverage
mvn clean test jacoco:report

# Report location
target/site/jacoco/index.html
```

### Coverage Exclusions

The following are excluded from coverage:

- Configuration classes with only getters/setters
- Exception classes
- Application entry points

---

## Build Commands

### Full Quality Check

```bash
# Run all quality checks
mvn clean checkstyle:check test jacoco:report
```

### Individual Commands

```bash
# Compile only
mvn clean compile

# Run tests only
mvn clean test

# Build JAR
mvn clean package

# Generate Javadoc
mvn clean javadoc:javadoc
```

### Verification Command

```bash
# Full verification (checkstyle + tests + coverage)
mvn clean verify
```

---

## Continuous Integration

### GitHub Actions Workflow

The build workflow (`.github/workflows/build.yml`) runs:

1. **Checkstyle Check** - Validates code style
2. **Unit Tests** - Runs all tests
3. **Coverage Report** - Generates JaCoCo report
4. **Build JAR** - Creates distributable artifact

### Quality Gates

A PR will fail if:

- Any checkstyle violation exists
- Any test fails
- Coverage drops below 85%

### Running Locally Before Push

```bash
# Simulate CI build
mvn clean checkstyle:check test jacoco:report package
```

---

## Best Practices

### Code Style

1. **Naming:** Use descriptive names
2. **Methods:** Keep methods under 100 lines
3. **Complexity:** Keep cyclomatic complexity under 10
4. **Comments:** Document public APIs with Javadoc

### Testing

1. **Unit Tests:** Test all public methods
2. **Edge Cases:** Test boundary conditions
3. **Error Cases:** Test error handling
4. **Mocking:** Use Mockito for dependencies

### Documentation

1. **Javadoc:** All public classes and methods
2. **README:** Keep under 300 lines
3. **Comments:** Explain "why", not "what"

---

**Last Updated:** December 2025
