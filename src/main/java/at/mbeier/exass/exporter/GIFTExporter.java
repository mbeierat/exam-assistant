package at.mbeier.exass.exporter;

import at.mbeier.exass.model.Category;
import at.mbeier.exass.model.Question;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GIFTExporter extends Exporter {

    public GIFTExporter(List<Category> toExport) {
        super(toExport);
    }

    @Override
    public void export() throws IOException {
        List<Category> cat = super.getToExport();
        for (Category category : cat) {
            List<String> lines = new ArrayList<>();
            lines.add("$CATEGORY: $module$/top/" + category.getName());
            for (Question question : category.getQuestions()) {
                lines.add(question.toGIFTString());
            }
            Files.write(Path.of(super.getFolder(), "export_" + category.getName() + ".GIFT"), lines);
        }
    }
}
