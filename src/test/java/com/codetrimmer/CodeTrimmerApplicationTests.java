// ...existing code...
package com.codetrimmer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.codetrimmer.config.CodeTrimmerConfig;
import com.codetrimmer.TestShellConfig;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Code Trimmer application.
 * Tests configuration and Spring context loading with interactive shell disabled.
 */
@SpringBootTest(properties = {
  "spring.shell.interactive.enabled=false",
  "spring.jmx.enabled=false",
  "server.port=0",
  "spring.main.allow-bean-definition-overriding=true"
})
@Import(TestShellConfig.class)
@DisplayName("Code Trimmer Integration Tests")
public class CodeTrimmerApplicationTests {

  @Autowired
  private CodeTrimmerConfig config;

  @Test
  @DisplayName("Application context loads successfully")
  void contextLoads() {
    assertNotNull(config, "Configuration should be autowired");
  }

  @Test
  @DisplayName("Configuration bean loads successfully")
  void configurationBeanLoads() {
    assertNotNull(config, "CodeTrimmerConfig bean should be loaded");
  }

  @Test
  @DisplayName("Config has correct default max file size")
  void configMaxFileSizeDefault() {
    assertEquals(5242880, config.getMaxFileSize(), "Default max file size should be 5MB");
  }

  @Test
  @DisplayName("Config has correct default max files")
  void configMaxFilesDefault() {
    assertEquals(50, config.getMaxFiles(), "Default max files should be 50");
  }

  @Test
  @DisplayName("Config trimTrailingWhitespace is enabled by default")
  void configTrimTrailingWhitespaceDefault() {
    assertTrue(config.isTrimTrailingWhitespace(), "Trim trailing whitespace should be enabled by default");
  }

  @Test
  @DisplayName("Config ensureFinalNewline is enabled by default")
  void configEnsureFinalNewlineDefault() {
    assertTrue(config.isEnsureFinalNewline(), "Ensure final newline should be enabled by default");
  }

  @Test
  @DisplayName("Config maxConsecutiveBlankLines has correct default")
  void configMaxConsecutiveBlankLinesDefault() {
    assertEquals(2, config.getMaxConsecutiveBlankLines(), "Max consecutive blank lines should be 2");
  }

  @Test
  @DisplayName("Config createBackups is enabled by default")
  void configCreateBackupsDefault() {
    assertTrue(config.isCreateBackups(), "Create backups should be enabled by default");
  }

  @Test
  @DisplayName("Config can modify max file size")
  void configModifyMaxFileSize() {
    long originalSize = config.getMaxFileSize();
    config.setMaxFileSize(10485760L);
    assertEquals(10485760L, config.getMaxFileSize(), "Max file size should be updated");
    config.setMaxFileSize(originalSize); // Reset
  }

  @Test
  @DisplayName("Config can modify max files")
  void configModifyMaxFiles() {
    int originalMax = config.getMaxFiles();
    config.setMaxFiles(100);
    assertEquals(100, config.getMaxFiles(), "Max files should be updated");
    config.setMaxFiles(originalMax); // Reset
  }

  @Test
  @DisplayName("Config can toggle dry run")
  void configToggleDryRun() {
    boolean original = config.isDryRun();
    config.setDryRun(true);
    assertTrue(config.isDryRun(), "Dry run should be enabled");
    config.setDryRun(false);
    assertFalse(config.isDryRun(), "Dry run should be disabled");
    config.setDryRun(original); // Reset
  }

  @Test
  @DisplayName("Config can toggle verbose")
  void configToggleVerbose() {
    boolean original = config.isVerbose();
    config.setVerbose(true);
    assertTrue(config.isVerbose(), "Verbose should be enabled");
    config.setVerbose(false);
    assertFalse(config.isVerbose(), "Verbose should be disabled");
    config.setVerbose(original); // Reset
  }

  @Test
  @DisplayName("Config can toggle quiet")
  void configToggleQuiet() {
    boolean original = config.isQuiet();
    config.setQuiet(true);
    assertTrue(config.isQuiet(), "Quiet should be enabled");
    config.setQuiet(false);
    assertFalse(config.isQuiet(), "Quiet should be disabled");
    config.setQuiet(original); // Reset
  }

  @Test
  @DisplayName("Config can toggle noColor")
  void configToggleNoColor() {
    boolean original = config.isNoColor();
    config.setNoColor(true);
    assertTrue(config.isNoColor(), "No color should be enabled");
    config.setNoColor(false);
    assertFalse(config.isNoColor(), "No color should be disabled");
    config.setNoColor(original); // Reset
  }

  @Test
  @DisplayName("Config can toggle includeHidden")
  void configToggleIncludeHidden() {
    boolean original = config.isIncludeHidden();
    config.setIncludeHidden(true);
    assertTrue(config.isIncludeHidden(), "Include hidden should be enabled");
    config.setIncludeHidden(false);
    assertFalse(config.isIncludeHidden(), "Include hidden should be disabled");
    config.setIncludeHidden(original); // Reset
  }

