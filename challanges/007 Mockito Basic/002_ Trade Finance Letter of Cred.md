# Mockito Assignment 2: Trade Finance Letter of Credit Processing

**Difficulty Level: 3**

## Business Requirements

In **Trade Finance**, a **Letter of Credit (LC)** is a financial instrument that guarantees payment from a buyer to a seller. Our system needs to handle the complete LC processing workflow:

1. **LC Application Processing**: Banks receive LC applications from importers, validate them, and issue LCs to facilitate international trade
2. **Document Verification**: When exporters present shipping documents, the system must verify them against LC terms and conditions
3. **Fee Management**: The system must calculate and charge various processing fees (issuance fees, amendment fees, document processing fees) to client accounts

## Testable Requirements Derived from Business Requirements

1. **LC Application Validation \& Storage**:
    - Validate LC application completeness and business rules
    - Save validated applications with appropriate status
    - Notify relevant parties upon successful processing
2. **Document Verification Against LC Terms**:
    - Verify presented trade documents against LC conditions
    - Check document authenticity and compliance
    - Update LC status based on document verification results
3. **Fee Calculation \& Account Charging**:
    - Calculate processing fees based on LC amount and type
    - Charge calculated fees to the client's account
    - Record fee transactions for audit purposes

***

## Step-by-Step TDD Guide: RED-GREEN-REFACTOR Cycle

### Prerequisites

- JUnit 5 dependencies
- Mockito 5 dependencies
- Java 11 or higher

***

## PASS 1: LC Application Validation \& Storage (RED-GREEN-REFACTOR)

### STEP 1: RED Phase - Write Failing Test

**Test Class to Create:** `LetterOfCreditProcessorTest.java`

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LetterOfCreditProcessorTest {

    @Mock
    private LCRepository lcRepository;
    
    @Mock
    private NotificationService notificationService;
    
    private LetterOfCreditProcessor lcProcessor;
    
    @BeforeEach
    void setUp() {
        lcProcessor = new LetterOfCreditProcessor(lcRepository, notificationService);
    }
    
    @Test
    void shouldValidateAndSaveLCApplication() {
        // Given
        LCApplication application = new LCApplication(
            "LC001", 
            "Importer Corp", 
            "Exporter Ltd", 
            50000.0, 
            "USD"
        );
        
        LetterOfCredit expectedLC = new LetterOfCredit(
            "LC001", 
            "Importer Corp", 
            "Exporter Ltd", 
            50000.0, 
            "USD", 
            LCStatus.ISSUED
        );
        
        when(lcRepository.save(any(LetterOfCredit.class))).thenReturn(expectedLC);
        
        // When
        LetterOfCredit result = lcProcessor.processApplication(application);
        
        // Then
        assertNotNull(result);
        assertEquals("LC001", result.getLcNumber());
        assertEquals(LCStatus.ISSUED, result.getStatus());
        
        // Verify interactions - Method Call Verification
        verify(lcRepository).save(any(LetterOfCredit.class));
        verify(notificationService).notifyLCIssued(eq("LC001"));
    }
}
```

**Supporting Classes to Create:**

```java
// LCApplication.java
class LCApplication {
    private String lcNumber;
    private String applicant;
    private String beneficiary;
    private double amount;
    private String currency;
    
    public LCApplication(String lcNumber, String applicant, String beneficiary, 
                        double amount, String currency) {
        this.lcNumber = lcNumber;
        this.applicant = applicant;
        this.beneficiary = beneficiary;
        this.amount = amount;
        this.currency = currency;
    }
    
    // Getters
    public String getLcNumber() { return lcNumber; }
    public String getApplicant() { return applicant; }
    public String getBeneficiary() { return beneficiary; }
    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
}
```

```java
// LetterOfCredit.java
class LetterOfCredit {
    private String lcNumber;
    private String applicant;
    private String beneficiary;
    private double amount;
    private String currency;
    private LCStatus status;
    
    public LetterOfCredit(String lcNumber, String applicant, String beneficiary, 
                         double amount, String currency, LCStatus status) {
        this.lcNumber = lcNumber;
        this.applicant = applicant;
        this.beneficiary = beneficiary;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }
    
