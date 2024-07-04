package ru.chainichek.neostudy.loggerutils.util;

import ru.chainichek.neostudy.loggerutils.annotation.ControllerLoggable;
import ru.chainichek.neostudy.loggerutils.annotation.TransactionLoggable;
import ru.chainichek.neostudy.loggerutils.annotation.Loggable;

import java.lang.reflect.Method;

public class AnnotationUtils {

    public static Loggable getEffectiveLoggable(Method method, Class<?> targetClass) {
        if (method.isAnnotationPresent(Loggable.class)) {
            return method.getAnnotation(Loggable.class);
        }
        if (method.isAnnotationPresent(TransactionLoggable.class)) {
            return method.getAnnotation(TransactionLoggable.class).annotationType().getAnnotation(Loggable.class);
        }
        if (method.isAnnotationPresent(ControllerLoggable.class)) {
            return method.getAnnotation(ControllerLoggable.class).annotationType().getAnnotation(Loggable.class);
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

        final Method declaredMethod = MethodUtils.getDeclaredMethod(method, targetClass);
        if (declaredMethod != null) {
            if (declaredMethod.isAnnotationPresent(Loggable.class)) {
                return method.getAnnotation(Loggable.class);
            }
            if (declaredMethod.isAnnotationPresent(TransactionLoggable.class)) {
                return method.getAnnotation(TransactionLoggable.class).annotationType().getAnnotation(Loggable.class);
            }
            if (declaredMethod.isAnnotationPresent(ControllerLoggable.class)) {
                return method.getAnnotation(ControllerLoggable.class).annotationType().getAnnotation(Loggable.class);
            }
        }



        return null;
    }
}