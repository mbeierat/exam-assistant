package at.mbeier.exass.model.properties;

public enum ShowGrading {
    SHOW("SHOW"), HIDE("HIDE");

    private final String name;

    ShowGrading(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