    // Getters
    public String getLcNumber() { return lcNumber; }
    public String getApplicant() { return applicant; }
    public String getBeneficiary() { return beneficiary; }
    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public LCStatus getStatus() { return status; }
}
```

```java
// LCStatus.java
enum LCStatus {
    PENDING, ISSUED, DOCUMENTS_RECEIVED, COMPLIANT, NON_COMPLIANT, PAID, EXPIRED
}
```

```java
// LCRepository.java
interface LCRepository {
    LetterOfCredit save(LetterOfCredit lc);
    LetterOfCredit findByLcNumber(String lcNumber);
}
```

```java
// NotificationService.java
interface NotificationService {
    void notifyLCIssued(String lcNumber);
    void notifyDocumentStatus(String lcNumber, boolean compliant);
}
```

**Run Test**: Test should FAIL (RED) - LetterOfCreditProcessor class doesn't exist yet.

### STEP 2: GREEN Phase - Make Test Pass

**Create:** `LetterOfCreditProcessor.java`

```java
// LetterOfCreditProcessor.java
class LetterOfCreditProcessor {
    private final LCRepository lcRepository;
    private final NotificationService notificationService;
    
    public LetterOfCreditProcessor(LCRepository lcRepository, 
                                  NotificationService notificationService) {
        this.lcRepository = lcRepository;
        this.notificationService = notificationService;
    }
    
    public LetterOfCredit processApplication(LCApplication application) {
        // Create LC from application
        LetterOfCredit lc = new LetterOfCredit(
            application.getLcNumber(),
            application.getApplicant(),
            application.getBeneficiary(),
            application.getAmount(),
            application.getCurrency(),
            LCStatus.ISSUED
        );
        
        // Save LC
        LetterOfCredit savedLC = lcRepository.save(lc);
        
        // Send notification
        notificationService.notifyLCIssued(application.getLcNumber());
        
        return savedLC;
    }
}
```

**Run Test**: Test should PASS (GREEN).

### STEP 3: REFACTOR Phase - Improve Code

No major refactoring needed for this simple case, but we can add validation:

```java
// LetterOfCreditProcessor.java - REFACTORED VERSION (COMPLETE REPLACEMENT)
class LetterOfCreditProcessor {
    private final LCRepository lcRepository;
    private final NotificationService notificationService;
    
    public LetterOfCreditProcessor(LCRepository lcRepository, 
                                  NotificationService notificationService) {
        this.lcRepository = lcRepository;
        this.notificationService = notificationService;
    }
    
    public LetterOfCredit processApplication(LCApplication application) {
        // NEW CODE: Add basic validation
        validateApplication(application);
        
        // EXISTING CODE: Create LC from application
        LetterOfCredit lc = new LetterOfCredit(
            application.getLcNumber(),
            application.getApplicant(),
            application.getBeneficiary(),
            application.getAmount(),
            application.getCurrency(),
            LCStatus.ISSUED
        );
        
        // EXISTING CODE: Save LC
        LetterOfCredit savedLC = lcRepository.save(lc);
        
        // EXISTING CODE: Send notification
        notificationService.notifyLCIssued(application.getLcNumber());
        
        return savedLC;
    }
    
