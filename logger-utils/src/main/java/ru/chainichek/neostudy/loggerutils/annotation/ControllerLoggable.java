package ru.chainichek.neostudy.loggerutils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Loggable(invokeMessage = "Request on \"{}\"",
        invokeParametersMessage = "Got: {}",
        executeMessage = "Response on \"{}\"",
        executeParametersMessage = "Sending: {}",
        delimiter = "; ")
public @interface ControllerLoggable {
}
