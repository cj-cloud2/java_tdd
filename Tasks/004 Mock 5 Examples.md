
### Assignment 1: Basic Loan Application Intake

**Assignment Description:**
**Service goal:** Accept and validate raw loan applications.

- **Test Case 1:** Validate application with all required fields present.
- **Test Case 2:** Reject application missing mandatory field(s).
- **Test Case 3:** Ensure newly accepted applications are persisted (mock repository).

**Service class after this step:** Handles intake and simple validation, persists loan applications.

## 1. ServiceTest Class: `LoanProcessingServiceTest`



## 2. Service and Helper Classes

```java
// LoanProcessingService handles intake and validation
public class LoanProcessingService 

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

// Repository interface for persistence (Mockito will mock this)
public interface LoanRepository


// Simple DTO for processing result
public class LoanProcessingResult
```


------------------------------------------------------------------------------------------------------------

### Assignment 2: Integrate Credit Score Check

**Assignment Description:**
**Service goal:** Incorporate applicant credit score for eligibility.

- **Test Case 1:** Accept application if credit score is above threshold (mock credit bureau).
- **Test Case 2:** Reject application for low credit score.
- **Test Case 3:** Handle failure to fetch credit score (mock failure or timeout).

**Service class after this step:** Adds external integration for credit evaluation and error handling.

## 1. ServiceTest Class: `LoanProcessingWithCreditScoreServiceTest`

## 2. Service and Helper Classes

```java
// New Service class for Assignment 2: Intakes, credit checks, and persists if eligible
public class LoanProcessingWithCreditScoreService
// New interface for external credit bureau service integration

public interface CreditBureauService {
    int fetchCreditScore(String applicantId) throws RuntimeException; // can throw RuntimeException on failure
}

// Keep existing LoanApplication, LoanRepository, LoanProcessingResult from Assignment 1, unchanged
```


------------------------------------------------------------------------------------------------------------




### Assignment 3: Employment Verification Integration

**Assignment Description:**
**Service goal:** Expand processing to include employment verification.

- **Test Case 1:** Approve when employment is confirmed (mock HR service).
- **Test Case 2:** Reject when employment is unverified.
- **Test Case 3:** Ensure loan status is updated to "verification pending" if HR system is unavailable.

**Service class after this step:** Matures to handle status management and multiple external dependencies.

## 1. ServiceTest Class: `LoanProcessingWithEmploymentVerificationServiceTest`

## 2. Service and Helper Classes

```java
// Enum to represent employment verification status
public enum EmploymentStatus {
    VERIFIED,
    UNVERIFIED,
    UNAVAILABLE
}

// Enum to represent loan statuses
public enum LoanStatus {
    PENDING,
    APPROVED,
    REJECTED,
    VERIFICATION_PENDING
}

// New interface for HR verification service integration
public interface HrVerificationService {
    EmploymentStatus verifyEmployment(String applicantId) throws RuntimeException;
}

// New service class for Assignment 3: integrates employment verification with previous checks
public class LoanProcessingWithEmploymentVerificationService 

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
}

// Reuse LoanRepository and LoanProcessingResult unchanged from previous assignments
```

------------------------------------------------------------------------------------------------------------

### Assignment 4: Document Validation Layer

**Assignment Description:**
**Service goal:** Add a missing/invalid documents verification workflow.

- **Test Case 1:** Accept with all required documents present (mock document service).
- **Test Case 2:** Mark application as "awaiting documents" if any are missing.
- **Test Case 3:** Reject application with invalid/expired documents.

**Service class after this step:** Capable of multi-stage, conditional processing and more nuanced statuses.

## 1. ServiceTest Class: `LoanProcessingWithDocumentValidationServiceTest`

## 2. Service and Helper Classes

```java
// Enum for document validation results
public enum DocumentValidationStatus {
    VALID,
    MISSING,
    INVALID
}

// New interface for document validation service integration
public interface DocumentValidationService {
    DocumentValidationStatus validateDocuments(String applicantId) throws RuntimeException;
}

// New service class for Assignment 4: extends previous service to add document validation
public class LoanProcessingWithDocumentValidationService 

// Reuse the updated LoanApplication with status field from Assignment 3:
// public class LoanApplication {
//     private String applicantName;
//     private String applicantId;
//     private Integer amount;
//     private LoanStatus status;
//     // ... constructor, getters, setters
// }

// Reuse enums EmploymentStatus and LoanStatus from Assignment 3

// Reuse interfaces and classes from previous assignments:
// CreditBureauService, HrVerificationService, LoanRepository, LoanProcessingResult
```
------------------------------------------------------------------------------------------------------------

### Assignment 5: Outcome Notification and Persistence

**Assignment Description:**
**Service goal:** Deliver application outcome (approval/rejection/pending) and log all actions.

- **Test Case 1:** Send approval notification on successful approval (mock notification service).
- **Test Case 2:** Ensure rejection notification with reason is sent.
- **Test Case 3:** Audit trail is created for every application outcome (mock audit/logging).

**Service class after this step:** Fully-featured loan processing engine, capable of sophisticated workflow orchestration (validation, multiple integrations, error handling, and communications).

## 1. ServiceTest Class: `LoanProcessingWithOutcomeNotificationServiceTest`

## 2. Service and Helper Classes

```java
// New interface for notification service
public interface NotificationService {
    void sendApprovalNotification(LoanApplication application);
    void sendRejectionNotification(LoanApplication application, String reason);
    void sendPendingNotification(LoanApplication application, String info);
}

// New interface for audit logging service
public interface AuditService {
    void logOutcome(LoanApplication application, String outcome);
}

// New service class for Assignment 5: adds notifications and audit logging to previous services
public class LoanProcessingWithOutcomeNotificationService

// Reuse enums EmploymentStatus, LoanStatus, DocumentValidationStatus

// Updated LoanApplication class with status from previous assignments
// public class LoanApplication {
//     private String applicantName;
//     private String applicantId;
//     private Integer amount;
//     private LoanStatus status;
//     // Constructor, getters, and setters ...
// }

// Reuse from previous assignments:
// CreditBureauService, HrVerificationService, DocumentValidationService, LoanRepository, LoanProcessingResult
```

------------------------------------------------------------------------------------------------------------