    // NEW CODE: Private validation method
    private void validateApplication(LCApplication application) {
        if (application.getLcNumber() == null || application.getLcNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("LC Number is required");
        }
        if (application.getAmount() <= 0) {
            throw new IllegalArgumentException("LC Amount must be positive");
        }
    }
}
```


***

## PASS 2: Document Verification Against LC Terms (RED-GREEN-REFACTOR)

### STEP 4: RED Phase - Add Second Failing Test

**Update Test Class:** `LetterOfCreditProcessorTest.java` (COMPLETE REPLACEMENT)

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LetterOfCreditProcessorTest {

    @Mock
    private LCRepository lcRepository;
    
    @Mock
    private NotificationService notificationService;
    
    // NEW CODE: Additional mock for document verification
    @Mock
    private DocumentValidator documentValidator;
    
    private LetterOfCreditProcessor lcProcessor;
    
    @BeforeEach
    void setUp() {
        // UPDATED CODE: Include documentValidator in constructor
        lcProcessor = new LetterOfCreditProcessor(lcRepository, notificationService, documentValidator);
    }
    
    // EXISTING TEST: shouldValidateAndSaveLCApplication
    @Test
    void shouldValidateAndSaveLCApplication() {
        // Given
        LCApplication application = new LCApplication(
            "LC001", 
            "Importer Corp", 
            "Exporter Ltd", 
            50000.0, 
            "USD"
        );
        
        LetterOfCredit expectedLC = new LetterOfCredit(
            "LC001", 
            "Importer Corp", 
            "Exporter Ltd", 
            50000.0, 
            "USD", 
            LCStatus.ISSUED
        );
        
        when(lcRepository.save(any(LetterOfCredit.class))).thenReturn(expectedLC);
        
        // When
        LetterOfCredit result = lcProcessor.processApplication(application);
        
        // Then
        assertNotNull(result);
        assertEquals("LC001", result.getLcNumber());
        assertEquals(LCStatus.ISSUED, result.getStatus());
        
        // Verify interactions - Method Call Verification
        verify(lcRepository).save(any(LetterOfCredit.class));
        verify(notificationService).notifyLCIssued(eq("LC001"));
    }
    
    // NEW TEST: Document verification with verification modes
    @Test
    void shouldVerifyDocumentsAndUpdateLCStatus() {
        // Given
        String lcNumber = "LC002";
        TradeDocument shipmentDoc = new TradeDocument("BILL_OF_LADING", "BL123");
        TradeDocument invoiceDoc = new TradeDocument("COMMERCIAL_INVOICE", "INV456");
        
        LetterOfCredit existingLC = new LetterOfCredit(
            lcNumber, "Importer Corp", "Exporter Ltd", 75000.0, "EUR", LCStatus.ISSUED
        );
        
        LetterOfCredit updatedLC = new LetterOfCredit(
            lcNumber, "Importer Corp", "Exporter Ltd", 75000.0, "EUR", LCStatus.COMPLIANT
        );
        
        when(lcRepository.findByLcNumber(lcNumber)).thenReturn(existingLC);
        when(documentValidator.validateDocument(any(TradeDocument.class), any(LetterOfCredit.class)))
            .thenReturn(true);
        when(lcRepository.save(any(LetterOfCredit.class))).thenReturn(updatedLC);
        
        // When
        DocumentVerificationResult result = lcProcessor.verifyDocuments(
            lcNumber, 
            shipmentDoc, 
            invoiceDoc
        );
        
        // Then
        assertTrue(result.isCompliant());
        assertEquals(2, result.getVerifiedDocumentCount());
        
        // Verification Modes - times() for exact count verification
        verify(documentValidator, times(2)).validateDocument(any(TradeDocument.class), any(LetterOfCredit.class));
        
        // Basic interaction checking
        verify(lcRepository).findByLcNumber(eq(lcNumber));
        verify(lcRepository).save(any(LetterOfCredit.class));
        verify(notificationService).notifyDocumentStatus(eq(lcNumber), eq(true));
        
        // Verify that invalid methods were never called
        verify(notificationService, never()).notifyLCIssued(anyString());
    }
}
```

**New Supporting Classes:**

```java
// TradeDocument.java
class TradeDocument {
    private String documentType;
    private String documentNumber;
    
    public TradeDocument(String documentType, String documentNumber) {
        this.documentType = documentType;
        this.documentNumber = documentNumber;
    }
    
    public String getDocumentType() { return documentType; }
    public String getDocumentNumber() { return documentNumber; }
}
```

```java
// DocumentValidator.java
interface DocumentValidator {
    boolean validateDocument(TradeDocument document, LetterOfCredit lc);
}
```

```java
// DocumentVerificationResult.java
class DocumentVerificationResult {
    private boolean compliant;
    private int verifiedDocumentCount;
    
    public DocumentVerificationResult(boolean compliant, int verifiedDocumentCount) {
        this.compliant = compliant;
        this.verifiedDocumentCount = verifiedDocumentCount;
    }
    
    public boolean isCompliant() { return compliant; }
    public int getVerifiedDocumentCount() { return verifiedDocumentCount; }
}
```

**Run Test**: Test should FAIL (RED) - verifyDocuments method doesn't exist.

### STEP 5: GREEN Phase - Make Test Pass

**Update:** `LetterOfCreditProcessor.java` (COMPLETE REPLACEMENT)

