package com.codetrimmer.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Advanced comprehensive tests for CodeTrimmerConfig.
 * Tests all configuration properties, setters, getters, and edge cases.
 */
@DisplayName("CodeTrimmerConfig Advanced Tests")
class CodeTrimmerConfigAdvancedTest {

  private CodeTrimmerConfig config;

  @BeforeEach
  void setUp() {
    config = new CodeTrimmerConfig();
  }

  // ============================================
  // Tests for include/exclude patterns
  // ============================================

  @Nested
  @DisplayName("Include Pattern Tests")
  class IncludePatternTests {

    @Test
    @DisplayName("Set and get include pattern")
    void setAndGetInclude() {
      config.setInclude("**/*.java");
      assertEquals("**/*.java", config.getInclude());
    }

    @Test
    @DisplayName("Include pattern with wildcards")
    void includeWithWildcards() {
      config.setInclude("src/**/*.{java,xml}");
      assertEquals("src/**/*.{java,xml}", config.getInclude());
    }

    @Test
    @DisplayName("Multiple include patterns")
    void multipleIncludePatterns() {
      config.setInclude("**/*.java,**/*.xml");
      assertTrue(config.getInclude().contains("*.java"));
      assertTrue(config.getInclude().contains("*.xml"));
    }

    @Test
    @DisplayName("Empty include pattern")
    void emptyIncludePattern() {
      config.setInclude("");
      assertEquals("", config.getInclude());
    }

    @Test
    @DisplayName("Include pattern with special characters")
    void includeSpecialCharacters() {
      config.setInclude("**/[test]*.java");
      assertEquals("**/[test]*.java", config.getInclude());
    }

    @ParameterizedTest
    @ValueSource(strings = {"*.java", "src/**/*.java", "**/*Test.java", "*.{java,kt}"})
    @DisplayName("Various include patterns")
    void variousIncludePatterns(String pattern) {
      config.setInclude(pattern);
      assertEquals(pattern, config.getInclude());
    }
  }

  @Nested
  @DisplayName("Exclude Pattern Tests")
  class ExcludePatternTests {

    @Test
    @DisplayName("Set and get exclude pattern")
    void setAndGetExclude() {
      config.setExclude("**/target/**");
      assertEquals("**/target/**", config.getExclude());
    }

    @Test
    @DisplayName("Multiple exclude patterns")
    void multipleExcludePatterns() {
      config.setExclude("**/target/**,**/node_modules/**,**/build/**");
      assertTrue(config.getExclude().contains("target"));
      assertTrue(config.getExclude().contains("node_modules"));
      assertTrue(config.getExclude().contains("build"));
    }

    @Test
    @DisplayName("Exclude hidden files")
    void excludeHiddenFiles() {
      config.setExclude("**/.*");
      assertEquals("**/.*", config.getExclude());
    }

    @Test
    @DisplayName("Empty exclude pattern")
    void emptyExcludePattern() {
      config.setExclude("");
      assertEquals("", config.getExclude());
    }

    @ParameterizedTest
    @ValueSource(strings = {"**/target/**", "**/*.class", "**/.*", "build,dist"})
    @DisplayName("Various exclude patterns")
    void variousExcludePatterns(String pattern) {
      config.setExclude(pattern);
      assertEquals(pattern, config.getExclude());
    }
  }

  // ============================================
  // Tests for file size limits
  // ============================================

  @Nested
  @DisplayName("Max File Size Tests")
  class MaxFileSizeTests {

    @Test
    @DisplayName("Set and get max file size")
    void setAndGetMaxFileSize() {
      config.setMaxFileSize(10485760L);
      assertEquals(10485760L, config.getMaxFileSize());
    }

    @Test
    @DisplayName("Max file size of zero (no limit)")
    void maxFileSizeZero() {
      config.setMaxFileSize(0L);
      assertEquals(0L, config.getMaxFileSize());
    }

