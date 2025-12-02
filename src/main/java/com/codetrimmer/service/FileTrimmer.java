package com.codetrimmer.service;

import com.codetrimmer.config.CodeTrimmerConfig;

/**
 * Service for trimming whitespace from file content.
 */
public class FileTrimmer {

  private final String content;
  private final CodeTrimmerConfig config;

  public FileTrimmer(String content, CodeTrimmerConfig config) {
    this.content = content;
    this.config = config;
  }

  /**
   * Applies all trimming rules to the content.
   *
   * @return trim result with modified content and statistics
   */
  public TrimResult trim() {
    String result = content;
    int linesTrimmed = 0;
    int blankLinesRemoved = 0;

    // Trim trailing whitespace from lines
    if (config.isTrimTrailingWhitespace()) {
      int[] trimStats = trimTrailingWhitespace(result);
      result = (String) getTrimmedContent(result);
      linesTrimmed = trimStats[0];
    }

    // Reduce excessive blank lines
    int blankLinesStats = reduceConsecutiveBlankLines(result);
    result = reduceBlankLines(result);
    blankLinesRemoved = blankLinesStats;

    // Ensure final newline
    if (config.isEnsureFinalNewline()) {
      result = ensureFinalNewline(result);
    }

    return new TrimResult(result, linesTrimmed, blankLinesRemoved);
  }

  /**
   * Trims trailing whitespace from all lines.
   *
   * @param text the text to process
   * @return array with [linesTrimmed, lines]
   */
  private int[] trimTrailingWhitespace(String text) {
    String[] lines = text.split("\n", -1);
    int trimmedCount = 0;

    for (int i = 0; i < lines.length; i++) {
      String trimmed = lines[i].replaceAll("\\s+$", "");
      if (!trimmed.equals(lines[i])) {
        trimmedCount++;
      }
      lines[i] = trimmed;
    }

    return new int[]{trimmedCount};
  }

  /**
   * Gets trimmed content after removing trailing whitespace.
   *
   * @param text the text to process
   * @return the trimmed content
   */
  private Object getTrimmedContent(String text) {
    String[] lines = text.split("\n", -1);
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < lines.length; i++) {
      String trimmed = lines[i].replaceAll("\\s+$", "");
      result.append(trimmed);
      if (i < lines.length - 1) {
        result.append("\n");
      }
    }

    return result.toString();
  }

  /**
   * Counts and prepares for reducing consecutive blank lines.
   *
   * @param text the text to process
   * @return number of blank lines that will be removed
   */
  private int reduceConsecutiveBlankLines(String text) {
    String[] lines = text.split("\n", -1);
    int maxBlank = config.getMaxConsecutiveBlankLines();
    int removedCount = 0;
    int consecutiveBlank = 0;

    for (String line : lines) {
      if (line.trim().isEmpty()) {
        consecutiveBlank++;
        if (consecutiveBlank > maxBlank) {
          removedCount++;
        }
      } else {
        consecutiveBlank = 0;
      }
    }

    return removedCount;
  }

  /**
   * Reduces consecutive blank lines to the maximum allowed.
   *
   * @param text the text to process
   * @return text with reduced blank lines
   */
  private String reduceBlankLines(String text) {
    String[] lines = text.split("\n", -1);
    int maxBlank = config.getMaxConsecutiveBlankLines();
    StringBuilder result = new StringBuilder();
    int consecutiveBlank = 0;

    for (int i = 0; i < lines.length; i++) {
      String line = lines[i];

      if (line.trim().isEmpty()) {
        consecutiveBlank++;
        if (consecutiveBlank <= maxBlank) {
          result.append(line);
          if (i < lines.length - 1) {
            result.append("\n");
          }
        }
      } else {
        consecutiveBlank = 0;
        result.append(line);
        if (i < lines.length - 1) {
          result.append("\n");
        }
      }
    }

    return result.toString();
  }

  /**
   * Ensures the file ends with exactly one newline.
   *
   * @param text the text to process
   * @return text with single final newline
   */
  private String ensureFinalNewline(String text) {
    if (text == null || text.isEmpty()) {
      return "\n";
    }

    // Remove all trailing newlines
    while (text.endsWith("\n")) {
      text = text.substring(0, text.length() - 1);
    }

    // Add single newline
    return text + "\n";
  }

  /**
   * Result of trimming operation.
   */
  public static class TrimResult {
    private final String content;
    private final int linesTrimmed;
    private final int blankLinesRemoved;

    public TrimResult(String content, int linesTrimmed, int blankLinesRemoved) {
      this.content = content;
      this.linesTrimmed = linesTrimmed;
      this.blankLinesRemoved = blankLinesRemoved;
    }

    public String getContent() {
      return content;
    }

    public int getLinesTrimmed() {
      return linesTrimmed;
    }

    public int getBlankLinesRemoved() {
      return blankLinesRemoved;
    }
  }
}
