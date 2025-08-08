package org.p011_mocks2.v3;

// New interface for HR verification service integration
public interface HrVerificationService {
    EmploymentStatus verifyEmployment(String applicantId) throws RuntimeException;
}