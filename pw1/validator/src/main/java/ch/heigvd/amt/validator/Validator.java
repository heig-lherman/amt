package ch.heigvd.amt.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * This class is used to validate annotated objects at runtime.
 */
public class Validator {

    /**
     * This method validates an object by checking all its fields.
     * @param object the object to validate.
     */
    public static void validate(Object object) {
        System.out.printf("Validating %s%n", object);
        for (Field field : object.getClass().getDeclaredFields()) {
            handleValidation(object, field);
        }
    }

    private static void handleValidation(Object object, Field field) {
        var accessible = field.canAccess(object);
        try {
            field.setAccessible(true);
            var value = field.get(object);
            for (var annotation : field.getDeclaredAnnotations()) {
                switch (annotation) {
                    case NotNull notNull -> validateNotNull(notNull, field, value);
                    case Range range -> validateRange(range, field, value);
                    case Regex regex -> validateRegex(regex, field, value);
                    default -> throw new UnsupportedOperationException("Unsupported annotation: " + annotation);
                }
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } finally {
            field.setAccessible(accessible);
        }
    }

    private static void validateNotNull(NotNull notNull, Field field, Object value) {
        if (Objects.isNull(value)) {
            System.out.printf("- %s is null%n", field.getName());
        } else {
            System.out.printf("- %s is valid%n", field.getName());
        }
    }

    private static void validateRange(Range range, Field field, Object value) {
        if (!(value instanceof Number number)) {
            throw new IllegalArgumentException("Field " + field.getName() + " is not a number");
        }

        if (number.doubleValue() < range.min() || number.doubleValue() > range.max()) {
            System.out.printf("- %s is not in range [%.1f, %.1f]%n", field.getName(), range.min(), range.max());
        } else {
            System.out.printf("- %s is valid%n", field.getName());
        }
    }

    private static void validateRegex(Regex regex, Field field, Object value) {
        if (!(value instanceof String string)) {
            throw new IllegalArgumentException("Field " + field.getName() + " is not a string");
        }

        if (!string.matches(regex.value())) {
            System.out.printf("- %s does not match regex %s%n", field.getName(), regex.value());
        } else {
            System.out.printf("- %s is valid%n", field.getName());
        }
    }

    /**
     * This class is used to test the validator.
     * Its fields are annotated with the validator annotations.
     */
    static class Person {

        @NotNull
        String username;

        @Range(min = 0, max = 100)
        int age;

        @Regex("[a-z]+@[a-z]+\\.[a-z]+")
        String email;

        @Regex("\\+[0-9]{2} [0-9]{2} [0-9]{3} [0-9]{2} [0-9]{2}")
        String phoneNumber;

        public Person(String username, int age, String email, String phoneNumber) {
            this.username = username;
            this.age = age;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "username='" + username + '\'' +
                    ", age=" + age +
                    ", email='" + email + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    '}';
        }
    }

    public static void main(String... args) {
        validate(new Person("john", 42, "john@example.com", "+41 79 123 45 67"));
        validate(new Person(null, 200, "john@example", "079 123 45 67"));
    }
}
