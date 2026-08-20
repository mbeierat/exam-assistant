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
 * whole category export and calls toXMLString(doc) exactly once, after every
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
     * The <question type="..."> root every export fragment starts from.
     */
    public static Element question(Document doc, String type) {
        return attr(element(doc, "question"), "type", type);
    }

    /**
     * Moodle's near-universal "<tag><text>...</text></tag>" shape - used for
     * <name>, <questiontext>, <generalfeedback>, per-answer <feedback>, etc.
     * No format attribute, plain text content. Use this overload for fields
     * Moodle never treats as HTML, like <name>.
     */
    public static Element moodleText(Document doc, String tag, String content) {
        return moodleText(doc, tag, null, content);
    }

    /**
     * Same wrapper shape, but with a format="..." attribute, and - when
     * format is "html" and the content isn't empty - the text wrapped in a
     * CDATA section instead of a plain text node. That's what lets HTML
     * markup in question/feedback text come through as real markup instead
     * of escaped entities, matching how Moodle itself exports these fields.
     * Empty content is left as a plain empty <text/>, since that's what a
     * real Moodle export does too (no point CDATA-wrapping nothing).
     */
    public static Element moodleText(Document doc, String tag, String format, String content) {
        Element el = element(doc, tag);
        if (format != null) {
            attr(el, "format", format);
        }
        Element textEl = element(doc, "text");
        String value = content == null ? "" : content;
        if ("html".equals(format) && !value.isEmpty()) {
            textEl.appendChild(doc.createCDATASection(value));
        } else {
            textEl.appendChild(doc.createTextNode(value));
        }
        el.appendChild(textEl);
        return el;
    }

    /**
     * A Moodle <answer>: the moodleText wrapper shape plus the required
     * fraction attribute and a nested <feedback> (itself the same shape
     * again).
     */
    public static Element answer(Document doc, String weight, String format, String text,
                                  String feedbackFormat, String feedbackText) {
        Element answerEl = moodleText(doc, "answer", format, text);
        attr(answerEl, "fraction", weight);
        answerEl.appendChild(moodleText(doc, "feedback", feedbackFormat, feedbackText));
        return answerEl;
    }

    /**
     * Final step: call once per finished Document
     */
    public static String toXMLString(Document doc) {
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
