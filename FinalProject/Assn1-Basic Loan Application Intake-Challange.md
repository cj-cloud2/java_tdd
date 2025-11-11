# Assignment 1: Basic Loan Application Intake - Challenge Document

## Business Requirements

The loan processing service must be able to accept loan applications from potential borrowers and validate that all mandatory information is provided before proceeding with further processing. The system should:

1. Accept loan applications containing applicant personal information, loan amount, and purpose
2. Validate that all mandatory fields are present and properly formatted
3. Store valid applications for further processing
4. Reject applications that are missing required information

## Testable Requirements

From the business requirements, we derive the following testable requirements:

1. **TR1**: The service should accept and return a success status for a loan application containing all required fields (applicant name, email, phone, loan amount, loan purpose)
2. **TR2**: The service should reject and return an error for a loan application missing any mandatory field
3. **TR3**: The service should persist valid loan applications using a repository interface

## Dependencies

Before starting, ensure your project includes:

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

**LoanApplication.java**

```java
public class LoanApplication {
    private String applicantName;
    private String email;
    private String phone;
    private Double loanAmount;
    private String loanPurpose;
    
    public LoanApplication(String applicantName, String email, String phone, 
                          Double loanAmount, String loanPurpose) {
        this.applicantName = applicantName;
        this.email = email;
        this.phone = phone;
        this.loanAmount = loanAmount;
        this.loanPurpose = loanPurpose;
    }
    
    // Getters
    public String getApplicantName() { return applicantName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Double getLoanAmount() { return loanAmount; }
    public String getLoanPurpose() { return loanPurpose; }
}
```

**LoanProcessingResult.java**

```java
public class LoanProcessingResult {
    private LoanProcessingStatus status;
    private String errorMessage;
    
    public LoanProcessingResult(LoanProcessingStatus status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
    
    public LoanProcessingStatus getStatus() { return status; }
    public String getErrorMessage() { return errorMessage; }
}
```

**LoanProcessingStatus.java**

```java
public enum LoanProcessingStatus {
    ACCEPTED,
    REJECTED
}
```

**LoanRepository.java**

```java
public interface LoanRepository {
    void save(LoanApplication application);
}
```


## Your Challenge: Implement Using TDD

Follow the TDD lifecycle (RED-GREEN-REFACTOR) to implement the following test functions and their corresponding functions under test.

### Test Function 1: Validate Application with All Required Fields

#### RED Phase - Write Failing Test

Create the test class with the first failing test:

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanProcessingServiceTest {
    
    private LoanProcessingService loanProcessingService;
    
    @Mock
    private LoanRepository loanRepository;
    
    @BeforeEach
    void setUp() {
        // Initialize mockito annotations and create service instance with mocked repository
    }
    
    @Test
    void shouldAcceptValidLoanApplication() {
        // TODO: Write test that:
        // 1. Creates a valid LoanApplication with all required fields
        //    (applicant name, email, phone, loan amount, loan purpose)
        // 2. Calls processApplication method on the service
        // 3. Asserts that result status is ACCEPTED
        // 4. Asserts that error message is null
    }
}
```


#### GREEN Phase - Make Test Pass

**LoanProcessingService.java**

```java
public class LoanProcessingService {
    private LoanRepository loanRepository;
    
    public LoanProcessingService(LoanRepository loanRepository) {
        // TODO: Initialize the repository field
    }
    
    public LoanProcessingResult processApplication(LoanApplication application) {
        // TODO: Return a result that makes the first test pass
        // For now, simply return ACCEPTED status with no error message
        // (This will be enhanced in later iterations)
    }
}
```


### Test Function 2: Reject Application Missing Mandatory Fields

#### RED Phase - Write Failing Test

Add the second test to the test class:

```java
@Test
void shouldRejectApplicationWithMissingMandatoryFields() {
    // TODO: Write test that:
    // 1. Creates an invalid LoanApplication with at least one missing required field
    //    (e.g., null applicant name)
    // 2. Calls processApplication method on the service
    // 3. Asserts that result status is REJECTED
    // 4. Asserts that error message is not null
    // 5. Asserts that error message contains appropriate validation message
}
```


#### GREEN Phase - Make Test Pass

Update the LoanProcessingService:

```java
public LoanProcessingResult processApplication(LoanApplication application) {
    // TODO: Add validation logic:
    // 1. Check if applicant name is null or empty - return REJECTED with error message
    // 2. Check if email is null or empty - return REJECTED with error message
    // 3. Check if phone is null or empty - return REJECTED with error message
    // 4. Check if loan amount is null or less than/equal to zero - return REJECTED with error message
    // 5. Check if loan purpose is null or empty - return REJECTED with error message
    // 6. If all validations pass, return ACCEPTED with no error message
}
```


#### REFACTOR Phase

Consider refactoring the validation logic:

```java
public LoanProcessingResult processApplication(LoanApplication application) {
    // TODO: Refactor validation logic:
    // 1. Create a method validateApplication() that returns a list of validation errors
    // 2. If errors exist, return REJECTED with concatenated error messages
    // 3. If no errors, return ACCEPTED with null error message
}

private List<String> validateApplication(LoanApplication application) {
    // TODO: Implement validation method:
    // 1. Create empty list of errors
    // 2. Add error message for each missing/invalid field
    // 3. Return the list of errors
}

private boolean isNullOrEmpty(String value) {
    // TODO: Helper method to check if string is null or empty after trimming
}
```


### Test Function 3: Ensure Newly Accepted Applications are Persisted

#### RED Phase - Write Failing Test

Add the third test to verify persistence:

```java
@Test
void shouldPersistAcceptedApplications() {
    // TODO: Write test that:
    // 1. Creates a valid LoanApplication with all required fields
    // 2. Calls processApplication method on the service
    // 3. Asserts that result status is ACCEPTED
    // 4. Uses Mockito to verify that loanRepository.save() was called exactly once
    //    with the application as parameter
}
```


#### GREEN Phase - Make Test Pass

Update the service to persist accepted applications:

```java
public LoanProcessingResult processApplication(LoanApplication application) {
    // TODO: Update implementation:
    // 1. Perform validation as before
    // 2. If validation fails, return REJECTED result
    // 3. If validation passes:
    //    a. Call loanRepository.save(application) to persist the application
    //    b. Return ACCEPTED result
}
```


## Challenge Instructions

1. **Start with RED**: Write each failing test first
2. **Move to GREEN**: Write minimal production code to make the test pass
3. **Apply REFACTOR**: Improve code quality without breaking tests
4. **Repeat the cycle** for each test function

## Success Criteria

Your implementation should:

- ✅ Pass all three test functions
- ✅ Validate all mandatory fields (applicant name, email, phone, loan amount, loan purpose)
- ✅ Return appropriate status codes (ACCEPTED/REJECTED)
- ✅ Provide meaningful error messages for validation failures
- ✅ Persist accepted applications using the repository
- ✅ Follow clean code principles and proper separation of concerns


## Key TDD Learning Objectives

By completing this challenge, you will practice:

- Writing tests before implementation
- Making tests pass with minimal code
- Refactoring while maintaining test coverage
- Using mocks to isolate units under test
- Following the RED-GREEN-REFACTOR cycle

Good luck with your TDD implementation!