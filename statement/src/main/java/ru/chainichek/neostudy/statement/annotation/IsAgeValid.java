package ru.chainichek.neostudy.statement.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.chainichek.neostudy.statement.util.Validation;
import ru.chainichek.neostudy.statement.util.ValidationMessage;
import ru.chainichek.neostudy.statement.validator.AgeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeValidator.class)
public @interface IsAgeValid {
    String message() default ValidationMessage.AGE_MESSAGE;
    int min() default Validation.AGE_MIN;
    int max() default Integer.MAX_VALUE;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
