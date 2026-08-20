package at.mbeier.exass.controller;

import at.mbeier.exass.exporter.Exporter;
import at.mbeier.exass.exporter.GIFTExporter;
import at.mbeier.exass.exporter.XMLExporter;
import at.mbeier.exass.model.Category;

import java.io.IOException;
import java.util.List;

public class ExportController {

    public void export(List<Category> categories, String folder, boolean asXml) throws IOException {
        Exporter exporter = asXml ? new XMLExporter(categories) : new GIFTExporter(categories);
        exporter.setFolder(folder);
        exporter.export();
    }
}
