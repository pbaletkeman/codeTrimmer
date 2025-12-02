package com.codetrimmer.options;

import java.util.HashSet;
import java.util.Set;

/** Configuration options for the code trimmer. */
public class TrimOptions {

  private String inputDirectory;
  private String outputDirectory;
  private boolean trimComments;
  private boolean trimWhitespace;
  private boolean trimBlankLines;
  private boolean verbose;
  private boolean dryRun;
  private long maxFileSize;
  private final Set<String> includedFileTypes;
  private final Set<String> excludedFileTypes;

  /** Constructs a new TrimOptions with default settings. */
  public TrimOptions() {
    this.includedFileTypes = new HashSet<>();
    this.excludedFileTypes = new HashSet<>();
    this.maxFileSize = Long.MAX_VALUE;
  }

  // Path methods

  /**
   * Sets the input directory path.
   *
   * @param inputDirectory the input directory path
   */
  public void setInputDirectory(String inputDirectory) {
    this.inputDirectory = inputDirectory;
  }

  /**
   * Gets the input directory path.
   *
   * @return the input directory path
   */
  public String getInputDirectory() {
    return inputDirectory;
  }

  /**
   * Sets the output directory path.
   *
   * @param outputDirectory the output directory path
   */
  public void setOutputDirectory(String outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  /**
   * Gets the output directory path.
   *
   * @return the output directory path
   */
  public String getOutputDirectory() {
    return outputDirectory;
  }

  // Trim options

  /**
   * Sets whether to trim comments.
   *
   * @param trimComments true to enable comment trimming
   */
  public void setTrimComments(boolean trimComments) {
    this.trimComments = trimComments;
  }

  /**
   * Gets whether comment trimming is enabled.
   *
   * @return true if comment trimming is enabled
   */
  public boolean isTrimComments() {
    return trimComments;
  }

  /**
   * Sets whether to trim whitespace.
   *
   * @param trimWhitespace true to enable whitespace trimming
   */
  public void setTrimWhitespace(boolean trimWhitespace) {
    this.trimWhitespace = trimWhitespace;
  }

  /**
   * Gets whether whitespace trimming is enabled.
   *
   * @return true if whitespace trimming is enabled
   */
  public boolean isTrimWhitespace() {
    return trimWhitespace;
  }

  /**
   * Sets whether to trim blank lines.
   *
   * @param trimBlankLines true to enable blank line trimming
   */
  public void setTrimBlankLines(boolean trimBlankLines) {
    this.trimBlankLines = trimBlankLines;
  }

  /**
   * Gets whether blank line trimming is enabled.
   *
   * @return true if blank line trimming is enabled
   */
  public boolean isTrimBlankLines() {
    return trimBlankLines;
  }

  // Output options

  /**
   * Sets verbose output mode.
   *
   * @param verbose true to enable verbose output
   */
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  /**
   * Gets whether verbose output is enabled.
   *
   * @return true if verbose output is enabled
   */
  public boolean isVerbose() {
    return verbose;
  }

  /**
   * Sets dry run mode (no files modified).
   *
   * @param dryRun true to enable dry run mode
   */
  public void setDryRun(boolean dryRun) {
    this.dryRun = dryRun;
  }

  /**
   * Gets whether dry run mode is enabled.
   *
   * @return true if dry run mode is enabled
   */
  public boolean isDryRun() {
    return dryRun;
  }

  // Size limits

  /**
   * Sets the maximum file size in bytes.
   *
   * @param maxFileSize the maximum file size
   */
  public void setMaxFileSize(long maxFileSize) {
    this.maxFileSize = maxFileSize;
  }

  /**
   * Gets the maximum file size in bytes.
   *
   * @return the maximum file size
   */
  public long getMaxFileSize() {
    return maxFileSize;
  }

  // File type filtering

  /**
   * Adds a file type to the inclusion list.
   *
   * @param fileType the file type extension to include
   */
  public void includeFileType(String fileType) {
    includedFileTypes.add(fileType);
  }

  /**
   * Checks if a file type is included.
   *
   * @param fileType the file type to check
   * @return true if the file type is included
   */
  public boolean isFileTypeIncluded(String fileType) {
    return includedFileTypes.contains(fileType);
  }

  /**
   * Adds a file type to the exclusion list.
   *
   * @param fileType the file type extension to exclude
   */
  public void excludeFileType(String fileType) {
    excludedFileTypes.add(fileType);
  }

  /**
   * Checks if a file type is excluded.
   *
   * @param fileType the file type to check
   * @return true if the file type is excluded
   */
  public boolean isFileTypeExcluded(String fileType) {
    return excludedFileTypes.contains(fileType);
  }

  @Override
  public String toString() {
    return "TrimOptions{"
        + "inputDirectory='"
        + inputDirectory
        + '\''
        + ", outputDirectory='"
        + outputDirectory
        + '\''
        + ", trimComments="
        + trimComments
        + ", trimWhitespace="
        + trimWhitespace
        + ", trimBlankLines="
        + trimBlankLines
        + ", verbose="
        + verbose
        + ", dryRun="
        + dryRun
        + ", maxFileSize="
        + maxFileSize
        + '}';
  }
}
