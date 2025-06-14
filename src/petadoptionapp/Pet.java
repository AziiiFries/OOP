// Pet.java
package petadoptionapp;

import java.io.Serializable; // Import Serializable

public abstract class Pet implements Serializable { // Implement Serializable
	private static final long serialVersionUID = 1L; // Recommended for Serializable classes

	private String name;
	private int age; // Age in years
	private int months; // Age in months (additional detail)
	private String description;
	private String imagePath; // Path to the image resource
	private String gender; // Added gender property

	public Pet(String name, int age, String description, String imagePath, String gender) {
		this(name, age, 0, description, imagePath, gender); // Call overloaded constructor with 0 months
	}

	public Pet(String name, int age, int months, String description, String imagePath, String gender) {
		this.name = name;
		this.age = age;
		this.months = months;
		this.description = description;
		this.imagePath = imagePath;
		this.gender = gender; // Initialize gender
	}

	// Getters and setters (Encapsulation)
	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public int getMonths() { // Getter for months
		return months;
	}

	public String getDescription() {
		return description;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getGender() { // Getter for gender
		return gender;
	}

	// You might need setters if you plan to edit pet details later
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

	public abstract void displayDetails();
}