    @Test
    @DisplayName("Max file size of 1 byte")
    void maxFileSizeOneByte() {
      config.setMaxFileSize(1L);
      assertEquals(1L, config.getMaxFileSize());
    }

    @Test
    @DisplayName("Max file size at 1 MB")
    void maxFileSizeOneMB() {
      config.setMaxFileSize(1048576L);
      assertEquals(1048576L, config.getMaxFileSize());
    }

    @Test
    @DisplayName("Max file size at 5 MB")
    void maxFileSizeFiveMB() {
      config.setMaxFileSize(5242880L);
      assertEquals(5242880L, config.getMaxFileSize());
    }

    @Test
    @DisplayName("Max file size at 1 GB")
    void maxFileSizeOneGB() {
      config.setMaxFileSize(1073741824L);
      assertEquals(1073741824L, config.getMaxFileSize());
    }

    @Test
    @DisplayName("Max file size at maximum long value")
    void maxFileSizeMaxValue() {
      config.setMaxFileSize(Long.MAX_VALUE);
      assertEquals(Long.MAX_VALUE, config.getMaxFileSize());
    }

    @ParameterizedTest
    @ValueSource(longs = {1024, 1048576, 5242880, 10485760, 104857600})
    @DisplayName("Various file size limits")
    void variousFileSizeLimits(long size) {
      config.setMaxFileSize(size);
      assertEquals(size, config.getMaxFileSize());
    }
  }

  // ============================================
  // Tests for file count limits
  // ============================================

  @Nested
  @DisplayName("Max Files Tests")
  class MaxFilesTests {

    @Test
    @DisplayName("Set and get max files")
    void setAndGetMaxFiles() {
      config.setMaxFiles(100);
      assertEquals(100, config.getMaxFiles());
    }

    @Test
    @DisplayName("Max files of zero (no limit)")
    void maxFilesZero() {
      config.setMaxFiles(0);
      assertEquals(0, config.getMaxFiles());
    }

    @Test
    @DisplayName("Max files of 1")
    void maxFilesOne() {
      config.setMaxFiles(1);
      assertEquals(1, config.getMaxFiles());
    }

    @Test
    @DisplayName("Max files at 50")
    void maxFilesFifty() {
      config.setMaxFiles(50);
      assertEquals(50, config.getMaxFiles());
    }

    @Test
    @DisplayName("Max files at 1000")
    void maxFilesThousand() {
      config.setMaxFiles(1000);
      assertEquals(1000, config.getMaxFiles());
    }

