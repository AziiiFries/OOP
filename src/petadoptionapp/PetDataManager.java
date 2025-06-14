// PetDataManager.java
package petadoptionapp;

import java.io.*;
import java.util.ArrayList;

public class PetDataManager {
    private static final String FILE_NAME = "pets.dat";

    /**
     * Loads pet data from a file. If the file is not found or an error occurs during loading,
     * it initializes the pet list with default pets and attempts to save them.
     *
     * @return An ArrayList of Pet objects loaded from file or default pets.
     */
    public static ArrayList<Pet> loadPets() {
        ArrayList<Pet> pets = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            pets = (ArrayList<Pet>) ois.readObject();
            System.out.println("Pets loaded successfully from " + FILE_NAME);
        } catch (FileNotFoundException e) {
            System.out.println("No existing pets file found. Initializing with default pets.");
            pets = getDefaultPets(); // Initialize with default pets if file not found
            savePets(pets); // Save defaults immediately
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading pets: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            pets = getDefaultPets(); // Fallback to default pets on error
            savePets(pets); // IMPORTANT: Save defaults if loading failed to fix corrupted file for next run
        }
        return pets;
    }

    /**
     * Saves the current list of pets to a file.
     *
     * @param pets The ArrayList of Pet objects to be saved.
     */
    public static void savePets(ArrayList<Pet> pets) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(pets);
            System.out.println("Pets saved successfully to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving pets: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }

    /**
     * Provides a predefined list of default Cat and Dog objects.
     * This method is called when no saved pet data is found.
     *
     * @return An ArrayList containing default Pet objects.
     */
    private static ArrayList<Pet> getDefaultPets() {
        ArrayList<Pet> defaultPets = new ArrayList<>();

        // --- Male Cat Rescues ---
        defaultPets.add(new Cat("Ash", 2, 0,
                "Color: Blue\n" +
                "Breed: British Shorthair\n" +
                "Health Status: Asthma\n" +
                "Spayed/Neutered: Neutered\n" +
                "Vaccinations & Deworm: Deworm, 4-in-1, Anti-rabies\n" +
                "Description: Ash was adopted from a shelter by a caring family who\n" +
                "continues to manage his asthma with love and attention.",
                "/resources/cat_ash.png", "Male"));
        defaultPets.add(new Cat("Choknat", 0, 12,
                "Color: Black gray\n" +
                "Breed: Philippine Street Cat\n" +
                "Health Status: Allergic to wet food\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: Deworm, 4-in-1\n" +
                "Description: Choknat was found wandering in a local neighborhood and\n" +
                "was adopted by a compassionate rescuer who gave her a safe home.",
                "/resources/cat_choknat.png", "Male"));
        defaultPets.add(new Cat("Frank", 2, 0,
                "Color: Black and Gray\n" +
                "Breed: Philippine Street Cat and Persian\n" +
                "Health Status: No health status\n" +
                "Spayed/Neutered: Neutered\n" +
                "Vaccinations & Deworm: Deworm, 4-in-1, Anti-rabies\n" +
                "Description: Frank was born to a stray mother and was adopted by a\n" +
                "family who embraced his unique Persian mix and playful personality.",
                "/resources/cat_frank.png", "Male"));
        defaultPets.add(new Cat("Jobet", 1, 0,
                "Color: Ginger white\n" +
                "Breed: Philippine Street Cat\n" +
                "Health Status: No health issues\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: Deworm, 4-in-1, Anti-rabies\n" +
                "Description: Jobet was rescued from the streets as a kitten and\n" +
                "welcomed into a warm and loving household.",
                "/resources/cat_jobet.png", "Male"));
        defaultPets.add(new Cat("Kitty", 0, 2,
                "Color: Orange\n" +
                "Breed: Philippine Street Cat\n" +
                "Health Status: No health issues\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: Deworm, 4-in-1, Anti-rabies\n" +
                "Description: Kitty was rescued as a stray kitten and taken in by\n" +
                "a kind-hearted animal lover who gave her a safe and loving home.",
                "/resources/cat_kitty.png", "Female"));
        defaultPets.add(new Cat("Pipoy", 0, 2,
                "Color: Ginger\n" +
                "Breed: Persian\n" +
                "Health Status: No health issues\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: Deworm, 4-in-1\n" +
                "Description: Pipoy was adopted from a friend’s accidental litter\n" +
                "and quickly became the adored baby of his new family.",
                "/resources/cat_pipoy.png", "Male"));
        defaultPets.add(new Cat("Toffee", 0, 3,
                "Color: Black and gray\n" +
                "Breed: Philippine Street Cat\n" +
                "Health Status: No health issues\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: Deworm, 4-in-1\n" +
                "Description: Toffee was taken in by a kind-hearted individual after\n" +
                "being found alone near a market as a tiny kitten.",
                "/resources/cat_toffee.png", "Male"));
        defaultPets.add(new Cat("Riley", 2, 0,
                "Color: Black and White\n" +
                "Breed: Persian\n" +
                "Health Status: Paralyzed lower feet due to abuse\n" +
                "Spayed/Neutered: Neutered\n" +
                "Vaccinations & Deworm: Deworm, 4-in-1, Anti-rabies\n" +
                "Description: Riley was rescued from an abusive situation that left\n" +
                "him paralyzed, and he was adopted by a devoted caregiver who\n" +
                "provides him with constant love and support.",
                "/resources/cat_riley.png", "Male"));

        // --- Female Cat Rescues ---
        defaultPets.add(new Cat("Osang", 0, 3,
                "Color: Ginger\n" +
                "Breed: Persian Ragdoll\n" +
                "Health Status: No health issues\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: Oral Deworm\n" +
                "Description: Osang was adopted from a neighbor whose cat unexpectedly\n" +
                "gave birth, and she quickly became the youngest member of her new family.",
                "/resources/cat_osang.png", "Female"));
        defaultPets.add(new Cat("Mimay", 2, 0,
                "Color: Ginger white\n" +
                "Breed: Philippine Street Cat\n" +
                "Health Status: No right eye due to accident\n" +
                "Spayed/Neutered: Spayed\n" +
                "Vaccinations & Deworm: Deworm, 4-in-1, Anti-rabies\n" +
                "Description: Mimay was found injured on the streets and lovingly\n" +
                "adopted after surviving an accident that took her right eye.",
                "/resources/cat_mimay.png", "Female"));
        defaultPets.add(new Cat("Mimi", 2, 0,
                "Color: Black Gray and White\n" +
                "Breed: Philippine Street Cat\n" +
                "Health Status: Allergic to any Monello cat food\n" +
                "Spayed/Neutered: Spayed\n" +
                "Vaccinations & Deworm: Deworm, 4-in-1, Anti-rabies\n" +
                "Description: Mimi was rescued from the streets as a young stray and\n" +
                "adopted into a caring home that helped her recover and thrive.",
                "/resources/cat_mimi.png", "Female"));
        defaultPets.add(new Cat("Nene", 0, 11,
                "Color: Blue\n" +
                "Breed: British Shorthair\n" +
                "Health Status: No health issues\n" +
                "Spayed/Neutered: Spayed\n" +
                "Vaccinations & Deworm: Deworm, 4-in-1, Anti-rabies\n" +
                "Description: Nene was adopted from a trusted breeder and has grown up\n" +
                "in a comfortable and affectionate environment.",
                "/resources/cat_nene.png", "Female"));
        defaultPets.add(new Cat("Tisay", 0, 4,
                "Color: Ginger white\n" +
                "Breed: Philippine Street Cat\n" +
                "Health Status: Difficulty to walk due to abuse\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: Deworm, 4-in-1\n" +
                "Description: Tisay was saved from an abusive situation and adopted by\n" +
                "a compassionate rescuer who is helping her heal and learn to trust again.",
                "/resources/cat_tisay.png", "Female"));
        defaultPets.add(new Cat("Sassa", 4, 0,
                "Color: Black\n" +
                "Breed: Philippine Street Cat\n" +
                "Health Status: Difficulty to jump due to old age\n" +
                "Spayed/Neutered: Spayed\n" +
                "Vaccinations & Deworm: Deworm, 4-in-1, Anti-rabies\n" +
                "Description: Sassa was adopted as a senior stray and now enjoys a\n" +
                "peaceful, loving home where she can age gracefully.",
                "/resources/cat_sassa.png", "Female"));
        defaultPets.add(new Cat("Siopao", 3, 0,
                "Color: Multi-colored\n" +
                "Breed: Philippine Street Cat\n" +
                "Health Status: No right eye, surgically removed\n" +
                "Spayed/Neutered: Spayed\n" +
                "Vaccinations & Deworm: Deworm, 4-in-1, Anti-rabies\n" +
                "Description: Siopao was rescued after an accident left her blind in\n" +
                "one eye, and she was adopted into a nurturing home that continues\n" +
                "to care for her special needs.",
                "/resources/cat_siopao.png", "Female"));

        // --- Male Dog Rescues ---
        defaultPets.add(new Dog("Alexis", 2, 0,
                "Color: Brown\n" +
                "Breed: Aspin\n" +
                "Health Status: One eye surgically removed due to a past injury.\n" +
                "Currently under medication maintenance but stable and responding well.\n" +
                "Spayed/Neutered: Neutered\n" +
                "Vaccinations & Deworm: Anti-rabies, 8-in-1 & Oral Deworm\n" +
                "Description: Alexis was found in a vacant lot in Marikina City,\n" +
                "alone and injured. Despite her early hardships, he remains incredibly\n" +
                "gentle and affectionate.",
                "/resources/dog_alexis.png", "Male"));
        defaultPets.add(new Dog("Arian", 1, 2,
                "Color: Brown and White\n" +
                "Breed: Basenji\n" +
                "Health Status: Allergic to Royal Canin dry dog food\n" +
                "Spayed/Neutered: Neutered\n" +
                "Vaccinations & Deworm: Anti-rabies, 8-in-1 & Oral Deworm\n" +
                "Description: He was rescued as a malnourished stray pup searching for\n" +
                "food and shelter. With love and care, he’s now healthy, happy, and\n" +
                "ready for his forever home.",
                "/resources/dog_arian.png", "Male"));
        defaultPets.add(new Dog("Billie", 0, 12,
                "Color: Black and White\n" +
                "Breed: Border Collie\n" +
                "Health Status: Left eye has cataract\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: Anti-rabies, 8-in-1 & Oral Deworm\n" +
                "Description: Billie was rescued from neglect but has blossomed into\n" +
                "a lively, intelligent dog. Despite having a cataract in one eye,\n" +
                "he’s full of energy and love.",
                "/resources/dog_billie.png", "Male"));
        defaultPets.add(new Dog("Brisket", 0, 10,
                "Color: Off white and Gray\n" +
                "Breed: Maltese\n" +
                "Health Status: No health issues\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: Anti-rabies, 8-in-1 & Oral Deworm\n" +
                "Description: Brisket was rescued after being abandoned in a box near\n" +
                "a busy road, scared and hungry. Now safe and healthy, he’s a\n" +
                "cheerful pup ready for a loving home.",
                "/resources/dog_brisket.png", "Male"));
        defaultPets.add(new Dog("Brix", 0, 11,
                "Color: White\n" +
                "Breed: Shih Tzu\n" +
                "Health Status: Underbite and has injury in left foot (still recovering)\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: Anti-rabies, 8-in-1 & Oral Deworm\n" +
                "Description: Brix was found limping near a roadside, likely abandoned,\n" +
                "and was gently rescued by a kind passerby. He is now safe and\n" +
                "recovering well from his foot injury.",
                "/resources/dog_brix.png", "Male"));
        defaultPets.add(new Dog("Bruno", 3, 0,
                "Color: Brown and Black\n" +
                "Breed: Belgian Malinois\n" +
                "Health Status: No health issues\n" +
                "Spayed/Neutered: Neutered\n" +
                "Vaccinations & Deworm: Anti-rabies, 8-in-1 & Oral Deworm\n" +
                "Description: Bruno was rescued after being spotted wandering alone near\n" +
                "a construction site, hungry and scared. He was safely brought in and\n" +
                "has since regained his strength and confidence.",
                "/resources/dog_bruno.png", "Male"));
        defaultPets.add(new Dog("Brutos", 1, 0,
                "Color: Brown and White\n" +
                "Breed: Aspin\n" +
                "Health Status: No health issues\n" +
                "Spayed/Neutered: Neutered\n" +
                "Vaccinations & Deworm:Anti-rabies, 8-in-1 & Oral Deworm\n" +
                "Description: Brutos was rescued from the streets after being seen scavenging\n" +
                "for food near a marketplace. He quickly adapted to care and is now thriving\n" +
                "in a safe environment.",
                "/resources/dog_brutos.png", "Male"));
        defaultPets.add(new Dog("Frankie", 0, 7,
                "Color: Multi-colored\n" +
                "Breed: Chihuahua\n" +
                "Health Status: Has epilepsy\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: Anti-rabies, 8-in-1 & Oral Deworm\n" +
                "Description: Frankie was rescued after being abandoned outside a veterinary clinic,\n" +
                "trembling and alone. Despite his epilepsy, he is now receiving the care he needs\n" +
                "and continues to show a loving spirit.",
                "/resources/dog_frankie.png", "Male"));

        // --- Female Dog Rescues ---
        defaultPets.add(new Dog("Alusha", 2, 0,
                "Color: Light Brown\n" +
                "Breed: Golden Retriever\n" +
                "Health Status: No health issues\n" +
                "Spayed/Neutered: Spayed\n" +
                "Vaccinations & Deworm: Anti-rabies, 8-in-1 & Oral Deworm\n" +
                "Description: Alusha was rescued from a backyard breeder who could no longer\n" +
                "care for her and her littermates. She was the smallest of the group but\n" +
                "full of energy and love.",
                "/resources/dog_alusha.png", "Female"));
        defaultPets.add(new Dog("Andy", 0, 5,
                "Color: Black\n" +
                "Breed: Dachshund\n" +
                "Health Status: No health issues\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: 8-in-1 & Oral Deworm\n" +
                "Description: Andy was found wandering alone near a marketplace,\n" +
                "likely abandoned due to his breed's health maintenance needs.\n" +
                "A kind passerby alerted rescuers just in time.",
                "/resources/dog_andy.png", "Female"));
        defaultPets.add(new Dog("Biscoff", 0, 2,
                "Color: Brown and White\n" +
                "Breed: Corgi\n" +
                "Health Status: No health issues\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: (1) 8-in-1 & Oral Deworm\n" +
                "Description: Biscoff was discovered inside a cardboard box\n" +
                "left outside a veterinary clinic. Despite his young age,\n" +
                "he showed remarkable resilience and playfulness.",
                "/resources/dog_biscoff.png", "Female"));
        defaultPets.add(new Dog("Bleu", 0, 3,
                "Color: Black Brown\n" +
                "Breed: Rottweiler\n" +
                "Health Status: Has allergy to Nutri chunks dry dog food\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: 8-in-1 & Oral Deworm\n" +
                "Description: Bleu was surrendered by a family who could not manage his dietary needs.\n" +
                "He was malnourished and itchy but is now recovering in foster care.",
                "/resources/dog_bleu.png", "Female"));
        defaultPets.add(new Dog("Cassie", 0, 5,
                "Color: Black\n" +
                "Breed: Labrador\n" +
                "Health Status: Allergic to Aozi dry and wet dog food\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: 8-in-1 & Oral Deworm\n" +
                "Description: Cassie was rescued from a cramped cage at an overrun shelter.\n" +
                "She had been overlooked due to her allergies and black fur, but she’s now thriving.",
                "/resources/dog_cassie.png", "Female"));
        defaultPets.add(new Dog("Chichay", 1, 0,
                "Color: Chocolate Brown\n" +
                "Breed: Labrador\n" +
                "Health Status: Deaf due to abusement\n" +
                "Spayed/Neutered: Spayed\n" +
                "Vaccinations & Deworm: Anti-rabies, 8-in-1 & Oral Deworm\n" +
                "Description: Chichay was found chained and abused in a backyard;\n" +
                "her deafness is a lasting result of the trauma she endured.\n" +
                "She has since blossomed into a gentle and loyal companion.",
                "/resources/dog_chichay.png", "Female"));
        defaultPets.add(new Dog("Lucy", 0, 8,
                "Color: Black and white\n" +
                "Breed: Shih Tzu\n" +
                "Health Status: Deaf\n" +
                "Spayed/Neutered: Not yet\n" +
                "Vaccinations & Deworm: Anti-rabies, 8-in-1 & Oral Deworm\n" +
                "Description: Lucy was surrendered to the shelter after her breeder deemed her\n" +
                " due to her deafness. She has proven to be a sweet and loving pup.",
                "/resources/dog_lucy.png", "Female"));

        return defaultPets;
    }
}
