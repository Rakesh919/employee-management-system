package com.company.validation.validator;

import com.company.validation.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final List<String> COMMON_PASSWORDS = Arrays.asList(
            "123456", "password", "12345678", "qwerty", "123456789","abc@1234","john@1234","abcd@1234"
    );

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.length() < 6) {
            setMessage(context, "Password must be at least 6 characters long");
            return false;
        }

        if (COMMON_PASSWORDS.contains(password.toLowerCase())) {
            setMessage(context, "Password is too common");
            return false;
        }

        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*").matcher(password).matches();

        if (!(hasUppercase && hasLowercase && hasDigit && hasSpecial)) {
            setMessage(context, "Password must contain uppercase, lowercase, digit, and special character");
            return false;
        }

        return true;
    }

    private void setMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

}
