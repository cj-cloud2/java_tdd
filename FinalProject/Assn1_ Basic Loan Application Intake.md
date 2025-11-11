
# Assignment 1: Basic Loan Application Intake

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


## Test Function 1: Validate Application with All Required Fields

### RED Phase - Write Failing Test

Create the test class and first failing test:

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
        MockitoAnnotations.openMocks(this);
        loanProcessingService = new LoanProcessingService(loanRepository);
    }
    
    @Test
    void shouldAcceptValidLoanApplication() {
        // Arrange
        LoanApplication validApplication = new LoanApplication(
            "John Doe",
            "john.doe@email.com", 
            "1234567890",
            50000.0,
            "Home Purchase"
        );
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(validApplication);
        
        // Assert
        assertEquals(LoanProcessingStatus.ACCEPTED, result.getStatus());
        assertNull(result.getErrorMessage());
    }
}
```

**Run the test** - It should fail because the classes don't exist yet.

### GREEN Phase - Make Test Pass

Create the minimal code to make the test pass:

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

**LoanProcessingService.java**

```java
public class LoanProcessingService {
    private LoanRepository loanRepository;
    
    public LoanProcessingService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }
    
    public LoanProcessingResult processApplication(LoanApplication application) {
        return new LoanProcessingResult(LoanProcessingStatus.ACCEPTED, null);
    }
}
```

**Run the test** - It should now pass.

### REFACTOR Phase

The code is simple enough that no refactoring is needed at this point. Let's move to the next test.

## Test Function 2: Reject Application Missing Mandatory Fields

### RED Phase - Write Failing Test

Update the test class to include the second test:

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
        MockitoAnnotations.openMocks(this);
        loanProcessingService = new LoanProcessingService(loanRepository);
    }
    
    @Test
    void shouldAcceptValidLoanApplication() {
        // Arrange
        LoanApplication validApplication = new LoanApplication(
            "John Doe",
            "john.doe@email.com", 
            "1234567890",
            50000.0,
            "Home Purchase"
        );
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(validApplication);
        
        // Assert
        assertEquals(LoanProcessingStatus.ACCEPTED, result.getStatus());
        assertNull(result.getErrorMessage());
    }
    
    @Test
    void shouldRejectApplicationWithMissingMandatoryFields() {
        // Arrange
        LoanApplication invalidApplication = new LoanApplication(
            null,  // Missing applicant name
            "john.doe@email.com", 
            "1234567890",
            50000.0,
            "Home Purchase"
        );
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(invalidApplication);
        
        // Assert
        assertEquals(LoanProcessingStatus.REJECTED, result.getStatus());
        assertNotNull(result.getErrorMessage());
        assertTrue(result.getErrorMessage().contains("Applicant name is required"));
    }
}
```

**Run the test** - The new test should fail.

### GREEN Phase - Make Test Pass

Update the LoanProcessingService to validate required fields:

```java
public class LoanProcessingService {
    private LoanRepository loanRepository;
    
    public LoanProcessingService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }
    
    public LoanProcessingResult processApplication(LoanApplication application) {
        // Validate required fields
        if (application.getApplicantName() == null || application.getApplicantName().trim().isEmpty()) {
            return new LoanProcessingResult(LoanProcessingStatus.REJECTED, "Applicant name is required");
        }
        
        if (application.getEmail() == null || application.getEmail().trim().isEmpty()) {
            return new LoanProcessingResult(LoanProcessingStatus.REJECTED, "Email is required");
        }
        
        if (application.getPhone() == null || application.getPhone().trim().isEmpty()) {
            return new LoanProcessingResult(LoanProcessingStatus.REJECTED, "Phone number is required");
        }
        
        if (application.getLoanAmount() == null || application.getLoanAmount() <= 0) {
            return new LoanProcessingResult(LoanProcessingStatus.REJECTED, "Valid loan amount is required");
        }
        
        if (application.getLoanPurpose() == null || application.getLoanPurpose().trim().isEmpty()) {
            return new LoanProcessingResult(LoanProcessingStatus.REJECTED, "Loan purpose is required");
        }
        
        return new LoanProcessingResult(LoanProcessingStatus.ACCEPTED, null);
    }
}
```

**Run both tests** - They should pass.

### REFACTOR Phase

The validation logic is repetitive. Let's refactor it:

```java
import java.util.ArrayList;
import java.util.List;

public class LoanProcessingService {
    private LoanRepository loanRepository;
    
    public LoanProcessingService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }
    
    public LoanProcessingResult processApplication(LoanApplication application) {
        List<String> validationErrors = validateApplication(application);
        
        if (!validationErrors.isEmpty()) {
            return new LoanProcessingResult(LoanProcessingStatus.REJECTED, 
                String.join(", ", validationErrors));
        }
        
        return new LoanProcessingResult(LoanProcessingStatus.ACCEPTED, null);
    }
    
    private List<String> validateApplication(LoanApplication application) {
        List<String> errors = new ArrayList<>();
        
        if (isNullOrEmpty(application.getApplicantName())) {
            errors.add("Applicant name is required");
        }
        
        if (isNullOrEmpty(application.getEmail())) {
            errors.add("Email is required");
        }
        
        if (isNullOrEmpty(application.getPhone())) {
            errors.add("Phone number is required");
        }
        
        if (application.getLoanAmount() == null || application.getLoanAmount() <= 0) {
            errors.add("Valid loan amount is required");
        }
        
        if (isNullOrEmpty(application.getLoanPurpose())) {
            errors.add("Loan purpose is required");
        }
        
        return errors;
    }
    
    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
```

