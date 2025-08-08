package org.p011_mocks2.v5;

// New interface for external credit bureau service integration
public interface CreditBureauService {
    int fetchCreditScore(String applicantId) throws RuntimeException; // can throw RuntimeException on failure
}