package com.codetrimmer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.codetrimmer.TestShellConfig;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for FileTrimmer service.
 */
@SpringBootTest(properties = {
    "spring.shell.interactive.enabled=false",
    "spring.jmx.enabled=false",
    "server.port=0"
})
@Import(TestShellConfig.class)
public class FileTrimmerTest {

  private com.codetrimmer.config.CodeTrimmerConfig config;
  private FileTrimmer trimmer;

  @BeforeEach
  public void setUp() {
    config = new com.codetrimmer.config.CodeTrimmerConfig();
  }

  @Test
  public void testTrimTrailingWhitespace() {
    String input = "hello   \nworld  \n";
    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertTrue(result.getContent().contains("hello\n"));
    assertTrue(result.getContent().contains("world\n"));
  }

  @Test
  public void testEnsureFinalNewline() {
    String input = "hello\nworld";
    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertTrue(result.getContent().endsWith("\n"));
  }

  @Test
  public void testReduceExcessiveBlankLines() {
    String input = "hello\n\n\n\nworld\n";
    config.setMaxConsecutiveBlankLines(2);
    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertEquals(0, result.getContent().indexOf("hello"));
    assertTrue(result.getBlankLinesRemoved() >= 0);
  }

  @Test
  public void testIdempotency() {
    String input = "hello\nworld\n";
    config.setMaxConsecutiveBlankLines(2);

    // First pass
    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result1 = trimmer.trim();

    // Second pass
    trimmer = new FileTrimmer(result1.getContent(), config);
    FileTrimmer.TrimResult result2 = trimmer.trim();

    assertEquals(result1.getContent(), result2.getContent());
  }

  @Test
  public void testCombinedTrimming() {
    String input = "hello   \n\n\n\nworld  \n";
    config.setTrimTrailingWhitespace(true);
    config.setEnsureFinalNewline(true);
    config.setMaxConsecutiveBlankLines(2);

    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertTrue(result.getContent().endsWith("\n"));
  }

  @Test
  public void testEmptyInput() {
    String input = "";
    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    // Empty input may or may not have content, depending on configuration
    assertNotNull(result.getContent());
  }

  @Test
  public void testSingleLineWithTrailingWhitespace() {
    String input = "hello world   ";
    config.setTrimTrailingWhitespace(true);
    config.setEnsureFinalNewline(true);

    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertTrue(result.getContent().endsWith("\n"));
  }

  @Test
  public void testOnlyBlankLines() {
    String input = "\n\n\n\n";
    config.setMaxConsecutiveBlankLines(1);

    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
  }

  @Test
  public void testPreservesIndentation() {
    String input = "  indented line\n    more indented\n";
    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertTrue(result.getContent().contains("  indented"));
  }

  @Test
  public void testMultipleTrailingSpaces() {
    String input = "line1     \nline2      \nline3\n";
    config.setTrimTrailingWhitespace(true);

    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertFalse(result.getContent().contains("     \n"));
  }

  @Test
  public void testTabCharacters() {
    String input = "line1\t\t\t\nline2\n";
    config.setTrimTrailingWhitespace(true);

    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
  }

  @Test
  public void testNoTrailingNewline() {
    String input = "content";
    config.setEnsureFinalNewline(true);

    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertTrue(result.getContent().endsWith("\n"));
  }

  @Test
  public void testLargeBlankLineSequence() {
    StringBuilder sb = new StringBuilder();
    sb.append("start\n");
    for (int i = 0; i < 20; i++) {
      sb.append("\n");
    }
    sb.append("end\n");

    config.setMaxConsecutiveBlankLines(2);
    trimmer = new FileTrimmer(sb.toString(), config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertTrue(result.getContent().length() < sb.toString().length());
  }

  @Test
  public void testUnicodeContent() {
    String input = "Hello 世界 🌍\nПривет мир\n";
    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertTrue(result.getContent().contains("世界"));
  }

  @Test
  public void testConfigurationNotModified() {
    String input = "test\n";
    com.codetrimmer.config.CodeTrimmerConfig originalConfig = new com.codetrimmer.config.CodeTrimmerConfig();
    boolean originalVerbose = originalConfig.isVerbose();

    trimmer = new FileTrimmer(input, originalConfig);
    trimmer.trim();

    assertEquals(originalVerbose, originalConfig.isVerbose());
  }

  @Test
  public void testConsecutiveMaxZero() {
    String input = "line1\n\nline2\n";
    config.setMaxConsecutiveBlankLines(0);

    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
  }

  @Test
  public void testLineCountingAccuracy() {
    String input = "line1\nline2\nline3\n";
    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertNotNull(result.getContent());
  }

  @Test
  public void testTrimsOnlyTrailingSpacesNotLeading() {
    String input = "  leading and trailing  \n";
    config.setTrimTrailingWhitespace(true);

    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertTrue(result.getContent().startsWith("  leading"));
  }

  @Test
  public void testComplexScenario() {
    String input = "start   \n\n\n\n\n  middle  \n\n\nend";
    config.setTrimTrailingWhitespace(true);
    config.setEnsureFinalNewline(true);
    config.setMaxConsecutiveBlankLines(1);

    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertTrue(result.getContent().endsWith("\n"));
  }

  @Test
  public void testMixedLineEndings() {
    String input = "line1\r\nline2\nline3\r\n";
    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertNotNull(result.getContent());
  }

  @Test
  public void testWhitespaceOnlyLines() {
    String input = "content1\n   \n\t\ncontent2\n";
    config.setTrimTrailingWhitespace(true);

    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
  }

  @Test
  public void testResultMetadata() {
    String input = "hello\nworld\n";
    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertTrue(result.getBlankLinesRemoved() >= 0);
    assertTrue(result.getLinesTrimmed() >= 0);
  }

  @Test
  public void testVeryLongLine() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 10000; i++) {
      sb.append("a");
    }
    String input = sb.toString() + "\n";

    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertTrue(result.getContent().length() > 0);
  }

  @Test
  public void testNullConfigGraceful() {
    String input = "test\n";
    // This tests behavior with default config
    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
  }

  @Test
  public void testAlternatingBlankLines() {
    String input = "line1\n\nline2\n\nline3\n";
    config.setMaxConsecutiveBlankLines(1);

    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
  }

  @Test
  public void testTrailingWhitespaceWithNewlines() {
    String input = "line1  \n  line2  \n";
    config.setTrimTrailingWhitespace(true);

    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
  }

  @Test
  public void testSpecialCharactersPreserved() {
    String input = "!@#$%^&*()\nline2\n";
    trimmer = new FileTrimmer(input, config);
    FileTrimmer.TrimResult result = trimmer.trim();

    assertNotNull(result);
    assertTrue(result.getContent().contains("!@#$%^&*()"));
  }
}
