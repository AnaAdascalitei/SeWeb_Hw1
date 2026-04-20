package ro.semanticweb.recipeapp.model;

public enum DifficultyLevel {
    Beginner,
    Intermediate,
    Advanced;

    public static boolean isValid(String value) {
        for (DifficultyLevel level : values()) {
            if (level.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
