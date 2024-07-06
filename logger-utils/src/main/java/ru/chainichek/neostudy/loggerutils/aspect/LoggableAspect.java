package ru.chainichek.neostudy.loggerutils.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import ru.chainichek.neostudy.loggerutils.annotation.ControllerLoggable;
import ru.chainichek.neostudy.loggerutils.annotation.Loggable;
import ru.chainichek.neostudy.loggerutils.annotation.TransactionLoggable;

import java.lang.reflect.Method;

@Aspect
public class LoggableAspect {

    @Pointcut("""
            within(@ru.chainichek.neostudy.loggerutils.annotation.Loggable *)
            || within(@ru.chainichek.neostudy.loggerutils.annotation.ControllerLoggable *)
            || within(@ru.chainichek.neostudy.loggerutils.annotation.TransactionLoggable *)
            """)
    public void loggableTarget() {}

    @Pointcut("""
            @annotation(ru.chainichek.neostudy.loggerutils.annotation.Loggable)
            || @annotation(ru.chainichek.neostudy.loggerutils.annotation.ControllerLoggable)
            || @annotation(ru.chainichek.neostudy.loggerutils.annotation.TransactionLoggable)
            """)
    public void loggableMethod() {}

    @Around("loggableTarget() || loggableMethod()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        final Class<?> targetClass = joinPoint.getTarget().getClass();
        final Loggable loggable = getEffectiveLoggable(
                method,
                targetClass);
        final String methodName = getMethodName(method);
        final Object[] args = joinPoint.getArgs();

        Logger log = null;

        if (loggable != null) {
            log = LoggerFactory.getLogger(targetClass);

            if (loggable.value() == null) {
                throw new NullPointerException("No logging level found");
            }

            if (args == null || args.length == 0) {
                log(log, loggable.value(), loggable.invokeMessage(), methodName);
            } else {
                log(log, loggable.value(), "%s%s%s".formatted(loggable.invokeMessage(),
                        loggable.delimiter(),
                        loggable.invokeParametersMessage()), methodName, args);
            }
        }

        final Object result = joinPoint.proceed();

        if (loggable != null) {
            if (result == null) {
                log(log, loggable.value(), loggable.executeMessage(), methodName);
            } else {
                log(log, loggable.value(), "%s%s%s".formatted(loggable.executeMessage(),
                        loggable.delimiter(),
                        loggable.executeParametersMessage()), methodName, result);
            }
        }
        return result;
    }

    private Loggable getEffectiveLoggable(Method method, Class<?> targetClass) {
        if (method.isAnnotationPresent(Loggable.class)) {
            return method.getDeclaringClass().getAnnotation(Loggable.class);
        }
        if (method.isAnnotationPresent(TransactionLoggable.class)) {
            return method.getDeclaringClass().getAnnotation(TransactionLoggable.class).annotationType().getAnnotation(Loggable.class);
        }
        if (method.isAnnotationPresent(ControllerLoggable.class)) {
            return method.getDeclaringClass().getAnnotation(ControllerLoggable.class).annotationType().getAnnotation(Loggable.class);
        }

        if (targetClass.isAnnotationPresent(Loggable.class)) {
            return targetClass.getAnnotation(Loggable.class);
        }
        if (targetClass.isAnnotationPresent(TransactionLoggable.class)) {
            return targetClass.getAnnotation(TransactionLoggable.class).annotationType().getAnnotation(Loggable.class);
        }
        if (targetClass.isAnnotationPresent(ControllerLoggable.class)) {
            return targetClass.getAnnotation(ControllerLoggable.class).annotationType().getAnnotation(Loggable.class);
        }

        return null;
    }

    private String getMethodName(Method method) {
        final String name = method.getName();
        return name.replaceAll(String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ), " "
        ).toLowerCase();
    }

    private void log(Logger log,
                     Level level,
                     String message,
                     Object... args) {
        if (log == null) {
            throw new NullPointerException("No logger found");
        }

        switch (level) {
            case TRACE -> log.trace(message, args);
            case DEBUG -> log.debug(message, args);
            case INFO -> log.info(message, args);
            case WARN -> log.warn(message, args);
            case ERROR -> log.error(message, args);
        }
    }
}
