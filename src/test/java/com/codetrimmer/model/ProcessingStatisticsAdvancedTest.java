package com.codetrimmer.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("ProcessingStatistics Advanced Tests")
class ProcessingStatisticsAdvancedTest {

  private ProcessingStatistics stats;

  @BeforeEach
  void setUp() {
    stats = new ProcessingStatistics();
  }

  @Nested
  @DisplayName("File Scanning Counters")
  class FileScannedCounterTests {
    @Test
    void initialFilesScannedIsZero() {
      assertEquals(0, stats.getFilesScanned());
    }

    @Test
    void initialFilesModifiedIsZero() {
      assertEquals(0, stats.getFilesModified());
    }

    @Test
    void initialFilesSkippedIsZero() {
      assertEquals(0, stats.getFilesSkipped());
    }

    @Test
    void incrementFilesScanned() {
      stats.incrementFilesScanned();
      assertEquals(1, stats.getFilesScanned());
    }

    @Test
    void incrementFilesModified() {
      stats.incrementFilesModified();
      assertEquals(1, stats.getFilesModified());
    }

    @Test
    void incrementFilesSkipped() {
      stats.incrementFilesSkipped();
      assertEquals(1, stats.getFilesSkipped());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 100})
    void multipleIncrements(int count) {
      for (int i = 0; i < count; i++) {
        stats.incrementFilesScanned();
      }
      assertEquals(count, stats.getFilesScanned());
    }
  }

  @Nested
  @DisplayName("Lines Trimmed Counters")
  class LinesTrimmedCounterTests {
    @Test
    void initialLinesTrimmedIsZero() {
      assertEquals(0, stats.getLinesTrimmed());
    }

    @Test
    void addLinesTrimmed() {
      stats.addLinesTrimmed(5);
      assertEquals(5, stats.getLinesTrimmed());
    }

    @Test
    void addMultipleLinesTrimmed() {
      stats.addLinesTrimmed(5);
      stats.addLinesTrimmed(10);
      assertEquals(15, stats.getLinesTrimmed());
    }

    @Test
    void addZeroLinesTrimmed() {
      stats.addLinesTrimmed(0);
      assertEquals(0, stats.getLinesTrimmed());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 100, 1000})
    void variousLineAmounts(int lines) {
      stats.addLinesTrimmed(lines);
      assertEquals(lines, stats.getLinesTrimmed());
    }
  }

  @Nested
  @DisplayName("Blank Lines Removed Counters")
  class BlankLinesRemovedCounterTests {
    @Test
    void initialBlankLinesRemovedIsZero() {
      assertEquals(0, stats.getBlankLinesRemoved());
    }

    @Test
    void addBlankLinesRemoved() {
      stats.addBlankLinesRemoved(3);
      assertEquals(3, stats.getBlankLinesRemoved());
    }

    @Test
    void addMultipleBlankLinesRemoved() {
      stats.addBlankLinesRemoved(3);
      stats.addBlankLinesRemoved(2);
      assertEquals(5, stats.getBlankLinesRemoved());
    }

    @Test
    void addZeroBlankLinesRemoved() {
      stats.addBlankLinesRemoved(0);
      assertEquals(0, stats.getBlankLinesRemoved());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 3, 50, 500})
    void variousBlankLineAmounts(int lines) {
      stats.addBlankLinesRemoved(lines);
      assertEquals(lines, stats.getBlankLinesRemoved());
    }
  }

  @Nested
  @DisplayName("Skip Reasons Tracking")
  class SkipReasonsTrackingTests {
    @Test
    void initialSkipReasonsAreZero() {
      assertEquals(0, stats.getFilesSkippedBinary());
      assertEquals(0, stats.getFilesSkippedPermission());
      assertEquals(0, stats.getFilesSkippedSize());
      assertEquals(0, stats.getFilesSkippedOther());
    }

    @Test
    void setFilesSkippedBinary() {
      stats.setFilesSkippedBinary(1);
      assertEquals(1, stats.getFilesSkippedBinary());
    }

    @Test
    void setFilesSkippedPermission() {
      stats.setFilesSkippedPermission(1);
      assertEquals(1, stats.getFilesSkippedPermission());
    }

    @Test
    void setFilesSkippedSize() {
      stats.setFilesSkippedSize(1);
      assertEquals(1, stats.getFilesSkippedSize());
    }

    @Test
    void setFilesSkippedOther() {
      stats.setFilesSkippedOther(1);
      assertEquals(1, stats.getFilesSkippedOther());
    }

    @Test
    void multipleSkipReasons() {
      stats.setFilesSkippedBinary(2);
      stats.setFilesSkippedPermission(1);
      stats.setFilesSkippedSize(1);
      assertEquals(2, stats.getFilesSkippedBinary());
      assertEquals(1, stats.getFilesSkippedPermission());
      assertEquals(1, stats.getFilesSkippedSize());
    }

    @Test
    void skipCountIndependent() {
      stats.setFilesSkippedBinary(1);
      stats.incrementFilesSkipped();
      assertEquals(1, stats.getFilesSkippedBinary());
      assertEquals(1, stats.getFilesSkipped());
    }
  }

  @Nested
  @DisplayName("Execution Time Tracking")
  class ExecutionTimingTests {
    @Test
    void getExecutionTimeMs() {
      stats.endProcessing();
      long timeMs = stats.getExecutionTimeMs();
      assertTrue(timeMs >= 0, "Execution time should be non-negative");
      assertTrue(timeMs < 60000, "Execution time should be less than 60 seconds");
    }

    @Test
    void executionTimeAccumulates() {
      stats.endProcessing();
      long firstTime = stats.getExecutionTimeMs();
      stats.endProcessing();
      long secondTime = stats.getExecutionTimeMs();
      assertTrue(secondTime >= firstTime, "Execution time should accumulate or stay same");
    }

    @Test
    void getExecutionTimeSec() {
      stats.endProcessing();
      double timeSec = stats.getExecutionTimeSec();
      assertTrue(timeSec >= 0, "Execution time in seconds should be non-negative");
      assertTrue(timeSec < 60, "Execution time in seconds should be less than 60");
    }

    @Test
    void executionTimeSecAccuracy() {
      stats.endProcessing();
      long timeMs = stats.getExecutionTimeMs();
      double timeSec = stats.getExecutionTimeSec();
      double expectedSec = timeMs / 1000.0;
      double tolerance = 0.1;
      assertTrue(Math.abs(timeSec - expectedSec) <= tolerance,
          "Execution time in seconds should match milliseconds");
    }
  }

  @Nested
  @DisplayName("Combined Operations")
  class CombinedOperationsTests {
    @Test
    void allCountersWork() {
      stats.incrementFilesScanned();
      stats.incrementFilesModified();
      stats.addLinesTrimmed(10);

      assertEquals(1, stats.getFilesScanned());
      assertEquals(1, stats.getFilesModified());
      assertEquals(10, stats.getLinesTrimmed());
    }

    @Test
    void mixedCounterTypes() {
      stats.incrementFilesScanned();
      stats.addLinesTrimmed(5);
      stats.addBlankLinesRemoved(2);
      stats.setFilesSkippedBinary(1);

      assertEquals(1, stats.getFilesScanned());
      assertEquals(5, stats.getLinesTrimmed());
      assertEquals(2, stats.getBlankLinesRemoved());
      assertEquals(1, stats.getFilesSkippedBinary());
    }

    @Test
    void allCountersIndependent() {
      stats.incrementFilesScanned();
      stats.incrementFilesModified();
      stats.incrementFilesSkipped();

      assertEquals(1, stats.getFilesScanned());
      assertEquals(1, stats.getFilesModified());
      assertEquals(1, stats.getFilesSkipped());
    }
  }

  @Nested
  @DisplayName("Builder Pattern")
  class BuilderPatternTests {
    @Test
    void builderCreatesInstance() {
      ProcessingStatistics builtStats = ProcessingStatistics.builder()
          .startTime(1000)
          .endTime(2000)
          .filesScanned(5)
          .build();

      assertNotNull(builtStats);
      assertEquals(5, builtStats.getFilesScanned());
    }

    @Test
    void builderWithAllFields() {
      ProcessingStatistics builtStats = ProcessingStatistics.builder()
          .startTime(1000)
          .endTime(2000)
          .filesScanned(10)
          .filesModified(5)
          .filesSkipped(2)
          .linesTrimmed(50)
          .blankLinesRemoved(10)
          .filesSkippedBinary(1)
          .filesSkippedPermission(1)
          .filesSkippedSize(0)
          .filesSkippedOther(0)
          .build();

      assertEquals(10, builtStats.getFilesScanned());
      assertEquals(5, builtStats.getFilesModified());
      assertEquals(2, builtStats.getFilesSkipped());
      assertEquals(50, builtStats.getLinesTrimmed());
    }

    @Test
    void builderDefaults() {
      ProcessingStatistics builtStats = ProcessingStatistics.builder().build();

      assertNotNull(builtStats);
      assertEquals(0, builtStats.getFilesScanned());
    }

    @Test
    void builderPartialFields() {
      ProcessingStatistics builtStats = ProcessingStatistics.builder()
          .filesScanned(3)
          .linesTrimmed(15)
          .build();

      assertEquals(3, builtStats.getFilesScanned());
      assertEquals(15, builtStats.getLinesTrimmed());
    }

    @Test
    void multipleInstances() {
      ProcessingStatistics stats1 = ProcessingStatistics.builder().filesScanned(5).build();
      ProcessingStatistics stats2 = ProcessingStatistics.builder().filesScanned(10).build();

      assertEquals(5, stats1.getFilesScanned());
      assertEquals(10, stats2.getFilesScanned());
    }

    @Test
    void builderImmutability() {
      ProcessingStatistics builtStats = ProcessingStatistics.builder()
          .filesScanned(5)
          .build();

      // After building, modifications should work on the instance
      builtStats.incrementFilesScanned();
      assertEquals(6, builtStats.getFilesScanned());
    }

    @Test
    void copyWithBuilder() {
      ProcessingStatistics original = ProcessingStatistics.builder()
          .filesScanned(5)
          .linesTrimmed(20)
          .build();

      ProcessingStatistics copy = ProcessingStatistics.builder()
          .filesScanned(original.getFilesScanned())
          .linesTrimmed(original.getLinesTrimmed())
          .build();

      assertEquals(original.getFilesScanned(), copy.getFilesScanned());
      assertEquals(original.getLinesTrimmed(), copy.getLinesTrimmed());
    }
  }

  @Nested
  @DisplayName("Edge Cases")
  class EdgeCasesTests {
    @Test
    void largeCounterValues() {
      stats.addLinesTrimmed(1000000);
      assertEquals(1000000, stats.getLinesTrimmed());
    }

    @Test
    void zerosAreValid() {
      stats.addLinesTrimmed(0);
      stats.addBlankLinesRemoved(0);
      assertEquals(0, stats.getLinesTrimmed());
      assertEquals(0, stats.getBlankLinesRemoved());
    }

    @Test
    void multipleOperations() {
      for (int i = 0; i < 100; i++) {
        stats.incrementFilesScanned();
      }
      assertEquals(100, stats.getFilesScanned());
    }

    @Test
    void mixedPositiveValues() {
      stats.setFilesSkippedBinary(5);
      stats.setFilesSkippedPermission(3);
      stats.setFilesSkippedSize(2);
      stats.setFilesSkippedOther(1);
      assertEquals(5 + 3 + 2 + 1, stats.getFilesSkippedBinary()
          + stats.getFilesSkippedPermission()
          + stats.getFilesSkippedSize()
          + stats.getFilesSkippedOther());
    }
  }

  @Nested
  @DisplayName("Data Integrity")
  class DataIntegrityTests {
    @Test
    void gettersReturnCorrectValues() {
      stats.incrementFilesScanned();
      assertTrue(stats.getFilesScanned() > 0);
    }

    @Test
    void consistentState() {
      stats.incrementFilesScanned();
      assertEquals(stats.getFilesScanned(), stats.getFilesScanned());
    }

    @Test
    void constructorInitialization() {
      ProcessingStatistics newStats = new ProcessingStatistics();
      assertEquals(0, newStats.getFilesScanned());
      assertEquals(0, newStats.getFilesModified());
      assertEquals(0, newStats.getFilesSkipped());
    }
  }
}
