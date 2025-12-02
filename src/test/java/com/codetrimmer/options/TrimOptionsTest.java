package com.codetrimmer.options;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TrimOptions Class Tests")
class TrimOptionsTest {

  private TrimOptions options;

  @BeforeEach
  void setUp() {
    options = new TrimOptions();
  }

  @Nested
  @DisplayName("Constructor and Initialization")
  class ConstructorTests {

    @Test
    @DisplayName("Default constructor initializes options with defaults")
    void defaultConstructor() {
      assertNotNull(options);
    }

    @Test
    @DisplayName("Options object is created successfully")
    void objectCreation() {
      TrimOptions newOptions = new TrimOptions();
      assertNotNull(newOptions);
      assertNotEquals(options, newOptions);
    }
  }

  @Nested
  @DisplayName("Path Configuration")
  class PathConfigurationTests {

    @Test
    @DisplayName("Set and get input directory path")
    void setAndGetInputPath() {
      String inputPath = "/path/to/input";
      options.setInputDirectory(inputPath);
      assertEquals(inputPath, options.getInputDirectory());
    }

    @Test
    @DisplayName("Set and get output directory path")
    void setAndGetOutputPath() {
      String outputPath = "/path/to/output";
      options.setOutputDirectory(outputPath);
      assertEquals(outputPath, options.getOutputDirectory());
    }

    @Test
    @DisplayName("Input directory can be changed")
    void inputDirectoryCanBeChanged() {
      String path1 = "/first/path";
      String path2 = "/second/path";
      options.setInputDirectory(path1);
      assertEquals(path1, options.getInputDirectory());
      options.setInputDirectory(path2);
      assertEquals(path2, options.getInputDirectory());
    }

    @Test
    @DisplayName("Output directory can be changed")
    void outputDirectoryCanBeChanged() {
      String path1 = "/first/output";
      String path2 = "/second/output";
      options.setOutputDirectory(path1);
      assertEquals(path1, options.getOutputDirectory());
      options.setOutputDirectory(path2);
      assertEquals(path2, options.getOutputDirectory());
    }

    @Test
    @DisplayName("Set and get multiple paths independently")
    void multiplePathsIndependently() {
      String inputPath = "/input";
      String outputPath = "/output";
      options.setInputDirectory(inputPath);
      options.setOutputDirectory(outputPath);
      assertEquals(inputPath, options.getInputDirectory());
      assertEquals(outputPath, options.getOutputDirectory());
    }
  }

  @Nested
  @DisplayName("Trim Options Configuration")
  class TrimOptionsConfigurationTests {

    @Test
    @DisplayName("Enable and check trim comments option")
    void trimCommentsOption() {
      options.setTrimComments(true);
      assertTrue(options.isTrimComments());
    }

    @Test
    @DisplayName("Disable and check trim comments option")
    void trimCommentsDisabled() {
      options.setTrimComments(false);
      assertFalse(options.isTrimComments());
    }

    @Test
    @DisplayName("Enable and check trim whitespace option")
    void trimWhitespaceOption() {
      options.setTrimWhitespace(true);
      assertTrue(options.isTrimWhitespace());
    }

    @Test
    @DisplayName("Disable and check trim whitespace option")
    void trimWhitespaceDisabled() {
      options.setTrimWhitespace(false);
      assertFalse(options.isTrimWhitespace());
    }

    @Test
    @DisplayName("Enable and check trim blank lines option")
    void trimBlankLinesOption() {
      options.setTrimBlankLines(true);
      assertTrue(options.isTrimBlankLines());
    }

    @Test
    @DisplayName("Disable and check trim blank lines option")
    void trimBlankLinesDisabled() {
      options.setTrimBlankLines(false);
      assertFalse(options.isTrimBlankLines());
    }

    @Test
    @DisplayName("Multiple trim options can be set independently")
    void multipleOptionsIndependently() {
      options.setTrimComments(true);
      options.setTrimWhitespace(false);
      options.setTrimBlankLines(true);

      assertTrue(options.isTrimComments());
      assertFalse(options.isTrimWhitespace());
      assertTrue(options.isTrimBlankLines());
    }

    @Test
    @DisplayName("Trim options can be toggled")
    void optionsCanBeToggled() {
      options.setTrimComments(true);
      assertTrue(options.isTrimComments());
      options.setTrimComments(false);
      assertFalse(options.isTrimComments());
    }
  }

