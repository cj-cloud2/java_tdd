
### Assignment 1: Basic Loan Application Intake

**Assignment Description:**
**Service goal:** Accept and validate raw loan applications.

- **Test Case 1:** Validate application with all required fields present.
- **Test Case 2:** Reject application missing mandatory field(s).
- **Test Case 3:** Ensure newly accepted applications are persisted (mock repository).

**Service class after this step:** Handles intake and simple validation, persists loan applications.

## 1. ServiceTest Class: `LoanProcessingServiceTest`

```java
// Import necessary testing frameworks
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

// Import static methods for assertions and mocking
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Test class for loan processing service
class LoanProcessingServiceTest {
    // Declare mock repository instance
    private LoanRepository loanRepository;
    // Declare service instance to be tested
    private LoanProcessingService loanProcessingService;

    // Setup method to run before each test case
    @BeforeEach
    void setUp() {
        // Create mock implementation of LoanRepository
        // Initialize service instance with mocked repository dependency
    }

    // Test Case 1: Validate application with all required fields present
    @Test
    void shouldAcceptValidLoanApplication() {
        // Create valid loan application with name, ID, and positive amount
        // Process application through service
        // Verify result shows application was accepted
        // Verify repository saved application exactly once
    }

    // Test Case 2: Reject application missing mandatory field(s)
    @Test
    void shouldRejectApplicationMissingFields() {
        // Create invalid application with missing name (null value)
        // Process application through service
        // Verify result shows application was rejected
        // Verify repository never saved any application
    }

    // Test Case 3: Ensure accepted applications are persisted
    @Test
    void shouldPersistAcceptedApplication() {
        // Create valid loan application
        // Process application through service
        // Capture argument passed to repository's save method
        // Verify saved application has correct name value
        // Verify saved application has correct ID value
        // Verify saved application has correct amount value
    }
}
```


## 2. Service and Helper Classes

```java
// LoanProcessingService handles intake and validation
// Service class for loan application processing
public class LoanProcessingService {
    // Declare repository dependency (final)

    // Constructor for dependency injection
    public LoanProcessingService(LoanRepository loanRepository) {
        // Initialize repository dependency
    }

    // Process loan application method
    public LoanProcessingResult processApplication(LoanApplication application) {
        // Validate application has non-blank applicant name
        // Validate application has non-blank applicant ID
        // Validate application has positive loan amount
        
        // If any validation fails:
        //   Return rejection result with error message
        
        // If all validations pass:
        //   Persist application using repository
        //   Return acceptance result with success message
    }
}

// Data model for loan application
public class LoanApplication {
    // Field for applicant's name (String)
    // Field for applicant's ID (String)
    // Field for loan amount (Integer)

    // Constructor to initialize all fields
    public LoanApplication(String applicantName, String applicantId, Integer amount) {
        // Initialize name field
        // Initialize ID field
        // Initialize amount field
    }

    // Getter method for applicant name
    // Getter method for applicant ID
    // Getter method for loan amount
}

// Repository interface for data persistence
public interface LoanRepository {
    // Method declaration to save loan application
    void save(LoanApplication application);
}

// Result DTO for loan processing outcome
public class LoanProcessingResult {
    // Boolean field for acceptance status
    // String field for result message

    // Constructor to initialize both fields
    public LoanProcessingResult(boolean accepted, String message) {
        // Set acceptance status
        // Set result message
    }

    // Getter method for acceptance status (boolean)
    // Getter method for result message (String)
}
```

**Notes:**

- All classes shown are minimal and focused for this assignment, using mock repository (`LoanRepository`) for persistence.
- Each newly introduced method or logic block is specific to Assignment 1.
- The API design ensures all test-cases for this stage can pass and that the structure can safely evolve with future assignments—no code from this assignment will break as functionality grows.

------------------------------------------------------------------------------------------------------------

### Assignment 2: Integrate Credit Score Check

**Assignment Description:**
**Service goal:** Incorporate applicant credit score for eligibility.

