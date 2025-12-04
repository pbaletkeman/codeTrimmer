package com.codetrimmer.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for DiffGenerator service.
 */
class DiffGeneratorTest {

    private DiffGenerator diffGenerator;

    @BeforeEach
    void setUp() {
        diffGenerator = new DiffGenerator();
    }

    @Test
    void testNoDiff() {
        String content = "line1\nline2\nline3\n";
        String diff = diffGenerator.generateDiff(content, content, "test.txt");
        assertEquals("", diff);
    }

    @Test
    void testSimpleChange() {
        String original = "line1\nline2\nline3\n";
        String modified = "line1\nmodified\nline3\n";
        String diff = diffGenerator.generateDiff(original, modified, "test.txt");

        assertTrue(diff.contains("--- a/test.txt"));
        assertTrue(diff.contains("+++ b/test.txt"));
        assertTrue(diff.contains("-line2"));
        assertTrue(diff.contains("+modified"));
    }

    @Test
    void testAddedLine() {
        String original = "line1\nline2\n";
        String modified = "line1\nline2\nline3\n";
        String diff = diffGenerator.generateDiff(original, modified, "test.txt");

        assertTrue(diff.contains("+line3"));
    }

    @Test
    void testRemovedLine() {
        String original = "line1\nline2\nline3\n";
        String modified = "line1\nline3\n";
        String diff = diffGenerator.generateDiff(original, modified, "test.txt");

        assertTrue(diff.contains("-line2"));
    }

    @Test
    void testTrailingWhitespaceRemoval() {
        String original = "line1   \nline2  \nline3\n";
        String modified = "line1\nline2\nline3\n";
        String diff = diffGenerator.generateDiff(original, modified, "test.txt");

        assertNotEquals("", diff);
    }

    @Test
    void testDiffHeader() {
        String original = "old\n";
        String modified = "new\n";
        String diff = diffGenerator.generateDiff(original, modified, "myfile.java");

        assertTrue(diff.startsWith("--- a/myfile.java\n"));
        assertTrue(diff.contains("+++ b/myfile.java\n"));
    }

    @Test
    void testHunkHeader() {
        String original = "line1\nline2\n";
        String modified = "line1\nmodified\n";
        String diff = diffGenerator.generateDiff(original, modified, "test.txt");

        assertTrue(diff.contains("@@"));
    }

    @Test
    void testMultipleChanges() {
        String original = "line1\nline2\nline3\nline4\nline5\n";
        String modified = "line1\nchanged2\nline3\nchanged4\nline5\n";
        String diff = diffGenerator.generateDiff(original, modified, "test.txt");

        assertTrue(diff.contains("-line2"));
        assertTrue(diff.contains("+changed2"));
        assertTrue(diff.contains("-line4"));
        assertTrue(diff.contains("+changed4"));
    }

    @Test
    void testEmptyOriginal() {
        String original = "";
        String modified = "new line\n";
        String diff = diffGenerator.generateDiff(original, modified, "test.txt");

        assertTrue(diff.contains("+new line"));
    }

    @Test
    void testEmptyModified() {
        String original = "old line\n";
        String modified = "";
        String diff = diffGenerator.generateDiff(original, modified, "test.txt");

        assertTrue(diff.contains("-old line"));
    }

    @Test
    void testContextLines() {
        String original = "line1\nline2\nline3\nline4\nline5\nline6\nline7\n";
        String modified = "line1\nline2\nline3\nchanged\nline5\nline6\nline7\n";
        String diff = diffGenerator.generateDiff(original, modified, "test.txt");

        // Context should include surrounding lines
        assertTrue(diff.contains(" line3"));
        assertTrue(diff.contains("-line4"));
        assertTrue(diff.contains("+changed"));
        assertTrue(diff.contains(" line5"));
    }

    @Test
    void testBlankLineNormalization() {
        String original = "line1\n\n\n\nline2\n";
        String modified = "line1\n\nline2\n";
        String diff = diffGenerator.generateDiff(original, modified, "test.txt");

        assertNotEquals("", diff);
    }

    @Test
    void testSpecialCharacters() {
        String original = "line with $pecial ch@rs!\n";
        String modified = "line with special chars\n";
        String diff = diffGenerator.generateDiff(original, modified, "test.txt");

        assertTrue(diff.contains("-line with $pecial ch@rs!"));
        assertTrue(diff.contains("+line with special chars"));
    }
}
