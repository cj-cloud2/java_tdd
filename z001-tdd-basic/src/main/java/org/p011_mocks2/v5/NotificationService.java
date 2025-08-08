package org.p011_mocks2.v5;


// New interface for notification service
public interface NotificationService {
    void sendApprovalNotification(LoanApplication application);
    void sendRejectionNotification(LoanApplication application, String reason);
    void sendPendingNotification(LoanApplication application, String info);
}