- **Test Case 1:** Accept application if credit score is above threshold (mock credit bureau).
- **Test Case 2:** Reject application for low credit score.
- **Test Case 3:** Handle failure to fetch credit score (mock failure or timeout).

**Service class after this step:** Adds external integration for credit evaluation and error handling.

## 1. ServiceTest Class: `LoanProcessingWithCreditScoreServiceTest`

```java
// Import testing frameworks
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

// Import static methods for assertions and mocking
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Test class for loan processing with credit score integration
class LoanProcessingWithCreditScoreServiceTest {
    // Declare mock repository instance
    // Declare mock credit bureau service instance
    // Declare service instance to be tested

    // Setup method to run before each test case
    @BeforeEach
    void setUp() {
        // Create mock implementation of LoanRepository
        // Create mock implementation of CreditBureauService
        // Initialize service instance with both mocked dependencies
    }

    // Test Case 1: Accept application if credit score is above threshold
    @Test
    void shouldAcceptIfCreditScoreAboveThreshold() {
        // Create valid loan application
        // Configure mock credit bureau to return score above threshold (e.g., 750)
        // Process application through service
        // Verify result shows application was accepted
        // Verify repository saved application
    }

    // Test Case 2: Reject application for low credit score
    @Test
    void shouldRejectIfCreditScoreBelowThreshold() {
        // Create valid loan application
        // Configure mock credit bureau to return score below threshold (e.g., 550)
        // Process application through service
        // Verify result shows application was rejected
        // Verify repository never saved any application
    }

    // Test Case 3: Handle failure to fetch credit score
    @Test
    void shouldRejectIfCreditScoreFetchFails() {
        // Create valid loan application
        // Configure mock credit bureau to throw exception when fetching score
        // Process application through service
        // Verify result shows application was rejected
        // Verify repository never saved any application
        // Verify rejection message indicates credit score retrieval failure
    }
}
```

## 2. Service and Helper Classes

```java
// New Service class for Assignment 2: Intakes, credit checks, and persists if eligible
// Enhanced service class with credit score integration
public class LoanProcessingWithCreditScoreService {
    // Declare repository dependency (final)
    // Declare credit bureau service dependency (final)
    // Define constant for credit score threshold (e.g., 700)

    // Constructor for dependency injection
    public LoanProcessingWithCreditScoreService(LoanRepository loanRepository, 
                                               CreditBureauService creditBureauService) {
        // Initialize repository dependency
        // Initialize credit bureau service dependency
    }

    // Process loan application method with credit check
    public LoanProcessingResult processApplication(LoanApplication application) {
        // Step 1: Basic validation from Assignment 1
        //   Validate application has non-blank applicant name
        //   Validate application has non-blank applicant ID
        //   Validate application has positive loan amount
        //   If any validation fails, return rejection result
        
        // Step 2: Credit score retrieval
        //   Declare variable to store credit score
        //   Try to fetch credit score using applicant ID
        //   Catch exceptions from credit bureau service
        
        // Step 3: Credit score evaluation
        //   If credit score fetch failed (exception caught):
        //     Return rejection result indicating score retrieval failure
        //   If credit score below threshold:
        //     Return rejection result indicating low credit score
        
        // Step 4: Process valid application
        //   Persist valid application using repository
        //   Return acceptance result with success message
    }
}

// Interface for credit bureau service integration
public interface CreditBureauService {
    // Method to fetch credit score that may throw runtime exceptions
    // Takes applicant ID as parameter
    // Returns integer credit score
    int fetchCreditScore(String applicantId) throws RuntimeException;
}

// Reuse from Assignment 1:
// LoanApplication class (no changes needed)
// LoanRepository interface (no changes needed)
// LoanProcessingResult class (no changes needed)

// Keep existing LoanApplication, LoanRepository, LoanProcessingResult from Assignment 1, unchanged
```


### Notes:

- The new service class `LoanProcessingWithCreditScoreService` extends the functionality by injecting a `CreditBureauService` to obtain credit scores.
- The process first performs basic validation (copied from Assignment 1) to keep backward compatibility.
- It then fetches the credit score, handling exceptions like timeout or external service error by rejecting the application safely.
- Applications with scores below 700 are rejected; others are persisted.
- This code structure ensures existing validation remains intact and testable by earlier tests while adding the credit score check as required for Assignment 2.
- You can add the above code to your IDE and run the provided tests to verify.


