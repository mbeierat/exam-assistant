package at.mbeier.exass.exporter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * Small DOM helper shared by every Question#toXMLElement(Document) override.
 * A Question only ever builds a fragment into a Document it's handed - it
 * never serializes anything itself. XMLExporter owns the Document for a
 * whole category export and calls toXmlString(doc) exactly once, after every
 * question has been appended, so there's a single place where escaping and
 * serialization happen instead of 19 places that could each get it wrong.
 */
public final class XMLUtil {

    private XMLUtil() {}

    public static Document newDocument() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException("Cannot create a new XML document", e);
        }
    }

    public static Element element(Document doc, String tag) {
        return doc.createElement(tag);
    }

    /**
     * An element with a single text-node child. Going through
     * Document#createTextNode is what gives automatic escaping of
     * &amp; &lt; &gt; " in question/answer/feedback text - never build
     * text content by concatenating it directly into a tag string.
     */
    public static Element textElement(Document doc, String tag, String text) {
        Element el = doc.createElement(tag);
        el.appendChild(doc.createTextNode(text == null ? "" : text));
        return el;
    }

    /**
     * Sets an attribute and returns the element, so it chains at the call site:
     * append(question, attr(element(doc, "name"), "format", "html"))
     */
    public static Element attr(Element element, String name, String value) {
        element.setAttribute(name, value);
        return element;
    }

    public static Element append(Element parent, Node... children) {
        for (Node child : children) {
            parent.appendChild(child);
        }
        return parent;
    }

    /**
     * Final step: call once per finished Document
     */
    public static String toXmlString(Document doc) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.toString();
        } catch (TransformerException e) {
            throw new IllegalStateException("Cannot serialize XML document", e);
        }
    }
}
