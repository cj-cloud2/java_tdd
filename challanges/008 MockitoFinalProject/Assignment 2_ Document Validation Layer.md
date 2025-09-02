
# Assignment 2: Document Validation Layer

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


## Test Function 1: Accept Application with All Required Documents Present

### RED Phase - Write Failing Test

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
        MockitoAnnotations.openMocks(this);
        loanProcessingService = new LoanProcessingService(loanRepository, documentValidationService);
    }
    
    @Test
    void shouldAcceptApplicationWithAllValidDocuments() {
        // Arrange
        List<Document> documents = Arrays.asList(
            new Document("ID_PROOF", "valid-id-doc.pdf"),
            new Document("INCOME_PROOF", "salary-slip.pdf"),
            new Document("ADDRESS_PROOF", "utility-bill.pdf")
        );
        
        LoanApplication applicationWithDocuments = new LoanApplication(
            "John Doe",
            "john.doe@email.com", 
            "1234567890",
            50000.0,
            "Home Purchase",
            documents
        );
        
        when(documentValidationService.validateDocuments(documents))
            .thenReturn(new DocumentValidationResult(true, "All documents are valid", null));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(applicationWithDocuments);
        
        // Assert
        assertEquals(LoanProcessingStatus.ACCEPTED, result.getStatus());
        assertNull(result.getErrorMessage());
        verify(documentValidationService, times(1)).validateDocuments(documents);
    }
}
```

**Run the test** - It should fail because the new classes and updated constructor don't exist yet.

### GREEN Phase - Make Test Pass

First, create the new classes needed:

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

Update **LoanApplication.java** to include documents:

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

Add new status to **LoanProcessingStatus.java**:

```java
public enum LoanProcessingStatus {
    ACCEPTED,
    REJECTED,
    AWAITING_DOCUMENTS
}
```

Update **LoanProcessingService.java** with document validation:

```java
import java.util.ArrayList;
import java.util.List;

public class LoanProcessingService {
    private LoanRepository loanRepository;
    private DocumentValidationService documentValidationService;
    
    public LoanProcessingService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
        this.documentValidationService = null;
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService) {
        this.loanRepository = loanRepository;
        this.documentValidationService = documentValidationService;
    }
    
    public LoanProcessingResult processApplication(LoanApplication application) {
        List<String> validationErrors = validateApplication(application);
        
        if (!validationErrors.isEmpty()) {
            return new LoanProcessingResult(LoanProcessingStatus.REJECTED, 
                String.join(", ", validationErrors));
        }
        
        // Document validation if service is available
        if (documentValidationService != null && application.getDocuments() != null) {
            DocumentValidationResult documentResult = documentValidationService.validateDocuments(application.getDocuments());
            
            if (!documentResult.isValid()) {
                return new LoanProcessingResult(LoanProcessingStatus.REJECTED, documentResult.getMessage());
            }
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

**Run the test** - It should now pass.

### REFACTOR Phase

The code is working but we should improve the document validation logic. Let's move to the next test to drive more functionality.

## Test Function 2: Mark Application as Awaiting Documents When Missing

### RED Phase - Write Failing Test

Add the second test to the document validation test class:

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
        MockitoAnnotations.openMocks(this);
        loanProcessingService = new LoanProcessingService(loanRepository, documentValidationService);
    }
    
    @Test
    void shouldAcceptApplicationWithAllValidDocuments() {
        // Arrange
        List<Document> documents = Arrays.asList(
            new Document("ID_PROOF", "valid-id-doc.pdf"),
            new Document("INCOME_PROOF", "salary-slip.pdf"),
            new Document("ADDRESS_PROOF", "utility-bill.pdf")
        );
        
        LoanApplication applicationWithDocuments = new LoanApplication(
            "John Doe",
            "john.doe@email.com", 
            "1234567890",
            50000.0,
            "Home Purchase",
            documents
        );
        
        when(documentValidationService.validateDocuments(documents))
            .thenReturn(new DocumentValidationResult(true, "All documents are valid", null));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(applicationWithDocuments);
        
        // Assert
        assertEquals(LoanProcessingStatus.ACCEPTED, result.getStatus());
        assertNull(result.getErrorMessage());
        verify(documentValidationService, times(1)).validateDocuments(documents);
    }
    
    @Test
    void shouldMarkApplicationAsAwaitingDocumentsWhenMissing() {
        // Arrange
        List<Document> incompleteDocuments = Arrays.asList(
            new Document("ID_PROOF", "id-doc.pdf")
            // Missing INCOME_PROOF and ADDRESS_PROOF
        );
        
        LoanApplication applicationWithMissingDocs = new LoanApplication(
            "Jane Smith",
            "jane.smith@email.com", 
            "0987654321",
            75000.0,
            "Car Purchase",
            incompleteDocuments
        );
        
        List<String> missingDocs = Arrays.asList("INCOME_PROOF", "ADDRESS_PROOF");
        when(documentValidationService.validateDocuments(incompleteDocuments))
            .thenReturn(new DocumentValidationResult(false, "Missing required documents", missingDocs));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(applicationWithMissingDocs);
        
        // Assert
        assertEquals(LoanProcessingStatus.AWAITING_DOCUMENTS, result.getStatus());
        assertTrue(result.getErrorMessage().contains("Missing required documents"));
        verify(documentValidationService, times(1)).validateDocuments(incompleteDocuments);
        verify(loanRepository, never()).save(any());
    }
}
```

**Run the tests** - The new test should fail because we're not handling the AWAITING_DOCUMENTS status.

### GREEN Phase - Make Test Pass

Update **LoanProcessingService.java** to handle missing documents:

```java
import java.util.ArrayList;
import java.util.List;

