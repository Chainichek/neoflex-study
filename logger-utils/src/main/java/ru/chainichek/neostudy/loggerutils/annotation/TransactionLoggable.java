package ru.chainichek.neostudy.loggerutils.annotation;

import org.slf4j.event.Level;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Loggable(value = Level.DEBUG,
        invokeMessage = "Starting a transaction in order to \"{}\"",
        invokeParametersMessage = "Got: {}",
        executeMessage = "Ending the transaction for \"{}\"",
        executeParametersMessage = "Returning: {}",
        delimiter = "; ")
public @interface TransactionLoggable {
}
