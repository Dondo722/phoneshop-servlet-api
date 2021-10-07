package com.es.phoneshop.util;

public enum CheckoutRegex {
    NAME_REGEX("[a-zA-Z]+", "Please use only letters"),
    PHONE_REGEX("\\d{11}|(?:\\d{4}-)(?:\\d{3}-)\\d{4}|\\d{1}\\(\\d{3}\\)\\d{3}-?\\d{4}",
            "Please input existing number");

    private final String regex;
    private final String regexErrorMessage;

    CheckoutRegex(String regex, String regexErrorMessage) {
        this.regex = regex;
        this.regexErrorMessage = regexErrorMessage;
    }

    public String getRegex() {
        return regex;
    }

    public String getRegexErrorMessage() {
        return regexErrorMessage;
    }
}
