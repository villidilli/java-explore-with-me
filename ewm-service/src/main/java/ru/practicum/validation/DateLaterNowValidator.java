package ru.practicum.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class DateLaterNowValidator implements ConstraintValidator<DateLaterNow, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext constraintValidatorContext) {
        if (eventDate != null) {
            final Instant now = Instant.now();
            final Instant eventDateTime = eventDate.toInstant(ZoneOffset.UTC);
            long hoursDifference = ChronoUnit.HOURS.between(now, eventDateTime);
            if (hoursDifference < 2L) return false;
        }
        return true;
    }
}