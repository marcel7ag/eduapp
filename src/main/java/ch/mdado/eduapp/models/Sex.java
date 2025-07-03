package ch.mdado.eduapp.models;

public enum Sex {
    MASCULINE('M', "Male"),
    FEMININE('F', "Female"),
    OTHER('O', "Other");

    private char code;
    private String name;

    Sex(char code, String name) {
        this.code = code;
        this.name = name;
    }

    public char getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Sex toEnum(Character character){
        if(character == null){
            return null;
        }
        for(Sex Sex : Sex.values()){
            if(character.equals(Sex.getCode())){
                return Sex;
            }
        }
        throw new IllegalArgumentException("Invalid character: " + character);
    }
}
