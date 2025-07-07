package ch.mdado.eduapp.models;

public enum AbsenceType {
    LATE("Verspätung"),
    ABSENT("Abwesend"),
    EARLY_LEAVE("Früh gegangen"),
    EXCUSED_ABSENCE("Entschuldigte Abwesenheit");

    private final String description;

    AbsenceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}