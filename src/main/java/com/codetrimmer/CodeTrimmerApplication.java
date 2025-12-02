package com.codetrimmer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for Code Trimmer application.
 * Spring Boot application that provides a shell interface for file cleanup operations.
 */
@SpringBootApplication
public class CodeTrimmerApplication {

  public static void main(String[] args) {
    SpringApplication.run(CodeTrimmerApplication.class, args);
  }
}
