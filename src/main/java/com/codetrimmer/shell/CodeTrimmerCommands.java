package com.codetrimmer.shell;

import com.codetrimmer.config.CodeTrimmerConfig;
import com.codetrimmer.config.ConfigurationLoader;
import com.codetrimmer.config.TrimmerConfig;
import com.codetrimmer.error.CodeTrimmerException;
import com.codetrimmer.model.FileProcessingResult;
import com.codetrimmer.model.ProcessingStatistics;
import com.codetrimmer.report.ReportGenerator;
import com.codetrimmer.service.DiffGenerator;
import com.codetrimmer.service.FileProcessingService;
import com.codetrimmer.service.HookGenerator;
import com.codetrimmer.service.UndoService;
import com.codetrimmer.util.ColorOutput;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

/**
 * Spring Shell commands for Code Trimmer.
 */
@Component
public class CodeTrimmerCommands {

  private final FileProcessingService fileProcessingService;
  private final CodeTrimmerConfig config;
  private final ConfigurationLoader configLoader;
  private final ReportGenerator reportGenerator;
  private final HookGenerator hookGenerator;
  private final UndoService undoService;
  private final DiffGenerator diffGenerator;

  public CodeTrimmerCommands(FileProcessingService fileProcessingService,
                             CodeTrimmerConfig config,
                             ConfigurationLoader configLoader,
                             ReportGenerator reportGenerator,
                             HookGenerator hookGenerator,
                             UndoService undoService,
                             DiffGenerator diffGenerator) {
    this.fileProcessingService = fileProcessingService;
    this.config = config;
    this.configLoader = configLoader;
    this.reportGenerator = reportGenerator;
    this.hookGenerator = hookGenerator;
    this.undoService = undoService;
    this.diffGenerator = diffGenerator;
  }

  @Command(command = "trim", description = "Process files in a directory")
  public void trim(
      @Option(description = "Directory path to process") String directory,
      @Option(description = "Include file extensions (comma-separated)") String include,
      @Option(description = "Exclude file extensions (comma-separated)") String exclude,
      @Option(description = "Maximum file size in bytes") long maxSize,
      @Option(description = "Maximum number of files to process") int maxFiles,
      @Option(description = "Enable dry-run mode") boolean dryRun,
      @Option(description = "Enable verbose output") boolean verbose,
      @Option(description = "Enable quiet mode") boolean quiet,
      @Option(description = "Disable colored output") boolean noColor,
      @Option(description = "Include hidden files") boolean includeHidden,
      @Option(description = "Create backup files") boolean backup,
      @Option(description = "No file limits") boolean noLimits) {

    TrimOptions.Builder builder = TrimOptions.builder()
        .directory(directory)
        .include(include)
        .exclude(exclude)
        .maxSize(maxSize)
        .maxFiles(maxFiles)
        .dryRun(dryRun)
        .verbose(verbose)
        .quiet(quiet)
        .noColor(noColor)
        .includeHidden(includeHidden)
        .backup(backup)
        .noLimits(noLimits);
    executeTrim(builder.build());
  }

  @Command(command = "trim-config", description = "Process files using a configuration file")
  public void trimWithConfig(
      @Option(description = "Directory path to process") String directory,
      @Option(description = "Path to configuration file") String configFile,
      @Option(description = "Report format (json, csv, sqlite)") String report,
      @Option(description = "Report output path") String reportOutput,
      @Option(description = "HTTP endpoint for report") String reportEndpoint,
      @Option(description = "Show diff output in dry-run mode") boolean diff) {

    TrimOptions.Builder builder = TrimOptions.builder()
        .directory(directory)
        .backup(true)
        .configFile(configFile)
        .report(report)
        .reportOutput(reportOutput)
        .reportEndpoint(reportEndpoint)
        .diff(diff);
    executeTrim(builder.build());
  }

  private void executeTrim(TrimOptions options) {
    // Load configuration file if specified
    if (options.getConfigFile() != null && !options.getConfigFile().isEmpty()) {
      try {
        TrimmerConfig fileConfig = configLoader.loadFromFile(
            java.nio.file.Paths.get(options.getConfigFile()));
        configLoader.applyConfiguration(fileConfig, config);
      } catch (CodeTrimmerException e) {
        System.err.println(e.getFormattedMessage());
        return;
      }
    } else if (options.getDirectory() != null) {
      // Try to load from directory
      TrimmerConfig dirConfig = configLoader.loadConfiguration(options.getDirectory());
      configLoader.applyConfiguration(dirConfig, config);
    }

    configureSettings(options);
    boolean shouldUseColor = !options.isNoColor() && ColorOutput.isTerminal();
    ColorOutput output = new ColorOutput(shouldUseColor);

    displayStartMessage(options, output);

    List<FileProcessingResult> results = fileProcessingService.processDirectory(options.getDirectory());
    ProcessingStatistics stats = fileProcessingService.getStatistics();

    displayResults(results, stats, output, options.isVerbose(), options.isQuiet(), options.isDryRun());

    // Generate report if requested
    if (options.getReport() != null && !options.getReport().isEmpty()) {
      reportGenerator.generateReport(stats, options.getReport(), options.getReportOutput());
    }

    // Send to endpoint if specified
    if (options.getReportEndpoint() != null && !options.getReportEndpoint().isEmpty()) {
      reportGenerator.sendToEndpoint(stats, options.getReportEndpoint());
    }
  }

