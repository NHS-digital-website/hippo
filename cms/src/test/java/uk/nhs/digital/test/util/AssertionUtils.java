package uk.nhs.digital.test.util;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.Predicate;

public class AssertionUtils {

    public static <A extends Annotation> void assertClassHasFieldWithAnnotationWithAttributeValue(
        final Class<?> targetClass,
        final String fieldName,
        final Class<A> annotationClass,
        final String annotationAttributeName,
        final Object annotationAttributeValue
    ) {
        FieldUtils.getFieldsListWithAnnotation(targetClass, annotationClass)
            .stream()
            .filter(fieldsWithname(fieldName))
            .findFirst()
            .map(fieldToItsAnnotationOfType(annotationClass))
            .map(annotationToValueOfItsAttributeWith(annotationAttributeName))
            .filter(attributesWithValueMatching(annotationAttributeValue))
            .orElseThrow(() ->
                //noinspection ThrowableNotThrown
                new AssertionError(
                    "No matching field was found on given class.\n"
                        + "\n              On class '" + targetClass.getName() + "'"
                        + "\nexpected to find field '" + fieldName + "'"
                        + "\n       with annotation '" + annotationClass.getName() + "'"
                        + "\n        with attribute '" + annotationAttributeName + "'"
                        + "\n                set to '" + annotationAttributeValue + "'"
                        + "\n"
                        + "\n...but no field was found to match all of these conditions."
                )
            );
    }

    @NotNull private static Predicate<Object> attributesWithValueMatching(final Object annotationAttributeValue) {
        return obj -> annotationAttributeValue.equals(obj);
    }

    @NotNull private static <A extends Annotation> Function<Field, A> fieldToItsAnnotationOfType(final Class<A> annotationClass) {
        return field -> field.getAnnotation(annotationClass);
    }

    @NotNull private static <A extends Annotation> Function<A, Object> annotationToValueOfItsAttributeWith(final String annotationAttributeName) {
        return annotationInstance -> {
            try {
                final Method method = annotationInstance
                    .annotationType()
                    .getDeclaredMethod(annotationAttributeName);

                if (method.getParameterCount() == 0 && method.getReturnType() != void.class) {
                    return method.invoke(annotationInstance);
                }

            } catch (final Exception e) {
                // Exception means we don't have a match and that's all we care about.
                e.printStackTrace();
            }

            return null;
        };
    }

    private static Predicate<Field> fieldsWithname(final String fieldName) {
        return field -> field.getName().equals(fieldName);
    }
}
