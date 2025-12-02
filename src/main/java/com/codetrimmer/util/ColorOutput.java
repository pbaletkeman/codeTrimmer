package com.codetrimmer.util;

/**
 * Utility for ANSI color output to terminal.
 */
public class ColorOutput {

  public static final String RESET = "\u001B[0m";
  public static final String RED = "\u001B[31m";
  public static final String GREEN = "\u001B[32m";
  public static final String YELLOW = "\u001B[33m";
  public static final String BLUE = "\u001B[34m";
  public static final String CYAN = "\u001B[36m";

  private final boolean useColor;

  public ColorOutput(boolean useColor) {
    this.useColor = useColor;
  }

  public String success(String message) {
    return useColor ? GREEN + message + RESET : message;
  }

  public String error(String message) {
    return useColor ? RED + message + RESET : message;
  }

  public String warning(String message) {
    return useColor ? YELLOW + message + RESET : message;
  }

  public String info(String message) {
    return useColor ? CYAN + message + RESET : message;
  }

  public String debug(String message) {
    return useColor ? BLUE + message + RESET : message;
  }

  public static boolean isTerminal() {
    return System.console() != null;
  }
}
