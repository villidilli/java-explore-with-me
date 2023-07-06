package ru.practicum.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateLaterNowValidator.class)
public @interface DateLaterNow {
    String message() default "{EventDate must be 2 hours after now}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}