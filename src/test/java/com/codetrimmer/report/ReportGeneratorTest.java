package com.codetrimmer.report;

import static org.junit.jupiter.api.Assertions.*;

import com.codetrimmer.error.CodeTrimmerException;
import com.codetrimmer.model.ProcessingStatistics;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for ReportGenerator service.
 */
class ReportGeneratorTest {

    private ReportGenerator generator;
    private ProcessingStatistics stats;

    @TempDir
    private Path tempDir;

    @BeforeEach
    void setUp() {
        generator = new ReportGenerator();
        stats = new ProcessingStatistics();
        stats.incrementFilesScanned();
        stats.incrementFilesScanned();
        stats.incrementFilesModified();
        stats.addLinesTrimmed(10);
        stats.addBlankLinesRemoved(3);
        stats.endProcessing();
    }

    @Test
    void testGenerateJsonReport() throws IOException {
        Path outputPath = tempDir.resolve("report.json");
        generator.generateJsonReport(stats, outputPath.toString());

        assertTrue(Files.exists(outputPath));
        String content = Files.readString(outputPath);
        assertTrue(content.contains("filesScanned"));
        assertTrue(content.contains("filesModified"));
        assertTrue(content.contains("linesTrimmed"));
    }

    @Test
    void testGetJsonReport() {
        String json = generator.getJsonReport(stats);
        assertNotNull(json);
        assertTrue(json.contains("\"filesScanned\" : 2"));
        assertTrue(json.contains("\"filesModified\" : 1"));
        assertTrue(json.contains("\"linesTrimmed\" : 10"));
    }

    @Test
    void testGenerateCsvReport() throws IOException {
        Path outputPath = tempDir.resolve("report.csv");
        generator.generateCsvReport(stats, outputPath.toString());

        assertTrue(Files.exists(outputPath));
        String content = Files.readString(outputPath);
        assertTrue(content.contains("timestamp"));
        assertTrue(content.contains("files_scanned"));
        assertTrue(content.contains(",2,"));  // filesScanned = 2
    }

    @Test
    void testGetCsvReport() {
        String csv = generator.getCsvReport(stats);
        assertNotNull(csv);
        assertTrue(csv.contains("files_scanned"));
        assertTrue(csv.contains("files_modified"));
        assertTrue(csv.contains(",2,"));  // filesScanned = 2
    }

    @Test
    void testGenerateSqliteReport() {
        Path outputPath = tempDir.resolve("stats.db");
        generator.generateSqliteReport(stats, outputPath.toString());

        assertTrue(Files.exists(outputPath));
    }

    @Test
    void testGenerateReportJson() throws IOException {
        Path outputPath = tempDir.resolve("report.json");
        generator.generateReport(stats, "json", outputPath.toString());

        assertTrue(Files.exists(outputPath));
    }

    @Test
    void testGenerateReportCsv() throws IOException {
        Path outputPath = tempDir.resolve("report.csv");
        generator.generateReport(stats, "csv", outputPath.toString());

        assertTrue(Files.exists(outputPath));
    }

    @Test
    void testGenerateReportSqlite() {
        Path outputPath = tempDir.resolve("stats.db");
        generator.generateReport(stats, "sqlite", outputPath.toString());

        assertTrue(Files.exists(outputPath));
    }

    @Test
    void testInvalidFormatThrowsException() {
        assertThrows(CodeTrimmerException.class, () -> {
            generator.generateReport(stats, "invalid", "output.txt");
        });
    }

    @Test
    void testNullFormatDoesNothing() {
        assertDoesNotThrow(() -> {
            generator.generateReport(stats, null, "output.txt");
        });
    }

    @Test
    void testEmptyFormatDoesNothing() {
        assertDoesNotThrow(() -> {
            generator.generateReport(stats, "", "output.txt");
        });
    }

    @Test
    void testCsvAppendsToExistingFile() throws IOException {
        Path outputPath = tempDir.resolve("append.csv");

        // Generate first report
        generator.generateCsvReport(stats, outputPath.toString());
        String firstContent = Files.readString(outputPath);
        int firstLineCount = firstContent.split("\n").length;

        // Generate second report (should append)
        generator.generateCsvReport(stats, outputPath.toString());
        String secondContent = Files.readString(outputPath);
        int secondLineCount = secondContent.split("\n").length;

        assertEquals(firstLineCount + 1, secondLineCount);
    }

    @Test
    void testJsonReportContainsAllFields() {
        String json = generator.getJsonReport(stats);

        assertTrue(json.contains("timestamp"));
        assertTrue(json.contains("filesScanned"));
        assertTrue(json.contains("filesModified"));
        assertTrue(json.contains("filesSkipped"));
        assertTrue(json.contains("linesTrimmed"));
        assertTrue(json.contains("blankLinesRemoved"));
        assertTrue(json.contains("executionTimeMs"));
        assertTrue(json.contains("executionTimeSec"));
    }

    @Test
    void testCsvReportHeader() {
        String csv = generator.getCsvReport(stats);
        String[] lines = csv.split("\n");
        String header = lines[0];

        assertTrue(header.contains("timestamp"));
        assertTrue(header.contains("files_scanned"));
        assertTrue(header.contains("files_modified"));
        assertTrue(header.contains("files_skipped"));
        assertTrue(header.contains("lines_trimmed"));
        assertTrue(header.contains("blank_lines_removed"));
        assertTrue(header.contains("execution_time_ms"));
    }
}