public class LoanProcessingService {
    private LoanRepository loanRepository;
    private DocumentValidationService documentValidationService;
    
    public LoanProcessingService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
        this.documentValidationService = null;
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService) {
        this.loanRepository = loanRepository;
        this.documentValidationService = documentValidationService;
    }
    
    public LoanProcessingResult processApplication(LoanApplication application) {
        List<String> validationErrors = validateApplication(application);
        
        if (!validationErrors.isEmpty()) {
            return new LoanProcessingResult(LoanProcessingStatus.REJECTED, 
                String.join(", ", validationErrors));
        }
        
        // Document validation if service is available
        if (documentValidationService != null && application.getDocuments() != null) {
            DocumentValidationResult documentResult = documentValidationService.validateDocuments(application.getDocuments());
            
            if (!documentResult.isValid()) {
                // Check if it's missing documents (should be awaiting) or invalid documents (should be rejected)
                if (documentResult.getMissingDocuments() != null && !documentResult.getMissingDocuments().isEmpty()) {
                    return new LoanProcessingResult(LoanProcessingStatus.AWAITING_DOCUMENTS, documentResult.getMessage());
                } else {
                    return new LoanProcessingResult(LoanProcessingStatus.REJECTED, documentResult.getMessage());
                }
            }
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

**Run both tests** - They should both pass.

### REFACTOR Phase

The logic is getting complex. Let's refactor to separate document validation logic:

```java
import java.util.ArrayList;
import java.util.List;

public class LoanProcessingService {
    private LoanRepository loanRepository;
    private DocumentValidationService documentValidationService;
    
    public LoanProcessingService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
        this.documentValidationService = null;
    }
    
    public LoanProcessingService(LoanRepository loanRepository, DocumentValidationService documentValidationService) {
        this.loanRepository = loanRepository;
        this.documentValidationService = documentValidationService;
    }
    
    public LoanProcessingResult processApplication(LoanApplication application) {
        List<String> validationErrors = validateApplication(application);
        
        if (!validationErrors.isEmpty()) {
            return new LoanProcessingResult(LoanProcessingStatus.REJECTED, 
                String.join(", ", validationErrors));
        }
        
        // Document validation
        LoanProcessingResult documentValidationResult = validateDocuments(application);
        if (documentValidationResult != null) {
            return documentValidationResult;
        }
        
        // Persist the valid application
        loanRepository.save(application);
        
        return new LoanProcessingResult(LoanProcessingStatus.ACCEPTED, null);
    }
    
    private LoanProcessingResult validateDocuments(LoanApplication application) {
        if (documentValidationService != null && application.getDocuments() != null) {
            DocumentValidationResult documentResult = documentValidationService.validateDocuments(application.getDocuments());
            
            if (!documentResult.isValid()) {
                if (documentResult.getMissingDocuments() != null && !documentResult.getMissingDocuments().isEmpty()) {
                    return new LoanProcessingResult(LoanProcessingStatus.AWAITING_DOCUMENTS, documentResult.getMessage());
                } else {
                    return new LoanProcessingResult(LoanProcessingStatus.REJECTED, documentResult.getMessage());
                }
            }
        }
        return null; // No document validation issues
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

**Run both tests** - They should still pass after refactoring.

## Test Function 3: Reject Application with Invalid/Expired Documents

### RED Phase - Write Failing Test

Add the third test:

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
        MockitoAnnotations.openMocks(this);
        loanProcessingService = new LoanProcessingService(loanRepository, documentValidationService);
    }
    
    @Test
    void shouldAcceptApplicationWithAllValidDocuments() {
        // Arrange
        List<Document> documents = Arrays.asList(
            new Document("ID_PROOF", "valid-id-doc.pdf"),
            new Document("INCOME_PROOF", "salary-slip.pdf"),
            new Document("ADDRESS_PROOF", "utility-bill.pdf")
        );
        
        LoanApplication applicationWithDocuments = new LoanApplication(
            "John Doe",
            "john.doe@email.com", 
            "1234567890",
            50000.0,
            "Home Purchase",
            documents
        );
        
        when(documentValidationService.validateDocuments(documents))
            .thenReturn(new DocumentValidationResult(true, "All documents are valid", null));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(applicationWithDocuments);
        
        // Assert
        assertEquals(LoanProcessingStatus.ACCEPTED, result.getStatus());
        assertNull(result.getErrorMessage());
        verify(documentValidationService, times(1)).validateDocuments(documents);
    }
    
    @Test
    void shouldMarkApplicationAsAwaitingDocumentsWhenMissing() {
        // Arrange
        List<Document> incompleteDocuments = Arrays.asList(
            new Document("ID_PROOF", "id-doc.pdf")
            // Missing INCOME_PROOF and ADDRESS_PROOF
        );
        
        LoanApplication applicationWithMissingDocs = new LoanApplication(
            "Jane Smith",
            "jane.smith@email.com", 
            "0987654321",
            75000.0,
            "Car Purchase",
            incompleteDocuments
        );
        
        List<String> missingDocs = Arrays.asList("INCOME_PROOF", "ADDRESS_PROOF");
        when(documentValidationService.validateDocuments(incompleteDocuments))
            .thenReturn(new DocumentValidationResult(false, "Missing required documents", missingDocs));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(applicationWithMissingDocs);
        
        // Assert
        assertEquals(LoanProcessingStatus.AWAITING_DOCUMENTS, result.getStatus());
        assertTrue(result.getErrorMessage().contains("Missing required documents"));
        verify(documentValidationService, times(1)).validateDocuments(incompleteDocuments);
        verify(loanRepository, never()).save(any());
    }
    
    @Test
    void shouldRejectApplicationWithInvalidOrExpiredDocuments() {
        // Arrange
        List<Document> invalidDocuments = Arrays.asList(
            new Document("ID_PROOF", "expired-id-doc.pdf"),
            new Document("INCOME_PROOF", "invalid-salary-slip.pdf"),
            new Document("ADDRESS_PROOF", "utility-bill.pdf")
        );
        
        LoanApplication applicationWithInvalidDocs = new LoanApplication(
            "Bob Wilson",
            "bob.wilson@email.com", 
            "5555555555",
            100000.0,
            "Business Loan",
            invalidDocuments
        );
        
        // Invalid documents return false with null missing documents list (indicating invalid, not missing)
        when(documentValidationService.validateDocuments(invalidDocuments))
            .thenReturn(new DocumentValidationResult(false, "Documents are expired or invalid", null));
        
        // Act
        LoanProcessingResult result = loanProcessingService.processApplication(applicationWithInvalidDocs);
        
        // Assert
        assertEquals(LoanProcessingStatus.REJECTED, result.getStatus());
        assertTrue(result.getErrorMessage().contains("Documents are expired or invalid"));
        verify(documentValidationService, times(1)).validateDocuments(invalidDocuments);
        verify(loanRepository, never()).save(any());
    }
}
```

**Run all tests** - The new test should pass because our current implementation already handles this case (when missing documents is null, it rejects the application).

### GREEN Phase - Already Passing

The test is already passing with our current implementation! This shows that our refactored code correctly handles both missing documents (AWAITING_DOCUMENTS) and invalid documents (REJECTED).

### REFACTOR Phase

Our implementation is clean and handles all the cases correctly. No additional refactoring is needed.

## Assignment 2 Complete

Congratulations! You have successfully completed Assignment 2 using Test-Driven Development. Your `LoanProcessingService` now includes:

1. ✅ **Basic loan application validation** (from Assignment 1)
2. ✅ **Document validation integration** with external service
3. ✅ **Missing document handling** (AWAITING_DOCUMENTS status)
4. ✅ **Invalid/expired document rejection**

### Summary of What We Added

**New Classes:**

- **Document**: Represents a document with type and filename
- **DocumentValidationResult**: Result object for document validation
- **DocumentValidationService**: Interface for document validation
- **LoanProcessingStatus.AWAITING_DOCUMENTS**: New status for incomplete applications

**Enhanced Classes:**

- **LoanApplication**: Now supports optional document list
- **LoanProcessingService**: Integrated document validation workflow


### Current Service Capabilities

The service now provides a complete document validation layer:

- Validates all required documents are present
- Checks document validity and expiration via external service
- Properly routes applications based on document status
- Maintains backward compatibility with applications without documents

**Key TDD Learnings from Assignment 2:**

- Added new functionality without breaking existing tests
- Used dependency injection to integrate external services
- Separated concerns with dedicated validation methods
- Handled multiple validation scenarios with clear status codes
