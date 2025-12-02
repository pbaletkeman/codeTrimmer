package com.codetrimmer.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodeTrimmerConfigTest {

  private CodeTrimmerConfig config;

  @BeforeEach
  void setUp() {
    config = new CodeTrimmerConfig();
  }

  @Test
  void testDefaultValues() {
    assertEquals("*", config.getInclude());
    assertEquals("", config.getExclude());
    assertFalse(config.isIncludeHidden());
    assertFalse(config.isFollowSymlinks());
    assertEquals(2, config.getMaxConsecutiveBlankLines());
    assertTrue(config.isEnsureFinalNewline());
    assertTrue(config.isTrimTrailingWhitespace());
    assertEquals(5242880, config.getMaxFileSize());
    assertEquals(50, config.getMaxFiles());
    assertFalse(config.isNoLimits());
    assertFalse(config.isDryRun());
    assertTrue(config.isCreateBackups());
    assertFalse(config.isFailFast());
    assertFalse(config.isVerbose());
    assertFalse(config.isQuiet());
    assertFalse(config.isNoColor());
    assertTrue(config.isDisableColorForPipe());
  }

  @Test
  void testSetInclude() {
    config.setInclude("*.java");
    assertEquals("*.java", config.getInclude());
  }

  @Test
  void testSetExclude() {
    config.setExclude("*.min.js");
    assertEquals("*.min.js", config.getExclude());
  }

  @Test
  void testSetIncludeHidden() {
    config.setIncludeHidden(true);
    assertTrue(config.isIncludeHidden());
  }

  @Test
  void testSetFollowSymlinks() {
    config.setFollowSymlinks(true);
    assertTrue(config.isFollowSymlinks());
  }

  @Test
  void testSetMaxConsecutiveBlankLines() {
    config.setMaxConsecutiveBlankLines(5);
    assertEquals(5, config.getMaxConsecutiveBlankLines());
  }

  @Test
  void testSetEnsureFinalNewline() {
    config.setEnsureFinalNewline(false);
    assertFalse(config.isEnsureFinalNewline());
  }

  @Test
  void testSetTrimTrailingWhitespace() {
    config.setTrimTrailingWhitespace(false);
    assertFalse(config.isTrimTrailingWhitespace());
  }

  @Test
  void testSetMaxFileSize() {
    config.setMaxFileSize(10485760); // 10MB
    assertEquals(10485760, config.getMaxFileSize());
  }

  @Test
  void testSetMaxFiles() {
    config.setMaxFiles(100);
    assertEquals(100, config.getMaxFiles());
  }

  @Test
  void testSetNoLimits() {
    config.setNoLimits(true);
    assertTrue(config.isNoLimits());
  }

  @Test
  void testSetDryRun() {
    config.setDryRun(true);
    assertTrue(config.isDryRun());
  }

  @Test
  void testSetCreateBackups() {
    config.setCreateBackups(false);
    assertFalse(config.isCreateBackups());
  }

  @Test
  void testSetFailFast() {
    config.setFailFast(true);
    assertTrue(config.isFailFast());
  }

  @Test
  void testSetVerbose() {
    config.setVerbose(true);
    assertTrue(config.isVerbose());
  }

  @Test
  void testSetQuiet() {
    config.setQuiet(true);
    assertTrue(config.isQuiet());
  }

  @Test
  void testSetNoColor() {
    config.setNoColor(true);
    assertTrue(config.isNoColor());
  }

  @Test
  void testSetDisableColorForPipe() {
    config.setDisableColorForPipe(false);
    assertFalse(config.isDisableColorForPipe());
  }

  @Test
  void testMultipleConfigurationChanges() {
    config.setInclude("*.txt");
    config.setExclude("*.tmp");
    config.setMaxConsecutiveBlankLines(3);
    config.setMaxFileSize(1048576); // 1MB
    config.setDryRun(true);
    config.setVerbose(true);

    assertEquals("*.txt", config.getInclude());
    assertEquals("*.tmp", config.getExclude());
    assertEquals(3, config.getMaxConsecutiveBlankLines());
    assertEquals(1048576, config.getMaxFileSize());
    assertTrue(config.isDryRun());
    assertTrue(config.isVerbose());
  }

  @Test
  void testZeroMaxConsecutiveBlankLines() {
    config.setMaxConsecutiveBlankLines(0);
    assertEquals(0, config.getMaxConsecutiveBlankLines());
  }

  @Test
  void testLargeMaxConsecutiveBlankLines() {
    config.setMaxConsecutiveBlankLines(1000);
    assertEquals(1000, config.getMaxConsecutiveBlankLines());
  }

  @Test
  void testSmallMaxFileSize() {
    config.setMaxFileSize(1024); // 1KB
    assertEquals(1024, config.getMaxFileSize());
  }

  @Test
  void testLargeMaxFileSize() {
    config.setMaxFileSize(1099511627776L); // 1TB
    assertEquals(1099511627776L, config.getMaxFileSize());
  }

  @Test
  void testZeroMaxFiles() {
    config.setMaxFiles(0);
    assertEquals(0, config.getMaxFiles());
  }

  @Test
  void testLargeMaxFiles() {
    config.setMaxFiles(1000000);
    assertEquals(1000000, config.getMaxFiles());
  }

  @Test
  void testIncludeMultiplePatterns() {
    config.setInclude("*.java,*.xml,*.properties");
    assertEquals("*.java,*.xml,*.properties", config.getInclude());
  }

  @Test
  void testExcludeMultiplePatterns() {
    config.setExclude("*.class,*.jar,target/**");
    assertEquals("*.class,*.jar,target/**", config.getExclude());
  }

  @Test
  void testEmptyInclude() {
    config.setInclude("");
    assertEquals("", config.getInclude());
  }

  @Test
  void testComplexIncludePattern() {
    config.setInclude("src/**/*.java");
    assertEquals("src/**/*.java", config.getInclude());
  }

  @Test
  void testComplexExcludePattern() {
    config.setExclude("target/**,build/**,.git/**");
    assertEquals("target/**,build/**,.git/**", config.getExclude());
  }

  @Test
  void testToggleAllFlags() {
    // Toggle all boolean flags from default to true
    config.setIncludeHidden(true);
    config.setFollowSymlinks(true);
    config.setEnsureFinalNewline(true);
    config.setTrimTrailingWhitespace(true);
    config.setNoLimits(true);
    config.setDryRun(true);
    config.setCreateBackups(true);
    config.setFailFast(true);
    config.setVerbose(true);
    config.setQuiet(true);
    config.setNoColor(true);
    config.setDisableColorForPipe(true);

    assertTrue(config.isIncludeHidden());
    assertTrue(config.isFollowSymlinks());
    assertTrue(config.isEnsureFinalNewline());
    assertTrue(config.isTrimTrailingWhitespace());
    assertTrue(config.isNoLimits());
    assertTrue(config.isDryRun());
    assertTrue(config.isCreateBackups());
    assertTrue(config.isFailFast());
    assertTrue(config.isVerbose());
    assertTrue(config.isQuiet());
    assertTrue(config.isNoColor());
    assertTrue(config.isDisableColorForPipe());
  }

  @Test
  void testToggleAllFlagsToFalse() {
    // Set all to true first
    config.setIncludeHidden(true);
    config.setFollowSymlinks(true);
    config.setEnsureFinalNewline(true);
    config.setTrimTrailingWhitespace(true);
    config.setNoLimits(true);
    config.setDryRun(true);
    config.setCreateBackups(true);
    config.setFailFast(true);
    config.setVerbose(true);
    config.setQuiet(true);
    config.setNoColor(true);
    config.setDisableColorForPipe(true);

    // Then set all back to false
    config.setIncludeHidden(false);
    config.setFollowSymlinks(false);
    config.setEnsureFinalNewline(false);
    config.setTrimTrailingWhitespace(false);
    config.setNoLimits(false);
    config.setDryRun(false);
    config.setCreateBackups(false);
    config.setFailFast(false);
    config.setVerbose(false);
    config.setQuiet(false);
    config.setNoColor(false);
    config.setDisableColorForPipe(false);

    assertFalse(config.isIncludeHidden());
    assertFalse(config.isFollowSymlinks());
    assertFalse(config.isEnsureFinalNewline());
    assertFalse(config.isTrimTrailingWhitespace());
    assertFalse(config.isNoLimits());
    assertFalse(config.isDryRun());
    assertFalse(config.isCreateBackups());
    assertFalse(config.isFailFast());
    assertFalse(config.isVerbose());
    assertFalse(config.isQuiet());
    assertFalse(config.isNoColor());
    assertFalse(config.isDisableColorForPipe());
  }

  @Test
  void testNumbersAtBoundaries() {
    config.setMaxConsecutiveBlankLines(Integer.MAX_VALUE);
    config.setMaxFileSize(Long.MAX_VALUE);
    config.setMaxFiles(Integer.MAX_VALUE);

    assertEquals(Integer.MAX_VALUE, config.getMaxConsecutiveBlankLines());
    assertEquals(Long.MAX_VALUE, config.getMaxFileSize());
    assertEquals(Integer.MAX_VALUE, config.getMaxFiles());
  }

  @Test
  void testNegativeNumbers() {
    config.setMaxConsecutiveBlankLines(-1);
    config.setMaxFileSize(-1);
    config.setMaxFiles(-1);

    assertEquals(-1, config.getMaxConsecutiveBlankLines());
    assertEquals(-1, config.getMaxFileSize());
    assertEquals(-1, config.getMaxFiles());
  }

  @Test
  void testSpecialCharactersInPatterns() {
    config.setInclude("*.[jJ][aA][vV][aA]");
    config.setExclude("*.min.*");

    assertEquals("*.[jJ][aA][vV][aA]", config.getInclude());
    assertEquals("*.min.*", config.getExclude());
  }

  @Test
  void testConfigurationPreservation() {
    // Set various values
    config.setInclude("*.java");
    config.setExclude("*.class");
    config.setMaxConsecutiveBlankLines(3);
    config.setMaxFileSize(2097152);
    config.setDryRun(true);
    config.setVerbose(true);

    // Verify they're preserved
    assertEquals("*.java", config.getInclude());
    assertEquals("*.class", config.getExclude());
    assertEquals(3, config.getMaxConsecutiveBlankLines());
    assertEquals(2097152, config.getMaxFileSize());
    assertTrue(config.isDryRun());
    assertTrue(config.isVerbose());

    // Change other values
    config.setIncludeHidden(true);
    config.setQuiet(true);

    // Verify original values are still intact
    assertEquals("*.java", config.getInclude());
    assertEquals("*.class", config.getExclude());
    assertEquals(3, config.getMaxConsecutiveBlankLines());
    assertEquals(2097152, config.getMaxFileSize());
    assertTrue(config.isDryRun());
    assertTrue(config.isVerbose());
    assertTrue(config.isIncludeHidden());
    assertTrue(config.isQuiet());
  }

  @Test
  void testToString() {
    config.setDryRun(true);
    String str = config.toString();
    assertNotNull(str);
    assertTrue(str.contains("CodeTrimmerConfig"));
  }

  @Test
  void testEqualityWithSameValues() {
    CodeTrimmerConfig config1 = new CodeTrimmerConfig();
    CodeTrimmerConfig config2 = new CodeTrimmerConfig();

    assertEquals(config1, config2);
  }

  @Test
  void testDifferentValues() {
    CodeTrimmerConfig config1 = new CodeTrimmerConfig();
    CodeTrimmerConfig config2 = new CodeTrimmerConfig();

    config2.setDryRun(true);
    assertNotEquals(config1, config2);
  }

  @Test
  void testWhitespaceOptions() {
    config.setEnsureFinalNewline(true);
    config.setTrimTrailingWhitespace(true);
    config.setMaxConsecutiveBlankLines(1);

    assertTrue(config.isEnsureFinalNewline());
    assertTrue(config.isTrimTrailingWhitespace());
    assertEquals(1, config.getMaxConsecutiveBlankLines());
  }

  @Test
  void testOperationModes() {
    config.setDryRun(true);
    config.setFailFast(true);
    config.setCreateBackups(false);

    assertTrue(config.isDryRun());
    assertTrue(config.isFailFast());
    assertFalse(config.isCreateBackups());
  }

  @Test
  void testOutputOptions() {
    config.setVerbose(true);
    config.setQuiet(false);
    config.setNoColor(true);

    assertTrue(config.isVerbose());
    assertFalse(config.isQuiet());
    assertTrue(config.isNoColor());
  }
}
