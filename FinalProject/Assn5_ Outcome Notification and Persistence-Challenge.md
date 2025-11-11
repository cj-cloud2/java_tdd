# Assignment 5: Outcome Notification and Persistence - Challenge Document

## Business Requirements

The loan processing service must now deliver final application outcomes to applicants and maintain a complete audit trail of all processing decisions. The system should:

1. Send approval notifications to applicants when loans are approved
2. Send rejection notifications with specific reasons when loans are rejected
3. Send status update notifications for applications awaiting documents or verification
4. Create comprehensive audit logs for every application outcome and processing step
5. Ensure notifications are sent after all processing is completed and audit trail is recorded

## Testable Requirements

From the business requirements, we derive the following testable requirements:

1. **TR13**: The service should send approval notifications when applications are successfully approved (using notification service)
2. **TR14**: The service should send rejection notifications with specific reasons when applications are rejected
3. **TR15**: The service should create audit trail entries for every application outcome, recording all processing decisions

## Dependencies

Ensure your project still includes the same dependencies from previous assignments:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.9.2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.18.2</version>
    <scope>test</scope>
</dependency>
```

## Supporting Classes (Provided)

The following supporting classes are provided as-is to support your TDD implementation:

**NotificationRequest.java**
```java
public class NotificationRequest {
    private String recipientEmail;
    private String notificationType;
    private String subject;
    private String message;
    
    public NotificationRequest(String recipientEmail, String notificationType, String subject, String message) {
        this.recipientEmail = recipientEmail;
        this.notificationType = notificationType;
        this.subject = subject;
        this.message = message;
    }
    
    public String getRecipientEmail() { return recipientEmail; }
    public String getNotificationType() { return notificationType; }
    public String getSubject() { return subject; }
    public String getMessage() { return message; }
}
```

**NotificationService.java**
```java
public interface NotificationService {
    void sendNotification(NotificationRequest request);
}
```

**AuditEvent.java**
```java
import java.time.LocalDateTime;

public class AuditEvent {
    private String applicationId;
    private String applicantEmail;
    private String eventType;
    private String outcome;
    private String details;
    private LocalDateTime timestamp;
    