------------------------------------------------------------------------------------------------------------




### Assignment 3: Employment Verification Integration

**Assignment Description:**
**Service goal:** Expand processing to include employment verification.

- **Test Case 1:** Approve when employment is confirmed (mock HR service).
- **Test Case 2:** Reject when employment is unverified.
- **Test Case 3:** Ensure loan status is updated to "verification pending" if HR system is unavailable.

**Service class after this step:** Matures to handle status management and multiple external dependencies.

## 1. ServiceTest Class: `LoanProcessingWithEmploymentVerificationServiceTest`

```java
// Import testing frameworks
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

// Import static methods for assertions and mocking
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Test class for loan processing with employment verification
class LoanProcessingWithEmploymentVerificationServiceTest {
    // Declare mock repository instance
    // Declare mock credit bureau service instance
    // Declare mock HR verification service instance
    // Declare service instance to be tested

    // Setup method to run before each test case
    @BeforeEach
    void setUp() {
        // Create mock implementation of LoanRepository
        // Create mock implementation of CreditBureauService
        // Create mock implementation of HrVerificationService
        // Initialize service instance with all mocked dependencies
    }

    // Test Case 1: Approve when employment is confirmed
    @Test
    void shouldApproveIfEmploymentVerified() {
        // Create valid loan application
        // Configure mock credit bureau to return valid credit score (above threshold)
        // Configure mock HR service to return VERIFIED employment status
        // Process application through service
        // Verify result shows application was accepted
        // Verify result message indicates employment verification
        // Capture saved application from repository
        // Verify saved application has APPROVED status
    }

    // Test Case 2: Reject when employment is unverified
    @Test
    void shouldRejectIfEmploymentUnverified() {
        // Create valid loan application
        // Configure mock credit bureau to return valid credit score
        // Configure mock HR service to return UNVERIFIED employment status
        // Process application through service
        // Verify result shows application was rejected
        // Verify saved application has REJECTED status
        // Verify rejection message indicates employment verification failure
    }

    // Test Case 3: Handle HR system unavailability
    @Test
    void shouldMarkPendingWhenHrUnavailable() {
        // Create valid loan application
        // Configure mock credit bureau to return valid credit score
        // Configure mock HR service to throw exception (simulate unavailability)
        // Process application through service
        // Verify result shows application was not accepted
        // Verify saved application has VERIFICATION_PENDING status
        // Verify result message indicates HR system unavailability
    }
}
```


## 2. Service and Helper Classes

