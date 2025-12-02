# Contributing to Code Trimmer

First off, thank you for considering contributing to Code Trimmer! It's people like you that make Code Trimmer such a great tool.

## Code of Conduct

This project and everyone participating in it is governed by a Code of Conduct based on the Contributor Covenant. By participating, you are expected to uphold this code.

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check the issue list as you might find out that you don't need to create one. When you are creating a bug report, please include as many details as possible:

- **Use a clear and descriptive title**
- **Describe the exact steps which reproduce the problem**
- **Provide specific examples to demonstrate the steps**
- **Describe the behavior you observed after following the steps**
- **Explain which behavior you expected to see instead and why**
- **Include screenshots and animated GIFs if possible**
- **Include your environment details** (OS, Java version, Maven version)

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, please include:

- **Use a clear and descriptive title**
- **Provide a step-by-step description of the suggested enhancement**
- **Provide specific examples to demonstrate the steps**
- **Describe the current behavior and expected behavior**
- **Explain why this enhancement would be useful**

### Pull Requests

- Fill in the required template
- Follow the Java/Maven styleguides
- Include appropriate test cases
- End all files with a newline
- Document new code with JavaDoc comments

## Development Setup

### Prerequisites

- Java 21 or higher
- Maven 3.6.0 or higher
- Git

### Building from Source

```bash
# Clone the repository
git clone https://github.com/yourusername/code-trimmer.git
cd code-trimmer

# Build the project
mvn clean package

# Run tests
mvn test

# Build with full verification
mvn clean verify
```

### Running the Application

```bash
# From the command line
java -jar target/code-trimmer-1.0.0.jar

# Or directly with Maven
mvn spring-boot:run
```

## Styleguides

### Java Code Style

- Use 4 spaces for indentation (not tabs)
- Maximum line length: 120 characters
- Use meaningful variable and method names
- Place opening braces on the same line as the declaration
- Use annotations like `@Override`, `@Deprecated`, etc. appropriately
- Write JavaDoc comments for public classes and methods

**Example:**

```java
/**
 * Processes a single file to trim whitespace and normalize formatting.
 *
 * @param filePath the path to the file to process
 * @return a FileProcessingResult containing the processing details
 * @throws IOException if file reading/writing fails
 */
public FileProcessingResult processFile(Path filePath) throws IOException {
    // Implementation here
}
```

### Commit Messages

- Use the present tense ("Add feature" not "Added feature")
- Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
- Limit the first line to 72 characters or less
- Reference issues and pull requests liberally after the first line

**Example:**

```
Add dry-run mode to preview changes before processing

- Implement --dry-run flag in TrimOptions
- Update FileProcessingService to support preview mode
- Add tests for dry-run functionality

Fixes #123
```

### Documentation

- Use Markdown for all documentation
- Keep lines under 100 characters for readability
- Include code examples where appropriate
- Link to related sections and external references
- Update README.md if you add/modify features

## Testing

### Writing Tests

- Write unit tests for new features
- Use JUnit 5 and Mockito for testing
- Aim for high code coverage (>80% for new code)
- Test both success and failure cases
- Use descriptive test method names

**Example Test:**

```java
@Test
@DisplayName("Should trim trailing whitespace from lines")
void testTrimTrailingWhitespace() {
    // Arrange
    String input = "line with trailing spaces   \n";

    // Act
    String result = FileTrimmer.trim(input);

    // Assert
    assertThat(result).isEqualTo("line with trailing spaces\n");
}
```

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=FileProcessingServiceTest

# Run specific test method
mvn test -Dtest=FileProcessingServiceTest#testProcessFile

# Run with coverage reporting
mvn test jacoco:report
```

## Project Structure

```
code-trimmer/
├── src/
│   ├── main/java/com/codetrimmer/
│   │   ├── shell/           # Spring Shell commands
│   │   ├── service/         # Business logic
│   │   ├── model/           # Data models
│   │   ├── util/            # Utility classes
│   │   └── Application.java # Entry point
│   ├── test/java/com/codetrimmer/
│   │   ├── shell/           # Shell command tests
│   │   ├── service/         # Service tests
│   │   └── model/           # Model tests
│   └── resources/
│       ├── application.yml  # Application configuration
│       └── logback.xml      # Logging configuration
├── docs/                    # Documentation files
├── pom.xml                  # Maven configuration
├── README.md                # Project README
└── LICENSE                  # MIT License
```

## Code Review Process

1. **Automated Checks**: Pull requests must pass all automated tests
2. **Code Review**: At least one approval from maintainers required
3. **Documentation**: Ensure documentation is updated
4. **Testing**: New features must have corresponding tests
5. **Performance**: Consider performance impact of changes

## Merge Criteria

Pull requests can be merged when:

- ✅ All CI/CD checks pass
- ✅ Code review approved by at least one maintainer
- ✅ All conversations are resolved
- ✅ Branch is up to date with main
- ✅ Tests have been added/updated for new features

## Additional Notes

### Issue and Pull Request Labels

- `bug` - Something isn't working
- `enhancement` - New feature or request
- `documentation` - Improvements or additions to documentation
- `good first issue` - Good for newcomers
- `help wanted` - Extra attention is needed
- `question` - Further information is requested
- `wontfix` - This will not be worked on

### Recognition

Contributors will be recognized in:

- GitHub contributors list
- Project CHANGELOG
- Release notes for their contributions

## Questions?

Feel free to create an issue with the `question` label or reach out to the maintainers.

## License

By contributing to Code Trimmer, you agree that your contributions will be licensed under its MIT License.

---

Thank you for contributing to Code Trimmer! 🎉
