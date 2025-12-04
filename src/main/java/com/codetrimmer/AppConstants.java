package com.codetrimmer;

/**
 * Application-wide constants for Code Trimmer.
 */
public final class AppConstants {

    private AppConstants() {
        // Utility class - prevent instantiation
    }

    /**
     * Current application version.
     */
    public static final String VERSION = "2.0.0";

    /**
     * Application name.
     */
    public static final String APP_NAME = "Code Trimmer";

    /**
     * JAR file name pattern.
     */
    public static final String JAR_NAME = "code-trimmer";

    /**
     * Full version string for display.
     */
    public static final String VERSION_STRING = APP_NAME + " v" + VERSION;
}