  @Test
  @DisplayName("Config can toggle followSymlinks")
  void configToggleFollowSymlinks() {
    boolean original = config.isFollowSymlinks();
    config.setFollowSymlinks(true);
    assertTrue(config.isFollowSymlinks(), "Follow symlinks should be enabled");
    config.setFollowSymlinks(false);
    assertFalse(config.isFollowSymlinks(), "Follow symlinks should be disabled");
    config.setFollowSymlinks(original); // Reset
  }

  @Test
  @DisplayName("Config can toggle failFast")
  void configToggleFailFast() {
    boolean original = config.isFailFast();
    config.setFailFast(true);
    assertTrue(config.isFailFast(), "Fail fast should be enabled");
    config.setFailFast(false);
    assertFalse(config.isFailFast(), "Fail fast should be disabled");
    config.setFailFast(original); // Reset
  }

  @Test
  @DisplayName("Config can toggle noLimits")
  void configToggleNoLimits() {
    boolean original = config.isNoLimits();
    config.setNoLimits(true);
    assertTrue(config.isNoLimits(), "No limits should be enabled");
    config.setNoLimits(false);
    assertFalse(config.isNoLimits(), "No limits should be disabled");
    config.setNoLimits(original); // Reset
  }

  @Test
  @DisplayName("Config can set include filter")
  void configSetInclude() {
    String original = config.getInclude();
    config.setInclude("java,py,js");
    assertEquals("java,py,js", config.getInclude(), "Include filter should be set");
    config.setInclude(original); // Reset
  }

  @Test
  @DisplayName("Config can set exclude filter")
  void configSetExclude() {
    String original = config.getExclude();
    config.setExclude("min.js,test.py");
    assertEquals("min.js,test.py", config.getExclude(), "Exclude filter should be set");
    config.setExclude(original); // Reset
  }

  @Test
  @DisplayName("Config can set trim trailing whitespace")
  void configSetTrimTrailingWhitespace() {
    boolean original = config.isTrimTrailingWhitespace();
    config.setTrimTrailingWhitespace(false);
    assertFalse(config.isTrimTrailingWhitespace(), "Trim trailing whitespace should be disabled");
    config.setTrimTrailingWhitespace(original); // Reset
  }

  @Test
  @DisplayName("Config can set ensure final newline")
  void configSetEnsureFinalNewline() {
    boolean original = config.isEnsureFinalNewline();
    config.setEnsureFinalNewline(false);
    assertFalse(config.isEnsureFinalNewline(), "Ensure final newline should be disabled");
    config.setEnsureFinalNewline(original); // Reset
  }

  @Test
  @DisplayName("Config can set max consecutive blank lines")
  void configSetMaxConsecutiveBlankLines() {
    int original = config.getMaxConsecutiveBlankLines();
    config.setMaxConsecutiveBlankLines(5);
    assertEquals(5, config.getMaxConsecutiveBlankLines(), "Max consecutive blank lines should be updated");
    config.setMaxConsecutiveBlankLines(original); // Reset
  }

  @Test
  @DisplayName("Config can set create backups")
  void configSetCreateBackups() {
    boolean original = config.isCreateBackups();
    config.setCreateBackups(false);
    assertFalse(config.isCreateBackups(), "Create backups should be disabled");
    config.setCreateBackups(original); // Reset
  }

  @Test
  @DisplayName("All config properties are accessible")
  void allConfigPropertiesAccessible() {
    assertNotNull(config.getInclude());
    assertNotNull(config.getExclude());
    assertTrue(config.getMaxFileSize() > 0);
    assertTrue(config.getMaxFiles() > 0);
    assertTrue(config.getMaxConsecutiveBlankLines() >= 0);
  }

  @Test
  @DisplayName("Config can handle multiple property changes")
  void configMultiplePropertyChanges() {
    // Store originals
    long originalMaxSize = config.getMaxFileSize();
    int originalMaxFiles = config.getMaxFiles();
    boolean originalDryRun = config.isDryRun();

    // Change multiple properties
    config.setMaxFileSize(20971520L);
    config.setMaxFiles(200);
    config.setDryRun(true);

    // Verify all changes
    assertEquals(20971520L, config.getMaxFileSize());
    assertEquals(200, config.getMaxFiles());
    assertTrue(config.isDryRun());

    // Reset
    config.setMaxFileSize(originalMaxSize);
    config.setMaxFiles(originalMaxFiles);
    config.setDryRun(originalDryRun);
  }

  @Test
  @DisplayName("Config boolean flags are independent")
  void configBooleanFlagsIndependent() {
    // Store originals
    boolean[] originals = {
      config.isDryRun(),
      config.isVerbose(),
      config.isQuiet(),
      config.isNoColor(),
      config.isIncludeHidden(),
      config.isFollowSymlinks(),
      config.isFailFast(),
      config.isNoLimits()
    };

    // Set each flag and verify others are not affected
    config.setDryRun(true);
    assertTrue(config.isDryRun());
    assertEquals(originals[1], config.isVerbose());
    assertEquals(originals[2], config.isQuiet());

    // Reset
    config.setDryRun(originals[0]);
    config.setVerbose(originals[1]);
    config.setQuiet(originals[2]);
    config.setNoColor(originals[3]);
    config.setIncludeHidden(originals[4]);
    config.setFollowSymlinks(originals[5]);
    config.setFailFast(originals[6]);
    config.setNoLimits(originals[7]);
  }
}