```java
// Enum to represent employment verification status
// Enum to represent employment verification outcomes
public enum EmploymentStatus {
    // Case: Employment successfully confirmed
    VERIFIED,
    // Case: Employment could not be confirmed
    UNVERIFIED,
    // Case: HR system unavailable for verification
    UNAVAILABLE
}

// Enum to represent loan processing statuses
public enum LoanStatus {
    // Initial status when application is submitted
    PENDING,
    // Final status when application is approved
    APPROVED,
    // Final status when application is rejected
    REJECTED,
    // Temporary status when verification is pending
    VERIFICATION_PENDING
}

// Interface for HR verification service integration
public interface HrVerificationService {
    // Method to verify employment status
    // Takes applicant ID as parameter
    // Returns EmploymentStatus enum value
    // May throw runtime exceptions for service failures
    EmploymentStatus verifyEmployment(String applicantId) throws RuntimeException;
}

// Enhanced service class with employment verification
public class LoanProcessingWithEmploymentVerificationService {
    // Declare repository dependency (final)
    // Declare credit bureau service dependency (final)
    // Declare HR verification service dependency (final)
    // Define constant for credit score threshold (e.g., 700)

    // Constructor for dependency injection
    public LoanProcessingWithEmploymentVerificationService(
            LoanRepository loanRepository,
            CreditBureauService creditBureauService,
            HrVerificationService hrVerificationService) {
        // Initialize repository dependency
        // Initialize credit bureau service dependency
        // Initialize HR verification service dependency
    }

    // Process loan application method with employment verification
    public LoanProcessingResult processApplication(LoanApplication application) {
        // Step 1: Basic validation from Assignment 1
        //   Validate application has non-blank applicant name
        //   Validate application has non-blank applicant ID
        //   Validate application has positive loan amount
        //   If any validation fails, return rejection result
        
        // Step 2: Credit score evaluation from Assignment 2
        //   Try to fetch credit score
        //   Handle exceptions from credit bureau
        //   Reject if score below threshold
        
        // Step 3: Employment verification
        //   Declare variable to store employment status
        //   Try to verify employment through HR service
        //   Catch exceptions from HR service
        
        // Step 4: Process employment status
        //   If HR service unavailable (exception caught):
        //     Set application status to VERIFICATION_PENDING
        //     Save application with status
        //     Return pending result with HR unavailability message
        //   If employment UNVERIFIED:
        //     Set application status to REJECTED
        //     Save application with status
        //     Return rejection with employment verification failure
        //   If employment UNAVAILABLE:
        //     Set application status to VERIFICATION_PENDING
        //     Save application with status
        //     Return pending result
        //   If employment VERIFIED:
        //     Set application status to APPROVED
        //     Save application with status
        //     Return approval result with verification success message
    }
}

// Updated LoanApplication class from Assignment 3
public class LoanApplication {
    // Field for applicant's name (String)
    // Field for applicant's ID (String)
    // Field for loan amount (Integer)
    // Field for loan status (LoanStatus enum)

    // Constructor to initialize fields
    public LoanApplication(String applicantName, String applicantId, Integer amount) {
        // Initialize name field
        // Initialize ID field
        // Initialize amount field
        // Initialize status to PENDING
    }

    // Getter method for applicant name
    // Getter method for applicant ID
    // Getter method for loan amount
    // Getter method for loan status
    // Setter method for loan status
}

// Reuse from previous assignments:
// CreditBureauService interface
// LoanRepository interface
// LoanProcessingResult class

// Reuse LoanRepository and LoanProcessingResult unchanged from previous assignments
```


### Notes:

- The new service class `LoanProcessingWithEmploymentVerificationService` builds upon the previous credit score checks and adds employment verification logic.
- Employment verification results influence application approval, rejection, or a pending status if the HR service is unavailable.
- The `LoanApplication` class is updated to include a `LoanStatus` field to track the current status transparently.
- This approach preserves earlier validation and credit score tests without breaking them, maintaining backward compatibility.
- The single provided test case covers the successful employment verification acceptance path; additional test cases for rejection and pending status should be added similarly when expanding the tests.

------------------------------------------------------------------------------------------------------------

### Assignment 4: Document Validation Layer

**Assignment Description:**
**Service goal:** Add a missing/invalid documents verification workflow.

- **Test Case 1:** Accept with all required documents present (mock document service).
- **Test Case 2:** Mark application as "awaiting documents" if any are missing.
- **Test Case 3:** Reject application with invalid/expired documents.

**Service class after this step:** Capable of multi-stage, conditional processing and more nuanced statuses.

## 1. ServiceTest Class: `LoanProcessingWithDocumentValidationServiceTest`

