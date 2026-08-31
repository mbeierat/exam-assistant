package at.mbeier.exass.model.properties;

public enum LayoutType {
    HORIZONTAL("HORIZONTAL"), VERTICAL("VERTICAL");

    private final String name;

    LayoutType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
