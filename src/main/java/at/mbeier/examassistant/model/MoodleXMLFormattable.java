package at.mbeier.examassistant.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface MoodleXMLFormattable {
    void appendXMLElements(Document doc, Element parent);
}