    public AuditEvent(String applicationId, String applicantEmail, String eventType, String outcome, String details) {
        this.applicationId = applicationId;
        this.applicantEmail = applicantEmail;
        this.eventType = eventType;
        this.outcome = outcome;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getApplicationId() { return applicationId; }
    public String getApplicantEmail() { return applicantEmail; }
    public String getEventType() { return eventType; }
    public String getOutcome() { return outcome; }
    public String getDetails() { return details; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
```

**AuditService.java**  
```java
public interface AuditService {
    void logEvent(AuditEvent event);
}
```

**All Previous Classes** (From Assignments 1, 2, 3 & 4)
- Complete `Document` class
- Complete `DocumentValidationResult` class  
- Complete `DocumentValidationService` interface
- Complete `CreditScoreResult` class
- Complete `CreditBureauService` interface
- Complete `EmploymentVerificationResult` class
- Complete `EmploymentVerificationService` interface
- Complete `LoanApplication` class with document support
- Complete `LoanProcessingResult` class
- Complete `LoanProcessingStatus` enum (ACCEPTED, REJECTED, AWAITING_DOCUMENTS, VERIFICATION_PENDING)
- Complete `LoanRepository` interface

## Your Challenge: Implement Notification and Audit Using TDD

Follow the TDD lifecycle (RED-GREEN-REFACTOR) to enhance the existing LoanProcessingService and implement the following test functions.

### Test Function 1: Send Approval Notification on Successful Approval

#### RED Phase - Write Failing Test

Create a new test class for notification and audit tests:

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanProcessingServiceNotificationAndAuditTest {
    
    private LoanProcessingService loanProcessingService;
    
    @Mock
    private LoanRepository loanRepository;
    
    @Mock
    private DocumentValidationService documentValidationService;
    
    @Mock
    private CreditBureauService creditBureauService;
    
    @Mock
    private EmploymentVerificationService employmentVerificationService;
    
    @Mock
    private NotificationService notificationService;
    
    @Mock
    private AuditService auditService;
    
    @BeforeEach
    void setUp() {
        // TODO: Initialize mockito annotations and create service instance with all six dependencies
    }
    
    @Test
    void shouldSendApprovalNotificationOnSuccessfulApproval() {
        // TODO: Write test that:
        // 1. Creates a list of valid documents (ID_PROOF, INCOME_PROOF, ADDRESS_PROOF)
        // 2. Creates a LoanApplication with all required fields and documents
        // 3. Mocks documentValidationService.validateDocuments() to return valid result
        // 4. Mocks creditBureauService.getCreditScore() to return successful result with score >= 650
        // 5. Mocks employmentVerificationService.verifyEmployment() to return successful result with employed = true
        // 6. Calls processApplication method on the service
        // 7. Asserts that result status is ACCEPTED
        // 8. Asserts that error message is null
        // 9. Uses ArgumentCaptor to capture the NotificationRequest sent to notificationService
        // 10. Verifies that notification was sent once with correct recipient email
        // 11. Verifies that notification type is "APPROVAL"
        // 12. Verifies that message contains "approved"
        // 13. Verifies that loanRepository.save was called once with application
    }
}
```

#### GREEN Phase - Make Test Pass

**Updated LoanProcessingService.java**
```java
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoanProcessingService {
    private static final int MINIMUM_CREDIT_SCORE = 650;
    
    private final LoanRepository loanRepository;
    private final DocumentValidationService documentValidationService;
    private final CreditBureauService creditBureauService;
    private final EmploymentVerificationService employmentVerificationService;
    private final NotificationService notificationService;
    private final AuditService auditService;
    
    public LoanProcessingService(LoanRepository loanRepository) {
        // TODO: Initialize repository field and set all other services to null
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService) {
        // TODO: Initialize repository and documentValidationService fields, set others to null
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService, CreditBureauService creditBureauService) {
        // TODO: Initialize repository, documentValidationService, and creditBureauService fields, set others to null
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService, CreditBureauService creditBureauService, EmploymentVerificationService employmentVerificationService) {
        // TODO: Initialize first four service fields, set notificationService and auditService to null
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService, CreditBureauService creditBureauService, EmploymentVerificationService employmentVerificationService, NotificationService notificationService, AuditService auditService) {
        // TODO: Initialize all six service fields
    }
    
    public LoanProcessingResult processApplication(LoanApplication application) {
        // TODO: Implement complete processing logic with notifications and audit:
        // 1. Generate unique application ID using UUID.randomUUID().toString()
        // 2. Validate basic application fields (reuse existing logic)
        // 3. If basic validation fails, call finalizeResult method and return
        // 4. Validate documents if documentValidationService is available
        // 5. If document validation fails, call finalizeResult method and return
        // 6. Validate credit score if creditBureauService is available
        // 7. If credit score validation fails, call finalizeResult method and return
        // 8. Verify employment if employmentVerificationService is available
        // 9. If employment verification fails, call finalizeResult method and return
        // 10. If all validations pass, save application and create ACCEPTED result
        // 11. Call finalizeResult method with FINAL_APPROVAL event type and return
    }
    
    private LoanProcessingResult finalizeResult(String applicationId, LoanApplication application, String eventType, LoanProcessingResult result) {
        // TODO: Implement result finalization:
        // 1. Call sendNotification method with application and result
        // 2. Call logAuditEvent method with applicationId, application, eventType, and result
        // 3. Return the result
    }
    
    private void sendNotification(LoanApplication application, LoanProcessingResult result) {
        // TODO: Implement notification sending:
        // 1. Check if notificationService is available
        // 2. Use switch statement on result.getStatus() to determine notification details:
        //    - ACCEPTED: notificationType="APPROVAL", subject="Loan Application Approved", 
        //      message="Congratulations! Your loan application for $[amount] has been approved."
        //    - REJECTED: notificationType="REJECTION", subject="Loan Application Rejected",
        //      message="We regret to inform you that your loan application has been rejected. Reason: [error]"
        //    - AWAITING_DOCUMENTS: notificationType="AWAITING_DOCUMENTS", subject="Additional Documents Required",
        //      message="Your loan application is pending additional documents. [error]"
        //    - VERIFICATION_PENDING: notificationType="VERIFICATION_PENDING", subject="Loan Application Under Review",
        //      message="Your loan application is currently under review. [error]"
        // 3. Create NotificationRequest with recipient email, notification type, subject, and message
        // 4. Call notificationService.sendNotification() with the request
    }
    
    private void logAuditEvent(String applicationId, LoanApplication application, String eventType, LoanProcessingResult result) {
        // TODO: Implement audit event logging:
        // 1. Check if auditService is available
        // 2. Create AuditEvent with:
        //    - applicationId
        //    - application.getEmail() as applicant email
        //    - eventType parameter
        //    - result.getStatus().toString() as outcome
        //    - result.getErrorMessage() if not null, otherwise "Processing successful" as details
        // 3. Call auditService.logEvent() with the event
    }
    
    private LoanProcessingResult performBasicValidation(LoanApplication application) {
        // TODO: Reimplement basic validation from Assignment 1:
        // 1. Call validateApplication() method to get list of errors
        // 2. If errors exist, return REJECTED result with joined error messages
        // 3. Return null if no basic validation issues
    }
    
    private LoanProcessingResult verifyEmployment(LoanApplication application) {
        // TODO: Reimplement employment verification from Assignment 4:
        // 1. Check if employmentVerificationService is available
        // 2. Call employmentVerificationService.verifyEmployment() with application email
        // 3. If service call was not successful, return VERIFICATION_PENDING with service error message
        // 4. If employment is not verified (isEmployed = false), return REJECTED with appropriate message
        // 5. Return null if no employment verification issues
    }
    
    private LoanProcessingResult validateCreditScore(LoanApplication application) {
        // TODO: Reimplement credit score validation from Assignment 3:
        // 1. Check if creditBureauService is available
        // 2. Call creditBureauService.getCreditScore() with application phone number
        // 3. If service call was not successful, return REJECTED with service error message
        // 4. If credit score is below MINIMUM_CREDIT_SCORE, return REJECTED with appropriate message
        // 5. Return null if no credit score validation issues
    }
    
    private LoanProcessingResult validateDocuments(LoanApplication application) {
        // TODO: Reimplement document validation from Assignment 2:
        // 1. Check if documentValidationService is available and application has documents
        // 2. Call documentValidationService.validateDocuments()
        // 3. If documents are invalid:
        //    a. Check if missingDocuments list exists (return AWAITING_DOCUMENTS)
        //    b. Otherwise return REJECTED for invalid documents
        // 4. Return null if no document validation issues
    }
    
    private List<String> validateApplication(LoanApplication application) {
        // TODO: Reimplement basic validation from Assignment 1:
        // 1. Create empty list of errors
        // 2. Check each required field (name, email, phone, loan amount, loan purpose)
        // 3. Add appropriate error messages for missing/invalid fields
        // 4. Return the list of errors
    }
    
    private boolean isNullOrEmpty(String value) {
        // TODO: Helper method to check if string is null or empty after trimming
    }
}
```

### Test Function 2: Send Rejection Notification with Reason

#### RED Phase - Write Failing Test

Add the second test to the notification and audit test class:

```java
@Test
void shouldSendRejectionNotificationWithReason() {
    // TODO: Write test that:
    // 1. Creates a list of valid documents  
    // 2. Creates a LoanApplication with all required fields and documents
    // 3. Mocks documentValidationService.validateDocuments() to return valid result
    // 4. Mocks creditBureauService.getCreditScore() to return successful result with score < 650 (e.g., 580)
    // 5. Calls processApplication method on the service
    // 6. Asserts that result status is REJECTED
    // 7. Uses ArgumentCaptor to capture the NotificationRequest sent to notificationService
    // 8. Verifies that notification was sent once with correct recipient email
    // 9. Verifies that notification type is "REJECTION"
    // 10. Verifies that message contains "rejected"
    // 11. Verifies that message contains specific credit score information
    // 12. Verifies that loanRepository.save was never called
}
```

#### GREEN Phase - Make Test Pass

The current implementation should already handle this case correctly if the notification logic is implemented properly in the `sendNotification` method.

### Test Function 3: Create Audit Trail for Every Application Outcome

#### RED Phase - Write Failing Test

Add the third test to verify audit trail creation:

```java
@Test
void shouldCreateAuditTrailForEveryApplicationOutcome() {
    // TODO: Write test that:
    // 1. Creates a list of valid documents
    // 2. Creates a LoanApplication with all required fields and documents
    // 3. Mocks documentValidationService.validateDocuments() to return valid result
    // 4. Mocks creditBureauService.getCreditScore() to return successful result with score >= 650
    // 5. Mocks employmentVerificationService.verifyEmployment() to return successful result with employed = true
    // 6. Calls processApplication method on the service
    // 7. Asserts that result status is ACCEPTED
    // 8. Uses ArgumentCaptor to capture the AuditEvent sent to auditService
    // 9. Verifies that auditService.logEvent was called once
    // 10. Verifies the captured audit event has:
    //     - Non-null applicationId
    //     - Correct applicant email
    //     - "FINAL_APPROVAL" as eventType
    //     - "ACCEPTED" as outcome
    //     - "Processing successful" as details
    //     - Non-null timestamp
    // 11. Verifies that loanRepository.save was called once
}
```

#### GREEN Phase - Make Test Pass

The current implementation should already handle this case correctly if the audit logging logic is implemented properly in the `logAuditEvent` method.

#### REFACTOR Phase

Consider refactoring the service for cleaner flow:

```java
public LoanProcessingResult processApplication(LoanApplication application) {
    // TODO: Final implementation should follow this clean flow:
    // 1. Generate unique application ID
    // 2. Process through validation pipeline, returning early if any step fails
    // 3. If all validations pass, save application and create approval result
    // 4. Always finalize result with notification and audit logging
}

private LoanProcessingResult finalizeResult(String applicationId, LoanApplication application, String eventType, LoanProcessingResult result) {
    // TODO: Centralized result finalization method:
    // 1. Send notification to applicant
    // 2. Log audit event for compliance
    // 3. Return the result
}
```

## Challenge Instructions

1. **Start with RED**: Write each failing test first
2. **Move to GREEN**: Write minimal production code to make the test pass
3. **Apply REFACTOR**: Improve code quality without breaking tests
4. **Maintain backward compatibility**: Ensure Assignments 1, 2, 3 & 4 functionality still works
5. **Repeat the cycle** for each test function

## Success Criteria

Your implementation should:

- ✅ Pass all three new test functions
- ✅ Maintain backward compatibility with all previous assignments
- ✅ Send appropriate notifications for all application outcomes
- ✅ Create comprehensive audit trails for compliance
- ✅ Properly integrate with mocked NotificationService and AuditService
- ✅ Follow clean code principles with proper separation of concerns
- ✅ Provide complete production-ready loan processing system

## Key TDD Learning Objectives

By completing this challenge, you will practice:

- Building a complete production-ready system through iterative TDD
- Implementing cross-cutting concerns (notifications, auditing) using TDD
- Managing complex constructor overloading for flexible service integration
- Creating comprehensive audit trails for business compliance
- Implementing notification systems with proper message templating
- Refactoring complex systems while maintaining all functionality

## Testing Scenarios Covered

1. **Successful Approval**: All validations pass → ACCEPTED with approval notification and audit
2. **Credit Score Rejection**: Low credit score → REJECTED with rejection notification and audit  
3. **Comprehensive Audit**: All outcomes logged → Complete audit trail for compliance
4. **Notification System**: All status types handled → Appropriate notifications sent
5. **Complete Pipeline**: Maintains all functionality from Assignments 1-4

## Final System Architecture

The completed service provides comprehensive loan processing:

1. **Basic Validation**: Check required application fields
2. **Document Validation**: Verify all documents are present and valid  
3. **Credit Score Check**: Verify applicant meets minimum credit requirements (650+)
4. **Employment Verification**: Confirm active employment status through HR systems
5. **Notification System**: Send outcome notifications to applicants
6. **Audit System**: Create complete audit trails for compliance
7. **Final Decision**: Save approved applications to repository

## Status Management

- **ACCEPTED**: All validations passed, loan approved
- **REJECTED**: Failed validation at any step
- **AWAITING_DOCUMENTS**: Missing required documents  
- **VERIFICATION_PENDING**: External system unavailable

## Production-Ready Features

- **Robust Error Handling**: Graceful degradation when external services unavailable
- **Comprehensive Notifications**: Approval, rejection, status update, and verification pending notifications
- **Complete Audit Trail**: Unique application IDs, timestamped events, detailed outcome logging
- **Clean Architecture**: Separation of concerns with single responsibility principle

Good luck with your TDD implementation! By completing this assignment, you'll have built a comprehensive, production-ready loan processing service using proper TDD methodology and learned how to implement cross-cutting concerns like notifications and auditing through test-driven development.