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

class GIFTExporterTest {

    @TempDir
    Path tempDir;

    @Test
    void writesOneFilePerCategoryWithACategoryHeaderAndEveryQuestion() throws IOException {
        Category category = new Category("Chapter1");
        category.addQuestion(new Description("Q1", "First question"));
        category.addQuestion(new Description("Q2", "Second question"));

        GIFTExporter exporter = new GIFTExporter(List.of(category));
        exporter.setFolder(tempDir.toString());
        exporter.export();

        Path file = tempDir.resolve("export_Chapter1.GIFT");
        assertTrue(Files.exists(file));
        String content = Files.readString(file);
        assertTrue(content.contains("$CATEGORY: $module$/top/Chapter1"));
        assertTrue(content.contains("::Q1::[html]First question"));
        assertTrue(content.contains("::Q2::[html]Second question"));
    }
}
