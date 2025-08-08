package org.p011_mocks2.v4;

// Repository interface for persistence (Mockito will mock this)
public interface LoanRepository {
    void save(LoanApplication application);
}
