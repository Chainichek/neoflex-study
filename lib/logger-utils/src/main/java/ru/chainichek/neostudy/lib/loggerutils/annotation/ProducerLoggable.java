package ru.chainichek.neostudy.lib.loggerutils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Loggable(invokeMessage = "Sending message on \"{}\"",
        invokeParametersMessage = "Got: {}",
        executeMessage = "Message send on \"{}\"",
        executeParametersMessage = "Return: {}",
        delimiter = "; ")
public @interface ProducerLoggable {
}
