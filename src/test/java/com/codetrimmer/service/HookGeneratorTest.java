package com.codetrimmer.service;

import static org.junit.jupiter.api.Assertions.*;

import com.codetrimmer.error.CodeTrimmerException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for HookGenerator service.
 */
class HookGeneratorTest {

    private HookGenerator hookGenerator;

    @TempDir
    private Path tempDir;

    @BeforeEach
    void setUp() {
        hookGenerator = new HookGenerator();
    }

    @Test
    void testGeneratePreCommitHook() throws IOException {
        // Create .git directory
        Path gitDir = tempDir.resolve(".git");
        Files.createDirectories(gitDir);

        Path hookPath = hookGenerator.generatePreCommitHook(tempDir.toString(), false);

        assertNotNull(hookPath);
        assertTrue(Files.exists(hookPath));
        assertTrue(hookPath.toString().contains("pre-commit"));
    }

    @Test
    void testGenerateHookContent() throws IOException {
        Path gitDir = tempDir.resolve(".git");
        Files.createDirectories(gitDir);

        Path hookPath = hookGenerator.generatePreCommitHook(tempDir.toString(), false);
        String content = Files.readString(hookPath);

        assertTrue(content.contains("#!/bin/bash"));
        assertTrue(content.contains("Code Trimmer"));
        assertTrue(content.contains("git diff --cached"));
    }

    @Test
    void testGenerateWindowsBatchHook() throws IOException {
        Path gitDir = tempDir.resolve(".git");
        Files.createDirectories(gitDir);

        hookGenerator.generatePreCommitHook(tempDir.toString(), false);

        Path batchPath = tempDir.resolve(".git/hooks/pre-commit.bat");
        assertTrue(Files.exists(batchPath));

        String content = Files.readString(batchPath);
        assertTrue(content.contains("@echo off"));
        assertTrue(content.contains("Code Trimmer"));
    }

    @Test
    void testGeneratePowerShellHook() throws IOException {
        Path gitDir = tempDir.resolve(".git");
        Files.createDirectories(gitDir);

        hookGenerator.generatePreCommitHook(tempDir.toString(), false);

        Path ps1Path = tempDir.resolve(".git/hooks/pre-commit.ps1");
        assertTrue(Files.exists(ps1Path));

        String content = Files.readString(ps1Path);
        assertTrue(content.contains("Code Trimmer"));
        assertTrue(content.contains("Write-Host"));
    }

    @Test
    void testNoGitDirectoryThrowsException() {
        assertThrows(CodeTrimmerException.class, () -> {
            hookGenerator.generatePreCommitHook(tempDir.toString(), false);
        });
    }

    @Test
    void testHookExistsWithoutForce() throws IOException {
        Path gitDir = tempDir.resolve(".git");
        Path hooksDir = gitDir.resolve("hooks");
        Files.createDirectories(hooksDir);
        Files.writeString(hooksDir.resolve("pre-commit"), "existing hook");

        assertThrows(CodeTrimmerException.class, () -> {
            hookGenerator.generatePreCommitHook(tempDir.toString(), false);
        });
    }

    @Test
    void testHookExistsWithForce() throws IOException {
        Path gitDir = tempDir.resolve(".git");
        Path hooksDir = gitDir.resolve("hooks");
        Files.createDirectories(hooksDir);
        Files.writeString(hooksDir.resolve("pre-commit"), "existing hook");

        Path hookPath = hookGenerator.generatePreCommitHook(tempDir.toString(), true);

        assertNotNull(hookPath);
        String content = Files.readString(hookPath);
        assertTrue(content.contains("Code Trimmer"));
        assertFalse(content.contains("existing hook"));
    }

    @Test
    void testCreatesHooksDirectory() throws IOException {
        Path gitDir = tempDir.resolve(".git");
        Files.createDirectories(gitDir);
        // Hooks directory doesn't exist

        Path hookPath = hookGenerator.generatePreCommitHook(tempDir.toString(), false);

        assertTrue(Files.exists(hookPath.getParent()));
        assertTrue(Files.isDirectory(hookPath.getParent()));
    }
}
