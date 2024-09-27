# Exercise 4: Builder (CompileTime Annotation Processing)

In the builder-processor module, the `GenerateBuilder` annotation can be added to any class to tell the compiler to generate a builder for that class thanks to the `BuilderProcessor` class.

Your task is to complete the `process` method of the `BuilderProcessor` class in the builder-processor module, so that the builder-processor module can generate a builder for the `Person` class in the builder-example module.
More specifically, the `BuilderProcessor` should generate a `PersonBuilder` class similar to the following one in the `target/generated-sources` directory of the builder-example module:

```java
package ch.heigvd.amt.builder;

import java.lang.String;
import java.lang.reflect.Field;

public class PersonBuilder {
    private String name;

    private int age;

    public PersonBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder setAge(int age) {
        this.age = age;
        return this;
    }

    public Person build() {
        Person result = new Person();
        Class<?> clazz = result.getClass();
        try {
            Field nameField = clazz.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(result, this.name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Field ageField = clazz.getDeclaredField("age");
            ageField.setAccessible(true);
            ageField.set(result, this.age);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
```

Notice that the `BuilderProcessor` must be compiled before the `BuilderExample` module, hence the separate modules.
The builder-example module has a dependency on the builder-processor module and the compiler of the builder-example module is configured to use the `BuilderProcessor` class as annotation processor in the `pom.xml` file.

[JavaPoet](https://github.com/square/javapoet) can be used to facilitate the generation of the source code of the builder.

Then, executing the `main` method of the `Person` class should produce the following output:

```
Person{name='John Doe', age=42}
```
