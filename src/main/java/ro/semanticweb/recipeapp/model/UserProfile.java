package ro.semanticweb.recipeapp.model;

public class UserProfile {
    private final String id;
    private final String name;
    private final String surname;
    private final DifficultyLevel cookingSkillLevel;
    private final String preferredCuisineType;

    public UserProfile(String id, String name, String surname, DifficultyLevel cookingSkillLevel, String preferredCuisineType) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.cookingSkillLevel = cookingSkillLevel;
        this.preferredCuisineType = preferredCuisineType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public DifficultyLevel getCookingSkillLevel() {
        return cookingSkillLevel;
    }

    public String getPreferredCuisineType() {
        return preferredCuisineType;
    }

    public String getFullName() {
        return name + " " + surname;
    }
}
