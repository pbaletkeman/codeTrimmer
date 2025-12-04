package com.codetrimmer.service;

import com.codetrimmer.error.CodeTrimmerException;
import com.codetrimmer.error.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Service for restoring files from backups.
 * Supports undo operations using .bak files.
 */
@Service
public class UndoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UndoService.class);
    private static final String BACKUP_EXTENSION = ".bak";

    /**
     * Restores a single file from its backup.
     *
     * @param filePath the original file path (without .bak extension)
     * @return true if restore succeeded
     */
    public boolean restoreFile(String filePath) {
        Path original = Paths.get(filePath);
        Path backup = Paths.get(filePath + BACKUP_EXTENSION);

        if (!Files.exists(backup)) {
            throw new CodeTrimmerException(
                ErrorCode.CT_0042,
                "Backup file not found: " + backup,
                "Ensure backups were created (--backup flag) during trimming"
            );
        }

        try {
            Files.copy(backup, original, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(backup);
            LOGGER.info("Restored: {} from backup", original);
            return true;
        } catch (IOException e) {
            throw new CodeTrimmerException(
                ErrorCode.CT_0041,
                "Failed to restore file: " + e.getMessage(),
                "Check file permissions and disk space"
            );
        }
    }

    /**
     * Restores all backup files in a directory.
     *
     * @param directory the directory to search for backups
     * @param recursive if true, search subdirectories
     * @return result of the restore operation
     */
    public RestoreResult restoreDirectory(String directory, boolean recursive) {
        return restoreDirectory(directory, recursive, null);
    }

    /**
     * Restores backup files matching a pattern in a directory.
     *
     * @param directory the directory to search for backups
     * @param recursive if true, search subdirectories
     * @param pattern regex pattern to match file names (null for all)
     * @return result of the restore operation
     */
    public RestoreResult restoreDirectory(String directory, boolean recursive, String pattern) {
        Path dirPath = Paths.get(directory);

        if (!Files.exists(dirPath)) {
            throw new CodeTrimmerException(
                ErrorCode.CT_0010,
                "Directory not found: " + directory,
                "Verify the directory path exists"
            );
        }

        if (!Files.isDirectory(dirPath)) {
            throw new CodeTrimmerException(
                ErrorCode.CT_0014,
                "Not a directory: " + directory,
                "Specify a directory path"
            );
        }

        Pattern filePattern = pattern != null ? Pattern.compile(pattern) : null;
        RestoreResult result = new RestoreResult();

        try {
            List<Path> backupFiles = findBackupFiles(dirPath, recursive, filePattern);
            result.setTotalFound(backupFiles.size());

            for (Path backup : backupFiles) {
                String originalPath = backup.toString();
                originalPath = originalPath.substring(0, originalPath.length() - BACKUP_EXTENSION.length());

                try {
                    Path original = Paths.get(originalPath);
                    Files.copy(backup, original, StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(backup);
                    result.addRestored(originalPath);
                    LOGGER.info("Restored: {}", original);
                } catch (IOException e) {
                    result.addFailed(originalPath, e.getMessage());
                    LOGGER.error("Failed to restore: {}", originalPath, e);
                }
            }

        } catch (IOException e) {
            throw new CodeTrimmerException(ErrorCode.CT_0014, e);
        }

        return result;
    }

    /**
     * Finds all backup files in a directory.
     *
     * @param directory the directory to search
     * @param recursive if true, search subdirectories
     * @param pattern optional file name pattern
     * @return list of backup file paths
     */
    private List<Path> findBackupFiles(Path directory, boolean recursive, Pattern pattern)
            throws IOException {
        List<Path> backups = new ArrayList<>();

        int maxDepth = recursive ? Integer.MAX_VALUE : 1;

        try (Stream<Path> stream = Files.walk(directory, maxDepth)) {
            stream.filter(Files::isRegularFile)
                  .filter(p -> p.toString().endsWith(BACKUP_EXTENSION))
                  .filter(p -> matchesPattern(p, pattern))
                  .forEach(backups::add);
        }

        return backups;
    }

    /**
     * Checks if a file name matches the given pattern.
     *
     * @param path the file path
     * @param pattern the regex pattern (null matches all)
     * @return true if matches
     */
    private boolean matchesPattern(Path path, Pattern pattern) {
        if (pattern == null) {
            return true;
        }
        String fileName = path.getFileName().toString();
        // Remove .bak extension for matching
        fileName = fileName.substring(0, fileName.length() - BACKUP_EXTENSION.length());
        return pattern.matcher(fileName).matches();
    }

    /**
     * Lists all backup files in a directory without restoring them.
     *
     * @param directory the directory to search
     * @param recursive if true, search subdirectories
     * @return list of backup file paths
     */
    public List<String> listBackups(String directory, boolean recursive) {
        Path dirPath = Paths.get(directory);

        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            return new ArrayList<>();
        }

        try {
            return findBackupFiles(dirPath, recursive, null)
                .stream()
                .map(Path::toString)
                .toList();
        } catch (IOException e) {
            LOGGER.error("Error listing backups", e);
            return new ArrayList<>();
        }
    }

    /**
     * Result of a restore operation.
     */
    public static class RestoreResult {
        private int totalFound;
        private final List<String> restored;
        private final List<FailedRestore> failed;

        public RestoreResult() {
            this.restored = new ArrayList<>();
            this.failed = new ArrayList<>();
        }

        public int getTotalFound() {
            return totalFound;
        }

        public void setTotalFound(int totalFound) {
            this.totalFound = totalFound;
        }

        public List<String> getRestored() {
            return restored;
        }

        public void addRestored(String path) {
            restored.add(path);
        }

        public List<FailedRestore> getFailed() {
            return failed;
        }

        public void addFailed(String path, String reason) {
            failed.add(new FailedRestore(path, reason));
        }

        public int getRestoredCount() {
            return restored.size();
        }

        public int getFailedCount() {
            return failed.size();
        }

        public boolean isSuccess() {
            return failed.isEmpty();
        }

        public boolean isPartialSuccess() {
            return !restored.isEmpty() && !failed.isEmpty();
        }

        public int getExitCode() {
            if (failed.isEmpty()) {
                return 0; // All succeeded
            }
            if (restored.isEmpty()) {
                return 2; // All failed
            }
            return 1; // Partial success
        }
    }

    /**
     * Record of a failed restore operation.
     */
    public static class FailedRestore {
        private final String path;
        private final String reason;

        public FailedRestore(String path, String reason) {
            this.path = path;
            this.reason = reason;
        }

        public String getPath() {
            return path;
        }

        public String getReason() {
            return reason;
        }
    }
}