```java
// Import testing frameworks
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

// Import static methods for assertions and mocking
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Test class for loan processing with document validation
class LoanProcessingWithDocumentValidationServiceTest {
    // Declare mock repository instance
    // Declare mock credit bureau service instance
    // Declare mock HR verification service instance
    // Declare mock document validation service instance
    // Declare service instance to be tested

    // Setup method to run before each test case
    @BeforeEach
    void setUp() {
        // Create mock implementation of LoanRepository
        // Create mock implementation of CreditBureauService
        // Create mock implementation of HrVerificationService
        // Create mock implementation of DocumentValidationService
        // Initialize service instance with all mocked dependencies
    }

    // Test Case 1: Accept with all required documents present
    @Test
    void shouldAcceptIfAllDocumentsValid() {
        // Create valid loan application
        // Configure mock credit bureau to return valid credit score (above threshold)
        // Configure mock HR service to return VERIFIED employment status
        // Configure mock document service to return VALID document status
        // Process application through service
        // Verify result shows application was accepted
        // Verify result message indicates document validation success
        // Capture saved application from repository
        // Verify saved application has APPROVED status
    }

    // Test Case 2: Mark as awaiting documents if missing
    @Test
    void shouldMarkPendingWhenDocumentsMissing() {
        // Create valid loan application
        // Configure mock credit bureau to return valid credit score
        // Configure mock HR service to return VERIFIED employment status
        // Configure mock document service to return MISSING document status
        // Process application through service
        // Verify result shows application was not accepted
        // Verify saved application has VERIFICATION_PENDING status
        // Verify result message indicates missing documents
    }

    // Test Case 3: Reject for invalid/expired documents
    @Test
    void shouldRejectWhenDocumentsInvalid() {
        // Create valid loan application
        // Configure mock credit bureau to return valid credit score
        // Configure mock HR service to return VERIFIED employment status
        // Configure mock document service to return INVALID document status
        // Process application through service
        // Verify result shows application was rejected
        // Verify saved application has REJECTED status
        // Verify result message indicates invalid/expired documents
    }
}
```


## 2. Service and Helper Classes

```java
// Enum to represent document validation outcomes
public enum DocumentValidationStatus {
    // Case: All documents are valid and current
    VALID,
    // Case: Required documents are missing
    MISSING,
    // Case: Documents are invalid or expired
    INVALID
}

// Interface for document validation service integration
public interface DocumentValidationService {
    // Method to validate applicant documents
    // Takes applicant ID as parameter
    // Returns DocumentValidationStatus enum value
    // May throw runtime exceptions for service failures
    DocumentValidationStatus validateDocuments(String applicantId) throws RuntimeException;
}

// Enhanced service class with document validation
public class LoanProcessingWithDocumentValidationService {
    // Declare repository dependency (final)
    // Declare credit bureau service dependency (final)
    // Declare HR verification service dependency (final)
    // Declare document validation service dependency (final)
    // Define constant for credit score threshold (e.g., 700)

    // Constructor for dependency injection
    public LoanProcessingWithDocumentValidationService(
            LoanRepository loanRepository,
            CreditBureauService creditBureauService,
            HrVerificationService hrVerificationService,
            DocumentValidationService documentValidationService) {
        // Initialize repository dependency
        // Initialize credit bureau service dependency
        // Initialize HR verification service dependency
        // Initialize document validation service dependency
    }

    // Process loan application method with document validation
    public LoanProcessingResult processApplication(LoanApplication application) {
        // Step 1: Basic validation from Assignment 1
        //   Validate application has non-blank applicant name
        //   Validate application has non-blank applicant ID
        //   Validate application has positive loan amount
        //   If any validation fails, return rejection result
        
        // Step 2: Credit score evaluation from Assignment 2
        //   Try to fetch credit score
        //   Handle exceptions from credit bureau
        //   Reject if score below threshold
        
        // Step 3: Employment verification from Assignment 3
        //   Try to verify employment through HR service
        //   Handle exceptions from HR service
        //   Reject if employment UNVERIFIED
        //   Mark as pending if employment UNAVAILABLE
        
        // Step 4: Document validation
        //   Declare variable to store document status
        //   Try to validate documents through document service
        //   Catch exceptions from document service
        
        // Step 5: Process document status
        //   If document service unavailable (exception caught):
        //     Set application status to VERIFICATION_PENDING
        //     Save application with status
        //     Return pending result with document service failure message
        //   If documents VALID:
        //     Set application status to APPROVED
        //     Save application with status
        //     Return approval result with document validation success
        //   If documents MISSING:
        //     Set application status to VERIFICATION_PENDING
        //     Save application with status
        //     Return pending result with missing documents message
        //   If documents INVALID:
        //     Set application status to REJECTED
        //     Save application with status
        //     Return rejection result with invalid documents message
    }
}

// Reuse from previous assignments:
// LoanApplication class with status field
// EmploymentStatus enum
// LoanStatus enum
// CreditBureauService interface
// HrVerificationService interface
// LoanRepository interface
// LoanProcessingResult class

// Reuse the updated LoanApplication with status field from Assignment 3:
// public class LoanApplication {
//     private String applicantName;
//     private String applicantId;
//     private Integer amount;
//     private LoanStatus status;
//     // ... constructor, getters, setters
// }


```