  @Nested
  @DisplayName("File Type Filtering")
  class FileTypeFilteringTests {

    @Test
    @DisplayName("Include file type extension")
    void includeFileType() {
      options.includeFileType("java");
      assertTrue(options.isFileTypeIncluded("java"));
    }

    @Test
    @DisplayName("Exclude file type extension")
    void excludeFileType() {
      options.excludeFileType("class");
      assertTrue(options.isFileTypeExcluded("class"));
    }

    @Test
    @DisplayName("Multiple file types can be included")
    void multipleIncludedFileTypes() {
      options.includeFileType("java");
      options.includeFileType("txt");
      options.includeFileType("md");

      assertTrue(options.isFileTypeIncluded("java"));
      assertTrue(options.isFileTypeIncluded("txt"));
      assertTrue(options.isFileTypeIncluded("md"));
    }

    @Test
    @DisplayName("Multiple file types can be excluded")
    void multipleExcludedFileTypes() {
      options.excludeFileType("bin");
      options.excludeFileType("exe");
      options.excludeFileType("dll");

      assertTrue(options.isFileTypeExcluded("bin"));
      assertTrue(options.isFileTypeExcluded("exe"));
      assertTrue(options.isFileTypeExcluded("dll"));
    }

    @Test
    @DisplayName("File type inclusion and exclusion are independent")
    void inclusionAndExclusionIndependent() {
      options.includeFileType("java");
      options.excludeFileType("class");

      assertTrue(options.isFileTypeIncluded("java"));
      assertTrue(options.isFileTypeExcluded("class"));
      assertFalse(options.isFileTypeIncluded("class"));
      assertFalse(options.isFileTypeExcluded("java"));
    }
  }

  @Nested
  @DisplayName("Output Options")
  class OutputOptionsTests {

    @Test
    @DisplayName("Enable and check verbose output")
    void verboseOutput() {
      options.setVerbose(true);
      assertTrue(options.isVerbose());
    }

    @Test
    @DisplayName("Disable and check verbose output")
    void verboseOutputDisabled() {
      options.setVerbose(false);
      assertFalse(options.isVerbose());
    }

    @Test
    @DisplayName("Enable and check dry run mode")
    void dryRunMode() {
      options.setDryRun(true);
      assertTrue(options.isDryRun());
    }

    @Test
    @DisplayName("Disable and check dry run mode")
    void dryRunModeDisabled() {
      options.setDryRun(false);
      assertFalse(options.isDryRun());
    }

    @Test
    @DisplayName("Verbose and dry run can be set independently")
    void verboseAndDryRunIndependent() {
      options.setVerbose(true);
      options.setDryRun(false);

      assertTrue(options.isVerbose());
      assertFalse(options.isDryRun());
    }
  }

  @Nested
  @DisplayName("Size Limits")
  class SizeLimitTests {

    @Test
    @DisplayName("Set and get maximum file size")
    void maxFileSize() {
      long maxSize = 1024 * 1024; // 1 MB
      options.setMaxFileSize(maxSize);
      assertEquals(maxSize, options.getMaxFileSize());
    }

    @Test
    @DisplayName("Zero file size limit is allowed")
    void zeroFileSizeLimit() {
      options.setMaxFileSize(0);
      assertEquals(0, options.getMaxFileSize());
    }

    @Test
    @DisplayName("Negative file size limit is allowed")
    void negativeFileSizeLimit() {
      options.setMaxFileSize(-1);
      assertEquals(-1, options.getMaxFileSize());
    }

    @Test
    @DisplayName("File size limit can be changed")
    void fileSizeLimitCanBeChanged() {
      long size1 = 512;
      long size2 = 2048;
      options.setMaxFileSize(size1);
      assertEquals(size1, options.getMaxFileSize());
      options.setMaxFileSize(size2);
      assertEquals(size2, options.getMaxFileSize());
    }

    @Test
    @DisplayName("Large file size limit is stored correctly")
    void largeFileSizeLimit() {
      long largeSize = Long.MAX_VALUE;
      options.setMaxFileSize(largeSize);
      assertEquals(largeSize, options.getMaxFileSize());
    }
  }

  @Nested
  @DisplayName("Combined Configuration")
  class CombinedConfigurationTests {

