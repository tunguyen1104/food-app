package com.example.foodapp.enums;

import lombok.Getter;

public enum EnableStatus {
    ON("On", true),
    OFF("Off", false);

    @Getter
    private final String displayText;
    private final boolean value;

    EnableStatus(String displayText, boolean value) {
        this.displayText = displayText;
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public static EnableStatus fromDisplayText(String text) {
        for (EnableStatus status : EnableStatus.values()) {
            if (status.displayText.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return null;
    }

    public static String[] getDisplayOptions() {
        EnableStatus[] statuses = values();
        String[] options = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            options[i] = statuses[i].getDisplayText();
        }
        return options;
    }
}
