package org.p009_mocks;


class UserNotificationService {
    private final EmailService emailService;
    private final Logger logger;

    public UserNotificationService(EmailService emailService, Logger logger) {
        this.emailService = emailService;
        this.logger = logger;
    }

    public void registerUser(String email) {
        String name = email.split("@")[0];
        String msg = "Welcome " + capitalize(name) + "!";
        emailService.sendEmail(email, msg);
        logger.log("Sent welcome email to " + email);
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    interface EmailService {
        void sendEmail(String address, String msg);
    }

    interface Logger {
        void log(String msg);
    }
}
