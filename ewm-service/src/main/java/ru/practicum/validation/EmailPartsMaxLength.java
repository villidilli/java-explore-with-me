package ru.practicum.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailPartsMaxLengthValidator.class)
public @interface EmailPartsMaxLength {
    String message() default "{Email not valid. Check length}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}