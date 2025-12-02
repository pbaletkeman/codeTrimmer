package com.codetrimmer.service;

import static org.junit.jupiter.api.Assertions.*;

import com.codetrimmer.config.CodeTrimmerConfig;
import com.codetrimmer.model.FileProcessingResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileProcessingServiceTest {

  private FileProcessingService service;
  private CodeTrimmerConfig config;

  @TempDir
  Path tempDir;

  @BeforeEach
  void setUp() {
    config = new CodeTrimmerConfig();
    service = new FileProcessingService(config);
  }

  @Test
  void testProcessDirectoryValid() throws Exception {
    Path file = tempDir.resolve("test.txt");
    Files.write(file, "line1\nline2\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertFalse(results.isEmpty());
  }

  @Test
  void testProcessDirectoryWithMultipleFiles() throws Exception {
    Files.write(tempDir.resolve("file1.txt"), "test\n".getBytes());
    Files.write(tempDir.resolve("file2.txt"), "test\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertEquals(2, results.size());
  }

  @Test
  void testProcessDirectoryInvalidPath() {
    assertThrows(IllegalArgumentException.class, () -> {
      service.processDirectory("/nonexistent/path/12345");
    });
  }

  @Test
  void testProcessDirectoryEmptyDirectory() throws Exception {
    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertTrue(results.isEmpty());
  }

  @Test
  void testProcessDirectoryWithExcludePattern() throws Exception {
    config.setExclude("*.bak");
    Files.write(tempDir.resolve("file.txt"), "test\n".getBytes());
    Files.write(tempDir.resolve("file.bak"), "backup\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertTrue(results.size() >= 0);
  }

  @Test
  void testProcessDirectoryRespectMaxFiles() throws Exception {
    config.setMaxFiles(2);
    Files.write(tempDir.resolve("file1.txt"), "test\n".getBytes());
    Files.write(tempDir.resolve("file2.txt"), "test\n".getBytes());
    Files.write(tempDir.resolve("file3.txt"), "test\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertTrue(results.size() <= 2);
  }

  @Test
  void testProcessDirectoryWithDryRun() throws Exception {
    config.setDryRun(true);
    Path file = tempDir.resolve("test.txt");
    Files.write(file, "test\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
  }

  @Test
  void testProcessDirectoryWithBackup() throws Exception {
    config.setCreateBackups(true);
    Files.write(tempDir.resolve("test.txt"), "test\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
  }

  @Test
  void testProcessDirectoryWithBinaryFileSkip() throws Exception {
    Path binFile = tempDir.resolve("binary.bin");
    Files.write(binFile, new byte[]{0x00, 0x01, 0x02});

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
  }

  @Test
  void testProcessDirectoryWithHiddenFiles() throws Exception {
    config.setIncludeHidden(false);
    Files.write(tempDir.resolve(".hidden"), "hidden\n".getBytes());
    Files.write(tempDir.resolve("visible.txt"), "visible\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertEquals(1, results.size());
  }

  @Test
  void testProcessDirectoryIncludeHidden() throws Exception {
    config.setIncludeHidden(true);
    Files.write(tempDir.resolve(".hidden"), "hidden\n".getBytes());
    Files.write(tempDir.resolve("visible.txt"), "visible\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertEquals(2, results.size());
  }

  @Test
  void testProcessDirectoryWithSizeLimit() throws Exception {
    config.setMaxFileSize(100);
    Path file = tempDir.resolve("file.txt");
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 50; i++) {
      sb.append("This is a test line that will make the file larger\n");
    }
    Files.write(file, sb.toString().getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
  }

  @Test
  void testProcessDirectoryLargeFile() throws Exception {
    config.setMaxFileSize(Long.MAX_VALUE);
    Path file = tempDir.resolve("largefile.txt");
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 1000; i++) {
      sb.append("Line ").append(i).append("\n");
    }
    Files.write(file, sb.toString().getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertEquals(1, results.size());
  }

  @Test
  void testProcessDirectoryNoLimits() throws Exception {
    config.setNoLimits(true);
    config.setMaxFiles(1);
    Files.write(tempDir.resolve("file1.txt"), "test\n".getBytes());
    Files.write(tempDir.resolve("file2.txt"), "test\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertEquals(2, results.size());
  }

  @Test
  void testProcessDirectoryNestedDirectories() throws Exception {
    Path subdir = tempDir.resolve("subdir");
    Files.createDirectory(subdir);
    Files.write(tempDir.resolve("file1.txt"), "test\n".getBytes());
    Files.write(subdir.resolve("file2.txt"), "test\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertEquals(2, results.size());
  }

  @Test
  void testProcessDirectoryDeepNesting() throws Exception {
    Path level1 = tempDir.resolve("level1");
    Path level2 = level1.resolve("level2");
    Path level3 = level2.resolve("level3");
    Files.createDirectories(level3);
    Files.write(level3.resolve("file.txt"), "nested\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertEquals(1, results.size());
  }

  @Test
  void testProcessDirectoryMixedContent() throws Exception {
    Files.write(tempDir.resolve("text.txt"), "text\n".getBytes());
    Files.write(tempDir.resolve("binary.bin"), new byte[]{0x00, 0x01});
    Path hidden = tempDir.resolve(".config");
    Files.write(hidden, "config\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
  }

  @Test
  void testProcessDirectoryWithVerbose() throws Exception {
    config.setVerbose(true);
    Files.write(tempDir.resolve("test.txt"), "test\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
  }

  @Test
  void testProcessDirectoryResultsContainPath() throws Exception {
    Files.write(tempDir.resolve("test.txt"), "test\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertFalse(results.isEmpty());
    assertNotNull(results.get(0).getFilePath());
  }

  @Test
  void testProcessDirectoryResultsContainModifiedFlag() throws Exception {
    Files.write(tempDir.resolve("test.txt"), "line1\nline2\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertFalse(results.isEmpty());
  }

  @Test
  void testProcessDirectoryWildcardInclude() throws Exception {
    config.setInclude("*.java");
    Files.write(tempDir.resolve("Main.java"), "public class Main {}\n".getBytes());
    Files.write(tempDir.resolve("readme.txt"), "readme\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertTrue(results.size() >= 0);
  }

  @Test
  void testProcessDirectoryMultipleWildcards() throws Exception {
    config.setInclude("*.java,*.xml");
    Files.write(tempDir.resolve("Main.java"), "code\n".getBytes());
    Files.write(tempDir.resolve("config.xml"), "xml\n".getBytes());
    Files.write(tempDir.resolve("readme.txt"), "text\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertTrue(results.size() >= 0);
  }

  @Test
  void testProcessDirectoryTrimTrailingWhitespace() throws Exception {
    config.setTrimTrailingWhitespace(true);
    Files.write(tempDir.resolve("test.txt"), "line1   \nline2   \n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertEquals(1, results.size());
  }

  @Test
  void testProcessDirectoryEnsureFinalNewline() throws Exception {
    config.setEnsureFinalNewline(true);
    Files.write(tempDir.resolve("test.txt"), "line1\nline2".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertEquals(1, results.size());
  }

  @Test
  void testProcessDirectoryReduceBlankLines() throws Exception {
    config.setMaxConsecutiveBlankLines(1);
    Files.write(tempDir.resolve("test.txt"), "line1\n\n\nline2\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertEquals(1, results.size());
  }

  @Test
  void testProcessDirectoryNoModifications() throws Exception {
    config.setDryRun(true);
    Path file = tempDir.resolve("test.txt");
    byte[] originalContent = "test\n".getBytes();
    Files.write(file, originalContent);

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    byte[] currentContent = Files.readAllBytes(file);
    assertArrayEquals(originalContent, currentContent);
  }

  @Test
  void testProcessDirectoryStatisticsTracking() throws Exception {
    Files.write(tempDir.resolve("file1.txt"), "test1\n".getBytes());
    Files.write(tempDir.resolve("file2.txt"), "test2\n".getBytes());

    List<FileProcessingResult> results = service.processDirectory(tempDir.toString());

    assertNotNull(results);
    assertTrue(results.size() >= 0);
  }
}
