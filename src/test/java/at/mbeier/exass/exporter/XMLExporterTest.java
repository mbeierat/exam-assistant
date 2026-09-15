package at.mbeier.exass.exporter;

import at.mbeier.exass.model.Category;
import at.mbeier.exass.model.questions.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class XMLExporterTest {

    @TempDir
    Path tempDir;

    @Test
    void writesOneFilePerCategoryWithACategoryElementAndEveryQuestion() throws IOException {
        Category category = new Category("Chapter1");
        category.addQuestion(new Description("Q1", "First question"));

        XMLExporter exporter = new XMLExporter(List.of(category));
        exporter.setFolder(tempDir.toString());
        exporter.export();

        Path file = tempDir.resolve("export_Chapter1.XML");
        assertTrue(Files.exists(file));
        String content = Files.readString(file);
        assertTrue(content.contains("<quiz>"));
        assertTrue(content.contains("$module$/top/Chapter1"));
        assertTrue(content.contains("<question type=\"description\">"));
        assertTrue(content.contains("Q1"));
        assertTrue(content.contains("First question"));
    }
}
