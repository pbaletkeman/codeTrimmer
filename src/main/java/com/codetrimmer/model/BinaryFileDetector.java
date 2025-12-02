package com.codetrimmer.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class for detecting binary files.
 */
public final class BinaryFileDetector {

  private static final int BUFFER_SIZE = 8192;
  private static final byte[] BINARY_MARKERS = {
    0x00, // NULL byte - strong indicator of binary
  };

  private BinaryFileDetector() {
    // Utility class
  }

  /**
   * Detects if a file is binary by checking for null bytes in the first 8KB.
   *
   * @param path the file path to check
   * @return true if the file appears to be binary, false if it appears to be text
   */
  public static boolean isBinary(Path path) {
    try {
      byte[] buffer = Files.readAllBytes(path);
      int bytesRead = Math.min(buffer.length, BUFFER_SIZE);

      for (int i = 0; i < bytesRead; i++) {
        if (buffer[i] == 0x00) {
          return true; // Null byte indicates binary
        }
      }
      return false;
    } catch (IOException e) {
      // If we can't read it, assume it's binary to be safe
      return true;
    }
  }

  /**
   * Checks if a file is likely to be a common binary file type based on extension.
   *
   * @param path the file path to check
   * @return true if the file extension suggests it's binary
   */
  public static boolean isBinaryByExtension(Path path) {
    String filename = path.getFileName().toString().toLowerCase();

    // Common binary file extensions
    String[] binaryExtensions = {
      ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".ico", ".webp",
      ".mp3", ".mp4", ".wav", ".avi", ".mov", ".mkv", ".flv",
      ".exe", ".dll", ".so", ".dylib", ".bin", ".com", ".msi",
      ".zip", ".rar", ".7z", ".gz", ".tar", ".jar", ".war", ".ear",
      ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
      ".class", ".o", ".pyc", ".pyo", ".swp", ".swo",
      ".git", ".lock", ".cache"
    };

    for (String ext : binaryExtensions) {
      if (filename.endsWith(ext)) {
        return true;
      }
    }
    return false;
  }
}
