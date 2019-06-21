package uk.nhs.digital.ps.test.util;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionHelper {

    public static <T> void setField(final T targetObject, final String fieldName, final Object targetValue) {
        final Field field = ReflectionUtils.findField(targetObject.getClass(), fieldName);
        field.setAccessible(true);
        ReflectionUtils.setField(field, targetObject, targetValue);
    }

    public static <T> void setField(final Class<?> targetClass, final String fieldName, final Object targetValue) {
        final Field field = ReflectionUtils.findField(targetClass, fieldName);
        field.setAccessible(true);
        try {
            field.set(null, targetValue);
        } catch (final IllegalAccessException exception) {
            throw new RuntimeException("Failed to assign value " + targetValue + " to field " + field);
        }
    }

    public static <T> void callMethod(Class<T> targetClass, String methodName, Class<?> argType, Object arg) throws Exception {
        Method method = targetClass.getDeclaredMethod(methodName, argType);
        method.setAccessible(true);
        method.invoke(null, arg);
    }
}