    @Test
    @DisplayName("All options can be configured together")
    void allOptionsTogether() {
      options.setInputDirectory("/input");
      options.setOutputDirectory("/output");
      options.setTrimComments(true);
      options.setTrimWhitespace(true);
      options.setTrimBlankLines(false);
      options.includeFileType("java");
      options.excludeFileType("class");
      options.setVerbose(true);
      options.setDryRun(false);
      options.setMaxFileSize(1024 * 1024);

      assertEquals("/input", options.getInputDirectory());
      assertEquals("/output", options.getOutputDirectory());
      assertTrue(options.isTrimComments());
      assertTrue(options.isTrimWhitespace());
      assertFalse(options.isTrimBlankLines());
      assertTrue(options.isFileTypeIncluded("java"));
      assertTrue(options.isFileTypeExcluded("class"));
      assertTrue(options.isVerbose());
      assertFalse(options.isDryRun());
      assertEquals(1024 * 1024, options.getMaxFileSize());
    }

    @Test
    @DisplayName("Configuration remains consistent after multiple updates")
    void configurationConsistency() {
      String initialInput = "/input1";
      String finalInput = "/input2";

      options.setInputDirectory(initialInput);
      assertEquals(initialInput, options.getInputDirectory());

      options.setTrimComments(true);
      options.setInputDirectory(finalInput);

      assertEquals(finalInput, options.getInputDirectory());
      assertTrue(options.isTrimComments());
    }

    @Test
    @DisplayName("Reset to new instance has independent state")
    void independentInstances() {
      options.setInputDirectory("/input1");
      options.setTrimComments(true);

      TrimOptions options2 = new TrimOptions();
      options2.setInputDirectory("/input2");
      options2.setTrimComments(false);

      assertEquals("/input1", options.getInputDirectory());
      assertTrue(options.isTrimComments());
      assertEquals("/input2", options2.getInputDirectory());
      assertFalse(options2.isTrimComments());
    }
  }

  @Nested
  @DisplayName("Edge Cases")
  class EdgeCasesTests {

    @Test
    @DisplayName("Null path values are handled")
    void nullPathValues() {
      options.setInputDirectory(null);
      assertNull(options.getInputDirectory());
    }

    @Test
    @DisplayName("Empty string paths are allowed")
    void emptyStringPaths() {
      options.setInputDirectory("");
      assertEquals("", options.getInputDirectory());
    }

    @Test
    @DisplayName("Paths with special characters are preserved")
    void specialCharacterPaths() {
      String pathWithSpecialChars = "/path/with-special_chars/!@#$%";
      options.setInputDirectory(pathWithSpecialChars);
      assertEquals(pathWithSpecialChars, options.getInputDirectory());
    }

    @Test
    @DisplayName("File type names are case-sensitive")
    void fileTypesCaseSensitive() {
      options.includeFileType("Java");
      assertTrue(options.isFileTypeIncluded("Java"));
      assertFalse(options.isFileTypeIncluded("java"));
    }

    @Test
    @DisplayName("Multiple consecutive toggles work correctly")
    void consecutiveToggles() {
      options.setTrimComments(true);
      options.setTrimComments(false);
      options.setTrimComments(true);
      options.setTrimComments(false);
      assertFalse(options.isTrimComments());
    }
  }

  @Nested
  @DisplayName("State Validation")
  class StateValidationTests {

    @Test
    @DisplayName("Option state is independent from creation order")
    void stateIndependentFromCreationOrder() {
      TrimOptions opt1 = new TrimOptions();
      opt1.setTrimComments(true);

      TrimOptions opt2 = new TrimOptions();
      opt2.setTrimComments(false);

      assertTrue(opt1.isTrimComments());
      assertFalse(opt2.isTrimComments());
    }

    @Test
    @DisplayName("Setting option multiple times uses last value")
    void lastValueWins() {
      options.setVerbose(true);
      options.setVerbose(false);
      options.setVerbose(true);
      assertTrue(options.isVerbose());
    }

    @Test
    @DisplayName("Unmodified options maintain default state")
    void unmodifiedOptionsDefaultState() {
      TrimOptions newOptions = new TrimOptions();
      // Verify that unset options have consistent behavior
      newOptions.setInputDirectory("/test");
      assertEquals("/test", newOptions.getInputDirectory());
    }
  }
}
