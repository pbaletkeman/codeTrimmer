package com.codetrimmer.model;

/**
 * File processing result containing information about changes made.
 */
public final class FileProcessingResult {

  private final String filePath;
  private final boolean modified;
  private final boolean skipped;
  private final String skipReason;
  private final int linesTrimmed;
  private final int blankLinesRemoved;
  private final String errorMessage;

  private FileProcessingResult(Builder builder) {
    this.filePath = builder.filePath;
    this.modified = builder.modified;
    this.skipped = builder.skipped;
    this.skipReason = builder.skipReason;
    this.linesTrimmed = builder.linesTrimmed;
    this.blankLinesRemoved = builder.blankLinesRemoved;
    this.errorMessage = builder.errorMessage;
  }

  public static class Builder {
    private final String filePath;
    private boolean modified = false;
    private boolean skipped = false;
    private String skipReason = "";
    private int linesTrimmed = 0;
    private int blankLinesRemoved = 0;
    private String errorMessage = "";

    public Builder(String filePath) {
      this.filePath = filePath;
    }

    public Builder modified(boolean modified) {
      this.modified = modified;
      return this;
    }

    public Builder skipped(boolean skipped) {
      this.skipped = skipped;
      return this;
    }

    public Builder skipReason(String reason) {
      this.skipReason = reason;
      return this;
    }

    public Builder linesTrimmed(int count) {
      this.linesTrimmed = count;
      return this;
    }

    public Builder blankLinesRemoved(int count) {
      this.blankLinesRemoved = count;
      return this;
    }

    public Builder error(String message) {
      this.errorMessage = message;
      return this;
    }

    public FileProcessingResult build() {
      return new FileProcessingResult(this);
    }
  }

  // Getters
  public String getFilePath() {
    return filePath;
  }

  public boolean isModified() {
    return modified;
  }

  public boolean isSkipped() {
    return skipped;
  }

  public String getSkipReason() {
    return skipReason;
  }

  public int getLinesTrimmed() {
    return linesTrimmed;
  }

  public int getBlankLinesRemoved() {
    return blankLinesRemoved;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public boolean hasError() {
    return errorMessage != null && !errorMessage.isEmpty();
  }

  @Override
  public String toString() {
    return "FileProcessingResult{" +
           "filePath='" + filePath + '\'' +
           ", modified=" + modified +
           ", skipped=" + skipped +
           ", skipReason='" + skipReason + '\'' +
           ", linesTrimmed=" + linesTrimmed +
           ", blankLinesRemoved=" + blankLinesRemoved +
           ", errorMessage='" + errorMessage + '\'' +
           '}';
  }
}
