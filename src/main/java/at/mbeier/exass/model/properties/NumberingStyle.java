package at.mbeier.exass.model.properties;

public enum NumberingStyle {
    NONE("none"),
    LOWER_ABC("abc"),
    UPPER_ABC("ABCD"),
    NUMBERS("123"),
    LOWER_ROMAN("iii"),
    UPPER_ROMAN("IIII");

    private final String name;

    NumberingStyle(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
