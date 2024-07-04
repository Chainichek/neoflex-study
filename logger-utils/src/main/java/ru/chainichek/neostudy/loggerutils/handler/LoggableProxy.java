package ru.chainichek.neostudy.loggerutils.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import ru.chainichek.neostudy.loggerutils.annotation.Loggable;
import ru.chainichek.neostudy.loggerutils.util.AnnotationUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class LoggableProxy implements InvocationHandler {
    private static final Logger log = LoggerFactory.getLogger(LoggableProxy.class);
    private final Object target;

    public LoggableProxy(Object target) {
        this.target = target;
    }

    public static Object createProxy(Object target) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new LoggableProxy(target));
    }

    @Override
    public Object invoke(Object proxy,
                         Method method,
                         Object[] args) throws Throwable {
        System.out.println(Arrays.toString(target.getClass().getAnnotatedInterfaces()));
        final Loggable loggable = AnnotationUtils.getEffectiveLoggable(method, target.getClass());
        final String methodName = getMethodName(method);
        if (loggable != null) {
            if (loggable.value() == null) {
                throw new NullPointerException("No logging level found");
            }

            if (args == null || args.length == 0) {
                log(loggable.value(), loggable.invokeMessage(), methodName);
            } else {
                log(loggable.value(), "%s%s%s".formatted(loggable.invokeMessage(),
                        loggable.delimiter(),
                        loggable.invokeParametersMessage()), methodName, args);
            }
        }

        final Object result = method.invoke(target, args);

        if (loggable != null) {
            if (result == null) {
                log(loggable.value(), loggable.executeMessage(), methodName);
            } else {
                log(loggable.value(), "%s%s%s".formatted(loggable.executeMessage(),
                        loggable.delimiter(),
                        loggable.executeParametersMessage()), methodName, result);
            }
        }
        return result;
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

    private void log(Level level, String message, Object... args) {
        switch (level) {
            case TRACE -> log.trace(message, args);
            case DEBUG -> log.debug(message, args);
            case INFO -> log.info(message, args);
            case WARN -> log.warn(message, args);
            case ERROR -> log.error(message, args);
        }
    }
}
