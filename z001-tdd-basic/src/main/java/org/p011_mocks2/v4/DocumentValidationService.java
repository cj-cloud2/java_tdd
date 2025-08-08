package org.p011_mocks2.v4;

// New interface for document validation service integration
public interface DocumentValidationService {
    DocumentValidationStatus validateDocuments(String applicantId) throws RuntimeException;
}
