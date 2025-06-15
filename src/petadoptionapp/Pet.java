package petadoptionapp;

import java.io.Serializable;

//Abstraction - Abstract base class for pets (implements Serializable)
public abstract class Pet implements Serializable {
    private static final long serialVersionUID = 1L;

    // Encapsulation - Private fields with public getters/setters
    private String name;
    private int age;
    private int months;
    private String description;
    private String imagePath;
    private String gender;

    // Polymorphism - Constructor overloading
    public Pet(String name, int age, String description, String imagePath, String gender) {
        this(name, age, 0, description, imagePath, gender);
    }

    public Pet(String name, int age, int months, String description, String imagePath, String gender) {
        this.name = name;
        this.age = age;
        this.months = months;
        this.description = description;
        this.imagePath = imagePath;
        this.gender = gender;
    }

    // Encapsulation - Getters and setters
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getMonths() {
        return months;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getGender() {
        return gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Abstraction - Abstract method (must be implemented by subclasses)
    public abstract void displayDetails();
}