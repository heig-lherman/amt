package ch.heigvd.amt.builder;

/**
 * This class is annotated with @GenerateBuilder, which triggers the annotation processor.
 */
@GenerateBuilder
public class Person {

    private String name;

    private int age;

    public Person() {
        this.name = "John Doe";
        this.age = 42;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    /**
     * Demonstrates the use of the @GenerateBuilder annotation.
     *
     * @param args the command line arguments
     */
    public static void main(String... args) {
        // The builder class is generated at compile time
        // if the annotation processor is implemented and configured correctly
        // (see builder-processor module)

        Person person = new PersonBuilder()
                .setName("John Doe")
                .setAge(42)
                .build();
        System.out.println(person);
    }

}
