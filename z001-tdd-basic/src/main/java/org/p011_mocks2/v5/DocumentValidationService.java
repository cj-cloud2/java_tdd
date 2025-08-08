package org.p011_mocks2.v5;

// New interface for document validation service integration
public interface DocumentValidationService {
    DocumentValidationStatus validateDocuments(String applicantId) throws RuntimeException;
}
