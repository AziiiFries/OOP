package petadoptionapp;

public class AdoptionController {
	public void processAdoption(Pet pet) {
		// Handle adoption logic
		System.out.println("Processing adoption for: " + pet.getName());
		// In a real app, this would involve database updates, form submission, etc.
	}

	public void processDonation(double amount) {
		// Handle donation logic
		System.out.println("Processing donation of: " + amount);
		// In a real app, this would involve payment gateway integration, database updates, etc.
	}
}