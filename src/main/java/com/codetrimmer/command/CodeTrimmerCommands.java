package com.codetrimmer.command;

import com.codetrimmer.options.TrimOptions;
import java.util.HashSet;
import java.util.Set;

/**
 * Holder for command-line arguments and configuration options for the CodeTrimmer application.
 * Provides methods to configure trimming options, file paths, and file type filters.
 */
public class CodeTrimmerCommands {
  private String inputPath;
  private String outputPath;
  private boolean verbose = false;
  private boolean dryRun = false;
  private boolean trimComments = false;
  private boolean trimWhitespace = false;
  private boolean trimBlankLines = false;
  private long maxFileSize = Long.MAX_VALUE;
  private Set<String> includedFileTypes = new HashSet<>();
  private Set<String> excludedFileTypes = new HashSet<>();

  /**
   * Sets the input path for source files to be processed.
   *
   * @param inputPath the path to input files
   */
  public void setInputPath(String inputPath) {
    this.inputPath = inputPath;
  }

  /**
   * Gets the input path for source files.
   *
   * @return the input path
   */
  public String getInputPath() {
    return this.inputPath;
  }

  /**
   * Sets the output path where processed files will be written.
   *
   * @param outputPath the path where output files will be written
   */
  public void setOutputPath(String outputPath) {
    this.outputPath = outputPath;
  }

  /**
   * Gets the output path for processed files.
   *
   * @return the output path
   */
  public String getOutputPath() {
    return this.outputPath;
  }

  /**
   * Sets the verbose flag for detailed logging output.
   *
   * @param verbose true to enable verbose output
   */
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  /**
   * Checks if verbose output is enabled.
   *
   * @return true if verbose output is enabled
   */
  public boolean isVerbose() {
    return this.verbose;
  }

  /**
   * Sets the dry-run flag to simulate operations without modifying files.
   *
   * @param dryRun true to enable dry-run mode
   */
  public void setDryRun(boolean dryRun) {
    this.dryRun = dryRun;
  }

  /**
   * Checks if dry-run mode is enabled.
   *
   * @return true if dry-run mode is enabled
   */
  public boolean isDryRun() {
    return this.dryRun;
  }

  /**
   * Sets whether comments should be trimmed from code.
   *
   * @param trimComments true to trim comments
   */
  public void setTrimComments(boolean trimComments) {
    this.trimComments = trimComments;
  }

  /**
   * Checks if comments should be trimmed.
   *
   * @return true if comments should be trimmed
   */
  public boolean isTrimComments() {
    return this.trimComments;
  }

  /**
   * Sets whether whitespace should be trimmed from code.
   *
   * @param trimWhitespace true to trim whitespace
   */
  public void setTrimWhitespace(boolean trimWhitespace) {
    this.trimWhitespace = trimWhitespace;
  }

  /**
   * Checks if whitespace should be trimmed.
   *
   * @return true if whitespace should be trimmed
   */
  public boolean isTrimWhitespace() {
    return this.trimWhitespace;
  }

  /**
   * Sets whether blank lines should be trimmed from code.
   *
   * @param trimBlankLines true to trim blank lines
   */
  public void setTrimBlankLines(boolean trimBlankLines) {
    this.trimBlankLines = trimBlankLines;
  }

  /**
   * Checks if blank lines should be trimmed.
   *
   * @return true if blank lines should be trimmed
   */
  public boolean isTrimBlankLines() {
    return this.trimBlankLines;
  }

  /**
   * Sets the maximum file size (in bytes) to process.
   *
   * @param maxFileSize the maximum file size in bytes
   */
  public void setMaxFileSize(long maxFileSize) {
    this.maxFileSize = maxFileSize;
  }

  /**
   * Gets the maximum file size (in bytes) to process.
   *
   * @return the maximum file size in bytes
   */
  public long getMaxFileSize() {
    return this.maxFileSize;
  }

  /**
   * Includes a file type in the processing list.
   *
   * @param fileType the file extension to include (e.g., "java", "cpp")
   */
  public void includeFileType(String fileType) {
    this.includedFileTypes.add(fileType);
  }

  /**
   * Checks if a file type is included in the processing list.
   *
   * @param fileType the file extension to check
   * @return true if the file type is included
   */
  public boolean isFileTypeIncluded(String fileType) {
    return this.includedFileTypes.contains(fileType);
  }

  /**
   * Excludes a file type from the processing list.
   *
   * @param fileType the file extension to exclude (e.g., "bin", "exe")
   */
  public void excludeFileType(String fileType) {
    this.excludedFileTypes.add(fileType);
  }

  /**
   * Checks if a file type is excluded from the processing list.
   *
   * @param fileType the file extension to check
   * @return true if the file type is excluded
   */
  public boolean isFileTypeExcluded(String fileType) {
    return this.excludedFileTypes.contains(fileType);
  }

  /**
   * Converts this CodeTrimmerCommands object to a TrimOptions object.
   *
   * @return a new TrimOptions instance with the same configuration
   */
  public TrimOptions toTrimOptions() {
    TrimOptions options = new TrimOptions();
    options.setInputDirectory(this.inputPath);
    options.setOutputDirectory(this.outputPath);
    options.setVerbose(this.verbose);
    options.setDryRun(this.dryRun);
    options.setTrimComments(this.trimComments);
    options.setTrimWhitespace(this.trimWhitespace);
    options.setTrimBlankLines(this.trimBlankLines);
    options.setMaxFileSize(this.maxFileSize);
    for (String fileType : this.includedFileTypes) {
      options.includeFileType(fileType);
    }
    for (String fileType : this.excludedFileTypes) {
      options.excludeFileType(fileType);
    }
    return options;
  }
}