```java
// LetterOfCreditProcessor.java - UPDATED VERSION WITH DOCUMENT VERIFICATION
class LetterOfCreditProcessor {
    private final LCRepository lcRepository;
    private final NotificationService notificationService;
    // NEW CODE: Add DocumentValidator dependency
    private final DocumentValidator documentValidator;
    
    // UPDATED CODE: Constructor with additional parameter
    public LetterOfCreditProcessor(LCRepository lcRepository, 
                                  NotificationService notificationService,
                                  DocumentValidator documentValidator) {
        this.lcRepository = lcRepository;
        this.notificationService = notificationService;
        this.documentValidator = documentValidator;
    }
    
    // EXISTING CODE: Process application method
    public LetterOfCredit processApplication(LCApplication application) {
        // Validation
        validateApplication(application);
        
        // Create LC from application
        LetterOfCredit lc = new LetterOfCredit(
            application.getLcNumber(),
            application.getApplicant(),
            application.getBeneficiary(),
            application.getAmount(),
            application.getCurrency(),
            LCStatus.ISSUED
        );
        
        // Save LC
        LetterOfCredit savedLC = lcRepository.save(lc);
        
        // Send notification
        notificationService.notifyLCIssued(application.getLcNumber());
        
        return savedLC;
    }
    
    // EXISTING CODE: Validation method
    private void validateApplication(LCApplication application) {
        if (application.getLcNumber() == null || application.getLcNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("LC Number is required");
        }
        if (application.getAmount() <= 0) {
            throw new IllegalArgumentException("LC Amount must be positive");
        }
    }
    
    // NEW CODE: Document verification method
    public DocumentVerificationResult verifyDocuments(String lcNumber, TradeDocument... documents) {
        // Find existing LC
        LetterOfCredit lc = lcRepository.findByLcNumber(lcNumber);
        if (lc == null) {
            throw new IllegalArgumentException("LC not found: " + lcNumber);
        }
        
        // Verify each document
        boolean allCompliant = true;
        int verifiedCount = 0;
        
        for (TradeDocument document : documents) {
            boolean isValid = documentValidator.validateDocument(document, lc);
            if (!isValid) {
                allCompliant = false;
            }
            verifiedCount++;
        }
        
        // Update LC status
        LCStatus newStatus = allCompliant ? LCStatus.COMPLIANT : LCStatus.NON_COMPLIANT;
        LetterOfCredit updatedLC = new LetterOfCredit(
            lc.getLcNumber(), lc.getApplicant(), lc.getBeneficiary(),
            lc.getAmount(), lc.getCurrency(), newStatus
        );
        
        lcRepository.save(updatedLC);
        
        // Send notification
        notificationService.notifyDocumentStatus(lcNumber, allCompliant);
        
        return new DocumentVerificationResult(allCompliant, verifiedCount);
    }
}
```

**Run Test**: Test should PASS (GREEN).

### STEP 6: REFACTOR Phase

The code is getting complex. Let's refactor for better organization:

```java
// LetterOfCreditProcessor.java - REFACTORED VERSION (COMPLETE REPLACEMENT)
class LetterOfCreditProcessor {
    private final LCRepository lcRepository;
    private final NotificationService notificationService;
    private final DocumentValidator documentValidator;
    
    public LetterOfCreditProcessor(LCRepository lcRepository, 
                                  NotificationService notificationService,
                                  DocumentValidator documentValidator) {
        this.lcRepository = lcRepository;
        this.notificationService = notificationService;
        this.documentValidator = documentValidator;
    }
    
    // EXISTING CODE: Process application method (unchanged)
    public LetterOfCredit processApplication(LCApplication application) {
        validateApplication(application);
        
        LetterOfCredit lc = createLCFromApplication(application);
        LetterOfCredit savedLC = lcRepository.save(lc);
        notificationService.notifyLCIssued(application.getLcNumber());
        
        return savedLC;
    }
    
    // EXISTING CODE: Document verification (refactored for clarity)
    public DocumentVerificationResult verifyDocuments(String lcNumber, TradeDocument... documents) {
        LetterOfCredit lc = findLCOrThrow(lcNumber);
        
        DocumentVerificationResult result = performDocumentVerification(lc, documents);
        
        updateLCStatus(lc, result.isCompliant());
        notificationService.notifyDocumentStatus(lcNumber, result.isCompliant());
        
        return result;
    }
    
    // EXISTING CODE: Validation (unchanged)
    private void validateApplication(LCApplication application) {
        if (application.getLcNumber() == null || application.getLcNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("LC Number is required");
        }
        if (application.getAmount() <= 0) {
            throw new IllegalArgumentException("LC Amount must be positive");
        }
    }
    
    // NEW CODE: Extracted helper methods for better readability
    private LetterOfCredit createLCFromApplication(LCApplication application) {
        return new LetterOfCredit(
            application.getLcNumber(),
            application.getApplicant(),
            application.getBeneficiary(),
            application.getAmount(),
            application.getCurrency(),
            LCStatus.ISSUED
        );
    }
    
    private LetterOfCredit findLCOrThrow(String lcNumber) {
        LetterOfCredit lc = lcRepository.findByLcNumber(lcNumber);
        if (lc == null) {
            throw new IllegalArgumentException("LC not found: " + lcNumber);
        }
        return lc;
    }
    
    private DocumentVerificationResult performDocumentVerification(LetterOfCredit lc, TradeDocument[] documents) {
        boolean allCompliant = true;
        int verifiedCount = 0;
        
        for (TradeDocument document : documents) {
            boolean isValid = documentValidator.validateDocument(document, lc);
            if (!isValid) {
                allCompliant = false;
            }
            verifiedCount++;
        }
        
        return new DocumentVerificationResult(allCompliant, verifiedCount);
    }
    
    private void updateLCStatus(LetterOfCredit lc, boolean compliant) {
        LCStatus newStatus = compliant ? LCStatus.COMPLIANT : LCStatus.NON_COMPLIANT;
        LetterOfCredit updatedLC = new LetterOfCredit(
            lc.getLcNumber(), lc.getApplicant(), lc.getBeneficiary(),
            lc.getAmount(), lc.getCurrency(), newStatus
        );
        lcRepository.save(updatedLC);
    }
}
```


