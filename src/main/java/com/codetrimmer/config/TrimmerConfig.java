package com.codetrimmer.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for .trimmerrc configuration file.
 * Supports both YAML and JSON formats.
 */
public class TrimmerConfig {

    private String include = "*";
    private String exclude = "";
    private boolean includeHidden = false;
    private boolean followSymlinks = false;
    private int maxConsecutiveBlankLines = 2;
    private boolean ensureFinalNewline = true;
    private boolean trimTrailingWhitespace = true;
    private long maxFileSize = 5242880;
    private int maxFiles = 50;
    private boolean noLimits = false;
    private boolean dryRun = false;
    private boolean createBackups = true;
    private boolean failFast = false;
    private boolean verbose = false;
    private boolean quiet = false;
    private boolean noColor = false;
    private List<TrimRule> rules = new ArrayList<>();
    private ReportConfig report = new ReportConfig();

    // Getters and setters

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    public boolean isIncludeHidden() {
        return includeHidden;
    }

    public void setIncludeHidden(boolean includeHidden) {
        this.includeHidden = includeHidden;
    }

    public boolean isFollowSymlinks() {
        return followSymlinks;
    }

    public void setFollowSymlinks(boolean followSymlinks) {
        this.followSymlinks = followSymlinks;
    }

    public int getMaxConsecutiveBlankLines() {
        return maxConsecutiveBlankLines;
    }

    public void setMaxConsecutiveBlankLines(int maxConsecutiveBlankLines) {
        this.maxConsecutiveBlankLines = maxConsecutiveBlankLines;
    }

    public boolean isEnsureFinalNewline() {
        return ensureFinalNewline;
    }

    public void setEnsureFinalNewline(boolean ensureFinalNewline) {
        this.ensureFinalNewline = ensureFinalNewline;
    }

    public boolean isTrimTrailingWhitespace() {
        return trimTrailingWhitespace;
    }

    public void setTrimTrailingWhitespace(boolean trimTrailingWhitespace) {
        this.trimTrailingWhitespace = trimTrailingWhitespace;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public int getMaxFiles() {
        return maxFiles;
    }

    public void setMaxFiles(int maxFiles) {
        this.maxFiles = maxFiles;
    }

    public boolean isNoLimits() {
        return noLimits;
    }

    public void setNoLimits(boolean noLimits) {
        this.noLimits = noLimits;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }

    public boolean isCreateBackups() {
        return createBackups;
    }

    public void setCreateBackups(boolean createBackups) {
        this.createBackups = createBackups;
    }

    public boolean isFailFast() {
        return failFast;
    }

    public void setFailFast(boolean failFast) {
        this.failFast = failFast;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public boolean isNoColor() {
        return noColor;
    }

    public void setNoColor(boolean noColor) {
        this.noColor = noColor;
    }

    public List<TrimRule> getRules() {
        return rules;
    }

    public void setRules(List<TrimRule> rules) {
        this.rules = rules;
    }

    public ReportConfig getReport() {
        return report;
    }

    public void setReport(ReportConfig report) {
        this.report = report;
    }

    /**
     * Custom trimming rule definition.
     */
    public static class TrimRule {
        private String name;
        private String description;
        private String pattern;
        private String replacement;
        private String action;
        private boolean enabled = true;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public String getReplacement() {
            return replacement;
        }

        public void setReplacement(String replacement) {
            this.replacement = replacement;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * Report configuration settings.
     */
    public static class ReportConfig {
        private String format;
        private String outputPath;
        private String endpoint;

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getOutputPath() {
            return outputPath;
        }

        public void setOutputPath(String outputPath) {
            this.outputPath = outputPath;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
    }
}
