package com.codetrimmer.error;

/**
 * Exception class for Code Trimmer errors with error codes.
 */
public class CodeTrimmerException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String cause;
    private final String suggestedFix;

    /**
     * Creates a new CodeTrimmerException with an error code.
     *
     * @param errorCode the error code
     */
    public CodeTrimmerException(ErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
        this.cause = "";
        this.suggestedFix = "";
    }

    /**
     * Creates a new CodeTrimmerException with an error code and cause.
     *
     * @param errorCode the error code
     * @param cause the cause of the error
     */
    public CodeTrimmerException(ErrorCode errorCode, String cause) {
        super(errorCode.toString() + " - " + cause);
        this.errorCode = errorCode;
        this.cause = cause;
        this.suggestedFix = "";
    }

    /**
     * Creates a new CodeTrimmerException with full details.
     *
     * @param errorCode the error code
     * @param cause the cause of the error
     * @param suggestedFix suggested fix for the error
     */
    public CodeTrimmerException(ErrorCode errorCode, String cause, String suggestedFix) {
        super(errorCode.toString() + " - " + cause);
        this.errorCode = errorCode;
        this.cause = cause;
        this.suggestedFix = suggestedFix;
    }

    /**
     * Creates a CodeTrimmerException wrapping another exception.
     *
     * @param errorCode the error code
     * @param wrappedException the underlying exception
     */
    public CodeTrimmerException(ErrorCode errorCode, Throwable wrappedException) {
        super(errorCode.toString(), wrappedException);
        this.errorCode = errorCode;
        this.cause = wrappedException.getMessage();
        this.suggestedFix = "";
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorCause() {
        return cause;
    }

    public String getSuggestedFix() {
        return suggestedFix;
    }

    /**
     * Returns a formatted error message with all details.
     *
     * @return formatted error message
     */
    public String getFormattedMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Error ").append(errorCode.getCode()).append(": ")
          .append(errorCode.getTitle()).append("\n");
        sb.append("Description: ").append(errorCode.getDescription()).append("\n");
        if (!cause.isEmpty()) {
            sb.append("Cause: ").append(cause).append("\n");
        }
        if (!suggestedFix.isEmpty()) {
            sb.append("Suggested Fix: ").append(suggestedFix).append("\n");
        }
        return sb.toString();
    }
}
