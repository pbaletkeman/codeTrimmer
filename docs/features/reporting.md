# Statistics and Reporting

Export processing statistics to JSON, CSV, or SQLite for metrics tracking.

## Overview

Code Trimmer can export detailed processing statistics for integration with monitoring systems and metrics tracking.

## Report Formats

| Format | Use Case |
|--------|----------|
| JSON | API integration, dashboards |
| CSV | Spreadsheets, data analysis |
| SQLite | Historical tracking, queries |

## Usage

### Generate JSON Report

```bash
# Output to file
java -jar code-trimmer.jar trim-config . --report json --report-output report.json

# Output to stdout
java -jar code-trimmer.jar trim-config . --report json
```

### Generate CSV Report

```bash
# Output to file (appends to existing)
java -jar code-trimmer.jar trim-config . --report csv --report-output stats.csv
```

### Generate SQLite Report

```bash
# Save to database
java -jar code-trimmer.jar trim-config . --report sqlite --report-output stats.db
```

### Send to HTTP Endpoint

```bash
# Send JSON to metrics server
java -jar code-trimmer.jar trim-config . --report json --report-endpoint https://metrics.example.com/api/stats
```

## Statistics Collected

| Metric | Description |
|--------|-------------|
| `timestamp` | When processing occurred |
| `filesScanned` | Total files examined |
| `filesModified` | Files that were changed |
| `filesSkipped` | Files that were skipped |
| `linesTrimmed` | Lines with whitespace removed |
| `blankLinesRemoved` | Blank lines that were removed |
| `executionTimeMs` | Processing time in milliseconds |
| `executionTimeSec` | Processing time in seconds |

## Output Examples

### JSON Format

```json
{
  "timestamp": "2025-12-04T00:00:00",
  "filesScanned": 150,
  "filesModified": 23,
  "filesSkipped": 5,
  "linesTrimmed": 456,
  "blankLinesRemoved": 78,
  "executionTimeMs": 1234,
  "executionTimeSec": 1.234
}
```

### CSV Format

```csv
timestamp,files_scanned,files_modified,files_skipped,lines_trimmed,blank_lines_removed,execution_time_ms
2025-12-04T00:00:00,150,23,5,456,78,1234
```

### SQLite Schema

```sql
CREATE TABLE processing_stats (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    timestamp TEXT NOT NULL,
    files_scanned INTEGER,
    files_modified INTEGER,
    files_skipped INTEGER,
    lines_trimmed INTEGER,
    blank_lines_removed INTEGER,
    execution_time_ms INTEGER
);
```

## Configuration File

```yaml
report:
  format: "json"
  outputPath: "metrics/codetrimmer-report.json"
  endpoint: "https://metrics.example.com/api/stats"
```

## Querying SQLite Data

```sql
-- Recent runs
SELECT * FROM processing_stats 
ORDER BY timestamp DESC 
LIMIT 10;

-- Total files processed this month
SELECT SUM(files_scanned) as total 
FROM processing_stats 
WHERE timestamp >= date('now', 'start of month');

-- Average execution time
SELECT AVG(execution_time_ms) as avg_time 
FROM processing_stats;
```

## Integration Examples

### GitHub Actions

```yaml
- name: Run Code Trimmer with Report
  run: |
    java -jar code-trimmer.jar trim-config . \
      --report json \
      --report-output trimmer-report.json

- name: Upload Report
  uses: actions/upload-artifact@v4
  with:
    name: trimmer-report
    path: trimmer-report.json
```

### Cron Job with Historical Tracking

```bash
#!/bin/bash
# Run nightly and append to SQLite
java -jar code-trimmer.jar trim-config /project \
  --report sqlite \
  --report-output /var/metrics/codetrimmer.db
```

## Error Handling

| Error Code | Description | Solution |
|------------|-------------|----------|
| CT-0060 | Report generation failed | Check output path |
| CT-0061 | Invalid report format | Use json, csv, or sqlite |
| CT-0062 | Report endpoint failed | Check network connectivity |
| CT-0063 | SQLite database error | Check disk space/permissions |

---

**See Also:**
- [Configuration](./configuration.md)
- [Pipeline Support](./pipeline-support.md)
