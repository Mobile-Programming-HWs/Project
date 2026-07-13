package com.sharif.micromaster;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RegistrationValidatorTest {

    @Test
    public void validate_rejectsBlankName() {
        RegistrationValidator.ValidationResult result =
                RegistrationValidator.validate("   ", "student@example.com", "abc123");

        assertFalse(result.isValid());
        assertEquals(RegistrationValidator.NAME_REQUIRED, result.getNameError());
    }

    @Test
    public void validate_rejectsInvalidEmail() {
        RegistrationValidator.ValidationResult result =
                RegistrationValidator.validate("Student", "student.example.com", "abc123");

        assertFalse(result.isValid());
        assertEquals(RegistrationValidator.EMAIL_INVALID, result.getEmailError());
    }

    @Test
    public void validate_rejectsWeakPassword() {
        RegistrationValidator.ValidationResult result =
                RegistrationValidator.validate("Student", "student@example.com", "abcdef");

        assertFalse(result.isValid());
        assertEquals(RegistrationValidator.PASSWORD_WEAK, result.getPasswordError());
    }

    @Test
    public void validate_acceptsTrimmedNameAndEmailWithStrongPassword() {
        RegistrationValidator.ValidationResult result =
                RegistrationValidator.validate("  Student  ", "  student@example.com  ", "abc123");

        assertTrue(result.isValid());
        assertNull(result.getNameError());
        assertNull(result.getEmailError());
        assertNull(result.getPasswordError());
    }
}
