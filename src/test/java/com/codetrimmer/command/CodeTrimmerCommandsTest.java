package com.codetrimmer.command;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.codetrimmer.options.TrimOptions;

@DisplayName("CodeTrimmerCommands Class Tests")
class CodeTrimmerCommandsTest {

  private CodeTrimmerCommands commands;

  @BeforeEach
  void setUp() {
    commands = new CodeTrimmerCommands();
  }

  @Nested
  @DisplayName("Input/Output Paths Tests")
  class InputOutputPathsTests {
    @Test
    void inputPathCanBeSet() {
      commands.setInputPath("/input");
      assertEquals("/input", commands.getInputPath());
    }

    @Test
    void outputPathCanBeSet() {
      commands.setOutputPath("/output");
      assertEquals("/output", commands.getOutputPath());
    }

    @Test
    void pathsCanBeChanged() {
      commands.setInputPath("/input");
      commands.setOutputPath("/output");
      assertEquals("/input", commands.getInputPath());
      assertEquals("/output", commands.getOutputPath());
    }

    @Test
    void multiplePaths() {
      commands.setInputPath("/input1");
      commands.setOutputPath("/output1");
      commands.setInputPath("/input2");
      assertEquals("/input2", commands.getInputPath());
      assertEquals("/output1", commands.getOutputPath());
    }

    @Test
    void nullPathsHandled() {
      commands.setInputPath(null);
      assertNull(commands.getInputPath());
    }
  }

  @Nested
  @DisplayName("Command Line Options Tests")
  class CommandLineOptionsTests {
    @Test
    void verboseFlagCanBeSet() {
      commands.setVerbose(true);
      assertTrue(commands.isVerbose());
    }

    @Test
    void verboseFlagCanBeDisabled() {
      commands.setVerbose(false);
      assertFalse(commands.isVerbose());
    }

    @Test
    void dryRunFlagCanBeSet() {
      commands.setDryRun(true);
      assertTrue(commands.isDryRun());
    }

    @Test
    void dryRunFlagCanBeDisabled() {
      commands.setDryRun(false);
      assertFalse(commands.isDryRun());
    }

    @Test
    void flagsCanBeToggled() {
      commands.setVerbose(true);
      assertTrue(commands.isVerbose());
      commands.setVerbose(false);
      assertFalse(commands.isVerbose());
    }

    @Test
    void multipleFlags() {
      commands.setVerbose(true);
      commands.setDryRun(true);
      assertTrue(commands.isVerbose());
      assertTrue(commands.isDryRun());
    }
  }

  @Nested
  @DisplayName("Trim Options Configuration Tests")
  class TrimOptionsConfigurationTests {
    @Test
    void trimCommentsOption() {
      commands.setTrimComments(true);
      assertTrue(commands.isTrimComments());
    }

    @Test
    void trimWhitespaceOption() {
      commands.setTrimWhitespace(true);
      assertTrue(commands.isTrimWhitespace());
    }

    @Test
    void trimBlankLinesOption() {
      commands.setTrimBlankLines(true);
      assertTrue(commands.isTrimBlankLines());
    }

    @Test
    void multipleTrimOptions() {
      commands.setTrimComments(true);
      commands.setTrimWhitespace(true);
      commands.setTrimBlankLines(true);
      assertTrue(commands.isTrimComments());
      assertTrue(commands.isTrimWhitespace());
      assertTrue(commands.isTrimBlankLines());
    }

    @Test
    void trimOptionsCanBeToggled() {
      commands.setTrimComments(true);
      assertTrue(commands.isTrimComments());
      commands.setTrimComments(false);
      assertFalse(commands.isTrimComments());
    }
  }

  @Nested
  @DisplayName("File Type Configuration Tests")
  class FileTypeConfigurationTests {
    @Test
    void fileTypeCanBeIncluded() {
      commands.includeFileType("java");
      assertTrue(commands.isFileTypeIncluded("java"));
    }

    @Test
    void fileTypeCanBeExcluded() {
      commands.excludeFileType("txt");
      assertTrue(commands.isFileTypeExcluded("txt"));
    }

    @Test
    void multipleFileTypesIncluded() {
      commands.includeFileType("java");
      commands.includeFileType("cpp");
      assertTrue(commands.isFileTypeIncluded("java"));
      assertTrue(commands.isFileTypeIncluded("cpp"));
    }

    @Test
    void multipleFileTypesExcluded() {
      commands.excludeFileType("txt");
      commands.excludeFileType("bin");
      assertTrue(commands.isFileTypeExcluded("txt"));
      assertTrue(commands.isFileTypeExcluded("bin"));
    }

    @Test
    void includeAndExcludeIndependent() {
      commands.includeFileType("java");
      commands.excludeFileType("txt");
      assertTrue(commands.isFileTypeIncluded("java"));
      assertTrue(commands.isFileTypeExcluded("txt"));
      assertFalse(commands.isFileTypeIncluded("txt"));
      assertFalse(commands.isFileTypeExcluded("java"));
    }
  }

