package com.sharif.micromaster;

import java.util.regex.Pattern;

public final class RegistrationValidator {
    public static final String NAME_REQUIRED = "Name cannot be empty";
    public static final String EMAIL_REQUIRED = "Email cannot be empty";
    public static final String EMAIL_INVALID = "Enter a valid email";
    public static final String PASSWORD_REQUIRED = "Password cannot be empty";
    public static final String PASSWORD_WEAK = "Password must be at least 6 characters and include a letter and number";

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    private RegistrationValidator() {
    }

    public static ValidationResult validate(String name, String email, String password) {
        String nameError = isBlank(name) ? NAME_REQUIRED : null;
        String emailError = validateEmail(email);
        String passwordError = validatePassword(password);
        return new ValidationResult(nameError, emailError, passwordError);
    }

    public static String validateEmail(String email) {
        if (isBlank(email)) {
            return EMAIL_REQUIRED;
        }
        if (!isValidEmail(email)) {
            return EMAIL_INVALID;
        }
        return null;
    }

    public static String validatePassword(String password) {
        if (isBlank(password)) {
            return PASSWORD_REQUIRED;
        }
        if (!isStrongPassword(password)) {
            return PASSWORD_WEAK;
        }
        return null;
    }

    public static boolean isValidEmail(String email) {
        return !isBlank(email) && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (int i = 0; i < password.length(); i++) {
            char value = password.charAt(i);
            if (Character.isLetter(value)) {
                hasLetter = true;
            }
            if (Character.isDigit(value)) {
                hasDigit = true;
            }
        }
        return hasLetter && hasDigit;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static final class ValidationResult {
        private final String nameError;
        private final String emailError;
        private final String passwordError;

        private ValidationResult(String nameError, String emailError, String passwordError) {
            this.nameError = nameError;
            this.emailError = emailError;
            this.passwordError = passwordError;
        }

        public boolean isValid() {
            return nameError == null && emailError == null && passwordError == null;
        }

        public String getNameError() {
            return nameError;
        }

        public String getEmailError() {
            return emailError;
        }

        public String getPasswordError() {
            return passwordError;
        }

        public String getFirstError() {
            if (nameError != null) {
                return nameError;
            }
            if (emailError != null) {
                return emailError;
            }
            return passwordError;
        }
    }
}