***

## PASS 3: Fee Calculation \& Account Charging (RED-GREEN-REFACTOR)

### STEP 7: RED Phase - Add Third Failing Test

**Update Test Class:** `LetterOfCreditProcessorTest.java` (COMPLETE REPLACEMENT)

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LetterOfCreditProcessorTest {

    @Mock
    private LCRepository lcRepository;
    
    @Mock
    private NotificationService notificationService;
    
    @Mock
    private DocumentValidator documentValidator;
    
    // NEW CODE: Additional mocks for fee processing
    @Mock
    private FeeCalculator feeCalculator;
    
    @Mock
    private AccountService accountService;
    
    private LetterOfCreditProcessor lcProcessor;
    
    @BeforeEach
    void setUp() {
        // UPDATED CODE: Include new dependencies
        lcProcessor = new LetterOfCreditProcessor(
            lcRepository, 
            notificationService, 
            documentValidator,
            feeCalculator,
            accountService
        );
    }
    
    // EXISTING TEST 1: shouldValidateAndSaveLCApplication (unchanged)
    @Test
    void shouldValidateAndSaveLCApplication() {
        // Given
        LCApplication application = new LCApplication(
            "LC001", 
            "Importer Corp", 
            "Exporter Ltd", 
            50000.0, 
            "USD"
        );
        
        LetterOfCredit expectedLC = new LetterOfCredit(
            "LC001", 
            "Importer Corp", 
            "Exporter Ltd", 
            50000.0, 
            "USD", 
            LCStatus.ISSUED
        );
        
        when(lcRepository.save(any(LetterOfCredit.class))).thenReturn(expectedLC);
        
        // When
        LetterOfCredit result = lcProcessor.processApplication(application);
        
        // Then
        assertNotNull(result);
        assertEquals("LC001", result.getLcNumber());
        assertEquals(LCStatus.ISSUED, result.getStatus());
        
        // Verify interactions - Method Call Verification
        verify(lcRepository).save(any(LetterOfCredit.class));
        verify(notificationService).notifyLCIssued(eq("LC001"));
    }
    
    // EXISTING TEST 2: shouldVerifyDocumentsAndUpdateLCStatus (unchanged)
    @Test
    void shouldVerifyDocumentsAndUpdateLCStatus() {
        // Given
        String lcNumber = "LC002";
        TradeDocument shipmentDoc = new TradeDocument("BILL_OF_LADING", "BL123");
        TradeDocument invoiceDoc = new TradeDocument("COMMERCIAL_INVOICE", "INV456");
        
        LetterOfCredit existingLC = new LetterOfCredit(
            lcNumber, "Importer Corp", "Exporter Ltd", 75000.0, "EUR", LCStatus.ISSUED
        );
        
        LetterOfCredit updatedLC = new LetterOfCredit(
            lcNumber, "Importer Corp", "Exporter Ltd", 75000.0, "EUR", LCStatus.COMPLIANT
        );
        
        when(lcRepository.findByLcNumber(lcNumber)).thenReturn(existingLC);
        when(documentValidator.validateDocument(any(TradeDocument.class), any(LetterOfCredit.class)))
            .thenReturn(true);
        when(lcRepository.save(any(LetterOfCredit.class))).thenReturn(updatedLC);
        
        // When
        DocumentVerificationResult result = lcProcessor.verifyDocuments(
            lcNumber, 
            shipmentDoc, 
            invoiceDoc
        );
        
        // Then
        assertTrue(result.isCompliant());
        assertEquals(2, result.getVerifiedDocumentCount());
        
        // Verification Modes - times() for exact count verification
        verify(documentValidator, times(2)).validateDocument(any(TradeDocument.class), any(LetterOfCredit.class));
        
        // Basic interaction checking
        verify(lcRepository).findByLcNumber(eq(lcNumber));
        verify(lcRepository).save(any(LetterOfCredit.class));
        verify(notificationService).notifyDocumentStatus(eq(lcNumber), eq(true));
        
        // Verify that invalid methods were never called
        verify(notificationService, never()).notifyLCIssued(anyString());
    }
    
    // NEW TEST: Fee calculation and charging with atLeastOnce verification
    @Test
    void shouldCalculateFeesAndChargeAccount() {
        // Given
        String lcNumber = "LC003";
        String clientAccount = "ACC12345";
        FeeType feeType = FeeType.DOCUMENT_PROCESSING;
        
        LetterOfCredit lc = new LetterOfCredit(
            lcNumber, "Importer Corp", "Exporter Ltd", 100000.0, "USD", LCStatus.COMPLIANT
        );
        
        Fee calculatedFee = new Fee(feeType, 250.0, "USD");
        TransactionResult chargeResult = new TransactionResult("TXN789", true, "Fee charged successfully");
        
        when(lcRepository.findByLcNumber(lcNumber)).thenReturn(lc);
        when(feeCalculator.calculateFee(feeType, 100000.0, "USD")).thenReturn(calculatedFee);
        when(accountService.chargeAccount(eq(clientAccount), any(Fee.class))).thenReturn(chargeResult);
        
        // When
        FeeProcessingResult result = lcProcessor.processLCFees(lcNumber, clientAccount, feeType);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isSuccessful());
        assertEquals(250.0, result.getChargedAmount());
        assertEquals("TXN789", result.getTransactionId());
        
        // Verification with different modes
        verify(lcRepository, atLeastOnce()).findByLcNumber(eq(lcNumber));
        verify(feeCalculator, times(1)).calculateFee(eq(feeType), eq(100000.0), eq("USD"));
        verify(accountService, times(1)).chargeAccount(eq(clientAccount), any(Fee.class));
        
        // Verify that notification methods were never called for fee processing
        verify(notificationService, never()).notifyLCIssued(anyString());
        verify(notificationService, never()).notifyDocumentStatus(anyString(), anyBoolean());
    }
}
```

**New Supporting Classes:**

```java
// FeeType.java
enum FeeType {
    ISSUANCE, AMENDMENT, DOCUMENT_PROCESSING, NEGOTIATION
}
```

```java
// Fee.java
class Fee {
    private FeeType feeType;
    private double amount;
    private String currency;
    
