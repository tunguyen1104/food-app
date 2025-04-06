package com.example.foodapp.enums;

public enum EnableStatus {
    ON("On", true),
    OFF("Off", false);

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
}
