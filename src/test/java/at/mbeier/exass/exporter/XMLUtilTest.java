package at.mbeier.exass.exporter;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XMLUtilTest {

    @Test
    void textElementEscapesSpecialCharacters() {
        Document doc = XMLUtil.newDocument();
        Element element = XMLUtil.textElement(doc, "name", "<b>Tom & Jerry</b>");
        doc.appendChild(element);

        String xml = XMLUtil.toXMLString(doc);
        assertTrue(xml.contains("&lt;b&gt;"));
        assertTrue(xml.contains("&amp;"));
        assertFalse(xml.contains("<b>Tom"));
    }

    @Test
    void moodleTextWithoutFormatUsesPlainTextNode() {
        Document doc = XMLUtil.newDocument();
        Element element = XMLUtil.moodleText(doc, "name", "Plain text");
        doc.appendChild(element);

        String xml = XMLUtil.toXMLString(doc);
        assertFalse(xml.contains("format="));
        assertFalse(xml.contains("CDATA"));
        assertTrue(xml.contains("Plain text"));
    }

    @Test
    void moodleTextWithHtmlFormatWrapsNonEmptyContentInCdata() {
        Document doc = XMLUtil.newDocument();
        Element element = XMLUtil.moodleText(doc, "questiontext", "html", "<p>Hi</p>");
        doc.appendChild(element);

        String xml = XMLUtil.toXMLString(doc);
        assertTrue(xml.contains("format=\"html\""));
        assertTrue(xml.contains("<![CDATA["));
        assertTrue(xml.contains("<p>Hi</p>"));
    }

    @Test
    void moodleTextWithHtmlFormatAndEmptyContentSkipsCdata() {
        Document doc = XMLUtil.newDocument();
        Element element = XMLUtil.moodleText(doc, "questiontext", "html", "");
        doc.appendChild(element);

        assertFalse(XMLUtil.toXMLString(doc).contains("CDATA"));
    }

    @Test
    void questionSetsTheTypeAttribute() {
        Document doc = XMLUtil.newDocument();
        Element element = XMLUtil.question(doc, "truefalse");
        assertEquals("truefalse", element.getAttribute("type"));
    }

    @Test
    void appendAttachesChildrenInGivenOrder() {
        Document doc = XMLUtil.newDocument();
        Element parent = XMLUtil.element(doc, "parent");
        Element first = XMLUtil.element(doc, "first");
        Element second = XMLUtil.element(doc, "second");

        XMLUtil.append(parent, first, second);

        assertEquals(2, parent.getChildNodes().getLength());
        assertEquals("first", parent.getChildNodes().item(0).getNodeName());
        assertEquals("second", parent.getChildNodes().item(1).getNodeName());
    }

    @Test
    void answerSetsFractionAndNestsFeedback() {
        Document doc = XMLUtil.newDocument();
        Element answer = XMLUtil.answer(doc, "100", "html", "Correct", "html", "Well done");
        doc.appendChild(answer);

        assertEquals("100", answer.getAttribute("fraction"));
        String xml = XMLUtil.toXMLString(doc);
        assertTrue(xml.contains("Correct"));
        assertTrue(xml.contains("Well done"));
    }
}