  @Nested
  @DisplayName("Size Limit Configuration Tests")
  class SizeLimitConfigurationTests {
    @Test
    void maxFileSizeCanBeSet() {
      commands.setMaxFileSize(1024);
      assertEquals(1024, commands.getMaxFileSize());
    }

    @Test
    void zeroFileSizeAllowed() {
      commands.setMaxFileSize(0);
      assertEquals(0, commands.getMaxFileSize());
    }

    @Test
    void largeFileSizeHandled() {
      commands.setMaxFileSize(1000000000L);
      assertEquals(1000000000L, commands.getMaxFileSize());
    }

    @Test
    void fileSizeCanBeChanged() {
      commands.setMaxFileSize(512);
      assertEquals(512, commands.getMaxFileSize());
      commands.setMaxFileSize(1024);
      assertEquals(1024, commands.getMaxFileSize());
    }
  }

  @Nested
  @DisplayName("Configuration State Tests")
  class ConfigurationStateTests {
    @Test
    void stateIndependentBetweenInstances() {
      CodeTrimmerCommands cmd1 = new CodeTrimmerCommands();
      CodeTrimmerCommands cmd2 = new CodeTrimmerCommands();
      cmd1.setVerbose(true);
      assertFalse(cmd2.isVerbose());
    }

    @Test
    void stateChangesImmediate() {
      commands.setVerbose(false);
      assertFalse(commands.isVerbose());
      commands.setVerbose(true);
      assertTrue(commands.isVerbose());
    }

    @Test
    void configurationPersists() {
      commands.setInputPath("/input");
      commands.setVerbose(true);
      assertEquals("/input", commands.getInputPath());
      assertTrue(commands.isVerbose());
    }

    @Test
    void allOptionsTogether() {
      commands.setInputPath("/input");
      commands.setOutputPath("/output");
      commands.setVerbose(true);
      commands.setDryRun(true);
      commands.setTrimComments(true);
      assertEquals("/input", commands.getInputPath());
      assertEquals("/output", commands.getOutputPath());
      assertTrue(commands.isVerbose());
      assertTrue(commands.isDryRun());
      assertTrue(commands.isTrimComments());
    }
  }

  @Nested
  @DisplayName("Edge Cases Tests")
  class EdgeCasesTests {
    @Test
    void emptyStringPathsAllowed() {
      commands.setInputPath("");
      assertEquals("", commands.getInputPath());
    }

    @Test
    void specialCharactersPreserved() {
      commands.setInputPath("/path/with/special@chars#");
      assertEquals("/path/with/special@chars#", commands.getInputPath());
    }

    @Test
    void fileTypesCaseSensitive() {
      commands.includeFileType("Java");
      assertFalse(commands.isFileTypeIncluded("java"));
    }

    @Test
    void lastValueWins() {
      commands.setInputPath("/first");
      commands.setInputPath("/second");
      assertEquals("/second", commands.getInputPath());
    }

    @Test
    void consecutiveToggles() {
      commands.setVerbose(true);
      commands.setVerbose(false);
      commands.setVerbose(true);
      assertTrue(commands.isVerbose());
    }
  }

  @Nested
  @DisplayName("Validation Tests")
  class ValidationTests {
    @Test
    void requiredFieldsPresent() {
      assertNotNull(commands);
      assertNotNull(commands.getClass().getSimpleName());
    }

    @Test
    void optionsInitialized() {
      assertFalse(commands.isVerbose());
      assertFalse(commands.isDryRun());
      assertFalse(commands.isTrimComments());
    }

    @Test
    void consistencyAfterComplexOperations() {
      commands.setInputPath("/input");
      commands.setVerbose(true);
      commands.includeFileType("java");
      commands.setMaxFileSize(1024);
      assertEquals("/input", commands.getInputPath());
      assertTrue(commands.isVerbose());
      assertTrue(commands.isFileTypeIncluded("java"));
      assertEquals(1024, commands.getMaxFileSize());
    }
  }

  @Nested
  @DisplayName("Integration Tests")
  class IntegrationTests {
    @Test
    void stateIsolatedBetweenInstances() {
      CodeTrimmerCommands cmd1 = new CodeTrimmerCommands();
      CodeTrimmerCommands cmd2 = new CodeTrimmerCommands();
      cmd1.setInputPath("/input1");
      cmd2.setInputPath("/input2");
      assertEquals("/input1", cmd1.getInputPath());
      assertEquals("/input2", cmd2.getInputPath());
    }

    @Test
    void multipleInstancesCanBeConfigured() {
      CodeTrimmerCommands cmd1 = new CodeTrimmerCommands();
      CodeTrimmerCommands cmd2 = new CodeTrimmerCommands();
      cmd1.setVerbose(true);
      cmd2.setVerbose(false);
      assertTrue(cmd1.isVerbose());
      assertFalse(cmd2.isVerbose());
    }

    @Test
    void toTrimOptionsConversion() {
      commands.setInputPath("/input");
      commands.setVerbose(true);
      commands.setTrimComments(true);
      TrimOptions opts = commands.toTrimOptions();
      assertNotNull(opts);
      assertEquals("/input", opts.getInputDirectory());
      assertTrue(opts.isVerbose());
      assertTrue(opts.isTrimComments());
    }
  }
}
