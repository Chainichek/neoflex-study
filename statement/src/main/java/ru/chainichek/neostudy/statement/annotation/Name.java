package ru.chainichek.neostudy.statement.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import ru.chainichek.neostudy.statement.util.Validation;
import ru.chainichek.neostudy.statement.util.ValidationMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Pattern(regexp = Validation.NAME_PATTERN, message = ValidationMessage.NAME_MESSAGE)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {}
)
public @interface Name {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
