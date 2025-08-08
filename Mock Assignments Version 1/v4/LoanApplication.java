package org.p011_mocks2.v4;

// Update LoanApplication to include loan status
public class LoanApplication {
    private String applicantName;
    private String applicantId;
    private Integer amount;
    private LoanStatus status;

    public LoanApplication(String applicantName, String applicantId, Integer amount) {
        this.applicantName = applicantName;
        this.applicantId = applicantId;
        this.amount = amount;
        this.status = LoanStatus.PENDING; // default initial status
    }

    // getters and setters
    public String getApplicantName() { return applicantName; }
    public String getApplicantId() { return applicantId; }
    public Integer getAmount() { return amount; }
    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }
}
