package com.codetrimmer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Statistics tracking for file processing operations.
 */
@Data
@Builder
@AllArgsConstructor
public class ProcessingStatistics {

  private long startTime;
  private long endTime;
  private int filesScanned;
  private int filesModified;
  private int filesSkipped;
  private int linesTrimmed;
  private int blankLinesRemoved;
  private int filesSkippedBinary;
  private int filesSkippedPermission;
  private int filesSkippedSize;
  private int filesSkippedOther;

  public ProcessingStatistics() {
    this.startTime = System.currentTimeMillis();
    this.filesScanned = 0;
    this.filesModified = 0;
    this.filesSkipped = 0;
    this.linesTrimmed = 0;
    this.blankLinesRemoved = 0;
    this.filesSkippedBinary = 0;
    this.filesSkippedPermission = 0;
    this.filesSkippedSize = 0;
    this.filesSkippedOther = 0;
  }

  public void endProcessing() {
    this.endTime = System.currentTimeMillis();
  }

  public long getExecutionTimeMs() {
    return endTime - startTime;
  }

  public double getExecutionTimeSec() {
    return (endTime - startTime) / 1000.0;
  }

  public void incrementFilesScanned() {
    this.filesScanned++;
  }

  public void incrementFilesModified() {
    this.filesModified++;
  }

  public void incrementFilesSkipped() {
    this.filesSkipped++;
  }

  public void addLinesTrimmed(int count) {
    this.linesTrimmed += count;
  }

  public void addBlankLinesRemoved(int count) {
    this.blankLinesRemoved += count;
  }
}
