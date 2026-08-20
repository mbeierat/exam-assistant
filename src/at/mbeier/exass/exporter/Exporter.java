package at.mbeier.exass.exporter;

import at.mbeier.exass.model.Category;

import java.io.IOException;
import java.util.List;

public abstract class Exporter {

    private List<Category> toExport;
    private String folder;

    public Exporter(List<Category> toExport) {
        this.toExport = toExport;
    }

    public List<Category> getToExport() {
        return this.toExport;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return this.folder;
    }

    public abstract void export() throws IOException;

}
