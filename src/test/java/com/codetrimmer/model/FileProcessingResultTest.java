package com.codetrimmer.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for FileProcessingResult model.
 */
public class FileProcessingResultTest {

  @Test
  @DisplayName("create result with builder")
  public void testBuilderCreation() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.txt").build();

    assertNotNull(result);
    assertEquals("test.txt", result.getFilePath());
  }

  @Test
  @DisplayName("file path accessor")
  public void testGetFilePath() {
    FileProcessingResult result = new FileProcessingResult.Builder("myfile.txt").build();

    assertEquals("myfile.txt", result.getFilePath());
  }

  @Test
  @DisplayName("modified status")
  public void testModifiedStatus() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.txt")
        .modified(true)
        .build();

    assertTrue(result.isModified());
  }

  @Test
  @DisplayName("not modified status")
  public void testNotModifiedStatus() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.txt")
        .modified(false)
        .build();

    assertFalse(result.isModified());
  }

  @Test
  @DisplayName("skipped status")
  public void testSkippedStatus() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.bin")
        .skipped(true)
        .skipReason("Binary file")
        .build();

    assertTrue(result.isSkipped());
  }

  @Test
  @DisplayName("not skipped status")
  public void testNotSkippedStatus() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.txt")
        .skipped(false)
        .build();

    assertFalse(result.isSkipped());
  }

  @Test
  @DisplayName("skip reason")
  public void testSkipReason() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.bin")
        .skipped(true)
        .skipReason("Binary file detected")
        .build();

    assertEquals("Binary file detected", result.getSkipReason());
  }

  @Test
  @DisplayName("lines trimmed count")
  public void testLinesTrimmedCount() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.txt")
        .linesTrimmed(5)
        .build();

    assertEquals(5, result.getLinesTrimmed());
  }

  @Test
  @DisplayName("blank lines removed count")
  public void testBlankLinesRemovedCount() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.txt")
        .blankLinesRemoved(3)
        .build();

    assertEquals(3, result.getBlankLinesRemoved());
  }

  @Test
  @DisplayName("error message")
  public void testErrorMessage() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.txt")
        .error("Permission denied")
        .build();

    assertEquals("Permission denied", result.getErrorMessage());
  }

  @Test
  @DisplayName("has error flag")
  public void testHasError() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.txt")
        .error("File not found")
        .build();

    assertTrue(result.hasError());
  }

  @Test
  @DisplayName("no error flag")
  public void testNoError() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.txt").build();

    assertFalse(result.hasError());
  }

  @Test
  @DisplayName("builder chaining")
  public void testBuilderChaining() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.txt")
        .modified(true)
        .linesTrimmed(10)
        .blankLinesRemoved(5)
        .build();

    assertEquals("test.txt", result.getFilePath());
    assertTrue(result.isModified());
    assertEquals(10, result.getLinesTrimmed());
    assertEquals(5, result.getBlankLinesRemoved());
  }

  @Test
  @DisplayName("complex builder scenario")
  public void testComplexBuilderScenario() {
    FileProcessingResult result = new FileProcessingResult.Builder("document.txt")
        .modified(true)
        .skipped(false)
        .linesTrimmed(15)
        .blankLinesRemoved(8)
        .build();

    assertNotNull(result);
    assertTrue(result.isModified());
    assertFalse(result.isSkipped());
    assertEquals(15, result.getLinesTrimmed());
  }

  @Test
  @DisplayName("toString method")
  public void testToString() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.txt").build();

    String str = result.toString();
    assertNotNull(str);
    assertTrue(str.length() > 0);
  }

  @Test
  @DisplayName("multiple files with different statuses")
  public void testMultipleFileDifferentStatuses() {
    FileProcessingResult result1 = new FileProcessingResult.Builder("file1.txt")
        .modified(true)
        .build();

    FileProcessingResult result2 = new FileProcessingResult.Builder("file2.bin")
        .skipped(true)
        .skipReason("Binary")
        .build();

    assertTrue(result1.isModified());
    assertTrue(result2.isSkipped());
  }

  @Test
  @DisplayName("zero counts")
  public void testZeroCounts() {
    FileProcessingResult result = new FileProcessingResult.Builder("unchanged.txt")
        .linesTrimmed(0)
        .blankLinesRemoved(0)
        .build();

    assertEquals(0, result.getLinesTrimmed());
    assertEquals(0, result.getBlankLinesRemoved());
  }

  @Test
  @DisplayName("large counts")
  public void testLargeCounts() {
    FileProcessingResult result = new FileProcessingResult.Builder("large.txt")
        .linesTrimmed(10000)
        .blankLinesRemoved(5000)
        .build();

    assertEquals(10000, result.getLinesTrimmed());
    assertEquals(5000, result.getBlankLinesRemoved());
  }

  @Test
  @DisplayName("modified with error")
  public void testModifiedWithError() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.txt")
        .modified(true)
        .error("Partial write error")
        .build();

    assertTrue(result.isModified());
    assertTrue(result.hasError());
  }

  @Test
  @DisplayName("skipped with error")
  public void testSkippedWithError() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.bin")
        .skipped(true)
        .skipReason("Binary detected")
        .error("Attempted to process binary")
        .build();

    assertTrue(result.isSkipped());
    assertTrue(result.hasError());
  }

  @Test
  @DisplayName("special characters in filepath")
  public void testSpecialCharactersInFilepath() {
    FileProcessingResult result = new FileProcessingResult.Builder("file@#$%.txt").build();

    assertEquals("file@#$%.txt", result.getFilePath());
  }

  @Test
  @DisplayName("long filepath")
  public void testLongFilepath() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 200; i++) {
      sb.append("a");
    }
    sb.append(".txt");

    FileProcessingResult result = new FileProcessingResult.Builder(sb.toString()).build();

    assertEquals(sb.toString(), result.getFilePath());
  }

  @Test
  @DisplayName("unicode characters in skip reason")
  public void testUnicodeInSkipReason() {
    FileProcessingResult result = new FileProcessingResult.Builder("test.bin")
        .skipped(true)
        .skipReason("File skipped: 文件类型 🚫")
        .build();

    assertNotNull(result.getSkipReason());
    assertTrue(result.getSkipReason().contains("文件"));
  }

  @Test
  @DisplayName("result independence")
  public void testResultIndependence() {
    FileProcessingResult result1 = new FileProcessingResult.Builder("file1.txt")
        .modified(true)
        .linesTrimmed(5)
        .build();

    FileProcessingResult result2 = new FileProcessingResult.Builder("file2.txt")
        .modified(false)
        .linesTrimmed(0)
        .build();

    assertTrue(result1.isModified());
    assertFalse(result2.isModified());
  }
}