### Notes:

- The new service class `LoanProcessingWithDocumentValidationService` builds on previous validations by integrating an external `DocumentValidationService`.
- Document validation influences loan approval, rejection, or pending status for missing/invalid documents.
- The test case provided covers the positive scenario where all documents are valid.
- The code maintains backward compatibility by preserving all earlier validations and behaviors.
- You can add this code to your IDE and run the test along with previous tests to verify correct incremental behavior.


------------------------------------------------------------------------------------------------------------

### Assignment 5: Outcome Notification and Persistence


###  Final Application:
Final service integrates notification and auditing with previous validation stages
Three helper methods standardize approval, rejection, and pending workflows
Approval workflow: sets APPROVED status → saves → sends notification → logs audit
Rejection workflow: sets REJECTED status → saves → sends notification with reason → logs audit
Pending workflow: sets VERIFICATION_PENDING status → saves → sends notification → logs audit
Audit service logs every outcome with descriptive messages
Notification service delivers appropriate messages for each outcome type
The architecture maintains separation of concerns while providing comprehensive processing


**Assignment Description:**
**Service goal:** Deliver application outcome (approval/rejection/pending) and log all actions.

- **Test Case 1:** Send approval notification on successful approval (mock notification service).
- **Test Case 2:** Ensure rejection notification with reason is sent.
- **Test Case 3:** Audit trail is created for every application outcome (mock audit/logging).

**Service class after this step:** Fully-featured loan processing engine, capable of sophisticated workflow orchestration (validation, multiple integrations, error handling, and communications).

## 1. ServiceTest Class: `LoanProcessingWithOutcomeNotificationServiceTest`

```java
// Import testing frameworks
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

// Import static methods for assertions and mocking
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Test class for loan processing with outcome notifications
class LoanProcessingWithOutcomeNotificationServiceTest {
    // Declare mock repository instance
    // Declare mock credit bureau service instance
    // Declare mock HR verification service instance
    // Declare mock document validation service instance
    // Declare mock notification service instance
    // Declare mock audit service instance
    // Declare service instance to be tested

    // Setup method to run before each test case
    @BeforeEach
    void setUp() {
        // Create mock implementation of LoanRepository
        // Create mock implementation of CreditBureauService
        // Create mock implementation of HrVerificationService
        // Create mock implementation of DocumentValidationService
        // Create mock implementation of NotificationService
        // Create mock implementation of AuditService
        // Initialize service instance with all mocked dependencies
    }

    // Test Case 1: Send approval notification on successful approval
    @Test
    void shouldSendApprovalNotificationOnSuccessfulApproval() {
        // Create valid loan application
        // Configure mock credit bureau to return valid credit score (above threshold)
        // Configure mock HR service to return VERIFIED employment status
        // Configure mock document service to return VALID document status
        // Process application through service
        // Verify result shows application was accepted
        // Verify repository saved application with APPROVED status
        // Verify notification service sent approval notification
        // Verify audit service logged "Application approved" outcome
    }

    // Test Case 2: Ensure rejection notification with reason is sent
    @Test
    void shouldSendRejectionNotificationWithReason() {
        // Create loan application with invalid document status
        // Configure mock credit bureau to return valid credit score
        // Configure mock HR service to return VERIFIED employment status
        // Configure mock document service to return INVALID document status
        // Process application through service
        // Verify result shows application was rejected
        // Verify notification service sent rejection notification with invalid document reason
        // Verify audit service logged rejection outcome with reason
    }

    // Test Case 3: Audit trail for every application outcome
    @Test
    void shouldCreateAuditTrailForAllOutcomes() {
        // Create loan application with missing name field
        // Process application through service
        // Verify audit service logged rejection outcome with "Mandatory field missing" reason
        // Verify notification service sent rejection notification if applicable
    }
}
```


