package com.codetrimmer.service;

import com.codetrimmer.config.CodeTrimmerConfig;
import com.codetrimmer.model.BinaryFileDetector;
import com.codetrimmer.model.FileProcessingResult;
import com.codetrimmer.model.ProcessingStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service for processing files and applying whitespace cleanup rules.
 */
@Service
public class FileProcessingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessingService.class);

  private final CodeTrimmerConfig config;
  private final ProcessingStatistics statistics;

  public FileProcessingService(CodeTrimmerConfig config) {
    this.config = config;
    this.statistics = new ProcessingStatistics();
  }

  /**
   * Processes all files in a directory recursively.
   *
   * @param directory the directory to process
   * @return list of processing results
   */
  public List<FileProcessingResult> processDirectory(String directory) {
    List<FileProcessingResult> results = new ArrayList<>();
    Path dirPath = Paths.get(directory);

    try {
      if (!Files.isDirectory(dirPath)) {
        throw new IllegalArgumentException("Path is not a directory: " + directory);
      }

      List<Path> files = getFilesForProcessing(dirPath);
      results.addAll(processFiles(files));

    } catch (IOException e) {
      LOGGER.error("Error processing directory: " + directory, e);
    }

    statistics.endProcessing();
    return results;
  }

  /**
   * Retrieves all files matching filter criteria from the directory tree.
   *
   * @param dirPath the root directory
   * @return list of paths to process
   */
  private List<Path> getFilesForProcessing(Path dirPath) throws IOException {
    List<Path> files = new ArrayList<>();

    try (Stream<Path> walkStream = Files.walk(dirPath)) {
      walkStream
          .filter(Files::isRegularFile)
          .filter(this::shouldProcessFile)
          .limit(config.isNoLimits() ? Long.MAX_VALUE : config.getMaxFiles())
          .collect(Collectors.toCollection(() -> files));
    }

    return files;
  }

  /**
   * Determines if a file should be processed based on filter criteria.
   *
   * @param path the file path
   * @return true if the file should be processed
   */
  private boolean shouldProcessFile(Path path) {
    try {
      String filename = path.getFileName().toString();

      // Check hidden files
      if (!config.isIncludeHidden() && filename.startsWith(".")) {
        return false;
      }

      // Check symbolic links
      if (!config.isFollowSymlinks() && Files.isSymbolicLink(path)) {
        return false;
      }

      // Check file size
      if (!config.isNoLimits()) {
        long size = Files.size(path);
        if (size > config.getMaxFileSize()) {
          return false;
        }
      }

      // Check binary files
      if (BinaryFileDetector.isBinaryByExtension(path) || BinaryFileDetector.isBinary(path)) {
        return false;
      }

      // Check extension filters
      return matchesExtensionFilter(filename);

    } catch (IOException e) {
      LOGGER.warn("Error checking file: " + path, e);
      return false;
    }
  }

  /**
   * Checks if a file extension matches the include/exclude filters.
   *
   * @param filename the filename to check
   * @return true if the file should be processed
   */
  private boolean matchesExtensionFilter(String filename) {
    String[] includeList = parseFilterList(config.getInclude());
    String[] excludeList = parseFilterList(config.getExclude());

    // Check if any exclude pattern matches
    for (String exclude : excludeList) {
      if (matches(filename, exclude)) {
        return false;
      }
    }

    // Check if include is "*" (all files)
    if (includeList.length == 1 && "*".equals(includeList[0])) {
      return true;
    }

    // Check if any include pattern matches
    for (String include : includeList) {
      if (matches(filename, include)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if a filename matches a filter pattern.
   *
   * @param filename the filename to check
   * @param pattern the pattern (e.g., "js", "*.js", "test-*.js")
   * @return true if the filename matches
   */
  private boolean matches(String filename, String pattern) {
    if ("*".equals(pattern)) {
      return true;
    }

    filename = filename.toLowerCase();
    pattern = pattern.toLowerCase();

    if (!pattern.startsWith(".") && filename.contains(".")) {
      // Pattern is an extension without dot, match extension
      return filename.endsWith("." + pattern);
    } else if (pattern.startsWith(".")) {
      // Pattern includes dot
      return filename.endsWith(pattern);
    } else {
      // Direct match
      return filename.equals(pattern);
    }
  }

  /**
   * Processes a list of files.
   *
   * @param files the files to process
   * @return list of processing results
   */
  private List<FileProcessingResult> processFiles(List<Path> files) {
    List<FileProcessingResult> results = new ArrayList<>();

    for (Path file : files) {
      try {
        statistics.incrementFilesScanned();

        // Check permissions
        if (!Files.isReadable(file)) {
          results.add(new FileProcessingResult.Builder(file.toString())
              .skipped(true)
              .skipReason("No read permission")
              .build());
          statistics.incrementFilesSkipped();
          continue;
        }

        if (!Files.isWritable(file)) {
          results.add(new FileProcessingResult.Builder(file.toString())
              .skipped(true)
              .skipReason("No write permission")
              .build());
          statistics.incrementFilesSkipped();
          continue;
        }

        results.add(processFile(file));

      } catch (Exception e) {
        LOGGER.error("Error processing file: " + file, e);
        results.add(new FileProcessingResult.Builder(file.toString())
            .skipped(true)
            .skipReason("Error: " + e.getMessage())
            .error(e.getMessage())
            .build());
        statistics.incrementFilesSkipped();

        if (config.isFailFast()) {
          break;
        }
      }
    }

    return results;
  }

  /**
   * Processes a single file.
   *
   * @param path the file to process
   * @return processing result
   */
  private FileProcessingResult processFile(Path path) throws IOException {
    String originalContent = Files.readString(path, StandardCharsets.UTF_8);

    // Apply trimming rules
    FileTrimmer trimmer = new FileTrimmer(originalContent, config);
    FileTrimmer.TrimResult trimResult = trimmer.trim();

    // Check if content changed
    if (trimResult.getContent().equals(originalContent)) {
      return new FileProcessingResult.Builder(path.toString())
          .modified(false)
          .build();
    }

    // If dry-run mode, just report what would change
    if (config.isDryRun()) {
      return new FileProcessingResult.Builder(path.toString())
          .modified(true)
          .linesTrimmed(trimResult.getLinesTrimmed())
          .blankLinesRemoved(trimResult.getBlankLinesRemoved())
          .build();
    }

    // Create backup
    if (config.isCreateBackups()) {
      Path backupPath = Paths.get(path.toString() + ".bak");
      Files.writeString(backupPath, originalContent, StandardCharsets.UTF_8);
    }

    // Write modified content
    try {
      Files.writeString(path, trimResult.getContent(), StandardCharsets.UTF_8);
      statistics.incrementFilesModified();
      statistics.addLinesTrimmed(trimResult.getLinesTrimmed());
      statistics.addBlankLinesRemoved(trimResult.getBlankLinesRemoved());

      return new FileProcessingResult.Builder(path.toString())
          .modified(true)
          .linesTrimmed(trimResult.getLinesTrimmed())
          .blankLinesRemoved(trimResult.getBlankLinesRemoved())
          .build();

    } catch (IOException e) {
      // Try to restore from backup
      if (config.isCreateBackups()) {
        Path backupPath = Paths.get(path.toString() + ".bak");
        if (Files.exists(backupPath)) {
          Files.copy(backupPath, path, StandardCopyOption.REPLACE_EXISTING);
          Files.delete(backupPath);
        }
      }
      throw e;
    }
  }

  /**
   * Parses a comma-separated filter list.
   *
   * @param filterString the filter string
   * @return array of filter items
   */
  private String[] parseFilterList(String filterString) {
    if (filterString == null || filterString.trim().isEmpty()) {
      return new String[]{};
    }
    return Arrays.stream(filterString.split(","))
        .map(String::trim)
        .toArray(String[]::new);
  }

  public ProcessingStatistics getStatistics() {
    return statistics;
  }
}
