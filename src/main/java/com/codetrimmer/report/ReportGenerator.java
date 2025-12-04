package com.codetrimmer.report;

import com.codetrimmer.error.CodeTrimmerException;
import com.codetrimmer.error.ErrorCode;
import com.codetrimmer.model.ProcessingStatistics;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Service for generating and exporting processing reports.
 * Supports JSON, CSV, and SQLite formats.
 */
@Service
public class ReportGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportGenerator.class);
    private final ObjectMapper objectMapper;

    public ReportGenerator() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Generates a report in the specified format.
     *
     * @param stats the processing statistics
     * @param format the output format (json, csv, sqlite)
     * @param outputPath the output file path
     */
    public void generateReport(ProcessingStatistics stats, String format, String outputPath) {
        if (format == null || format.trim().isEmpty()) {
            return;
        }

        String normalizedFormat = format.toLowerCase().trim();
        try {
            switch (normalizedFormat) {
                case "json" -> generateJsonReport(stats, outputPath);
                case "csv" -> generateCsvReport(stats, outputPath);
                case "sqlite" -> generateSqliteReport(stats, outputPath);
                default -> throw new CodeTrimmerException(
                    ErrorCode.CT_0061,
                    "Unknown format: " + format,
                    "Use json, csv, or sqlite"
                );
            }
            LOGGER.info("Report generated: {}", outputPath);
        } catch (CodeTrimmerException e) {
            throw e;
        } catch (Exception e) {
            throw new CodeTrimmerException(ErrorCode.CT_0060, e);
        }
    }

    /**
     * Generates a JSON report.
     *
     * @param stats the processing statistics
     * @param outputPath the output file path
     */
    public void generateJsonReport(ProcessingStatistics stats, String outputPath) throws IOException {
        Map<String, Object> reportData = createReportData(stats);
        String json = objectMapper.writeValueAsString(reportData);

        if (outputPath != null && !outputPath.trim().isEmpty()) {
            Files.writeString(Paths.get(outputPath), json);
        } else {
            System.out.println(json);
        }
    }

    /**
     * Gets JSON report as a string.
     *
     * @param stats the processing statistics
     * @return JSON string
     */
    public String getJsonReport(ProcessingStatistics stats) {
        try {
            Map<String, Object> reportData = createReportData(stats);
            return objectMapper.writeValueAsString(reportData);
        } catch (Exception e) {
            throw new CodeTrimmerException(ErrorCode.CT_0060, e);
        }
    }

    /**
     * Generates a CSV report.
     *
     * @param stats the processing statistics
     * @param outputPath the output file path
     */
    public void generateCsvReport(ProcessingStatistics stats, String outputPath) throws IOException {
        StringWriter writer = new StringWriter();

        // CSV Header
        String header = "timestamp,files_scanned,files_modified,files_skipped,"
            + "lines_trimmed,blank_lines_removed,execution_time_ms\n";

        // CSV Data
        writer.write(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        writer.write(",");
        writer.write(String.valueOf(stats.getFilesScanned()));
        writer.write(",");
        writer.write(String.valueOf(stats.getFilesModified()));
        writer.write(",");
        writer.write(String.valueOf(stats.getFilesSkipped()));
        writer.write(",");
        writer.write(String.valueOf(stats.getLinesTrimmed()));
        writer.write(",");
        writer.write(String.valueOf(stats.getBlankLinesRemoved()));
        writer.write(",");
        writer.write(String.valueOf(stats.getExecutionTimeMs()));
        writer.write("\n");

        String dataLine = writer.toString();

        if (outputPath != null && !outputPath.trim().isEmpty()) {
            Path path = Paths.get(outputPath);
            if (Files.exists(path)) {
                // Append to existing file (without header)
                Files.write(path, dataLine.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                    java.nio.file.StandardOpenOption.APPEND);
            } else {
                Files.writeString(path, header + dataLine);
            }
        } else {
            System.out.println(header + dataLine);
        }
    }

    /**
     * Gets CSV report as a string.
     *
     * @param stats the processing statistics
     * @return CSV string
     */
    public String getCsvReport(ProcessingStatistics stats) {
        StringWriter writer = new StringWriter();
        writer.write("timestamp,files_scanned,files_modified,files_skipped,");
        writer.write("lines_trimmed,blank_lines_removed,execution_time_ms\n");
        writer.write(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        writer.write(",");
        writer.write(String.valueOf(stats.getFilesScanned()));
        writer.write(",");
        writer.write(String.valueOf(stats.getFilesModified()));
        writer.write(",");
        writer.write(String.valueOf(stats.getFilesSkipped()));
        writer.write(",");
        writer.write(String.valueOf(stats.getLinesTrimmed()));
        writer.write(",");
        writer.write(String.valueOf(stats.getBlankLinesRemoved()));
        writer.write(",");
        writer.write(String.valueOf(stats.getExecutionTimeMs()));
        writer.write("\n");
        return writer.toString();
    }

    /**
     * Generates a SQLite report.
     *
     * @param stats the processing statistics
     * @param outputPath the SQLite database path
     */
    public void generateSqliteReport(ProcessingStatistics stats, String outputPath) {
        String dbPath = outputPath != null && !outputPath.trim().isEmpty()
            ? outputPath : "codetrimmer_stats.db";

        String url = "jdbc:sqlite:" + dbPath;

        try (Connection conn = DriverManager.getConnection(url)) {
            createTableIfNotExists(conn);
            insertStats(conn, stats);
            LOGGER.info("Statistics saved to SQLite database: {}", dbPath);
        } catch (SQLException e) {
            throw new CodeTrimmerException(
                ErrorCode.CT_0063,
                "Failed to write to SQLite: " + e.getMessage(),
                "Check database permissions and disk space"
            );
        }
    }

    private void createTableIfNotExists(Connection conn) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS processing_stats (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                timestamp TEXT NOT NULL,
                files_scanned INTEGER,
                files_modified INTEGER,
                files_skipped INTEGER,
                lines_trimmed INTEGER,
                blank_lines_removed INTEGER,
                execution_time_ms INTEGER
            )
            """;
        conn.createStatement().execute(sql);
    }

    private void insertStats(Connection conn, ProcessingStatistics stats) throws SQLException {
        String sql = """
            INSERT INTO processing_stats
            (timestamp, files_scanned, files_modified, files_skipped,
             lines_trimmed, blank_lines_removed, execution_time_ms)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            pstmt.setInt(2, stats.getFilesScanned());
            pstmt.setInt(3, stats.getFilesModified());
            pstmt.setInt(4, stats.getFilesSkipped());
            pstmt.setInt(5, stats.getLinesTrimmed());
            pstmt.setInt(6, stats.getBlankLinesRemoved());
            pstmt.setLong(7, stats.getExecutionTimeMs());
            pstmt.executeUpdate();
        }
    }

    private Map<String, Object> createReportData(ProcessingStatistics stats) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        data.put("filesScanned", stats.getFilesScanned());
        data.put("filesModified", stats.getFilesModified());
        data.put("filesSkipped", stats.getFilesSkipped());
        data.put("linesTrimmed", stats.getLinesTrimmed());
        data.put("blankLinesRemoved", stats.getBlankLinesRemoved());
        data.put("executionTimeMs", stats.getExecutionTimeMs());
        data.put("executionTimeSec", stats.getExecutionTimeSec());
        return data;
    }

    /**
     * Sends report to HTTP endpoint.
     *
     * @param stats the processing statistics
     * @param endpoint the HTTP endpoint URL
     */
    public void sendToEndpoint(ProcessingStatistics stats, String endpoint) {
        if (endpoint == null || endpoint.trim().isEmpty()) {
            return;
        }

        try {
            String json = getJsonReport(stats);
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(json))
                .build();

            java.net.http.HttpResponse<String> response = client.send(request,
                java.net.http.HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                LOGGER.info("Report sent to endpoint: {}", endpoint);
            } else {
                throw new CodeTrimmerException(
                    ErrorCode.CT_0062,
                    "HTTP error " + response.statusCode(),
                    "Check endpoint URL and availability"
                );
            }
        } catch (CodeTrimmerException e) {
            throw e;
        } catch (Exception e) {
            throw new CodeTrimmerException(ErrorCode.CT_0062, e);
        }
    }
}
