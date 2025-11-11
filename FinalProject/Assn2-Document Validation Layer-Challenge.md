# Assignment 2: Document Validation Layer - Challenge Document

## Business Requirements

The loan processing service must now validate that applicants have submitted all required documents and that these documents are valid and not expired. The system should:

1. Check for all mandatory documents (ID proof, income proof, address proof)
2. Validate that submitted documents are not expired and are authentic
3. Mark applications as "awaiting documents" when required documents are missing
4. Reject applications with invalid or expired documents
5. Only proceed with loan processing when all document requirements are satisfied

## Testable Requirements

From the business requirements, we derive the following testable requirements:

1. **TR4**: The service should accept applications when all required documents are present and valid (using document validation service)
2. **TR5**: The service should mark applications as "awaiting documents" when any required documents are missing
3. **TR6**: The service should reject applications when documents are invalid or expired

## Dependencies

Ensure your project still includes the same dependencies from Assignment 1:

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

**Document.java**
```java
public class Document {
    private String documentType;
    private String fileName;
    
    public Document(String documentType, String fileName) {
        this.documentType = documentType;
        this.fileName = fileName;
    }
    
    public String getDocumentType() { return documentType; }
    public String getFileName() { return fileName; }
}
```

**DocumentValidationResult.java**
```java
import java.util.List;

public class DocumentValidationResult {
    private boolean isValid;
    private String message;
    private List<String> missingDocuments;
    
    public DocumentValidationResult(boolean isValid, String message, List<String> missingDocuments) {
        this.isValid = isValid;
        this.message = message;
        this.missingDocuments = missingDocuments;
    }
    
    public boolean isValid() { return isValid; }
    public String getMessage() { return message; }
    public List<String> getMissingDocuments() { return missingDocuments; }
}
```

**DocumentValidationService.java**
```java
import java.util.List;

public interface DocumentValidationService {
    DocumentValidationResult validateDocuments(List<Document> documents);
}
```

**Updated LoanApplication.java** (Enhanced to support documents)
```java
import java.util.List;

public class LoanApplication {
    private String applicantName;
    private String email;
    private String phone;
    private Double loanAmount;
    private String loanPurpose;
    private List<Document> documents;
    
    public LoanApplication(String applicantName, String email, String phone, 
                          Double loanAmount, String loanPurpose) {
        this.applicantName = applicantName;
        this.email = email;
        this.phone = phone;
        this.loanAmount = loanAmount;
        this.loanPurpose = loanPurpose;
    }
    
    public LoanApplication(String applicantName, String email, String phone, 
                          Double loanAmount, String loanPurpose, List<Document> documents) {
        this.applicantName = applicantName;
        this.email = email;
        this.phone = phone;
        this.loanAmount = loanAmount;
        this.loanPurpose = loanPurpose;
        this.documents = documents;
    }
    
    // Getters
    public String getApplicantName() { return applicantName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Double getLoanAmount() { return loanAmount; }
    public String getLoanPurpose() { return loanPurpose; }
    public List<Document> getDocuments() { return documents; }
}
```

**Updated LoanProcessingStatus.java** (Enhanced with new status)
```java
public enum LoanProcessingStatus {
    ACCEPTED,
    REJECTED,
    AWAITING_DOCUMENTS
}
```

**LoanProcessingResult.java** (Same as Assignment 1)
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

**LoanRepository.java** (Same as Assignment 1)
```java
public interface LoanRepository {
    void save(LoanApplication application);
}
```

## Your Challenge: Implement Document Validation Using TDD

Follow the TDD lifecycle (RED-GREEN-REFACTOR) to enhance the existing LoanProcessingService and implement the following test functions.

### Test Function 1: Accept Application with All Required Documents Present

#### RED Phase - Write Failing Test

Create a new test class for document validation tests:

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanProcessingServiceDocumentValidationTest {
    
    private LoanProcessingService loanProcessingService;
    
    @Mock
    private LoanRepository loanRepository;
    
    @Mock
    private DocumentValidationService documentValidationService;
    
    @BeforeEach
    void setUp() {
        // TODO: Initialize mockito annotations and create service instance with both dependencies
    }
    
    @Test
    void shouldAcceptApplicationWithAllValidDocuments() {
        // TODO: Write test that:
        // 1. Creates a list of valid documents (ID_PROOF, INCOME_PROOF, ADDRESS_PROOF)
        // 2. Creates a LoanApplication with all required fields and documents
        // 3. Mocks documentValidationService.validateDocuments() to return valid result
        // 4. Calls processApplication method on the service
        // 5. Asserts that result status is ACCEPTED
        // 6. Asserts that error message is null
        // 7. Verifies that documentValidationService.validateDocuments was called once
    }
}
```

#### GREEN Phase - Make Test Pass

**Updated LoanProcessingService.java**
```java
import java.util.ArrayList;
import java.util.List;

public class LoanProcessingService {
    private LoanRepository loanRepository;
    private DocumentValidationService documentValidationService;
    
    public LoanProcessingService(LoanRepository loanRepository) {
        // TODO: Initialize repository field and set documentValidationService to null
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService) {
        // TODO: Initialize both repository and documentValidationService fields
    }
    
