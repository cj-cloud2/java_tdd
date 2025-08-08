package org.p011_mocks2.v4;

// Simple DTO for processing result
public class LoanProcessingResult {
    private boolean accepted;
    private String message;

    public LoanProcessingResult(boolean accepted, String message) {
        this.accepted = accepted;
        this.message = message;
    }

    public boolean isAccepted() { return accepted; }
    public String getMessage() { return message; }
}