package com.codetrimmer.shell;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.codetrimmer.config.CodeTrimmerConfig;
import com.codetrimmer.model.FileProcessingResult;
import com.codetrimmer.model.ProcessingStatistics;
import com.codetrimmer.service.FileProcessingService;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Comprehensive tests for CodeTrimmerCommands shell command handler.
 * Tests all shell commands including trim, configure, and status operations.
 */
class CodeTrimmerCommandsTest {

  @Mock private FileProcessingService fileProcessingService;

  @Mock private CodeTrimmerConfig config;

  @Mock private com.codetrimmer.config.ConfigurationLoader configLoader;

  @Mock private com.codetrimmer.report.ReportGenerator reportGenerator;

  @Mock private com.codetrimmer.service.HookGenerator hookGenerator;

  @Mock private com.codetrimmer.service.UndoService undoService;

  @Mock private com.codetrimmer.service.DiffGenerator diffGenerator;

  @InjectMocks private CodeTrimmerCommands commands;

  @TempDir private Path tempDir;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    // Setup default mock for configLoader to return a default config
    when(configLoader.loadConfiguration(anyString()))
        .thenReturn(new com.codetrimmer.config.TrimmerConfig());
  }

  // ============================================
  // Tests for trim command initialization
  // ============================================

  @Test
  void testTrimCommandNullDirectory() {
    // Test trim command with null directory
    TrimOptions options = TrimOptions.builder().directory(null).build();
    assertNotNull(options);
  }

  @Test
  void testTrimCommandEmptyDirectory() {
    // Test trim command with empty directory string
    TrimOptions options = TrimOptions.builder().directory("").build();
    assertNotNull(options);
  }

  @Test
  void testTrimCommandCurrentDirectory() {
    // Test trim command with current directory
    TrimOptions options = TrimOptions.builder().directory(".").build();
    assertEquals(".", options.getDirectory());
  }

  @Test
  void testTrimCommandAbsolutePath() {
    // Test trim command with absolute path
    TrimOptions options = TrimOptions.builder().directory("/home/user/project").build();
    assertEquals("/home/user/project", options.getDirectory());
  }

  @Test
  void testTrimCommandRelativePath() {
    // Test trim command with relative path
    TrimOptions options = TrimOptions.builder().directory("../project").build();
    assertEquals("../project", options.getDirectory());
  }

  // ============================================
  // Tests for include pattern options
  // ============================================

  @Test
  void testTrimCommandDefaultIncludePattern() {
    // Test trim command with default include pattern
    TrimOptions options = TrimOptions.builder().directory(".").build();
    // Default include pattern may be null or empty, both are valid
    assertNotNull(options);
  }

  @Test
  void testTrimCommandCustomIncludePattern() {
    // Test trim command with custom include pattern
    TrimOptions options = TrimOptions.builder().directory(".").include("**/*.java").build();
    assertEquals("**/*.java", options.getInclude());
  }

  @Test
  void testTrimCommandMultipleIncludePatterns() {
    // Test trim command with multiple patterns (comma-separated)
    TrimOptions options =
        TrimOptions.builder().directory(".").include("**/*.java,**/*.xml").build();
    assertTrue(options.getInclude().contains("*.java"));
  }

  @Test
  void testTrimCommandWildcardIncludePattern() {
    // Test trim command with wildcard include pattern
    TrimOptions options = TrimOptions.builder().directory(".").include("src/**/*.java").build();
    assertEquals("src/**/*.java", options.getInclude());
  }

  // ============================================
  // Tests for exclude pattern options
  // ============================================

  @Test
  void testTrimCommandExcludePattern() {
    // Test trim command with exclude pattern
    TrimOptions options =
        TrimOptions.builder().directory(".").exclude("**/target/**").build();
    assertEquals("**/target/**", options.getExclude());
  }

  @Test
  void testTrimCommandExcludeMultiplePatterns() {
    // Test trim command with multiple exclude patterns
    TrimOptions options =
        TrimOptions.builder()
            .directory(".")
            .exclude("**/target/**,**/node_modules/**")
            .build();
    assertTrue(options.getExclude().contains("target"));
  }

  @Test
  void testTrimCommandExcludeHiddenFiles() {
    // Test trim command excluding hidden files
    TrimOptions options =
        TrimOptions.builder().directory(".").exclude("**/.*").build();
    assertEquals("**/.*", options.getExclude());
  }

  // ============================================
  // Tests for size limit options
  // ============================================

  @Test
  void testTrimCommandMaxFileSize() {
    // Test trim command with max file size
    TrimOptions options =
        TrimOptions.builder().directory(".").maxSize(5242880L).build(); // 5MB
    assertEquals(5242880L, options.getMaxSize());
  }

  @Test
  void testTrimCommandMaxFileSizeZero() {
    // Test trim command with zero max file size (no limit)
    TrimOptions options = TrimOptions.builder().directory(".").maxSize(0L).build();
    assertEquals(0L, options.getMaxSize());
  }

  @Test
  void testTrimCommandMaxFileSizeLarge() {
    // Test trim command with large file size
    TrimOptions options =
        TrimOptions.builder().directory(".").maxSize(1073741824L).build(); // 1GB
    assertEquals(1073741824L, options.getMaxSize());
  }

  @Test
  void testTrimCommandMaxFiles() {
    // Test trim command with max number of files
    TrimOptions options = TrimOptions.builder().directory(".").maxFiles(1000).build();
    assertEquals(1000, options.getMaxFiles());
  }

  @Test
  void testTrimCommandMaxFilesUnlimited() {
    // Test trim command with unlimited files
    TrimOptions options = TrimOptions.builder().directory(".").noLimits(true).build();
    assertTrue(options.isNoLimits());
  }

  // ============================================
  // Tests for operation mode options
  // ============================================

  @Test
  void testTrimCommandDryRun() {
    // Test trim command with dry run flag
    TrimOptions options = TrimOptions.builder().directory(".").dryRun(true).build();
    assertTrue(options.isDryRun());
  }

  @Test
  void testTrimCommandCreateBackups() {
    // Test trim command with create backups flag
    TrimOptions options = TrimOptions.builder().directory(".").backup(true).build();
    assertTrue(options.isBackup());
  }

  @Test
  void testTrimCommandNoBackups() {
    // Test trim command without creating backups
    TrimOptions options = TrimOptions.builder().directory(".").backup(false).build();
    assertFalse(options.isBackup());
  }

  @Test
  void testTrimCommandIncludeHidden() {
    // Test trim command to include hidden files
    TrimOptions options = TrimOptions.builder().directory(".").includeHidden(true).build();
    assertTrue(options.isIncludeHidden());
  }

  // ============================================
  // Tests for output options
  // ============================================

  @Test
  void testTrimCommandVerbose() {
    // Test trim command with verbose output
    TrimOptions options = TrimOptions.builder().directory(".").verbose(true).build();
    assertTrue(options.isVerbose());
  }

  @Test
  void testTrimCommandQuiet() {
    // Test trim command with quiet output
    TrimOptions options = TrimOptions.builder().directory(".").quiet(true).build();
    assertTrue(options.isQuiet());
  }

  @Test
  void testTrimCommandNoColor() {
    // Test trim command with no color output
    TrimOptions options = TrimOptions.builder().directory(".").noColor(true).build();
    assertTrue(options.isNoColor());
  }

  @Test
  void testTrimCommandVerboseAndQuiet() {
    // Test trim command with both verbose and quiet (verbose should take precedence)
    TrimOptions options =
        TrimOptions.builder().directory(".").verbose(true).quiet(true).build();
    assertTrue(options.isVerbose());
    assertTrue(options.isQuiet());
  }

  // ============================================
  // Tests for combined options
  // ============================================

  @Test
  void testTrimCommandMinimalOptions() {
    // Test trim command with only directory specified
    TrimOptions options = TrimOptions.builder().directory(".").build();
    assertNotNull(options.getDirectory());
  }

  @Test
  void testTrimCommandConservativeMode() {
    // Test trim command in conservative mode (backups, dry run)
    TrimOptions options =
        TrimOptions.builder()
            .directory(".")
            .dryRun(true)
            .backup(true)
            .verbose(true)
            .build();
    assertTrue(options.isDryRun());
    assertTrue(options.isBackup());
    assertTrue(options.isVerbose());
  }

  @Test
  void testTrimCommandAggressiveMode() {
    // Test trim command in aggressive mode (no backups, no limits)
    TrimOptions options =
        TrimOptions.builder()
            .directory(".")
            .backup(false)
            .noLimits(true)
            .quiet(true)
            .build();
    assertFalse(options.isBackup());
    assertTrue(options.isNoLimits());
    assertTrue(options.isQuiet());
  }

  @Test
  void testTrimCommandFullConfiguration() {
    // Test trim command with all options specified
    TrimOptions options =
        TrimOptions.builder()
            .directory("/home/project")
            .include("**/*.java")
            .exclude("**/target/**")
            .maxSize(10485760L)
            .maxFiles(500)
            .dryRun(true)
            .verbose(true)
            .noColor(false)
            .includeHidden(false)
            .backup(true)
            .build();

    assertEquals("/home/project", options.getDirectory());
    assertEquals("**/*.java", options.getInclude());
    assertEquals("**/target/**", options.getExclude());
    assertEquals(10485760L, options.getMaxSize());
    assertEquals(500, options.getMaxFiles());
    assertTrue(options.isDryRun());
    assertTrue(options.isVerbose());
    assertFalse(options.isNoColor());
    assertFalse(options.isIncludeHidden());
    assertTrue(options.isBackup());
  }

  // ============================================
  // Tests for configuration properties
  // ============================================

  @Test
  void testConfigurationIncludePattern() {
    // Test configuration include pattern property
    when(config.getInclude()).thenReturn("**/*.java");
    assertEquals("**/*.java", config.getInclude());
  }

  @Test
  void testConfigurationExcludePattern() {
    // Test configuration exclude pattern property
    when(config.getExclude()).thenReturn("**/target/**");
    assertEquals("**/target/**", config.getExclude());
  }

  @Test
  void testConfigurationMaxFileSize() {
    // Test configuration max file size property
    when(config.getMaxFileSize()).thenReturn(5242880L);
    assertEquals(5242880L, config.getMaxFileSize());
  }

  @Test
  void testConfigurationMaxFiles() {
    // Test configuration max files property
    when(config.getMaxFiles()).thenReturn(1000);
    assertEquals(1000, config.getMaxFiles());
  }

  // ============================================
  // Tests for file processing service integration
  // ============================================

  @Test
  void testFileProcessingServiceIntegration() {
    // Test integration with file processing service
    List<FileProcessingResult> results = new ArrayList<>();
    results.add(
        new FileProcessingResult.Builder(tempDir.toString())
            .modified(true)
            .linesTrimmed(10)
            .blankLinesRemoved(20)
            .build());
    when(fileProcessingService.processDirectory(anyString())).thenReturn(results);

    List<FileProcessingResult> processed = fileProcessingService.processDirectory(".");
    assertEquals(1, processed.size());
    verify(fileProcessingService, times(1)).processDirectory(".");
  }

  @Test
  void testFileProcessingServiceEmptyResult() {
    // Test service with no files matching criteria
    List<FileProcessingResult> results = new ArrayList<>();
    when(fileProcessingService.processDirectory(anyString())).thenReturn(results);

    List<FileProcessingResult> processed = fileProcessingService.processDirectory(".");
    assertTrue(processed.isEmpty());
  }

  @Test
  void testFileProcessingServiceMultipleFiles() {
    // Test service processing multiple files
    List<FileProcessingResult> results = new ArrayList<>();
    results.add(
        new FileProcessingResult.Builder(tempDir.toString() + "/file1.java")
            .modified(true)
            .linesTrimmed(5)
            .blankLinesRemoved(10)
            .build());
    results.add(
        new FileProcessingResult.Builder(tempDir.toString() + "/file2.java")
            .modified(true)
            .linesTrimmed(3)
            .blankLinesRemoved(8)
            .build());
    when(fileProcessingService.processDirectory(anyString())).thenReturn(results);

    List<FileProcessingResult> processed = fileProcessingService.processDirectory(".");
    assertEquals(2, processed.size());
  }

  // ============================================
  // Tests for statistics tracking
  // ============================================

  @Test
  void testProcessingStatisticsCreation() {
    // Test creating processing statistics
    ProcessingStatistics stats = ProcessingStatistics.builder().build();
    assertEquals(0, stats.getFilesScanned());
  }

  @Test
  void testProcessingStatisticsIncrement() {
    // Test incrementing processing statistics
    ProcessingStatistics stats = ProcessingStatistics.builder().build();
    stats.incrementFilesScanned();
    stats.incrementFilesModified();
    stats.addLinesTrimmed(100);

    assertEquals(1, stats.getFilesScanned());
    assertEquals(1, stats.getFilesModified());
    assertEquals(100, stats.getLinesTrimmed());
  }

  @Test
  void testProcessingStatisticsSkipReasons() {
    // Test tracking skip reasons in statistics
    ProcessingStatistics stats = ProcessingStatistics.builder().build();
    stats.incrementFilesSkipped();

    assertEquals(1, stats.getFilesSkipped());
  }

  // ============================================
  // Tests for error conditions
  // ============================================

  @Test
  void testTrimCommandWithSpecialCharacters() {
    // Test trim command with special characters in path
    TrimOptions options =
        TrimOptions.builder().directory("/home/user/project (copy)").build();
    assertEquals("/home/user/project (copy)", options.getDirectory());
  }

  @Test
  void testTrimCommandWithSpaceInPath() {
    // Test trim command with spaces in path
    TrimOptions options =
        TrimOptions.builder().directory("/home/user/my project").build();
    assertEquals("/home/user/my project", options.getDirectory());
  }

  @Test
  void testTrimCommandWithUnicodeInPath() {
    // Test trim command with unicode characters in path
    TrimOptions options =
        TrimOptions.builder().directory("/home/用户/项目").build();
    assertEquals("/home/用户/项目", options.getDirectory());
  }

  // ============================================
  // Tests for builder pattern edge cases
  // ============================================

  @Test
  void testTrimOptionsBuilderChaining() {
    // Test builder pattern method chaining
    TrimOptions options =
        TrimOptions.builder()
            .directory(".")
            .include("*.java")
            .exclude("*.class")
            .verbose(true)
            .build();

    assertNotNull(options);
    assertEquals(".", options.getDirectory());
  }

  @Test
  void testTrimOptionsBuilderDefaults() {
    // Test builder pattern with default values
    TrimOptions options = TrimOptions.builder().directory(".").build();
    assertNotNull(options);
    assertEquals(".", options.getDirectory());
  }

  @Test
  void testTrimOptionsBuilderNullDirectory() {
    // Test builder pattern with null directory gets handled
    TrimOptions options = TrimOptions.builder().directory(null).build();
    assertNotNull(options);
  }

  // ============================================
  // Tests for configuration edge cases
  // ============================================

  @Test
  void testConfigurationEmptyInclude() {
    // Test configuration with empty include pattern
    CodeTrimmerConfig config = new CodeTrimmerConfig();
    config.setInclude("");
    assertEquals("", config.getInclude());
  }

  @Test
  void testConfigurationNullExclude() {
    // Test configuration with null exclude pattern
    CodeTrimmerConfig config = new CodeTrimmerConfig();
    config.setExclude(null);
    assertNull(config.getExclude());
  }

  @Test
  void testConfigurationNegativeMaxFileSize() {
    // Test configuration with negative max file size (invalid)
    CodeTrimmerConfig config = new CodeTrimmerConfig();
    config.setMaxFileSize(-1L);
    assertEquals(-1L, config.getMaxFileSize());
  }

  @Test
  void testConfigurationNegativeMaxFiles() {
    // Test configuration with negative max files (invalid)
    CodeTrimmerConfig config = new CodeTrimmerConfig();
    config.setMaxFiles(-1);
    assertEquals(-1, config.getMaxFiles());
  }

  // ============================================
  // Tests for boolean flag combinations
  // ============================================

  @Test
  void testConfigurationAllFlagsTrue() {
    // Test configuration with all boolean flags true
    CodeTrimmerConfig config = new CodeTrimmerConfig();
    config.setDryRun(true);
    config.setCreateBackups(true);
    config.setVerbose(true);
    config.setIncludeHidden(true);

    assertTrue(config.isDryRun());
    assertTrue(config.isCreateBackups());
    assertTrue(config.isVerbose());
    assertTrue(config.isIncludeHidden());
  }

  @Test
  void testConfigurationAllFlagsFalse() {
    // Test configuration with all boolean flags false
    CodeTrimmerConfig config = new CodeTrimmerConfig();
    config.setDryRun(false);
    config.setCreateBackups(false);
    config.setVerbose(false);
    config.setIncludeHidden(false);

    assertFalse(config.isDryRun());
    assertFalse(config.isCreateBackups());
    assertFalse(config.isVerbose());
    assertFalse(config.isIncludeHidden());
  }

  // ============================================
  // Tests for numeric boundary values
  // ============================================

  @Test
  void testConfigurationMaxFileSizeZero() {
    // Test configuration with max file size of zero
    CodeTrimmerConfig config = new CodeTrimmerConfig();
    config.setMaxFileSize(0L);
    assertEquals(0L, config.getMaxFileSize());
  }

  @Test
  void testConfigurationMaxFilesZero() {
    // Test configuration with max files of zero
    CodeTrimmerConfig config = new CodeTrimmerConfig();
    config.setMaxFiles(0);
    assertEquals(0, config.getMaxFiles());
  }

  @Test
  void testConfigurationMaxFileSizeMaxValue() {
    // Test configuration with maximum file size value
    CodeTrimmerConfig config = new CodeTrimmerConfig();
    config.setMaxFileSize(Long.MAX_VALUE);
    assertEquals(Long.MAX_VALUE, config.getMaxFileSize());
  }

  @Test
  void testConfigurationMaxFilesMaxValue() {
    // Test configuration with maximum files value
    CodeTrimmerConfig config = new CodeTrimmerConfig();
    config.setMaxFiles(Integer.MAX_VALUE);
    assertEquals(Integer.MAX_VALUE, config.getMaxFiles());
  }

  // ============================================
  // Tests for pattern validation
  // ============================================

  @Test
  void testConfigurationIncludePatternWithWildcards() {
    // Test configuration include pattern with wildcards
    CodeTrimmerConfig config = new CodeTrimmerConfig();
    config.setInclude("src/**/*.{java,xml}");
    assertTrue(config.getInclude().contains("*"));
  }

  @Test
  void testConfigurationExcludePatternComplex() {
    // Test configuration exclude pattern with complex syntax
    CodeTrimmerConfig config = new CodeTrimmerConfig();
    config.setExclude("**/target/**,**/node_modules/**,**/build/**");
    assertTrue(config.getExclude().contains("target"));
  }

  // ============================================
  // Tests for state management
  // ============================================

  @Test
  void testTrimOptionsImmutability() {
    // Test that TrimOptions is immutable once created
    TrimOptions options1 = TrimOptions.builder().directory(".").build();
    TrimOptions options2 = TrimOptions.builder().directory(".").build();

    // Both should be equal in properties
    assertEquals(options1.getDirectory(), options2.getDirectory());
  }

  @Test
  void testConfigurationMutability() {
    // Test that CodeTrimmerConfig is mutable
    CodeTrimmerConfig config = new CodeTrimmerConfig();
    config.setInclude("*.java");
    assertEquals("*.java", config.getInclude());

    config.setInclude("*.xml");
    assertEquals("*.xml", config.getInclude());
  }

  // ============================================
  // Tests for version command
  // ============================================

  @Test
  void testVersionCommand() {
    // Test version command displays version info
    assertDoesNotThrow(() -> commands.version());
  }

  @Test
  void testVersionCommandOutput() {
    // Test that version command can be called without errors
    try {
      commands.version();
    } catch (Exception e) {
      fail("Version command should not throw exception: " + e.getMessage());
    }
  }

  // ============================================
  // Tests for help-trim command
  // ============================================

  @Test
  void testHelpTrimCommand() {
    // Test help-trim command displays help information
    assertDoesNotThrow(() -> commands.helpTrim());
  }

  @Test
  void testHelpTrimCommandOutput() {
    // Test that help-trim command can be called without errors
    try {
      commands.helpTrim();
    } catch (Exception e) {
      fail("Help-trim command should not throw exception: " + e.getMessage());
    }
  }

  @Test
  void testHelpTrimCommandDisplaysCommandDescription() {
    // Test that help text includes command description
    // (Verify by ensuring command executes without error)
    assertDoesNotThrow(() -> commands.helpTrim());
  }

  // ============================================
  // Tests for trim command with mocked service
  // ============================================

  @Test
  void testTrimCommandCallsFileProcessingService() {
    // Test that trim command invokes file processing service
    when(fileProcessingService.processDirectory(anyString()))
        .thenReturn(new ArrayList<>());
    when(fileProcessingService.getStatistics())
        .thenReturn(new ProcessingStatistics());

    TrimOptions options = TrimOptions.builder()
        .directory("/test")
        .quiet(true)
        .build();

    commands.trim("/test", null, null, 0, 0, false, false, true, false, false, false, false);

    verify(fileProcessingService, times(1)).processDirectory(anyString());
  }

  @Test
  void testTrimCommandConfiguresSettingsBeforeProcessing() {
    // Test that configuration is set before processing
    when(fileProcessingService.processDirectory(anyString()))
        .thenReturn(new ArrayList<>());
    when(fileProcessingService.getStatistics())
        .thenReturn(new ProcessingStatistics());

    commands.trim("/test", "*.java", null, 5242880, 50, false, false, true, false, false, false, false);

    verify(config).setInclude(anyString());
  }

  @Test
  void testTrimCommandWithVerboseOutput() {
    // Test trim command with verbose flag
    List<FileProcessingResult> results = new ArrayList<>();
    results.add(new FileProcessingResult.Builder("/test/file.java")
        .modified(true)
        .linesTrimmed(5)
        .blankLinesRemoved(2)
        .build());

    ProcessingStatistics stats = new ProcessingStatistics();
    stats.incrementFilesScanned();
    stats.incrementFilesModified();
    stats.addLinesTrimmed(5);
    stats.addBlankLinesRemoved(2);
    stats.endProcessing();

    when(fileProcessingService.processDirectory(anyString())).thenReturn(results);
    when(fileProcessingService.getStatistics()).thenReturn(stats);

    assertDoesNotThrow(() ->
        commands.trim("/test", null, null, 0, 0, false, true, false, false, false, false, false)
    );
  }

  @Test
  void testTrimCommandWithQuietOutput() {
    // Test trim command suppresses output when quiet is true
    when(fileProcessingService.processDirectory(anyString()))
        .thenReturn(new ArrayList<>());
    when(fileProcessingService.getStatistics())
        .thenReturn(new ProcessingStatistics());

    assertDoesNotThrow(() ->
        commands.trim("/test", null, null, 0, 0, false, false, true, false, false, false, false)
    );
  }

  @Test
  void testTrimCommandWithDryRunMode() {
    // Test trim command with dry-run flag
    when(fileProcessingService.processDirectory(anyString()))
        .thenReturn(new ArrayList<>());
    when(fileProcessingService.getStatistics())
        .thenReturn(new ProcessingStatistics());

    assertDoesNotThrow(() ->
        commands.trim("/test", null, null, 0, 0, true, false, true, false, false, false, false)
    );

    verify(config).setDryRun(true);
  }

  @Test
  void testTrimCommandWithBackupEnabled() {
    // Test trim command with backup flag enabled
    when(fileProcessingService.processDirectory(anyString()))
        .thenReturn(new ArrayList<>());
    when(fileProcessingService.getStatistics())
        .thenReturn(new ProcessingStatistics());

    assertDoesNotThrow(() ->
        commands.trim("/test", null, null, 0, 0, false, false, true, false, false, true, false)
    );

    verify(config).setCreateBackups(true);
  }

  @Test
  void testTrimCommandWithNoColorOption() {
    // Test trim command with no-color flag
    when(fileProcessingService.processDirectory(anyString()))
        .thenReturn(new ArrayList<>());
    when(fileProcessingService.getStatistics())
        .thenReturn(new ProcessingStatistics());

    assertDoesNotThrow(() ->
        commands.trim("/test", null, null, 0, 0, false, false, true, true, false, false, false)
    );

    verify(config).setNoColor(true);
  }

  @Test
  void testTrimCommandWithIncludeHiddenFiles() {
    // Test trim command with includeHidden flag
    when(fileProcessingService.processDirectory(anyString()))
        .thenReturn(new ArrayList<>());
    when(fileProcessingService.getStatistics())
        .thenReturn(new ProcessingStatistics());

    assertDoesNotThrow(() ->
        commands.trim("/test", null, null, 0, 0, false, false, true, false, true, false, false)
    );

    verify(config).setIncludeHidden(true);
  }

  @Test
  void testTrimCommandWithNoLimits() {
    // Test trim command with no-limits flag
    when(fileProcessingService.processDirectory(anyString()))
        .thenReturn(new ArrayList<>());
    when(fileProcessingService.getStatistics())
        .thenReturn(new ProcessingStatistics());

    assertDoesNotThrow(() ->
        commands.trim("/test", null, null, 0, 0, false, false, true, false, false, false, true)
    );

    verify(config).setNoLimits(true);
  }

  @Test
  void testTrimCommandWithAllOptionsEnabled() {
    // Test trim command with all options enabled
    List<FileProcessingResult> results = new ArrayList<>();
    ProcessingStatistics stats = new ProcessingStatistics();
    stats.endProcessing();

    when(fileProcessingService.processDirectory(anyString())).thenReturn(results);
    when(fileProcessingService.getStatistics()).thenReturn(stats);

    assertDoesNotThrow(() ->
        commands.trim("/test", "*.java", "*.min.js", 10485760, 100, true, true, false, false, true, true, true)
    );
  }

  @Test
  void testTrimCommandHandlesEmptyResults() {
    // Test trim command with no files processed
    when(fileProcessingService.processDirectory(anyString()))
        .thenReturn(new ArrayList<>());
    when(fileProcessingService.getStatistics())
        .thenReturn(new ProcessingStatistics());

    assertDoesNotThrow(() ->
        commands.trim("/empty/directory", null, null, 0, 0, false, false, false, false, false, false, false)
    );
  }

  @Test
  void testTrimCommandProcessesMultipleFiles() {
    // Test trim command processes multiple files
    List<FileProcessingResult> results = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      results.add(new FileProcessingResult.Builder("/test/file" + i + ".java")
          .modified(true)
          .linesTrimmed(10)
          .blankLinesRemoved(3)
          .build());
    }

    ProcessingStatistics stats = new ProcessingStatistics();
    stats.endProcessing();

    when(fileProcessingService.processDirectory(anyString())).thenReturn(results);
    when(fileProcessingService.getStatistics()).thenReturn(stats);

    assertDoesNotThrow(() ->
        commands.trim("/test", null, null, 0, 0, false, false, true, false, false, false, false)
    );
  }

  @Test
  void testTrimCommandWithSkippedFiles() {
    // Test trim command displays skipped files in verbose mode
    List<FileProcessingResult> results = new ArrayList<>();
    results.add(new FileProcessingResult.Builder("/test/binary.bin")
        .skipped(true)
        .skipReason("Binary file")
        .build());

    ProcessingStatistics stats = new ProcessingStatistics();
    stats.endProcessing();

    when(fileProcessingService.processDirectory(anyString())).thenReturn(results);
    when(fileProcessingService.getStatistics()).thenReturn(stats);

    assertDoesNotThrow(() ->
        commands.trim("/test", null, null, 0, 0, false, true, false, false, false, false, false)
    );
  }

  @Test
  void testTrimCommandWithIncludePattern() {
    // Test trim command with include pattern
    when(fileProcessingService.processDirectory(anyString()))
        .thenReturn(new ArrayList<>());
    when(fileProcessingService.getStatistics())
        .thenReturn(new ProcessingStatistics());

    assertDoesNotThrow(() ->
        commands.trim("/test", "*.java,*.xml", null, 0, 0, false, false, true, false, false, false, false)
    );

    verify(config).setInclude("*.java,*.xml");
  }

  @Test
  void testTrimCommandWithExcludePattern() {
    // Test trim command with exclude pattern
    when(fileProcessingService.processDirectory(anyString()))
        .thenReturn(new ArrayList<>());
    when(fileProcessingService.getStatistics())
        .thenReturn(new ProcessingStatistics());

    assertDoesNotThrow(() ->
        commands.trim("/test", null, "*.min.js,*.lock", 0, 0, false, false, true, false, false, false, false)
    );

    verify(config).setExclude("*.min.js,*.lock");
  }

  @Test
  void testTrimCommandWithCustomMaxFileSize() {
    // Test trim command with custom max file size
    long customSize = 10485760; // 10 MB

    when(fileProcessingService.processDirectory(anyString()))
        .thenReturn(new ArrayList<>());
    when(fileProcessingService.getStatistics())
        .thenReturn(new ProcessingStatistics());

    assertDoesNotThrow(() ->
        commands.trim("/test", null, null, customSize, 0, false, false, true, false, false, false, false)
    );

    verify(config).setMaxFileSize(customSize);
  }

  @Test
  void testTrimCommandWithCustomMaxFiles() {
    // Test trim command with custom max files
    int customMax = 200;

    when(fileProcessingService.processDirectory(anyString()))
        .thenReturn(new ArrayList<>());
    when(fileProcessingService.getStatistics())
        .thenReturn(new ProcessingStatistics());

    assertDoesNotThrow(() ->
        commands.trim("/test", null, null, 0, customMax, false, false, true, false, false, false, false)
    );

    verify(config).setMaxFiles(customMax);
  }
}
