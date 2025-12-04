package com.codetrimmer.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for generating unified diff output.
 * Shows line-by-line changes in dry-run mode.
 */
@Service
public class DiffGenerator {

    private static final int CONTEXT_LINES = 3;

    /**
     * Generates a unified diff between original and modified content.
     *
     * @param originalContent the original file content
     * @param modifiedContent the modified content
     * @param fileName the file name for the diff header
     * @return unified diff as string
     */
    public String generateDiff(String originalContent, String modifiedContent, String fileName) {
        if (originalContent.equals(modifiedContent)) {
            return "";
        }

        String[] originalLines = originalContent.split("\n", -1);
        String[] modifiedLines = modifiedContent.split("\n", -1);

        StringBuilder diff = new StringBuilder();
        diff.append("--- a/").append(fileName).append("\n");
        diff.append("+++ b/").append(fileName).append("\n");

        List<DiffHunk> hunks = computeHunks(originalLines, modifiedLines);

        for (DiffHunk hunk : hunks) {
            diff.append(hunk.toString());
        }

        return diff.toString();
    }

    /**
     * Computes diff hunks between two line arrays.
     *
     * @param original original lines
     * @param modified modified lines
     * @return list of diff hunks
     */
    private List<DiffHunk> computeHunks(String[] original, String[] modified) {
        List<DiffHunk> hunks = new ArrayList<>();
        List<DiffLine> allChanges = computeLCS(original, modified);

        if (allChanges.isEmpty()) {
            return hunks;
        }

        HunkBuilder hunkBuilder = new HunkBuilder(allChanges, CONTEXT_LINES);
        return hunkBuilder.buildHunks(this::createHunk);
    }

    /**
     * Helper class to build diff hunks with reduced complexity.
     */
    private static class HunkBuilder {
        private final List<DiffLine> allChanges;
        private final int contextLines;
        private int hunkStart = -1;
        private List<DiffLine> currentHunkLines = new ArrayList<>();
        private int lastChangeIndex;

        HunkBuilder(List<DiffLine> allChanges, int contextLines) {
            this.allChanges = allChanges;
            this.contextLines = contextLines;
            this.lastChangeIndex = -contextLines - 1;
        }

        List<DiffHunk> buildHunks(HunkCreator creator) {
            List<DiffHunk> hunks = new ArrayList<>();
            for (int i = 0; i < allChanges.size(); i++) {
                DiffLine line = allChanges.get(i);
                processLine(hunks, line, i, creator);
            }
            if (!currentHunkLines.isEmpty()) {
                hunks.add(creator.create(currentHunkLines, hunkStart));
            }
            return hunks;
        }

        private void processLine(List<DiffHunk> hunks, DiffLine line, int i, HunkCreator creator) {
            if (line.type != DiffLine.Type.CONTEXT) {
                handleChangeLine(hunks, i, creator);
                lastChangeIndex = i;
            }
            if (hunkStart != -1) {
                handleHunkLine(hunks, line, i, creator);
            }
        }

        private void handleChangeLine(List<DiffHunk> hunks, int i, HunkCreator creator) {
            if (hunkStart == -1 || i - lastChangeIndex > contextLines * 2) {
                if (!currentHunkLines.isEmpty()) {
                    hunks.add(creator.create(currentHunkLines, hunkStart));
                    currentHunkLines = new ArrayList<>();
                }
                int contextStart = Math.max(0, i - contextLines);
                hunkStart = contextStart;
                for (int j = contextStart; j < i; j++) {
                    currentHunkLines.add(allChanges.get(j));
                }
            }
        }

        private void handleHunkLine(List<DiffHunk> hunks, DiffLine line, int i, HunkCreator creator) {
            if (line.type != DiffLine.Type.CONTEXT || i - lastChangeIndex <= contextLines) {
                currentHunkLines.add(line);
            } else if (i - lastChangeIndex == contextLines + 1) {
                hunks.add(creator.create(currentHunkLines, hunkStart));
                currentHunkLines = new ArrayList<>();
                hunkStart = -1;
            }
        }
    }

    @FunctionalInterface
    private interface HunkCreator {
        DiffHunk create(List<DiffLine> lines, int startLine);
    }

