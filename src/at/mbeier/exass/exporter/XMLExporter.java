package at.mbeier.exass.exporter;

import at.mbeier.exass.model.Category;
import at.mbeier.exass.model.Question;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class XMLExporter extends Exporter {

    public XMLExporter(List<Category> toExport) {
        super(toExport);
    }

    @Override
    public void export() throws IOException {
        List<Category> cat = super.getToExport();
        for (Category category : cat) {
            Document doc = XMLUtil.newDocument();
            Element quiz = XMLUtil.element(doc, "quiz");
            doc.appendChild(quiz);
            for (Question question : category.getQuestions()) {
                quiz.appendChild(question.toXMLElement(doc));
            }
            String xml = XMLUtil.toXmlString(doc);
            Files.writeString(Path.of(super.getFolder(), "export_" + category.getName() + ".XML"), xml);
        }
    }
}
