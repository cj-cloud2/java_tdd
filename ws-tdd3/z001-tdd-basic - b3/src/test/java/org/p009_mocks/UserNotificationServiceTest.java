package org.p009_mocks;

/*

### Example 1: User Notification Service

#### Task / Requirements

- Implement a service that:
    - Sends an email notification when a new user registers.
    - Logs notification actions.
- The email sending and logging dependencies should be mocked.
- Write tests to:

1. **Verify** that email and log are called when registering a user.
2. **Verify** correct message contents are used.

 */

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Level;
import java.util.logging.Logger;
 import java.util.logging.LogRecord;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserNotificationServiceTest {
    @Mock
    UserNotificationService.EmailService emailService; // Mocked dependency for sending emails
    @Mock
    Logger logger;             // Mocked dependency for logging
    @InjectMocks
    UserNotificationService userNotificationService;

    // Test 1: Verifies both notification and logging
    @Test
    void shouldSendEmailAndLogOnUserRegistration() {
        userNotificationService.registerUser("alice@example.com");
        verify(emailService).sendEmail("alice@example.com", "Welcome Alice!");
        verify(logger).log(new LogRecord(Level.INFO,"Sent welcome email to alice@example.com"));
    }

    // Test 2: Ensures correct email contents
    @Test
    void shouldSendCorrectWelcomeMessage() {
        userNotificationService.registerUser("bob@example.com");
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService).sendEmail(eq("bob@example.com"), emailCaptor.capture());
        assertEquals("Welcome Bob!", emailCaptor.getValue());
    }
    // Mocks are injected, ensuring no real email or logging is performed.
}