package ch.heigvd.amt.inspector;

/**
 * This class, used to demonstrate the use of the Inspector class, represents a dog.
 */
public class Dog {

    public String name;

    private int age;

    /**
     * Constructs a new dog.
     *
     * @param name the name of the dog
     * @param age the age of the dog
     */
    public Dog(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * Makes the dog bark.
     */
    public void bark() {
        System.out.println("Woof! Woof!");
    }

    /**
     * Makes the dog growl.
     */
    private void growl() {
        System.out.println("Grrrr...");
    }

    /**
     * Returns the age of the dog.
     *
     * @return the age of the dog
     */
    public int getAge() {
        return age;
    }

}