## 2. Service and Helper Classes

```java
// Interface for notification service
public interface NotificationService {
    // Method to send approval notification
    // Takes approved LoanApplication as parameter
    void sendApprovalNotification(LoanApplication application);
    
    // Method to send rejection notification
    // Takes rejected LoanApplication and reason string as parameters
    void sendRejectionNotification(LoanApplication application, String reason);
    
    // Method to send pending status notification
    // Takes pending LoanApplication and information string as parameters
    void sendPendingNotification(LoanApplication application, String info);
}

// Interface for audit logging service
public interface AuditService {
    // Method to log application outcome
    // Takes LoanApplication and outcome description string as parameters
    void logOutcome(LoanApplication application, String outcome);
}

// Final service class with notifications and auditing
public class LoanProcessingWithOutcomeNotificationService {
    // Declare repository dependency (final)
    // Declare credit bureau service dependency (final)
    // Declare HR verification service dependency (final)
    // Declare document validation service dependency (final)
    // Declare notification service dependency (final)
    // Declare audit service dependency (final)
    // Define constant for credit score threshold (e.g., 700)

    // Constructor for dependency injection
    public LoanProcessingWithOutcomeNotificationService(
            LoanRepository loanRepository,
            CreditBureauService creditBureauService,
            HrVerificationService hrVerificationService,
            DocumentValidationService documentValidationService,
            NotificationService notificationService,
            AuditService auditService) {
        // Initialize all service dependencies
    }

    // Process loan application method with notifications and auditing
    public LoanProcessingResult processApplication(LoanApplication application) {
        // Step 1: Basic validation
        //   If validation fails, call reject helper with rejection reason
        
        // Step 2: Credit score evaluation
        //   If credit score retrieval fails, call reject helper
        //   If credit score below threshold, call reject helper
        
        // Step 3: Employment verification
        //   If HR service unavailable, call pending helper
        //   If employment unverified, call reject helper
        //   If employment unavailable, call pending helper
        
        // Step 4: Document validation
        //   If document service unavailable, call pending helper
        //   If documents missing, call pending helper
        //   If documents invalid, call reject helper
        //   If documents valid, call approve helper
    }

    // Helper method to handle approval workflow
    private LoanProcessingResult approveApplication(LoanApplication application, String message) {
        // Set application status to APPROVED
        // Persist application using repository
        // Send approval notification
        // Log approval outcome in audit service
        // Return acceptance result with message
    }

    // Helper method to handle rejection workflow
    private LoanProcessingResult rejectApplication(LoanApplication application, String reason) {
        // Set application status to REJECTED
        // Persist application using repository
        // Send rejection notification with reason
        // Log rejection outcome with reason in audit service
        // Return rejection result with reason
    }

    // Helper method to handle pending status workflow
    private LoanProcessingResult markPending(LoanApplication application, String reason) {
        // Set application status to VERIFICATION_PENDING
        // Persist application using repository
        // Send pending notification with reason
        // Log pending outcome in audit service
        // Return pending result with reason
    }
}

// Reuse from previous assignments:
// LoanApplication class with status field
// EmploymentStatus enum
// LoanStatus enum
// DocumentValidationStatus enum
// CreditBureauService interface
// HrVerificationService interface
// DocumentValidationService interface
// LoanRepository interface
// LoanProcessingResult class

```


### Notes:

- The new `LoanProcessingWithOutcomeNotificationService` complements the existing layered validation and verification logic with notification and audit logging capabilities.
- Notification sending and audit logging are encapsulated through separate service interfaces, which are mocked in tests.
- Approvals trigger approval notifications, rejections trigger rejection notifications with clear reasons, and all outcomes are logged in the audit service.
- The provided test method verifies the approval notification and audit logging for a fully approved application.
- This architecture ensures complete separation of concerns and allows incremental evolution without breaking earlier test cases or logic.



------------------------------------------------------------------------------------------------------------