    public Fee(FeeType feeType, double amount, String currency) {
        this.feeType = feeType;
        this.amount = amount;
        this.currency = currency;
    }
    
    public FeeType getFeeType() { return feeType; }
    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
}
```

```java
// FeeCalculator.java
interface FeeCalculator {
    Fee calculateFee(FeeType feeType, double lcAmount, String currency);
}
```

```java
// TransactionResult.java
class TransactionResult {
    private String transactionId;
    private boolean successful;
    private String message;
    
    public TransactionResult(String transactionId, boolean successful, String message) {
        this.transactionId = transactionId;
        this.successful = successful;
        this.message = message;
    }
    
    public String getTransactionId() { return transactionId; }
    public boolean isSuccessful() { return successful; }
    public String getMessage() { return message; }
}
```

```java
// AccountService.java
interface AccountService {
    TransactionResult chargeAccount(String accountNumber, Fee fee);
}
```

```java
// FeeProcessingResult.java
class FeeProcessingResult {
    private boolean successful;
    private double chargedAmount;
    private String transactionId;
    
    public FeeProcessingResult(boolean successful, double chargedAmount, String transactionId) {
        this.successful = successful;
        this.chargedAmount = chargedAmount;
        this.transactionId = transactionId;
    }
    
    public boolean isSuccessful() { return successful; }
    public double getChargedAmount() { return chargedAmount; }
    public String getTransactionId() { return transactionId; }
}
```

**Run Test**: Test should FAIL (RED) - processLCFees method doesn't exist.

### STEP 8: GREEN Phase - Make Test Pass

**Update:** `LetterOfCreditProcessor.java` (COMPLETE REPLACEMENT)

```java
// LetterOfCreditProcessor.java - FINAL VERSION WITH FEE PROCESSING
class LetterOfCreditProcessor {
    private final LCRepository lcRepository;
    private final NotificationService notificationService;
    private final DocumentValidator documentValidator;
    // NEW CODE: Additional dependencies for fee processing
    private final FeeCalculator feeCalculator;
    private final AccountService accountService;
    
