package com.codetrimmer.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProcessingStatisticsTest {

  private ProcessingStatistics statistics;

  @BeforeEach
  void setUp() {
    statistics = new ProcessingStatistics();
  }

  @Test
  void testInitialState() {
    assertEquals(0, statistics.getFilesScanned());
    assertEquals(0, statistics.getFilesModified());
    assertEquals(0, statistics.getFilesSkipped());
    assertEquals(0, statistics.getLinesTrimmed());
    assertEquals(0, statistics.getBlankLinesRemoved());
    assertEquals(0, statistics.getFilesSkippedBinary());
    assertEquals(0, statistics.getFilesSkippedPermission());
    assertEquals(0, statistics.getFilesSkippedSize());
    assertEquals(0, statistics.getFilesSkippedOther());
  }

  @Test
  void testIncrementFilesScanned() {
    statistics.incrementFilesScanned();
    assertEquals(1, statistics.getFilesScanned());

    statistics.incrementFilesScanned();
    assertEquals(2, statistics.getFilesScanned());
  }

  @Test
  void testIncrementFilesModified() {
    statistics.incrementFilesModified();
    assertEquals(1, statistics.getFilesModified());

    statistics.incrementFilesModified();
    assertEquals(2, statistics.getFilesModified());
  }

  @Test
  void testIncrementFilesSkipped() {
    statistics.incrementFilesSkipped();
    assertEquals(1, statistics.getFilesSkipped());

    statistics.incrementFilesSkipped();
    assertEquals(2, statistics.getFilesSkipped());
  }

  @Test
  void testAddLinesTrimmed() {
    statistics.addLinesTrimmed(100);
    assertEquals(100, statistics.getLinesTrimmed());

    statistics.addLinesTrimmed(50);
    assertEquals(150, statistics.getLinesTrimmed());
  }

  @Test
  void testAddBlankLinesRemoved() {
    statistics.addBlankLinesRemoved(10);
    assertEquals(10, statistics.getBlankLinesRemoved());

    statistics.addBlankLinesRemoved(5);
    assertEquals(15, statistics.getBlankLinesRemoved());
  }

  @Test
  void testMixedOperations() {
    statistics.incrementFilesScanned();
    statistics.incrementFilesScanned();
    statistics.incrementFilesModified();
    statistics.incrementFilesSkipped();
    statistics.addLinesTrimmed(200);
    statistics.addBlankLinesRemoved(20);

    assertEquals(2, statistics.getFilesScanned());
    assertEquals(1, statistics.getFilesModified());
    assertEquals(1, statistics.getFilesSkipped());
    assertEquals(200, statistics.getLinesTrimmed());
    assertEquals(20, statistics.getBlankLinesRemoved());
  }

  @Test
  void testExecutionTiming() throws InterruptedException {
    statistics.endProcessing();

    long executionTimeMs = statistics.getExecutionTimeMs();
    assertTrue(executionTimeMs >= 0);

    double executionTimeSec = statistics.getExecutionTimeSec();
    assertTrue(executionTimeSec >= 0);
  }

  @Test
  void testExecutionTimingAccuracy() throws InterruptedException {
    Thread.sleep(100);
    statistics.endProcessing();

    long executionTimeMs = statistics.getExecutionTimeMs();
    assertTrue(executionTimeMs >= 100);
  }

  @Test
  void testLargeNumberOfScannedFiles() {
    for (int i = 0; i < 10000; i++) {
      statistics.incrementFilesScanned();
    }

    assertEquals(10000, statistics.getFilesScanned());
  }

  @Test
  void testLargeNumberOfModifiedFiles() {
    for (int i = 0; i < 5000; i++) {
      statistics.incrementFilesModified();
    }

    assertEquals(5000, statistics.getFilesModified());
  }

  @Test
  void testLargeAmountOfLinesTrimmed() {
    statistics.addLinesTrimmed(1000000);
    assertEquals(1000000, statistics.getLinesTrimmed());
  }

  @Test
  void testLargeAmountOfBlankLinesRemoved() {
    statistics.addBlankLinesRemoved(500000);
    assertEquals(500000, statistics.getBlankLinesRemoved());
  }

  @Test
  void testFilesSkippedBinaryCounter() {
    assertEquals(0, statistics.getFilesSkippedBinary());
    statistics.setFilesSkippedBinary(5);
    assertEquals(5, statistics.getFilesSkippedBinary());
  }

  @Test
  void testFilesSkippedPermissionCounter() {
    assertEquals(0, statistics.getFilesSkippedPermission());
    statistics.setFilesSkippedPermission(3);
    assertEquals(3, statistics.getFilesSkippedPermission());
  }

  @Test
  void testFilesSkippedSizeCounter() {
    assertEquals(0, statistics.getFilesSkippedSize());
    statistics.setFilesSkippedSize(7);
    assertEquals(7, statistics.getFilesSkippedSize());
  }

  @Test
  void testFilesSkippedOtherCounter() {
    assertEquals(0, statistics.getFilesSkippedOther());
    statistics.setFilesSkippedOther(2);
    assertEquals(2, statistics.getFilesSkippedOther());
  }

  @Test
  void testSkipReasonsTotal() {
    statistics.setFilesSkippedBinary(5);
    statistics.setFilesSkippedPermission(3);
    statistics.setFilesSkippedSize(2);
    statistics.setFilesSkippedOther(1);

    int totalSkipped = statistics.getFilesSkippedBinary()
        + statistics.getFilesSkippedPermission()
        + statistics.getFilesSkippedSize()
        + statistics.getFilesSkippedOther();

    assertEquals(11, totalSkipped);
  }

  @Test
  void testMultipleOperationsWithSkipReasons() {
    statistics.incrementFilesScanned();
    statistics.incrementFilesScanned();
    statistics.incrementFilesScanned();
    statistics.incrementFilesModified();
    statistics.incrementFilesModified();
    statistics.setFilesSkippedBinary(1);
    statistics.addLinesTrimmed(500);
    statistics.addBlankLinesRemoved(50);

    assertEquals(3, statistics.getFilesScanned());
    assertEquals(2, statistics.getFilesModified());
    assertEquals(1, statistics.getFilesSkippedBinary());
    assertEquals(500, statistics.getLinesTrimmed());
    assertEquals(50, statistics.getBlankLinesRemoved());
  }

  @Test
  void testRealisticWorkload() {
    // Simulate processing 100 files
    for (int i = 0; i < 100; i++) {
      statistics.incrementFilesScanned();
      if (i % 10 == 0) {
        statistics.setFilesSkippedBinary(statistics.getFilesSkippedBinary() + 1);
      } else if (i % 15 == 0) {
        statistics.setFilesSkippedPermission(statistics.getFilesSkippedPermission() + 1);
      } else {
        statistics.incrementFilesModified();
        statistics.addLinesTrimmed(100 + i);
        statistics.addBlankLinesRemoved(10 + i / 10);
      }
    }

    assertEquals(100, statistics.getFilesScanned());
    assertTrue(statistics.getFilesModified() > 0);
    assertTrue(statistics.getFilesSkippedBinary() > 0);
    assertTrue(statistics.getLinesTrimmed() > 0);
    assertTrue(statistics.getBlankLinesRemoved() > 0);
  }

  @Test
  void testBuilderPattern() {
    ProcessingStatistics stats = ProcessingStatistics.builder()
        .filesScanned(100)
        .filesModified(90)
        .filesSkipped(10)
        .linesTrimmed(5000)
        .blankLinesRemoved(500)
        .filesSkippedBinary(5)
        .filesSkippedPermission(3)
        .filesSkippedSize(1)
        .filesSkippedOther(1)
        .build();

    assertEquals(100, stats.getFilesScanned());
    assertEquals(90, stats.getFilesModified());
    assertEquals(10, stats.getFilesSkipped());
    assertEquals(5000, stats.getLinesTrimmed());
    assertEquals(500, stats.getBlankLinesRemoved());
    assertEquals(5, stats.getFilesSkippedBinary());
    assertEquals(3, stats.getFilesSkippedPermission());
    assertEquals(1, stats.getFilesSkippedSize());
    assertEquals(1, stats.getFilesSkippedOther());
  }

  @Test
  void testBuilderWithDefaults() {
    ProcessingStatistics stats = ProcessingStatistics.builder().build();

    assertNotNull(stats);
    assertEquals(0, stats.getFilesScanned());
  }

  @Test
  void testDataAnnotationEquality() {
    ProcessingStatistics stats1 = ProcessingStatistics.builder()
        .filesScanned(100)
        .filesModified(50)
        .build();

    ProcessingStatistics stats2 = ProcessingStatistics.builder()
        .filesScanned(100)
        .filesModified(50)
        .build();

    assertEquals(stats1, stats2);
  }

  @Test
  void testToString() {
    statistics.incrementFilesScanned();
    statistics.incrementFilesModified();

    String str = statistics.toString();
    assertNotNull(str);
    assertTrue(str.contains("ProcessingStatistics"));
  }

  @Test
  void testStartTimeInitialization() {
    long startTime = statistics.getStartTime();
    assertTrue(startTime > 0);
  }

  @Test
  void testEndTimeInitialization() {
    assertEquals(0, statistics.getEndTime());

    statistics.endProcessing();
    assertTrue(statistics.getEndTime() > 0);
  }

  @Test
  void testNegativeLinesTrimmedHandling() {
    statistics.addLinesTrimmed(100);
    statistics.addLinesTrimmed(-50);

    assertEquals(50, statistics.getLinesTrimmed());
  }

  @Test
  void testZeroOperations() {
    statistics.addLinesTrimmed(0);
    statistics.addBlankLinesRemoved(0);

    assertEquals(0, statistics.getLinesTrimmed());
    assertEquals(0, statistics.getBlankLinesRemoved());
  }

  @Test
  void testMultipleTimingCalls() {
    statistics.endProcessing();
    long time1 = statistics.getExecutionTimeMs();
    long time2 = statistics.getExecutionTimeMs();

    assertEquals(time1, time2);
  }

  @Test
  void testCompleteWorkflow() {
    // Scan 50 files, modify 40, skip 10 (5 binary, 3 permission, 2 other)
    for (int i = 0; i < 50; i++) {
      statistics.incrementFilesScanned();
    }

    for (int i = 0; i < 40; i++) {
      statistics.incrementFilesModified();
      statistics.addLinesTrimmed(100);
      statistics.addBlankLinesRemoved(10);
    }

    statistics.setFilesSkippedBinary(5);
    statistics.setFilesSkippedPermission(3);
    statistics.setFilesSkippedOther(2);

    statistics.endProcessing();

    assertEquals(50, statistics.getFilesScanned());
    assertEquals(40, statistics.getFilesModified());
    assertEquals(5, statistics.getFilesSkippedBinary());
    assertEquals(3, statistics.getFilesSkippedPermission());
    assertEquals(2, statistics.getFilesSkippedOther());
    assertEquals(4000, statistics.getLinesTrimmed());
    assertEquals(400, statistics.getBlankLinesRemoved());
    assertTrue(statistics.getExecutionTimeMs() >= 0);
  }
}