    /**
     * Computes longest common subsequence based diff.
     *
     * @param original original lines
     * @param modified modified lines
     * @return list of diff lines
     */
    private List<DiffLine> computeLCS(String[] original, String[] modified) {
        List<DiffLine> result = new ArrayList<>();
        int origIdx = 0;
        int modIdx = 0;

        // Simple diff algorithm - compare line by line
        while (origIdx < original.length || modIdx < modified.length) {
            if (origIdx >= original.length) {
                // Only additions remain
                result.add(new DiffLine(DiffLine.Type.ADDED, modified[modIdx], modIdx + 1));
                modIdx++;
            } else if (modIdx >= modified.length) {
                // Only deletions remain
                result.add(new DiffLine(DiffLine.Type.REMOVED, original[origIdx], origIdx + 1));
                origIdx++;
            } else if (original[origIdx].equals(modified[modIdx])) {
                // Lines match
                result.add(new DiffLine(DiffLine.Type.CONTEXT, original[origIdx], origIdx + 1));
                origIdx++;
                modIdx++;
            } else {
                // Lines differ - check if it's a modification or insertion/deletion
                int lookAhead = findNextMatch(original, modified, origIdx, modIdx);
                if (lookAhead == CHANGE_MODIFICATION) {
                    // Line was modified
                    result.add(new DiffLine(DiffLine.Type.REMOVED, original[origIdx], origIdx + 1));
                    result.add(new DiffLine(DiffLine.Type.ADDED, modified[modIdx], modIdx + 1));
                    origIdx++;
                    modIdx++;
                } else if (lookAhead == CHANGE_DELETION) {
                    // Line was deleted
                    result.add(new DiffLine(DiffLine.Type.REMOVED, original[origIdx], origIdx + 1));
                    origIdx++;
                } else {
                    // Line was added
                    result.add(new DiffLine(DiffLine.Type.ADDED, modified[modIdx], modIdx + 1));
                    modIdx++;
                }
            }
        }

        return result;
    }

    /**
     * Change type indicators for diff algorithm.
     */
    private static final int CHANGE_MODIFICATION = 1;
    private static final int CHANGE_DELETION = 2;
    private static final int CHANGE_ADDITION = 3;

    /**
     * Finds the next matching line to determine change type.
     *
     * @param original original lines
     * @param modified modified lines
     * @param origIdx current original index
     * @param modIdx current modified index
     * @return CHANGE_MODIFICATION, CHANGE_DELETION, or CHANGE_ADDITION
     */
    private int findNextMatch(String[] original, String[] modified, int origIdx, int modIdx) {
        // Look ahead to find if next original matches current modified
        if (origIdx + 1 < original.length && original[origIdx + 1].equals(modified[modIdx])) {
            return CHANGE_DELETION;
        }
        // Look ahead to find if current original matches next modified
        if (modIdx + 1 < modified.length && original[origIdx].equals(modified[modIdx + 1])) {
            return CHANGE_ADDITION;
        }
        return CHANGE_MODIFICATION;
    }

    /**
     * Creates a diff hunk from lines.
     *
     * @param lines the diff lines
     * @param startLine the starting line number
     * @return the diff hunk
     */
    private DiffHunk createHunk(List<DiffLine> lines, int startLine) {
        int origStart = startLine + 1;
        int origCount = 0;
        int modStart = startLine + 1;
        int modCount = 0;

        for (DiffLine line : lines) {
            if (line.type == DiffLine.Type.REMOVED) {
                origCount++;
            } else if (line.type == DiffLine.Type.ADDED) {
                modCount++;
            } else {
                origCount++;
                modCount++;
            }
        }

        return new DiffHunk(origStart, origCount, modStart, modCount, lines);
    }

    /**
     * Represents a single line in a diff.
     */
    private static class DiffLine {
        enum Type {
            CONTEXT, ADDED, REMOVED
        }

        final Type type;
        final String content;
        final int lineNumber;

        DiffLine(Type type, String content, int lineNumber) {
            this.type = type;
            this.content = content;
            this.lineNumber = lineNumber;
        }

        @Override
        public String toString() {
            return switch (type) {
                case ADDED -> "+" + content;
                case REMOVED -> "-" + content;
                case CONTEXT -> " " + content;
            };
        }
    }

    /**
     * Represents a diff hunk with context.
     */
    private static class DiffHunk {
        final int origStart;
        final int origCount;
        final int modStart;
        final int modCount;
        final List<DiffLine> lines;

        DiffHunk(int origStart, int origCount, int modStart, int modCount, List<DiffLine> lines) {
            this.origStart = origStart;
            this.origCount = origCount;
            this.modStart = modStart;
            this.modCount = modCount;
            this.lines = lines;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("@@ -").append(origStart).append(",").append(origCount);
            sb.append(" +").append(modStart).append(",").append(modCount).append(" @@\n");
            for (DiffLine line : lines) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }
}
