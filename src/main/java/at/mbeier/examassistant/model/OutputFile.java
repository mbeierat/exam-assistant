package at.mbeier.examassistant.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputFile implements GIFTFormattable, MoodleXMLFormattable {

    private final List<Category> categories;

    public OutputFile() {
        this.categories = new ArrayList<>();
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void exportXMLFile() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("parser configuration failed on creating xml document", e);
        }
        Document doc = db.newDocument();
        Element root = doc.createElement("quiz");
        doc.appendChild(root);

        this.appendXMLElements(doc, root);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException("transformer configuration failed on printing xml document", e);
        }
        File out = new File("output.xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(out);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException("error occurred when transforming dom source to result", e);
        }
    }

    public void exportGIFTFile() {
        File out = new File("output.gift");
        try (FileWriter fw = new FileWriter(out)) {
            fw.write(this.toGIFTString());
        } catch (IOException e) {
            throw new RuntimeException("couldn't write gift string to output file", e);
        }
    }

    @Override
    public String toGIFTString() {
        StringBuilder builder = new StringBuilder();
        for (Category category : this.categories) {
            builder.append(category.toGIFTString());
        }
        return builder.toString();
    }

    @Override
    public void appendXMLElements(Document doc, Element parent) {
        for (Category category : this.categories)
            category.appendXMLElements(doc, parent);
    }
}
