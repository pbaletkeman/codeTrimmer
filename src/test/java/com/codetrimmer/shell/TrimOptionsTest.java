package com.codetrimmer.shell;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TrimOptionsTest {

  @Test
  void testBuilderBasic() {
    TrimOptions options = TrimOptions.builder()
        .directory(".")
        .dryRun(true)
        .verbose(true)
        .build();

    assertEquals(".", options.getDirectory());
    assertTrue(options.isDryRun());
    assertTrue(options.isVerbose());
  }

  @Test
  void testBuilderAllOptions() {
    TrimOptions options = TrimOptions.builder()
        .directory("/home/user/project")
        .include("*.java")
        .exclude("*.class")
        .maxSize(1048576)
        .maxFiles(100)
        .dryRun(true)
        .verbose(true)
        .quiet(false)
        .noColor(false)
        .includeHidden(true)
        .backup(true)
        .noLimits(false)
        .build();

    assertEquals("/home/user/project", options.getDirectory());
    assertEquals("*.java", options.getInclude());
    assertEquals("*.class", options.getExclude());
    assertEquals(1048576, options.getMaxSize());
    assertEquals(100, options.getMaxFiles());
    assertTrue(options.isDryRun());
    assertTrue(options.isVerbose());
    assertFalse(options.isQuiet());
    assertFalse(options.isNoColor());
    assertTrue(options.isIncludeHidden());
    assertTrue(options.isBackup());
    assertFalse(options.isNoLimits());
  }

  @Test
  void testBuilderDefault() {
    TrimOptions options = TrimOptions.builder().build();

    assertNull(options.getDirectory());
    assertNull(options.getInclude());
    assertNull(options.getExclude());
    assertEquals(0, options.getMaxSize());
    assertEquals(0, options.getMaxFiles());
    assertFalse(options.isDryRun());
    assertFalse(options.isVerbose());
    assertFalse(options.isQuiet());
    assertFalse(options.isNoColor());
    assertFalse(options.isIncludeHidden());
    assertFalse(options.isBackup());
    assertFalse(options.isNoLimits());
  }

  @Test
  void testDirectoryOption() {
    TrimOptions options = TrimOptions.builder()
        .directory("/path/to/directory")
        .build();

    assertEquals("/path/to/directory", options.getDirectory());
  }

  @Test
  void testIncludeOption() {
    TrimOptions options = TrimOptions.builder()
        .include("*.java,*.xml")
        .build();

    assertEquals("*.java,*.xml", options.getInclude());
  }

  @Test
  void testExcludeOption() {
    TrimOptions options = TrimOptions.builder()
        .exclude("target/**,*.class")
        .build();

    assertEquals("target/**,*.class", options.getExclude());
  }

  @Test
  void testMaxSizeOption() {
    TrimOptions options = TrimOptions.builder()
        .maxSize(5242880)
        .build();

    assertEquals(5242880, options.getMaxSize());
  }

  @Test
  void testMaxFilesOption() {
    TrimOptions options = TrimOptions.builder()
        .maxFiles(1000)
        .build();

    assertEquals(1000, options.getMaxFiles());
  }

  @Test
  void testDryRunOption() {
    TrimOptions options = TrimOptions.builder()
        .dryRun(true)
        .build();

    assertTrue(options.isDryRun());
  }

  @Test
  void testVerboseOption() {
    TrimOptions options = TrimOptions.builder()
        .verbose(true)
        .build();

    assertTrue(options.isVerbose());
  }

  @Test
  void testQuietOption() {
    TrimOptions options = TrimOptions.builder()
        .quiet(true)
        .build();

    assertTrue(options.isQuiet());
  }

  @Test
  void testNoColorOption() {
    TrimOptions options = TrimOptions.builder()
        .noColor(true)
        .build();

    assertTrue(options.isNoColor());
  }

  @Test
  void testIncludeHiddenOption() {
    TrimOptions options = TrimOptions.builder()
        .includeHidden(true)
        .build();

    assertTrue(options.isIncludeHidden());
  }

  @Test
  void testBackupOption() {
    TrimOptions options = TrimOptions.builder()
        .backup(true)
        .build();

    assertTrue(options.isBackup());
  }

  @Test
  void testNoLimitsOption() {
    TrimOptions options = TrimOptions.builder()
        .noLimits(true)
        .build();

    assertTrue(options.isNoLimits());
  }

  @Test
  void testVerboseAndQuietNotTogether() {
    TrimOptions options = TrimOptions.builder()
        .verbose(true)
        .quiet(false)
        .build();

    assertTrue(options.isVerbose());
    assertFalse(options.isQuiet());
  }

  @Test
  void testCurrentDirectoryPath() {
    TrimOptions options = TrimOptions.builder()
        .directory(".")
        .build();

    assertEquals(".", options.getDirectory());
  }

  @Test
  void testAbsolutePathWindows() {
    TrimOptions options = TrimOptions.builder()
        .directory("C:\\Users\\project")
        .build();

    assertEquals("C:\\Users\\project", options.getDirectory());
  }

  @Test
  void testAbsolutePathUnix() {
    TrimOptions options = TrimOptions.builder()
        .directory("/home/user/project")
        .build();

    assertEquals("/home/user/project", options.getDirectory());
  }

  @Test
  void testRelativePathWithDotDot() {
    TrimOptions options = TrimOptions.builder()
        .directory("../project")
        .build();

    assertEquals("../project", options.getDirectory());
  }

  @Test
  void testWildcardPatterns() {
    TrimOptions options = TrimOptions.builder()
        .include("**/*.java")
        .exclude("**/test/**")
        .build();

    assertEquals("**/*.java", options.getInclude());
    assertEquals("**/test/**", options.getExclude());
  }

  @Test
  void testComplexPatterns() {
    TrimOptions options = TrimOptions.builder()
        .include("src/**/*.{java,xml,properties}")
        .exclude("target/**,build/**,.idea/**")
        .build();

    assertEquals("src/**/*.{java,xml,properties}", options.getInclude());
    assertEquals("target/**,build/**,.idea/**", options.getExclude());
  }

  @Test
  void testLargeMaxSize() {
    TrimOptions options = TrimOptions.builder()
        .maxSize(Long.MAX_VALUE)
        .build();

    assertEquals(Long.MAX_VALUE, options.getMaxSize());
  }

  @Test
  void testLargeMaxFiles() {
    TrimOptions options = TrimOptions.builder()
        .maxFiles(Integer.MAX_VALUE)
        .build();

    assertEquals(Integer.MAX_VALUE, options.getMaxFiles());
  }

  @Test
  void testZeroMaxSize() {
    TrimOptions options = TrimOptions.builder()
        .maxSize(0)
        .build();

    assertEquals(0, options.getMaxSize());
  }

  @Test
  void testZeroMaxFiles() {
    TrimOptions options = TrimOptions.builder()
        .maxFiles(0)
        .build();

    assertEquals(0, options.getMaxFiles());
  }

  @Test
  void testMultipleIncludePatterns() {
    TrimOptions options = TrimOptions.builder()
        .include("*.java,*.kt,*.scala")
        .build();

    assertEquals("*.java,*.kt,*.scala", options.getInclude());
  }

  @Test
  void testMultipleExcludePatterns() {
    TrimOptions options = TrimOptions.builder()
        .exclude("*.min.js,*.min.css,*.o,*.class")
        .build();

    assertEquals("*.min.js,*.min.css,*.o,*.class", options.getExclude());
  }

  @Test
  void testBooleanFlagsCombinations() {
    TrimOptions opts1 = TrimOptions.builder()
        .dryRun(true)
        .verbose(true)
        .includeHidden(true)
        .backup(true)
        .build();

    TrimOptions opts2 = TrimOptions.builder()
        .dryRun(false)
        .verbose(false)
        .includeHidden(false)
        .backup(false)
        .noColor(true)
        .quiet(true)
        .build();

    assertTrue(opts1.isDryRun());
    assertTrue(opts1.isVerbose());
    assertTrue(opts1.isIncludeHidden());
    assertTrue(opts1.isBackup());

    assertFalse(opts2.isDryRun());
    assertFalse(opts2.isVerbose());
    assertFalse(opts2.isIncludeHidden());
    assertFalse(opts2.isBackup());
    assertTrue(opts2.isNoColor());
    assertTrue(opts2.isQuiet());
  }

  @Test
  void testRealisticScenario1() {
    // Dry run with verbose output to see what would be changed
    TrimOptions options = TrimOptions.builder()
        .directory("src")
        .include("*.java")
        .exclude("**/generated/**")
        .dryRun(true)
        .verbose(true)
        .includeHidden(false)
        .backup(false)
        .maxFiles(1000)
        .build();

    assertEquals("src", options.getDirectory());
    assertEquals("*.java", options.getInclude());
    assertEquals("**/generated/**", options.getExclude());
    assertTrue(options.isDryRun());
    assertTrue(options.isVerbose());
    assertFalse(options.isIncludeHidden());
    assertFalse(options.isBackup());
    assertEquals(1000, options.getMaxFiles());
  }

  @Test
  void testRealisticScenario2() {
    // Process all files with backups, no limits
    TrimOptions options = TrimOptions.builder()
        .directory("/home/user/documents")
        .include("*")
        .exclude("")
        .backup(true)
        .noLimits(true)
        .verbose(false)
        .quiet(true)
        .build();

    assertEquals("/home/user/documents", options.getDirectory());
    assertEquals("*", options.getInclude());
    assertEquals("", options.getExclude());
    assertTrue(options.isBackup());
    assertTrue(options.isNoLimits());
    assertFalse(options.isVerbose());
    assertTrue(options.isQuiet());
  }

  @Test
  void testRealisticScenario3() {
    // Conservative mode: only Java files, with backups, size limits
    TrimOptions options = TrimOptions.builder()
        .directory(".")
        .include("**/*.java")
        .exclude("target/**,build/**")
        .maxSize(5242880)
        .maxFiles(500)
        .backup(true)
        .dryRun(false)
        .verbose(false)
        .noLimits(false)
        .build();

    assertEquals(".", options.getDirectory());
    assertEquals("**/*.java", options.getInclude());
    assertEquals("target/**,build/**", options.getExclude());
    assertEquals(5242880, options.getMaxSize());
    assertEquals(500, options.getMaxFiles());
    assertTrue(options.isBackup());
    assertFalse(options.isDryRun());
    assertFalse(options.isVerbose());
    assertFalse(options.isNoLimits());
  }

  @Test
  void testNullableDirectory() {
    TrimOptions options = TrimOptions.builder().build();
    assertNull(options.getDirectory());
  }

  @Test
  void testNullableInclude() {
    TrimOptions options = TrimOptions.builder().build();
    assertNull(options.getInclude());
  }

  @Test
  void testNullableExclude() {
    TrimOptions options = TrimOptions.builder().build();
    assertNull(options.getExclude());
  }

  @Test
  void testEmptyDirectory() {
    TrimOptions options = TrimOptions.builder()
        .directory("")
        .build();

    assertEquals("", options.getDirectory());
  }

  @Test
  void testEmptyInclude() {
    TrimOptions options = TrimOptions.builder()
        .include("")
        .build();

    assertEquals("", options.getInclude());
  }

  @Test
  void testEmptyExclude() {
    TrimOptions options = TrimOptions.builder()
        .exclude("")
        .build();

    assertEquals("", options.getExclude());
  }

  @Test
  void testBuilderChaining() {
    TrimOptions options = TrimOptions.builder()
        .directory(".")
        .include("*.txt")
        .exclude("")
        .dryRun(false)
        .verbose(true)
        .backup(true)
        .maxFiles(100)
        .maxSize(1024)
        .build();

    assertNotNull(options);
    assertEquals(".", options.getDirectory());
    assertEquals("*.txt", options.getInclude());
  }

  @Test
  void testGettersReturnCorrectValues() {
    TrimOptions options = TrimOptions.builder()
        .directory("test")
        .include("*.java")
        .exclude("*.class")
        .maxSize(100000)
        .maxFiles(50)
        .dryRun(true)
        .verbose(true)
        .quiet(false)
        .noColor(true)
        .includeHidden(true)
        .backup(false)
        .noLimits(false)
        .build();

    assertEquals("test", options.getDirectory());
    assertEquals("*.java", options.getInclude());
    assertEquals("*.class", options.getExclude());
    assertEquals(100000, options.getMaxSize());
    assertEquals(50, options.getMaxFiles());
    assertTrue(options.isDryRun());
    assertTrue(options.isVerbose());
    assertFalse(options.isQuiet());
    assertTrue(options.isNoColor());
    assertTrue(options.isIncludeHidden());
    assertFalse(options.isBackup());
    assertFalse(options.isNoLimits());
  }
}
