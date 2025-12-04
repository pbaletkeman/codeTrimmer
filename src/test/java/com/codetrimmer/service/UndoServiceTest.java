package com.codetrimmer.service;

import static org.junit.jupiter.api.Assertions.*;

import com.codetrimmer.error.CodeTrimmerException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for UndoService.
 */
class UndoServiceTest {

    private UndoService undoService;

    @TempDir
    private Path tempDir;

    @BeforeEach
    void setUp() {
        undoService = new UndoService();
    }

    @Test
    void testRestoreSingleFile() throws IOException {
        // Create original and backup files
        Path original = tempDir.resolve("test.txt");
        Path backup = tempDir.resolve("test.txt.bak");

        Files.writeString(original, "modified content");
        Files.writeString(backup, "original content");

        boolean result = undoService.restoreFile(original.toString());

        assertTrue(result);
        assertEquals("original content", Files.readString(original));
        assertFalse(Files.exists(backup));
    }

    @Test
    void testRestoreFileNoBackup() {
        Path original = tempDir.resolve("nobackup.txt");
        assertThrows(CodeTrimmerException.class, () -> {
            undoService.restoreFile(original.toString());
        });
    }

    @Test
    void testRestoreDirectory() throws IOException {
        // Create files with backups
        Files.writeString(tempDir.resolve("file1.txt"), "modified1");
        Files.writeString(tempDir.resolve("file1.txt.bak"), "original1");
        Files.writeString(tempDir.resolve("file2.txt"), "modified2");
        Files.writeString(tempDir.resolve("file2.txt.bak"), "original2");

        UndoService.RestoreResult result = undoService.restoreDirectory(
            tempDir.toString(), false);

        assertEquals(2, result.getTotalFound());
        assertEquals(2, result.getRestoredCount());
        assertEquals(0, result.getFailedCount());
        assertTrue(result.isSuccess());
        assertEquals(0, result.getExitCode());
    }

    @Test
    void testRestoreDirectoryRecursive() throws IOException {
        // Create subdirectory with backup
        Path subDir = tempDir.resolve("subdir");
        Files.createDirectories(subDir);
        Files.writeString(subDir.resolve("nested.txt"), "modified");
        Files.writeString(subDir.resolve("nested.txt.bak"), "original");

        UndoService.RestoreResult result = undoService.restoreDirectory(
            tempDir.toString(), true);

        assertEquals(1, result.getTotalFound());
        assertEquals(1, result.getRestoredCount());
    }

    @Test
    void testRestoreDirectoryNonRecursive() throws IOException {
        // Create subdirectory with backup
        Path subDir = tempDir.resolve("subdir");
        Files.createDirectories(subDir);
        Files.writeString(subDir.resolve("nested.txt.bak"), "original");

        UndoService.RestoreResult result = undoService.restoreDirectory(
            tempDir.toString(), false);

        assertEquals(0, result.getTotalFound());
    }

    @Test
    void testRestoreDirectoryWithPattern() throws IOException {
        Files.writeString(tempDir.resolve("test.java.bak"), "java");
        Files.writeString(tempDir.resolve("test.py.bak"), "python");

        UndoService.RestoreResult result = undoService.restoreDirectory(
            tempDir.toString(), false, ".*\\.java");

        assertEquals(1, result.getRestoredCount());
    }

    @Test
    void testRestoreDirectoryNotFound() {
        Path nonExistent = tempDir.resolve("nonexistent");
        assertThrows(CodeTrimmerException.class, () -> {
            undoService.restoreDirectory(nonExistent.toString(), false);
        });
    }

    @Test
    void testRestoreDirectoryNotADirectory() throws IOException {
        Path file = tempDir.resolve("file.txt");
        Files.writeString(file, "content");

        assertThrows(CodeTrimmerException.class, () -> {
            undoService.restoreDirectory(file.toString(), false);
        });
    }

    @Test
    void testListBackups() throws IOException {
        Files.writeString(tempDir.resolve("file1.txt.bak"), "backup1");
        Files.writeString(tempDir.resolve("file2.txt.bak"), "backup2");
        Files.writeString(tempDir.resolve("file3.txt"), "not backup");

        List<String> backups = undoService.listBackups(tempDir.toString(), false);

        assertEquals(2, backups.size());
    }

    @Test
    void testListBackupsEmpty() {
        List<String> backups = undoService.listBackups(tempDir.toString(), false);
        assertTrue(backups.isEmpty());
    }

    @Test
    void testListBackupsNonExistentDirectory() {
        List<String> backups = undoService.listBackups(
            tempDir.resolve("nonexistent").toString(), false);
        assertTrue(backups.isEmpty());
    }

    @Test
    void testRestoreResultPartialSuccess() throws IOException {
        // Create one valid backup
        Files.writeString(tempDir.resolve("valid.txt"), "modified");
        Files.writeString(tempDir.resolve("valid.txt.bak"), "original");

        // Create backup without original (will still work - creates original)
        Files.writeString(tempDir.resolve("orphan.txt.bak"), "orphan");

        UndoService.RestoreResult result = undoService.restoreDirectory(
            tempDir.toString(), false);

        assertEquals(2, result.getTotalFound());
        assertEquals(2, result.getRestoredCount());
    }

    @Test
    void testRestoreResultExitCodes() {
        UndoService.RestoreResult result = new UndoService.RestoreResult();
        result.setTotalFound(2);

        // All success
        result.addRestored("file1");
        result.addRestored("file2");
        assertEquals(0, result.getExitCode());

        // Reset and test partial
        result = new UndoService.RestoreResult();
        result.setTotalFound(2);
        result.addRestored("file1");
        result.addFailed("file2", "error");
        assertEquals(1, result.getExitCode());
        assertTrue(result.isPartialSuccess());

        // Reset and test all failed
        result = new UndoService.RestoreResult();
        result.setTotalFound(2);
        result.addFailed("file1", "error1");
        result.addFailed("file2", "error2");
        assertEquals(2, result.getExitCode());
    }
}