    // UPDATED CODE: Constructor with all dependencies
    public LetterOfCreditProcessor(LCRepository lcRepository, 
                                  NotificationService notificationService,
                                  DocumentValidator documentValidator,
                                  FeeCalculator feeCalculator,
                                  AccountService accountService) {
        this.lcRepository = lcRepository;
        this.notificationService = notificationService;
        this.documentValidator = documentValidator;
        this.feeCalculator = feeCalculator;
        this.accountService = accountService;
    }
    
    // EXISTING CODE: Process application method (unchanged)
    public LetterOfCredit processApplication(LCApplication application) {
        validateApplication(application);
        
        LetterOfCredit lc = createLCFromApplication(application);
        LetterOfCredit savedLC = lcRepository.save(lc);
        notificationService.notifyLCIssued(application.getLcNumber());
        
        return savedLC;
    }
    
    // EXISTING CODE: Document verification method (unchanged)
    public DocumentVerificationResult verifyDocuments(String lcNumber, TradeDocument... documents) {
        LetterOfCredit lc = findLCOrThrow(lcNumber);
        
        DocumentVerificationResult result = performDocumentVerification(lc, documents);
        
        updateLCStatus(lc, result.isCompliant());
        notificationService.notifyDocumentStatus(lcNumber, result.isCompliant());
        
        return result;
    }
    
    // NEW CODE: Fee processing method
    public FeeProcessingResult processLCFees(String lcNumber, String clientAccount, FeeType feeType) {
        // Find LC
        LetterOfCredit lc = findLCOrThrow(lcNumber);
        
        // Calculate fee
        Fee calculatedFee = feeCalculator.calculateFee(feeType, lc.getAmount(), lc.getCurrency());
        
        // Charge account
        TransactionResult transactionResult = accountService.chargeAccount(clientAccount, calculatedFee);
        
        return new FeeProcessingResult(
            transactionResult.isSuccessful(),
            calculatedFee.getAmount(),
            transactionResult.getTransactionId()
        );
    }
    
    // EXISTING CODE: All private helper methods (unchanged)
    private void validateApplication(LCApplication application) {
        if (application.getLcNumber() == null || application.getLcNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("LC Number is required");
        }
        if (application.getAmount() <= 0) {
            throw new IllegalArgumentException("LC Amount must be positive");
        }
    }
    
    private LetterOfCredit createLCFromApplication(LCApplication application) {
        return new LetterOfCredit(
            application.getLcNumber(),
            application.getApplicant(),
            application.getBeneficiary(),
            application.getAmount(),
            application.getCurrency(),
            LCStatus.ISSUED
        );
    }
    
    private LetterOfCredit findLCOrThrow(String lcNumber) {
        LetterOfCredit lc = lcRepository.findByLcNumber(lcNumber);
        if (lc == null) {
            throw new IllegalArgumentException("LC not found: " + lcNumber);
        }
        return lc;
    }
    
    private DocumentVerificationResult performDocumentVerification(LetterOfCredit lc, TradeDocument[] documents) {
        boolean allCompliant = true;
        int verifiedCount = 0;
        
        for (TradeDocument document : documents) {
            boolean isValid = documentValidator.validateDocument(document, lc);
            if (!isValid) {
                allCompliant = false;
            }
            verifiedCount++;
        }
        
        return new DocumentVerificationResult(allCompliant, verifiedCount);
    }
    
    private void updateLCStatus(LetterOfCredit lc, boolean compliant) {
        LCStatus newStatus = compliant ? LCStatus.COMPLIANT : LCStatus.NON_COMPLIANT;
        LetterOfCredit updatedLC = new LetterOfCredit(
            lc.getLcNumber(), lc.getApplicant(), lc.getBeneficiary(),
            lc.getAmount(), lc.getCurrency(), newStatus
        );
        lcRepository.save(updatedLC);
    }
}
```

**Run Test**: Test should PASS (GREEN).

### STEP 9: REFACTOR Phase - Final Optimization

```java
// LetterOfCreditProcessor.java - FINAL REFACTORED VERSION (COMPLETE REPLACEMENT)
class LetterOfCreditProcessor {
    private final LCRepository lcRepository;
    private final NotificationService notificationService;
    private final DocumentValidator documentValidator;
    private final FeeCalculator feeCalculator;
    private final AccountService accountService;
    
    public LetterOfCreditProcessor(LCRepository lcRepository, 
                                  NotificationService notificationService,
                                  DocumentValidator documentValidator,
                                  FeeCalculator feeCalculator,
                                  AccountService accountService) {
        this.lcRepository = lcRepository;
        this.notificationService = notificationService;
        this.documentValidator = documentValidator;
        this.feeCalculator = feeCalculator;
        this.accountService = accountService;
    }
    
