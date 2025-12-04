package com.codetrimmer.error;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests for ErrorCode and CodeTrimmerException.
 */
class ErrorCodeTest {

    @Test
    void testErrorCodeHasCode() {
        ErrorCode error = ErrorCode.CT_0001;
        assertEquals("CT-0001", error.getCode());
    }

    @Test
    void testErrorCodeHasTitle() {
        ErrorCode error = ErrorCode.CT_0001;
        assertEquals("Invalid configuration file", error.getTitle());
    }

    @Test
    void testErrorCodeHasDescription() {
        ErrorCode error = ErrorCode.CT_0001;
        assertNotNull(error.getDescription());
        assertFalse(error.getDescription().isEmpty());
    }

    @Test
    void testErrorCodeToString() {
        ErrorCode error = ErrorCode.CT_0010;
        assertTrue(error.toString().contains("CT-0010"));
        assertTrue(error.toString().contains("File not found"));
    }

    @Test
    void testCodeTrimmerExceptionWithCode() {
        CodeTrimmerException ex = new CodeTrimmerException(ErrorCode.CT_0010);
        assertEquals(ErrorCode.CT_0010, ex.getErrorCode());
    }

    @Test
    void testCodeTrimmerExceptionWithCause() {
        CodeTrimmerException ex = new CodeTrimmerException(
            ErrorCode.CT_0010, "File missing");
        assertEquals(ErrorCode.CT_0010, ex.getErrorCode());
        assertEquals("File missing", ex.getErrorCause());
    }

    @Test
    void testCodeTrimmerExceptionWithSuggestedFix() {
        CodeTrimmerException ex = new CodeTrimmerException(
            ErrorCode.CT_0010, "File missing", "Check the path");
        assertEquals("Check the path", ex.getSuggestedFix());
    }

    @Test
    void testCodeTrimmerExceptionWrapsThrowable() {
        MockIOException ioEx = new MockIOException("IO error");
        CodeTrimmerException ex = new CodeTrimmerException(ErrorCode.CT_0011, ioEx);
        assertEquals(ErrorCode.CT_0011, ex.getErrorCode());
        assertEquals("IO error", ex.getErrorCause());
    }

    @Test
    void testFormattedMessage() {
        CodeTrimmerException ex = new CodeTrimmerException(
            ErrorCode.CT_0010, "test.txt not found", "Verify file exists");
        String formatted = ex.getFormattedMessage();

        assertTrue(formatted.contains("CT-0010"));
        assertTrue(formatted.contains("File not found"));
        assertTrue(formatted.contains("test.txt not found"));
        assertTrue(formatted.contains("Verify file exists"));
    }

    @Test
    void testAllErrorCodesHaveUniqueCode() {
        java.util.Set<String> codes = new java.util.HashSet<>();
        for (ErrorCode error : ErrorCode.values()) {
            assertTrue(codes.add(error.getCode()),
                "Duplicate error code: " + error.getCode());
        }
    }

    @Test
    void testAllErrorCodesFollowFormat() {
        for (ErrorCode error : ErrorCode.values()) {
            assertTrue(error.getCode().matches("CT-\\d{4}"),
                "Invalid error code format: " + error.getCode());
        }
    }

    /**
     * Mock IOException for testing exception wrapping.
     */
    static class MockIOException extends Exception {
        MockIOException(String msg) {
            super(msg);
        }
    }
}
