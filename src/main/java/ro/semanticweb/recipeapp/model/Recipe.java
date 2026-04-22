package ro.semanticweb.recipeapp.model;

import java.util.List;

public class Recipe {
    private final String id;
    private final String title;
    private final List<String> cuisineTypes;
    private final DifficultyLevel difficultyLevel;

    public Recipe(String id, String title, List<String> cuisineTypes, DifficultyLevel difficultyLevel) {
        this.id = id;
        this.title = title;
        this.cuisineTypes = cuisineTypes;
        this.difficultyLevel = difficultyLevel;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getCuisineTypes() {
        return cuisineTypes;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }
}