  private void configureSettings(TrimOptions options) {
    config.setInclude(options.getInclude() != null && !options.getInclude().isEmpty() ? options.getInclude() : "*");
    if (options.getExclude() != null && !options.getExclude().isEmpty()) {
      config.setExclude(options.getExclude());
    }
    config.setMaxFileSize(options.getMaxSize() > 0 ? options.getMaxSize() : 5242880);
    config.setMaxFiles(options.getMaxFiles() > 0 ? options.getMaxFiles() : 50);
    config.setDryRun(options.isDryRun());
    config.setVerbose(options.isVerbose());
    config.setQuiet(options.isQuiet());
    config.setNoColor(options.isNoColor());
    config.setIncludeHidden(options.isIncludeHidden());
    config.setCreateBackups(options.isBackup());
    config.setNoLimits(options.isNoLimits());
  }

  private void displayStartMessage(TrimOptions options, ColorOutput output) {
    if (!options.isQuiet()) {
      System.out.println(output.info("Starting file processing..."));
      System.out.println(output.info("Directory: " + options.getDirectory()));
      System.out.println(output.info("Include: " + options.getInclude()));
      if (options.getExclude() != null && !options.getExclude().isEmpty()) {
        System.out.println(output.info("Exclude: " + options.getExclude()));
      }
      if (options.isDryRun()) {
        System.out.println(output.warning("DRY-RUN MODE: No files will be modified"));
      }
      System.out.println();
    }
  }

  @Command(command = "version", description = "Show application version")
  public void version() {
    System.out.println("Code Trimmer v2.0.0");
  }

  @Command(command = "generate-hook", description = "Generate Git pre-commit hook")
  public void generateHook(
      @Option(description = "Directory containing .git folder") String directory,
      @Option(description = "Force overwrite existing hook") boolean force) {

    String targetDir = directory != null ? directory : System.getProperty("user.dir");
    boolean shouldUseColor = ColorOutput.isTerminal();
    ColorOutput output = new ColorOutput(shouldUseColor);

    try {
      Path hookPath = hookGenerator.generatePreCommitHook(targetDir, force);
      System.out.println(output.success("Pre-commit hook generated: " + hookPath));
      System.out.println(output.info("Also generated: pre-commit.bat, pre-commit.ps1"));
      System.out.println(output.info("\nThe hook will run Code Trimmer on staged files before commit."));
    } catch (CodeTrimmerException e) {
      System.err.println(output.error(e.getFormattedMessage()));
    }
  }

  @Command(command = "undo", description = "Restore files from backup")
  public void undo(
      @Option(description = "Directory to restore backups from") String directory,
      @Option(description = "Regex pattern to match files") String pattern,
      @Option(description = "Search subdirectories recursively") boolean recursive) {

    String targetDir = directory != null ? directory : System.getProperty("user.dir");
    boolean shouldUseColor = ColorOutput.isTerminal();
    ColorOutput output = new ColorOutput(shouldUseColor);

    try {
      UndoService.RestoreResult result = undoService.restoreDirectory(targetDir, recursive, pattern);

      System.out.println(output.info("=== Restore Summary ==="));
      System.out.println("Backup files found: " + result.getTotalFound());
      System.out.println("Files restored: " + output.success(String.valueOf(result.getRestoredCount())));

      if (result.getFailedCount() > 0) {
        System.out.println("Files failed: " + output.error(String.valueOf(result.getFailedCount())));
        for (UndoService.FailedRestore failed : result.getFailed()) {
          System.out.println("  " + output.error("✗") + " " + failed.getPath() +
                           " (" + failed.getReason() + ")");
        }
      }

      if (result.isSuccess()) {
        System.out.println(output.success("\nAll backups restored successfully."));
      } else if (result.isPartialSuccess()) {
        System.out.println(output.warning("\nSome files could not be restored."));
      }
    } catch (CodeTrimmerException e) {
      System.err.println(output.error(e.getFormattedMessage()));
    }
  }

  @Command(command = "list-backups", description = "List available backup files")
  public void listBackups(
      @Option(description = "Directory to search") String directory,
      @Option(description = "Search subdirectories recursively") boolean recursive) {

    String targetDir = directory != null ? directory : System.getProperty("user.dir");
    boolean shouldUseColor = ColorOutput.isTerminal();
    ColorOutput output = new ColorOutput(shouldUseColor);

    List<String> backups = undoService.listBackups(targetDir, recursive);

    if (backups.isEmpty()) {
      System.out.println(output.info("No backup files found."));
    } else {
      System.out.println(output.info("Backup Files (" + backups.size() + "):"));
      for (String backup : backups) {
        System.out.println("  " + backup);
      }
    }
  }