    @Test
    @DisplayName("Max files at maximum int value")
    void maxFilesMaxValue() {
      config.setMaxFiles(Integer.MAX_VALUE);
      assertEquals(Integer.MAX_VALUE, config.getMaxFiles());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 50, 100, 1000})
    @DisplayName("Various file count limits")
    void variousFileCountLimits(int count) {
      config.setMaxFiles(count);
      assertEquals(count, config.getMaxFiles());
    }
  }

  // ============================================
  // Tests for whitespace settings
  // ============================================

  @Nested
  @DisplayName("Whitespace Settings Tests")
  class WhitespaceSettingsTests {

    @Test
    @DisplayName("Toggle trim trailing whitespace")
    void toggleTrimTrailingWhitespace() {
      config.setTrimTrailingWhitespace(false);
      assertFalse(config.isTrimTrailingWhitespace());
      config.setTrimTrailingWhitespace(true);
      assertTrue(config.isTrimTrailingWhitespace());
    }

    @Test
    @DisplayName("Toggle ensure final newline")
    void toggleEnsureFinalNewline() {
      config.setEnsureFinalNewline(false);
      assertFalse(config.isEnsureFinalNewline());
      config.setEnsureFinalNewline(true);
      assertTrue(config.isEnsureFinalNewline());
    }

    @Test
    @DisplayName("Set max consecutive blank lines")
    void setMaxConsecutiveBlankLines() {
      config.setMaxConsecutiveBlankLines(5);
      assertEquals(5, config.getMaxConsecutiveBlankLines());
    }

    @Test
    @DisplayName("Max consecutive blank lines at zero")
    void maxConsecutiveBlankLinesZero() {
      config.setMaxConsecutiveBlankLines(0);
      assertEquals(0, config.getMaxConsecutiveBlankLines());
    }

    @Test
    @DisplayName("Max consecutive blank lines at various values")
    void maxConsecutiveBlankLinesVariousValues() {
      int[] values = {1, 2, 5, 10, 100};
      for (int value : values) {
        config.setMaxConsecutiveBlankLines(value);
        assertEquals(value, config.getMaxConsecutiveBlankLines());
      }
    }
  }

  // ============================================
  // Tests for operation modes
  // ============================================

  @Nested
  @DisplayName("Operation Mode Tests")
  class OperationModeTests {

    @Test
    @DisplayName("Toggle dry run")
    void toggleDryRun() {
      config.setDryRun(false);
      assertFalse(config.isDryRun());
      config.setDryRun(true);
      assertTrue(config.isDryRun());
    }

    @Test
    @DisplayName("Toggle create backups")
    void toggleCreateBackups() {
      config.setCreateBackups(false);
      assertFalse(config.isCreateBackups());
      config.setCreateBackups(true);
      assertTrue(config.isCreateBackups());
    }

    @Test
    @DisplayName("Toggle fail fast")
    void toggleFailFast() {
      config.setFailFast(false);
      assertFalse(config.isFailFast());
      config.setFailFast(true);
      assertTrue(config.isFailFast());
    }

    @Test
    @DisplayName("Dry run and backup together")
    void dryRunAndBackupTogether() {
      config.setDryRun(true);
      config.setCreateBackups(true);
      assertTrue(config.isDryRun());
      assertTrue(config.isCreateBackups());
    }

    @Test
    @DisplayName("All operation modes off")
    void allOperationModesOff() {
      config.setDryRun(false);
      config.setCreateBackups(false);
      config.setFailFast(false);
      assertFalse(config.isDryRun());
      assertFalse(config.isCreateBackups());
      assertFalse(config.isFailFast());
    }

    @Test
    @DisplayName("All operation modes on")
    void allOperationModesOn() {
      config.setDryRun(true);
      config.setCreateBackups(true);
      config.setFailFast(true);
      assertTrue(config.isDryRun());
      assertTrue(config.isCreateBackups());
      assertTrue(config.isFailFast());
    }
  }

  // ============================================
  // Tests for output settings
  // ============================================

  @Nested
  @DisplayName("Output Settings Tests")
  class OutputSettingsTests {

    @Test
    @DisplayName("Toggle verbose")
    void toggleVerbose() {
      config.setVerbose(false);
      assertFalse(config.isVerbose());
      config.setVerbose(true);
      assertTrue(config.isVerbose());
    }

    @Test
    @DisplayName("Toggle quiet")
    void toggleQuiet() {
      config.setQuiet(false);
      assertFalse(config.isQuiet());
      config.setQuiet(true);
      assertTrue(config.isQuiet());
    }

    @Test
    @DisplayName("Toggle no color")
    void toggleNoColor() {
      config.setNoColor(false);
      assertFalse(config.isNoColor());
      config.setNoColor(true);
      assertTrue(config.isNoColor());
    }

    @Test
    @DisplayName("Toggle disable color for pipe")
    void toggleDisableColorForPipe() {
      config.setDisableColorForPipe(false);
      assertFalse(config.isDisableColorForPipe());
      config.setDisableColorForPipe(true);
      assertTrue(config.isDisableColorForPipe());
    }

    @Test
    @DisplayName("Verbose and quiet together")
    void verboseAndQuietTogether() {
      config.setVerbose(true);
      config.setQuiet(true);
      assertTrue(config.isVerbose());
      assertTrue(config.isQuiet());
    }

    @Test
    @DisplayName("Color settings combination")
    void colorSettingsCombination() {
      config.setNoColor(true);
      config.setDisableColorForPipe(true);
      assertTrue(config.isNoColor());
      assertTrue(config.isDisableColorForPipe());
    }
  }

  // ============================================
  // Tests for file discovery settings
  // ============================================

  @Nested
  @DisplayName("File Discovery Tests")
  class FileDiscoveryTests {

    @Test
    @DisplayName("Toggle include hidden")
    void toggleIncludeHidden() {
      config.setIncludeHidden(false);
      assertFalse(config.isIncludeHidden());
      config.setIncludeHidden(true);
      assertTrue(config.isIncludeHidden());
    }

    @Test
    @DisplayName("Toggle follow symlinks")
    void toggleFollowSymlinks() {
      config.setFollowSymlinks(false);
      assertFalse(config.isFollowSymlinks());
      config.setFollowSymlinks(true);
      assertTrue(config.isFollowSymlinks());
    }

    @Test
    @DisplayName("Both file discovery options enabled")
    void bothFileDiscoveryEnabled() {
      config.setIncludeHidden(true);
      config.setFollowSymlinks(true);
      assertTrue(config.isIncludeHidden());
      assertTrue(config.isFollowSymlinks());
    }

    @Test
    @DisplayName("Both file discovery options disabled")
    void bothFileDiscoveryDisabled() {
      config.setIncludeHidden(false);
      config.setFollowSymlinks(false);
      assertFalse(config.isIncludeHidden());
      assertFalse(config.isFollowSymlinks());
    }
  }

  // ============================================
  // Tests for no-limits mode
  // ============================================

  @Nested
  @DisplayName("No Limits Mode Tests")
  class NoLimitsModeTests {

    @Test
    @DisplayName("Toggle no limits")
    void toggleNoLimits() {
      config.setNoLimits(false);
      assertFalse(config.isNoLimits());
      config.setNoLimits(true);
      assertTrue(config.isNoLimits());
    }

    @Test
    @DisplayName("No limits mode with large file size")
    void noLimitsModeWithLargeSize() {
      config.setMaxFileSize(Long.MAX_VALUE);
      config.setNoLimits(true);
      assertTrue(config.isNoLimits());
      assertEquals(Long.MAX_VALUE, config.getMaxFileSize());
    }

    @Test
    @DisplayName("No limits mode with large file count")
    void noLimitsModeWithLargeCount() {
      config.setMaxFiles(Integer.MAX_VALUE);
      config.setNoLimits(true);
      assertTrue(config.isNoLimits());
      assertEquals(Integer.MAX_VALUE, config.getMaxFiles());
    }
  }

  // ============================================
  // Tests for combined configuration states
  // ============================================

  @Nested
  @DisplayName("Combined Configuration Tests")
  class CombinedConfigurationTests {

    @Test
    @DisplayName("Conservative configuration")
    void conservativeConfiguration() {
      config.setInclude("**/*.java");
      config.setExclude("**/target/**");
      config.setMaxFileSize(1048576L);
      config.setMaxFiles(100);
      config.setDryRun(true);
      config.setCreateBackups(true);
      config.setVerbose(true);

      assertEquals("**/*.java", config.getInclude());
      assertEquals("**/target/**", config.getExclude());
      assertEquals(1048576L, config.getMaxFileSize());
      assertEquals(100, config.getMaxFiles());
      assertTrue(config.isDryRun());
      assertTrue(config.isCreateBackups());
      assertTrue(config.isVerbose());
    }

    @Test
    @DisplayName("Aggressive configuration")
    void aggressiveConfiguration() {
      config.setInclude("**/*");
      config.setExclude("");
      config.setMaxFileSize(Long.MAX_VALUE);
      config.setMaxFiles(Integer.MAX_VALUE);
      config.setDryRun(false);
      config.setCreateBackups(false);
      config.setVerbose(false);
      config.setQuiet(true);
      config.setNoLimits(true);

      assertEquals("**/*", config.getInclude());
      assertEquals("", config.getExclude());
      assertTrue(config.isQuiet());
      assertTrue(config.isNoLimits());
    }

    @Test
    @DisplayName("Full configuration set")
    void fullConfigurationSet() {
      config.setInclude("src/**/*.java");
      config.setExclude("**/test/**");
      config.setIncludeHidden(false);
      config.setFollowSymlinks(true);
      config.setMaxFileSize(5242880L);
      config.setMaxFiles(500);
      config.setNoLimits(false);
      config.setDryRun(false);
      config.setCreateBackups(true);
      config.setFailFast(false);
      config.setTrimTrailingWhitespace(true);
      config.setEnsureFinalNewline(true);
      config.setMaxConsecutiveBlankLines(2);
      config.setVerbose(true);
      config.setQuiet(false);
      config.setNoColor(false);
      config.setDisableColorForPipe(true);

      assertEquals("src/**/*.java", config.getInclude());
      assertEquals("**/test/**", config.getExclude());
      assertFalse(config.isIncludeHidden());
      assertTrue(config.isFollowSymlinks());
      assertEquals(5242880L, config.getMaxFileSize());
      assertEquals(500, config.getMaxFiles());
      assertFalse(config.isNoLimits());
      assertFalse(config.isDryRun());
      assertTrue(config.isCreateBackups());
      assertFalse(config.isFailFast());
      assertTrue(config.isTrimTrailingWhitespace());
      assertTrue(config.isEnsureFinalNewline());
      assertEquals(2, config.getMaxConsecutiveBlankLines());
      assertTrue(config.isVerbose());
      assertFalse(config.isQuiet());
      assertFalse(config.isNoColor());
      assertTrue(config.isDisableColorForPipe());
    }
  }

  // ============================================
  // Tests for state persistence
  // ============================================

  @Nested
  @DisplayName("State Persistence Tests")
  class StatePersistenceTests {

    @Test
    @DisplayName("Configuration values persist after multiple operations")
    void valuesPersist() {
      config.setInclude("*.java");
      String include1 = config.getInclude();
      config.setMaxFiles(100);
      String include2 = config.getInclude();
      assertEquals(include1, include2);
    }

    @Test
    @DisplayName("Boolean flags persist independently")
    void booleanFlagsPersist() {
      config.setVerbose(true);
      config.setQuiet(true);
      assertTrue(config.isVerbose());
      assertTrue(config.isQuiet());
    }

    @Test
    @DisplayName("Numeric values persist independently")
    void numericValuesPersist() {
      config.setMaxFileSize(1000L);
      config.setMaxFiles(50);
      assertEquals(1000L, config.getMaxFileSize());
      assertEquals(50, config.getMaxFiles());
    }
  }

  // ============================================
  // Tests for null/empty values
  // ============================================

  @Nested
  @DisplayName("Null/Empty Value Tests")
  class NullEmptyValueTests {

    @Test
    @DisplayName("Include can be set to empty string")
    void includeEmptyString() {
      config.setInclude("");
      assertEquals("", config.getInclude());
    }

    @Test
    @DisplayName("Exclude can be set to empty string")
    void excludeEmptyString() {
      config.setExclude("");
      assertEquals("", config.getExclude());
    }

    @Test
    @DisplayName("Max file size zero is valid")
    void maxFileSizeZeroValid() {
      config.setMaxFileSize(0L);
      assertEquals(0L, config.getMaxFileSize());
    }

    @Test
    @DisplayName("Max files zero is valid")
    void maxFilesZeroValid() {
      config.setMaxFiles(0);
      assertEquals(0, config.getMaxFiles());
    }
  }
}
