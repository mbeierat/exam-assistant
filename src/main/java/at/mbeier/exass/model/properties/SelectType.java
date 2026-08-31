package at.mbeier.exass.model.properties;

public enum SelectType {
    ALL("ALL"),
    RANDOM("RANDOM"),
    CONTIGUOUS("CONTIGUOUS");

    private final String name;

    SelectType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
