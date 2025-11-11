# Assignment 3: Integrate Credit Score Check - Challenge Document

## Business Requirements

The loan processing service must now incorporate credit score verification to determine loan eligibility. The system should:

1. Fetch the applicant's credit score from an external credit bureau service
2. Accept applications only if the credit score meets the minimum threshold (650 or above)
3. Reject applications with credit scores below the minimum threshold
4. Handle scenarios where the credit bureau service is unavailable or fails to respond
5. Only proceed with credit checks after basic validation and document validation are complete

## Testable Requirements

From the business requirements, we derive the following testable requirements:

1. **TR7**: The service should accept applications when the applicant's credit score is above the minimum threshold (using credit bureau service)
2. **TR8**: The service should reject applications when the applicant's credit score is below the minimum threshold
3. **TR9**: The service should handle credit bureau service failures gracefully (timeout, unavailable service)

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

**CreditScoreResult.java**
```java
public class CreditScoreResult {
    private boolean isSuccessful;
    private Integer creditScore;
    private String message;
    
    public CreditScoreResult(boolean isSuccessful, Integer creditScore, String message) {
        this.isSuccessful = isSuccessful;
        this.creditScore = creditScore;
        this.message = message;
    }
    
    public boolean isSuccessful() { return isSuccessful; }
    public Integer getCreditScore() { return creditScore; }
    public String getMessage() { return message; }
}
```

**CreditBureauService.java**
```java
public interface CreditBureauService {
    CreditScoreResult getCreditScore(String phoneNumber);
}
```

**All Previous Classes** (From Assignments 1 & 2)
- Complete `Document` class
- Complete `DocumentValidationResult` class
- Complete `DocumentValidationService` interface
- Complete `LoanApplication` class with document support
- Complete `LoanProcessingStatus` enum (ACCEPTED, REJECTED, AWAITING_DOCUMENTS)
- Complete `LoanProcessingResult` class
- Complete `LoanRepository` interface

## Your Challenge: Implement Credit Score Validation Using TDD

Follow the TDD lifecycle (RED-GREEN-REFACTOR) to enhance the existing LoanProcessingService and implement the following test functions.

### Test Function 1: Accept Application if Credit Score Above Threshold

#### RED Phase - Write Failing Test

Create a new test class for credit score validation tests:

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanProcessingServiceCreditScoreTest {
    
    private LoanProcessingService loanProcessingService;
    
    @Mock
    private LoanRepository loanRepository;
    
    @Mock
    private DocumentValidationService documentValidationService;
    
    @Mock
    private CreditBureauService creditBureauService;
    
    @BeforeEach
    void setUp() {
        // TODO: Initialize mockito annotations and create service instance with all three dependencies
    }
    
    @Test
    void shouldAcceptApplicationWithHighCreditScore() {
        // TODO: Write test that:
        // 1. Creates a list of valid documents (ID_PROOF, INCOME_PROOF, ADDRESS_PROOF)
        // 2. Creates a LoanApplication with all required fields and documents
        // 3. Mocks documentValidationService.validateDocuments() to return valid result
        // 4. Mocks creditBureauService.getCreditScore() to return successful result with score >= 650
        // 5. Calls processApplication method on the service
        // 6. Asserts that result status is ACCEPTED
        // 7. Asserts that error message is null
        // 8. Verifies that creditBureauService.getCreditScore was called once with phone number
        // 9. Verifies that loanRepository.save was called once with application
    }
}
```

#### GREEN Phase - Make Test Pass

**Updated LoanProcessingService.java**
```java
import java.util.ArrayList;
import java.util.List;

public class LoanProcessingService {
    private static final int MINIMUM_CREDIT_SCORE = 650;
    
    private LoanRepository loanRepository;
    private DocumentValidationService documentValidationService;
    private CreditBureauService creditBureauService;
    
    public LoanProcessingService(LoanRepository loanRepository) {
        // TODO: Initialize repository field and set other services to null
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService) {
        // TODO: Initialize repository and documentValidationService fields, set creditBureauService to null
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService, CreditBureauService creditBureauService) {
        // TODO: Initialize all three service fields
    }
    
    public LoanProcessingResult processApplication(LoanApplication application) {
        // TODO: Implement enhanced processing logic:
        // 1. Validate basic application fields (reuse existing logic)
        // 2. If basic validation fails, return REJECTED with error messages
        // 3. Validate documents if documentValidationService is available
        // 4. If document validation fails, return appropriate result (AWAITING_DOCUMENTS or REJECTED)
        // 5. Validate credit score if creditBureauService is available
        // 6. If credit score validation fails, return REJECTED with error message
        // 7. If all validations pass, save application and return ACCEPTED
    }
    
    private LoanProcessingResult validateCreditScore(LoanApplication application) {
        // TODO: Implement credit score validation:
        // 1. Check if creditBureauService is available
        // 2. Call creditBureauService.getCreditScore() with applicant's phone number
        // 3. If service call was not successful, return REJECTED with service error message
        // 4. If credit score is below MINIMUM_CREDIT_SCORE, return REJECTED with appropriate message
        // 5. Return null if no credit score validation issues
    }
    