    // EXISTING CODE: LC Application Processing
    public LetterOfCredit processApplication(LCApplication application) {
        validateApplication(application);
        
        LetterOfCredit lc = createLCFromApplication(application);
        LetterOfCredit savedLC = lcRepository.save(lc);
        notificationService.notifyLCIssued(application.getLcNumber());
        
        return savedLC;
    }
    
    // EXISTING CODE: Document Verification
    public DocumentVerificationResult verifyDocuments(String lcNumber, TradeDocument... documents) {
        LetterOfCredit lc = findLCOrThrow(lcNumber);
        DocumentVerificationResult result = performDocumentVerification(lc, documents);
        
        updateLCStatus(lc, result.isCompliant());
        notificationService.notifyDocumentStatus(lcNumber, result.isCompliant());
        
        return result;
    }
    
    // EXISTING CODE: Fee Processing (refactored for error handling)
    public FeeProcessingResult processLCFees(String lcNumber, String clientAccount, FeeType feeType) {
        try {
            LetterOfCredit lc = findLCOrThrow(lcNumber);
            Fee calculatedFee = feeCalculator.calculateFee(feeType, lc.getAmount(), lc.getCurrency());
            TransactionResult transactionResult = accountService.chargeAccount(clientAccount, calculatedFee);
            
            return new FeeProcessingResult(
                transactionResult.isSuccessful(),
                calculatedFee.getAmount(),
                transactionResult.getTransactionId()
            );
        } catch (Exception e) {
            // NEW CODE: Enhanced error handling
            return new FeeProcessingResult(false, 0.0, null);
        }
    }
    
    // EXISTING CODE: Validation and helper methods (unchanged)
    private void validateApplication(LCApplication application) {
        if (application.getLcNumber() == null || application.getLcNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("LC Number is required");
        }
        if (application.getAmount() <= 0) {
            throw new IllegalArgumentException("LC Amount must be positive");
        }
    }
    
    private LetterOfCredit createLCFromApplication(LCApplication application) {
        return new LetterOfCredit(
            application.getLcNumber(), application.getApplicant(), application.getBeneficiary(),
            application.getAmount(), application.getCurrency(), LCStatus.ISSUED
        );
    }
    
    private LetterOfCredit findLCOrThrow(String lcNumber) {
        LetterOfCredit lc = lcRepository.findByLcNumber(lcNumber);
        if (lc == null) {
            throw new IllegalArgumentException("LC not found: " + lcNumber);
        }
        return lc;
    }
    
    private DocumentVerificationResult performDocumentVerification(LetterOfCredit lc, TradeDocument[] documents) {
        boolean allCompliant = true;
        int verifiedCount = 0;
        
        for (TradeDocument document : documents) {
            if (!documentValidator.validateDocument(document, lc)) {
                allCompliant = false;
            }
            verifiedCount++;
        }
        
        return new DocumentVerificationResult(allCompliant, verifiedCount);
    }
    
    private void updateLCStatus(LetterOfCredit lc, boolean compliant) {
        LCStatus newStatus = compliant ? LCStatus.COMPLIANT : LCStatus.NON_COMPLIANT;
        LetterOfCredit updatedLC = new LetterOfCredit(
            lc.getLcNumber(), lc.getApplicant(), lc.getBeneficiary(),
            lc.getAmount(), lc.getCurrency(), newStatus
        );
        lcRepository.save(updatedLC);
    }
}
```


***

## Summary

**Congratulations!** You have successfully completed a comprehensive TDD assignment using JUnit 5 and Mockito 5. You have implemented:

### ✅ **Business Requirements Covered:**

1. **LC Application Processing** - Validation and storage with proper status management
2. **Document Verification** - Compliance checking against LC terms
3. **Fee Management** - Calculation and account charging with transaction recording

### ✅ **Mockito Features Demonstrated:**

- **Method Call Verification** - `verify(mock)` for ensuring methods were called
- **Basic Interaction Checking** - Verifying methods called with expected arguments
- **Verification Modes:**
    - `times(n)` - Exact number of invocations
    - `never()` - Ensuring methods were NOT called
    - `atLeastOnce()` - Minimum invocation verification

**Key Learning Points:**

- Test-first development ensures robust, well-designed code
- Mockito enables testing of complex dependencies and interactions
- Refactoring maintains code quality while preserving functionality
- Trade Finance domain complexity can be managed through proper separation of concerns