  @Command(command = "validate-config", description = "Validate a configuration file")
  public void validateConfig(
      @Option(description = "Path to configuration file") String configPath) {

    boolean shouldUseColor = ColorOutput.isTerminal();
    ColorOutput output = new ColorOutput(shouldUseColor);

    if (configPath == null || configPath.isEmpty()) {
      System.err.println(output.error("Error: --config-path is required"));
      return;
    }

    try {
      TrimmerConfig loadedConfig = configLoader.loadFromFile(java.nio.file.Paths.get(configPath));
      System.out.println(output.success("Configuration file is valid."));
      System.out.println(output.info("Settings loaded:"));
      System.out.println("  Include: " + loadedConfig.getInclude());
      System.out.println("  Exclude: " + loadedConfig.getExclude());
      System.out.println("  Max file size: " + loadedConfig.getMaxFileSize());
      System.out.println("  Max files: " + loadedConfig.getMaxFiles());
      System.out.println("  Custom rules: " + loadedConfig.getRules().size());
    } catch (CodeTrimmerException e) {
      System.err.println(output.error(e.getFormattedMessage()));
    }
  }

  @Command(command = "help-trim", description = "Show help for trim command")
  public void helpTrim() {
    System.out.println("Trim Command - Clean up whitespace in files\n");
    System.out.println("Usage: trim [directory] [options]\n");
    System.out.println("Arguments:");
    System.out.println("  directory                Path to directory to process (required)\n");
    System.out.println("Options:");
    System.out.println("  --include <extensions>   Comma-separated list of extensions to include (default: *)");
    System.out.println("  --exclude <extensions>   Comma-separated list of extensions to exclude");
    System.out.println("  --max-size <bytes>       Maximum file size to process (default: 5242880)");
    System.out.println("  --max-files <count>      Maximum number of files to process (default: 50)");
    System.out.println("  --dry-run                Preview changes without modifying files");
    System.out.println("  --verbose                Show detailed processing information");
    System.out.println("  --quiet                  Suppress non-error output");
    System.out.println("  --no-color               Disable colored output");
    System.out.println("  --include-hidden         Process hidden files");
    System.out.println("  --backup                 Create backup files (default: true)");
    System.out.println("  --no-limits              Disable file size and count limits\n");
    System.out.println("Examples:");
    System.out.println("  trim /path/to/project");
    System.out.println("  trim /path/to/project --include \"js,py,md\"");
    System.out.println("  trim /path/to/project --exclude \"min.js,lock\" --dry-run");
  }

  private void displayResults(List<FileProcessingResult> results,
                              ProcessingStatistics stats,
                              ColorOutput output,
                              boolean verbose,
                              boolean quiet,
                              boolean dryRun) {

    if (!quiet) {
      // Display modified files
      List<FileProcessingResult> modified = results.stream()
          .filter(FileProcessingResult::isModified)
          .toList();

      if (!modified.isEmpty()) {
        System.out.println(output.success("Modified Files (" + modified.size() + "):"));
        for (FileProcessingResult result : modified) {
          System.out.println("  " + output.success("✓") + " " + result.getFilePath());
          if (verbose) {
            System.out.println("    Lines trimmed: " + result.getLinesTrimmed());
            System.out.println("    Blank lines removed: " + result.getBlankLinesRemoved());
          }
        }
        System.out.println();
      }

      // Display skipped files in verbose mode
      if (verbose) {
        List<FileProcessingResult> skipped = results.stream()
            .filter(FileProcessingResult::isSkipped)
            .toList();

        if (!skipped.isEmpty()) {
          System.out.println(output.warning("Skipped Files (" + skipped.size() + "):"));
          for (FileProcessingResult result : skipped) {
            System.out.println("  " + output.warning("⊘") + " " + result.getFilePath() +
                             " (" + result.getSkipReason() + ")");
          }
          System.out.println();
        }
      }

      // Display summary statistics
      System.out.println(output.info("=== Processing Summary ==="));
      System.out.println("Total files scanned: " + stats.getFilesScanned());
      System.out.println("Files modified: " + output.success(String.valueOf(stats.getFilesModified())));
      System.out.println("Files skipped: " + stats.getFilesSkipped());
      System.out.println("Total lines trimmed: " + stats.getLinesTrimmed());
      System.out.println("Total blank lines removed: " + stats.getBlankLinesRemoved());
      System.out.println("Execution time: " + String.format("%.2f", stats.getExecutionTimeSec()) + "s");

      if (dryRun) {
        System.out.println(output.warning("\nDRY-RUN: No files were actually modified"));
      }
    }
  }
}
