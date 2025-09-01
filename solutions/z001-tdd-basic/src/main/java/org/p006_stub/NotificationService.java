package org.p006_stub;


import java.util.ArrayList;
import java.util.List;

// Service to be tested
class NotificationService {
    private final UserSettingsProvider settingsProvider;

    public NotificationService(UserSettingsProvider settingsProvider) {
        this.settingsProvider = settingsProvider;
    }

    public List<String> filterNotifications(String username, List<String> notifications) {
        boolean doNotDisturb = settingsProvider.isDoNotDisturbEnabled(username);
        if (!doNotDisturb) {
            return notifications;
        }
        List<String> filtered = new ArrayList<>();
        for (String note : notifications) {
            if (!note.startsWith("DND:")) {
                filtered.add(note);
            }
        }
        return filtered;
    }
}

// Interface for collaboration
interface UserSettingsProvider {
    boolean isDoNotDisturbEnabled(String username);
}


