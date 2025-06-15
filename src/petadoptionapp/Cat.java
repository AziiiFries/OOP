package petadoptionapp;

import java.io.Serializable;

// Inheritance - Extends Pet class
public class Cat extends Pet implements Serializable {
    private static final long serialVersionUID = 1L;

    // Polymorphism - Constructor overloading
    public Cat(String name, int age, String description, String imagePath, String gender) {
        super(name, age, description, imagePath, gender);
    }

    // Polymorphism - Constructor overloading (different parameters)
    public Cat(String name, int age, int months, String description, String imagePath, String gender) {
        super(name, age, months, description, imagePath, gender);
    }

    // Polymorphism - Method overriding
    @Override
    public void displayDetails() {
        System.out.println("Cat: " + getName() + ", Age: " + getAge() + 
                         ", Description: " + getDescription() + 
                         ", Gender: " + getGender());
    }
}