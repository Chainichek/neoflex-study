package ru.chainichek.neostudy.statement.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import ru.chainichek.neostudy.statement.util.Validation;
import ru.chainichek.neostudy.statement.util.ValidationMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotNull
@DecimalMin(value = Validation.AMOUNT_MIN, message = ValidationMessage.AMOUNT_MESSAGE)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {}
)
public @interface Amount {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
