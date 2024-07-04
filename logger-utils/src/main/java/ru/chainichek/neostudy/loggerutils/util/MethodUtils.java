package ru.chainichek.neostudy.loggerutils.util;

import java.lang.reflect.Method;

public class MethodUtils {
    public static Method getDeclaredMethod(Method method, Class<?> targetClass) {
        final Method[] declaredMethods = targetClass.getDeclaredMethods();
        for (final Method declaredMethod : declaredMethods) {
            if (isMethodEqual(method, declaredMethod)) {
                return declaredMethod;
            }
        }

        return null;
    }

    private static boolean isMethodEqual(Method method1, Method method2) {
        if (!method1.getName().equals(method2.getName())) {
            return false;
        }

        final Class<?>[] params1 = method1.getParameterTypes();
        final Class<?>[] params2 = method2.getParameterTypes();

        if (params1.length != params2.length) {
            return false;
        }

        for (int i = 0; i < params1.length; i++) {
            if (params1[i] != params2[i]) {
                return false;
            }
        }
        return true;
    }
}
