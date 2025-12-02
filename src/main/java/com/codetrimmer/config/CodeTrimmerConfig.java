package com.codetrimmer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for Code Trimmer application.
 * Allows settings via application.properties or environment variables.
 */
@Component
@ConfigurationProperties(prefix = "codetrimmer")
@Data
public class CodeTrimmerConfig {

  // File filtering
  private String include = "*";
  private String exclude = "";
  private boolean includeHidden = false;
  private boolean followSymlinks = false;

  // Whitespace rules
  private int maxConsecutiveBlankLines = 2;
  private boolean ensureFinalNewline = true;
  private boolean trimTrailingWhitespace = true;

  // Performance limits
  private long maxFileSize = 5242880; // 5MB in bytes
  private int maxFiles = 50;
  private boolean noLimits = false;

  // Operation modes
  private boolean dryRun = false;
  private boolean createBackups = true;
  private boolean failFast = false;

  // Output options
  private boolean verbose = false;
  private boolean quiet = false;
  private boolean noColor = false;
  private boolean disableColorForPipe = true;
}
