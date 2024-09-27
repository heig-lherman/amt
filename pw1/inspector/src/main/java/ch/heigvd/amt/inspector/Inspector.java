package ch.heigvd.amt.inspector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

public class Inspector {

    /**
     * Inspect an object and print its class name, fields and methods.
     * @param object the object to inspect
     */
    public static void inspect(Object object) {
        System.out.printf("Class name: %s%n", object.getClass().getName());

        System.out.println("Fields:");
        Arrays.stream(object.getClass().getDeclaredFields())
                .sorted(Comparator.comparing(Field::getName))
                .forEach(field -> {
                    var accessible = field.canAccess(object);
                    try {
                        field.setAccessible(true);
                        System.out.printf(" - %s: %s%n", field.getName(), field.get(object));
                    } catch (IllegalAccessException e) {
                        System.out.printf(" - %s: <inaccessible>%n", field.getName());
                    } finally {
                        field.setAccessible(accessible);
                    }
                });

        System.out.println("Methods:");
        Arrays.stream(object.getClass().getDeclaredMethods())
                .sorted(Comparator.comparing(Method::getName))
                .forEach(method -> System.out.printf(" - %s%n", method.getName()));
    }

    /**
     * Demonstrate the use of the Inspector class by inspecting a dog.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        inspect(new Dog("Buddy", 5));
    }
}
