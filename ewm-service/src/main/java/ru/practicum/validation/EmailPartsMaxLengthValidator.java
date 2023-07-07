package ru.practicum.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailPartsMaxLengthValidator implements ConstraintValidator<EmailPartsMaxLength, String> {
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email != null && !email.isEmpty() && !email.isBlank()) {
            String[] emailParts = email.split("@");
            String local = emailParts[0];
            String[] domainParts = emailParts[1].split("\\.");
            if (local.length() > 64) return false;
            for (String domainPart : domainParts) {
                if (domainPart.length() > 63) return false;
            }
        }
        return true;
    }
}