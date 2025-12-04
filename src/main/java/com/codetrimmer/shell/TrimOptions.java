package com.codetrimmer.shell;

/**
 * Parameter object for trim command options.
 * Reduces cyclomatic complexity and parameter count violations using builder pattern.
 */
public final class TrimOptions {

  private final String directory;
  private final String include;
  private final String exclude;
  private final long maxSize;
  private final int maxFiles;
  private final boolean dryRun;
  private final boolean verbose;
  private final boolean quiet;
  private final boolean noColor;
  private final boolean includeHidden;
  private final boolean backup;
  private final boolean noLimits;
  private final String configFile;
  private final String report;
  private final String reportOutput;
  private final String reportEndpoint;
  private final boolean diff;

  private TrimOptions(Builder builder) {
    this.directory = builder.directory;
    this.include = builder.include;
    this.exclude = builder.exclude;
    this.maxSize = builder.maxSize;
    this.maxFiles = builder.maxFiles;
    this.dryRun = builder.dryRun;
    this.verbose = builder.verbose;
    this.quiet = builder.quiet;
    this.noColor = builder.noColor;
    this.includeHidden = builder.includeHidden;
    this.backup = builder.backup;
    this.noLimits = builder.noLimits;
    this.configFile = builder.configFile;
    this.report = builder.report;
    this.reportOutput = builder.reportOutput;
    this.reportEndpoint = builder.reportEndpoint;
    this.diff = builder.diff;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getDirectory() {
    return directory;
  }

  public String getInclude() {
    return include;
  }

  public String getExclude() {
    return exclude;
  }

  public long getMaxSize() {
    return maxSize;
  }

  public int getMaxFiles() {
    return maxFiles;
  }

  public boolean isDryRun() {
    return dryRun;
  }

  public boolean isVerbose() {
    return verbose;
  }

  public boolean isQuiet() {
    return quiet;
  }

  public boolean isNoColor() {
    return noColor;
  }

  public boolean isIncludeHidden() {
    return includeHidden;
  }

  public boolean isBackup() {
    return backup;
  }

  public boolean isNoLimits() {
    return noLimits;
  }

  public String getConfigFile() {
    return configFile;
  }

  public String getReport() {
    return report;
  }

  public String getReportOutput() {
    return reportOutput;
  }

  public String getReportEndpoint() {
    return reportEndpoint;
  }

  public boolean isDiff() {
    return diff;
  }

  /**
   * Builder for TrimOptions to avoid constructor with many parameters.
   */
  public static class Builder {
    private String directory;
    private String include;
    private String exclude;
    private long maxSize;
    private int maxFiles;
    private boolean dryRun;
    private boolean verbose;
    private boolean quiet;
    private boolean noColor;
    private boolean includeHidden;
    private boolean backup;
    private boolean noLimits;
    private String configFile;
    private String report;
    private String reportOutput;
    private String reportEndpoint;
    private boolean diff;

    public Builder directory(String directory) {
      this.directory = directory;
      return this;
    }

    public Builder include(String include) {
      this.include = include;
      return this;
    }

    public Builder exclude(String exclude) {
      this.exclude = exclude;
      return this;
    }

    public Builder maxSize(long maxSize) {
      this.maxSize = maxSize;
      return this;
    }

    public Builder maxFiles(int maxFiles) {
      this.maxFiles = maxFiles;
      return this;
    }

    public Builder dryRun(boolean dryRun) {
      this.dryRun = dryRun;
      return this;
    }

    public Builder verbose(boolean verbose) {
      this.verbose = verbose;
      return this;
    }

    public Builder quiet(boolean quiet) {
      this.quiet = quiet;
      return this;
    }

    public Builder noColor(boolean noColor) {
      this.noColor = noColor;
      return this;
    }

    public Builder includeHidden(boolean includeHidden) {
      this.includeHidden = includeHidden;
      return this;
    }

    public Builder backup(boolean backup) {
      this.backup = backup;
      return this;
    }

    public Builder noLimits(boolean noLimits) {
      this.noLimits = noLimits;
      return this;
    }

    public Builder configFile(String configFile) {
      this.configFile = configFile;
      return this;
    }

    public Builder report(String report) {
      this.report = report;
      return this;
    }

    public Builder reportOutput(String reportOutput) {
      this.reportOutput = reportOutput;
      return this;
    }

    public Builder reportEndpoint(String reportEndpoint) {
      this.reportEndpoint = reportEndpoint;
      return this;
    }

    public Builder diff(boolean diff) {
      this.diff = diff;
      return this;
    }

    public TrimOptions build() {
      return new TrimOptions(this);
    }
  }
}