**Run both tests** - They should still pass.

## Test Function 3: Ensure Newly Accepted Applications are Persisted

### RED Phase - Write Failing Test

Add the third test to verify persistence:

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
        MockitoAnnotations.openMocks(this);
        loanProcessingService = new LoanProcessingService(loanRepository);
    }
    
    @Test
    void shouldAcceptValidLoanApplication() {
        // Arrange
        LoanApplication validApplication = new LoanApplication(
            "John Doe",
            "john.doe@email.com", 
            "1234567890",
            50000.0,
            "Home Purchase"
        );
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(validApplication);
        
        // Assert
        assertEquals(LoanProcessingStatus.ACCEPTED, result.getStatus());
        assertNull(result.getErrorMessage());
    }
    
    @Test
    void shouldRejectApplicationWithMissingMandatoryFields() {
        // Arrange
        LoanApplication invalidApplication = new LoanApplication(
            null,  // Missing applicant name
            "john.doe@email.com", 
            "1234567890",
            50000.0,
            "Home Purchase"
        );
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(invalidApplication);
        
        // Assert
        assertEquals(LoanProcessingStatus.REJECTED, result.getStatus());
        assertNotNull(result.getErrorMessage());
        assertTrue(result.getErrorMessage().contains("Applicant name is required"));
    }
    
    @Test
    void shouldPersistAcceptedApplications() {
        // Arrange
        LoanApplication validApplication = new LoanApplication(
            "Jane Smith",
            "jane.smith@email.com", 
            "0987654321",
            75000.0,
            "Car Purchase"
        );
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(validApplication);
        
        // Assert
        assertEquals(LoanProcessingStatus.ACCEPTED, result.getStatus());
        verify(loanRepository, times(1)).save(validApplication);
    }
}
```

**Run the tests** - The new test should fail because we're not calling the repository.

### GREEN Phase - Make Test Pass

Update the service to persist accepted applications:

```java
import java.util.ArrayList;
import java.util.List;

public class LoanProcessingService {
    private LoanRepository loanRepository;
    
    public LoanProcessingService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }
    
    public LoanProcessingResult processApplication(LoanApplication application) {
        List<String> validationErrors = validateApplication(application);
        
        if (!validationErrors.isEmpty()) {
            return new LoanProcessingResult(LoanProcessingStatus.REJECTED, 
                String.join(", ", validationErrors));
        }
        
        // Persist the valid application
        loanRepository.save(application);
        
        return new LoanProcessingResult(LoanProcessingStatus.ACCEPTED, null);
    }
    
    private List<String> validateApplication(LoanApplication application) {
        List<String> errors = new ArrayList<>();
        
        if (isNullOrEmpty(application.getApplicantName())) {
            errors.add("Applicant name is required");
        }
        
        if (isNullOrEmpty(application.getEmail())) {
            errors.add("Email is required");
        }
        
        if (isNullOrEmpty(application.getPhone())) {
            errors.add("Phone number is required");
        }
        
        if (application.getLoanAmount() == null || application.getLoanAmount() <= 0) {
            errors.add("Valid loan amount is required");
        }
        
        if (isNullOrEmpty(application.getLoanPurpose())) {
            errors.add("Loan purpose is required");
        }
        
        return errors;
    }
    
    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
```

**Run all tests** - They should all pass.

### REFACTOR Phase

The current code is clean and well-organized. No further refactoring is needed.

## Assignment 1 Complete

Congratulations! You have successfully completed Assignment 1 using Test-Driven Development. Your `LoanProcessingService` now:

1. ✅ Validates loan applications with all required fields
2. ✅ Rejects applications missing mandatory information
3. ✅ Persists accepted applications using a repository interface

### Summary of What We Built

- **LoanApplication**: Data class representing a loan application
- **LoanProcessingService**: Main service class that validates and processes applications
- **LoanProcessingResult**: Result object containing processing outcome
- **LoanProcessingStatus**: Enum for application status
- **LoanRepository**: Interface for persisting applications

The service is now ready for Assignment 2, where we'll add document validation capabilities while following the same TDD approach.

**Key TDD Learnings:**

- Each test drove the creation of just enough production code to pass
- We refactored without breaking existing functionality
- The RED-GREEN-REFACTOR cycle ensured we built exactly what was needed
- Mock objects allowed us to test in isolation from external dependencies

