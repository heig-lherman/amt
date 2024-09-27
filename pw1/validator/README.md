# Exercise 3: Validator (RunTime Annotation Processing)

In the validator module, the `Validator` class uses annotations and reflection to validate the state of an object.

Your task is to complete the `validate` method of the `Validator` class.

Executing the `main` method of the `Validator` class should produce the following output:

```
Validating Person{username='john', age=42, email='john@example.com', phoneNumber='+41 79 123 45 67'}
- username is valid
- age is valid
- email is valid
- phoneNumber is valid
Validating Person{username='null', age=200, email='john@example', phoneNumber='079 123 45 67'}
- username is null
- age is not in range [0.0, 100.0]
- email does not match regex [a-z]+@[a-z]+\.[a-z]+
- phoneNumber does not match regex \+[0-9]{2} [0-9]{2} [0-9]{3} [0-9]{2} [0-9]{2}
```