    public LoanProcessingResult processApplication(LoanApplication application) {
        // TODO: Implement enhanced processing logic:
        // 1. Validate basic application fields (reuse existing logic from Assignment 1)
        // 2. If basic validation fails, return REJECTED with error messages
        // 3. If documentValidationService is available and application has documents:
        //    a. Call documentValidationService.validateDocuments()
        //    b. If documents are invalid, return REJECTED with error message
        // 4. If all validations pass, save application and return ACCEPTED
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

### Test Function 2: Mark Application as Awaiting Documents When Missing

#### RED Phase - Write Failing Test

Add the second test to the document validation test class:

```java
@Test
void shouldMarkApplicationAsAwaitingDocumentsWhenMissing() {
    // TODO: Write test that:
    // 1. Creates a list with incomplete documents (missing some required types)
    // 2. Creates a LoanApplication with basic fields and incomplete documents
    // 3. Creates a list of missing document types
    // 4. Mocks documentValidationService.validateDocuments() to return invalid result with missing documents
    // 5. Calls processApplication method on the service
    // 6. Asserts that result status is AWAITING_DOCUMENTS
    // 7. Asserts that error message contains information about missing documents
    // 8. Verifies that documentValidationService was called
    // 9. Verifies that repository.save() was NOT called
}
```

#### GREEN Phase - Make Test Pass

Update the LoanProcessingService:

```java
public LoanProcessingResult processApplication(LoanApplication application) {
    // TODO: Enhanced implementation:
    // 1. Validate basic application fields
    // 2. If basic validation fails, return REJECTED
    // 3. If documentValidationService is available and application has documents:
    //    a. Call documentValidationService.validateDocuments()
    //    b. If documents are invalid:
    //       - Check if missingDocuments list is not null and not empty
    //       - If missing documents, return AWAITING_DOCUMENTS status
    //       - If invalid documents (not missing), return REJECTED status
    // 4. If all validations pass, save application and return ACCEPTED
}
```

#### REFACTOR Phase

Consider extracting document validation logic:

```java
private LoanProcessingResult validateDocuments(LoanApplication application) {
    // TODO: Extract document validation logic:
    // 1. Check if documentValidationService is available and application has documents
    // 2. Call documentValidationService.validateDocuments()
    // 3. If documents are invalid:
    //    a. Check if it's missing documents or invalid documents
    //    b. Return appropriate status (AWAITING_DOCUMENTS or REJECTED)
    // 4. Return null if no document validation issues
}
```

### Test Function 3: Reject Application with Invalid/Expired Documents

#### RED Phase - Write Failing Test

Add the third test:

```java
@Test
void shouldRejectApplicationWithInvalidOrExpiredDocuments() {
    // TODO: Write test that:
    // 1. Creates a list of documents with invalid/expired files
    // 2. Creates a LoanApplication with basic fields and invalid documents
    // 3. Mocks documentValidationService.validateDocuments() to return invalid result with null missing documents
    //    (indicating documents are present but invalid/expired, not missing)
    // 4. Calls processApplication method on the service
    // 5. Asserts that result status is REJECTED
    // 6. Asserts that error message contains information about invalid/expired documents
    // 7. Verifies that documentValidationService was called
    // 8. Verifies that repository.save() was NOT called
}
```

#### GREEN Phase - Make Test Pass

The current implementation should already handle this case correctly. If not, update the document validation logic to distinguish between missing documents (AWAITING_DOCUMENTS) and invalid documents (REJECTED).

#### REFACTOR Phase

Ensure the service logic is clean and well-organized:

```java
public LoanProcessingResult processApplication(LoanApplication application) {
    // TODO: Final implementation should:
    // 1. Validate basic application fields
    // 2. If basic validation fails, return REJECTED
    // 3. Validate documents (call separate method)
    // 4. If document validation fails, return appropriate result
    // 5. If all validations pass, save application and return ACCEPTED
}

private LoanProcessingResult validateDocuments(LoanApplication application) {
    // TODO: Document validation method should:
    // 1. Check if document validation is needed
    // 2. Call external document validation service
    // 3. Interpret results and return appropriate status
    // 4. Return null if no issues found
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
4. **Maintain backward compatibility**: Ensure Assignment 1 functionality still works
5. **Repeat the cycle** for each test function

## Success Criteria

Your implementation should:

- ✅ Pass all three new test functions
- ✅ Maintain backward compatibility with Assignment 1 functionality
- ✅ Handle document validation through external service
- ✅ Distinguish between missing documents (AWAITING_DOCUMENTS) and invalid documents (REJECTED)
- ✅ Properly integrate with mocked DocumentValidationService
- ✅ Follow clean code principles with proper separation of concerns

## Key TDD Learning Objectives

By completing this challenge, you will practice:

- Extending existing functionality using TDD
- Integrating external services with dependency injection
- Using mocks to isolate units under test
- Handling multiple validation scenarios with appropriate status codes
- Refactoring complex logic while maintaining test coverage
- Maintaining backward compatibility during enhancement

## Testing Scenarios Covered

1. **Valid Complete Application**: All fields valid, all documents present and valid → ACCEPTED
2. **Missing Documents**: Valid fields, some required documents missing → AWAITING_DOCUMENTS  
3. **Invalid Documents**: Valid fields, documents present but invalid/expired → REJECTED
4. **Basic Field Validation**: Maintains existing validation from Assignment 1 → REJECTED

Good luck with your TDD implementation! Remember to follow the RED-GREEN-REFACTOR cycle for each test function.