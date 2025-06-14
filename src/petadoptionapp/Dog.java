// Dog.java
package petadoptionapp;

import java.io.Serializable; // Import Serializable

public class Dog extends Pet implements Serializable { // Implement Serializable
	private static final long serialVersionUID = 1L; // Recommended for Serializable classes

	public Dog(String name, int age, String description, String imagePath, String gender) {
		super(name, age, description, imagePath, gender);
	}

	public Dog(String name, int age, int months, String description, String imagePath, String gender) { // Overloaded
		super(name, age, months, description, imagePath, gender);
	}

	@Override
	public void displayDetails() {
		System.out.println("Dog: " + getName() + ", Age: " + getAge() + ", Description: " + getDescription() + ", Gender: " + getGender());
	}
}
