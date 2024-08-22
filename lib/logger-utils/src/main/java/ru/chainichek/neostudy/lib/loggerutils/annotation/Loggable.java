package ru.chainichek.neostudy.lib.loggerutils.annotation;

import org.slf4j.event.Level;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    Level value() default Level.INFO;
    String invokeMessage() default "Method \"{}\" is called";
    String invokeParametersMessage() default "with parameters: {}";
    String executeMessage() default "Method \"{}\" executed successfully";
    String executeParametersMessage() default "and returned: {}";
    String delimiter() default " ";
}