    private LoanProcessingResult validateDocuments(LoanApplication application) {
        // TODO: Reimplement document validation from Assignment 2:
        // 1. Check if documentValidationService is available and application has documents
        // 2. Call documentValidationService.validateDocuments()
        // 3. If documents are invalid:
        //    a. Check if missingDocuments list exists (AWAITING_DOCUMENTS)
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

### Test Function 2: Reject Application for Low Credit Score

#### RED Phase - Write Failing Test

Add the second test to the credit score validation test class:

```java
@Test
void shouldRejectApplicationWithLowCreditScore() {
    // TODO: Write test that:
    // 1. Creates a list of valid documents
    // 2. Creates a LoanApplication with all required fields and documents
    // 3. Mocks documentValidationService.validateDocuments() to return valid result
    // 4. Mocks creditBureauService.getCreditScore() to return successful result with score < 650
    // 5. Calls processApplication method on the service
    // 6. Asserts that result status is REJECTED
    // 7. Asserts that error message contains information about low credit score and minimum requirement
    // 8. Verifies that creditBureauService.getCreditScore was called once with phone number
    // 9. Verifies that loanRepository.save was never called
}
```

#### GREEN Phase - Make Test Pass

Update the `validateCreditScore` method in LoanProcessingService:

```java
private LoanProcessingResult validateCreditScore(LoanApplication application) {
    // TODO: Enhanced implementation:
    // 1. Check if creditBureauService is available
    // 2. Call creditBureauService.getCreditScore() with applicant's phone number
    // 3. If service call was not successful, return REJECTED with service error message
    // 4. If credit score is below MINIMUM_CREDIT_SCORE:
    //    a. Create error message: "Credit score {score} is below minimum required score of {minimum}"
    //    b. Return REJECTED with this error message
    // 5. Return null if no credit score validation issues (score is >= minimum)
}
```

### Test Function 3: Handle Credit Bureau Service Failures

#### RED Phase - Write Failing Test

Add the third test to handle service failures:

```java
@Test
void shouldHandleCreditBureauServiceFailure() {
    // TODO: Write test that:
    // 1. Creates a list of valid documents
    // 2. Creates a LoanApplication with all required fields and documents
    // 3. Mocks documentValidationService.validateDocuments() to return valid result
    // 4. Mocks creditBureauService.getCreditScore() to return unsuccessful result 
    //    (isSuccessful = false, creditScore = null, message = service error)
    // 5. Calls processApplication method on the service
    // 6. Asserts that result status is REJECTED
    // 7. Asserts that error message contains the service failure message
    // 8. Verifies that creditBureauService.getCreditScore was called once with phone number
    // 9. Verifies that loanRepository.save was never called
}
```

#### GREEN Phase - Make Test Pass

The current implementation should already handle this case correctly if the `validateCreditScore` method checks `isSuccessful()` first.

#### REFACTOR Phase

Consider refactoring the service to have cleaner separation:

```java
public LoanProcessingResult processApplication(LoanApplication application) {
    // TODO: Final implementation should:
    // 1. Validate basic application fields
    // 2. If basic validation fails, return REJECTED
    // 3. Validate documents (call separate method)
    // 4. If document validation fails, return appropriate result
    // 5. Validate credit score (call separate method)
    // 6. If credit score validation fails, return REJECTED
    // 7. If all validations pass, save application and return ACCEPTED
}

private LoanProcessingResult validateCreditScore(LoanApplication application) {
    // TODO: Credit score validation method should:
    // 1. Check if credit bureau service is available
    // 2. Call external credit bureau service with phone number
    // 3. Handle service failures (return REJECTED)
    // 4. Check credit score against minimum threshold
    // 5. Return appropriate result or null if valid
}

private LoanProcessingResult validateDocuments(LoanApplication application) {
    // TODO: Document validation method (same as Assignment 2)
}

private List<String> validateApplication(LoanApplication application) {
    // TODO: Basic field validation method (same as Assignment 1)
}

private boolean isNullOrEmpty(String value) {
    // TODO: Helper method for string validation
}
```

## Challenge Instructions

1. **Start with RED**: Write each failing test first
2. **Move to GREEN**: Write minimal production code to make the test pass
3. **Apply REFACTOR**: Improve code quality without breaking tests
4. **Maintain backward compatibility**: Ensure Assignments 1 & 2 functionality still works
5. **Repeat the cycle** for each test function

## Success Criteria

Your implementation should:

- ✅ Pass all three new test functions
- ✅ Maintain backward compatibility with Assignments 1 & 2 functionality
- ✅ Handle credit score validation through external credit bureau service
- ✅ Enforce minimum credit score threshold (650)
- ✅ Handle credit bureau service failures gracefully
- ✅ Properly integrate with mocked CreditBureauService
- ✅ Follow clean code principles with proper separation of concerns

## Key TDD Learning Objectives

By completing this challenge, you will practice:

- Extending existing functionality with additional external service integration
- Using multiple mocks in a single test to isolate the unit under test
- Handling external service failures and timeouts
- Implementing business rules (minimum credit score threshold)
- Maintaining backward compatibility while adding new features
- Refactoring complex validation logic while preserving functionality

## Testing Scenarios Covered

1. **High Credit Score**: Valid fields, valid documents, credit score >= 650 → ACCEPTED
2. **Low Credit Score**: Valid fields, valid documents, credit score < 650 → REJECTED
3. **Service Failure**: Valid fields, valid documents, credit service unavailable → REJECTED
4. **Previous Functionality**: Maintains existing validation from Assignments 1 & 2

## Processing Flow

The enhanced service now provides comprehensive loan application processing:

1. **Basic Validation**: Check required application fields
2. **Document Validation**: Verify all documents are present and valid
3. **Credit Score Check**: Verify applicant meets minimum credit requirements (650+)
4. **Persistence**: Save approved applications to repository

Good luck with your TDD implementation! Remember to follow the RED-GREEN-REFACTOR cycle for each test function.