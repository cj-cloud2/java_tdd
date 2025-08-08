package org.p011_mocks2.v2;

// Value object representing the loan application
public class LoanApplication {
    private String applicantName;
    private String applicantId;
    private Integer amount;

    public LoanApplication(String applicantName, String applicantId, Integer amount) {
        this.applicantName = applicantName;
        this.applicantId = applicantId;
        this.amount = amount;
    }

    public String getApplicantName() { return applicantName; }
    public String getApplicantId() { return applicantId; }
    public Integer getAmount() { return amount; }
}
