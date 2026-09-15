package at.mbeier.exass.model.questions;

import at.mbeier.exass.TestSupport;
import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.QuestionConfiguration;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DragNDropTextTest {

    @Test
    void parsesAnswerGroupsAndBuildsXmlElement() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Drag n Drop Text", "Q1", "Drag [[1]] into [[2]]", 1,
                "A", 1, false, "B", 2, true);

        DragNDropText question = new DragNDropText();
        question.createFrom(row);
        question.setConfig(new QuestionConfiguration());

        Document doc = XMLUtil.newDocument();
        Element element = question.toXMLElement(doc);
        doc.appendChild(element);

        assertEquals("ddwtos", element.getAttribute("type"));
        NodeList dragBoxes = element.getElementsByTagName("dragbox");
        assertEquals(2, dragBoxes.getLength());

        Element firstBox = (Element) dragBoxes.item(0);
        assertEquals("1", firstBox.getElementsByTagName("group").item(0).getTextContent());
        assertEquals(0, firstBox.getElementsByTagName("infinite").getLength());

        Element secondBox = (Element) dragBoxes.item(1);
        assertEquals("2", secondBox.getElementsByTagName("group").item(0).getTextContent());
        assertEquals(1, secondBox.getElementsByTagName("infinite").getLength());
    }

    @Test
    void toGIFTStringIsUnsupported() {
        DragNDropText question = new DragNDropText("Q1", "Text", 1);
        assertThrows(UnsupportedOperationException.class, question::toGIFTString);
    }

    @Test
    void placeholderForOneMoreAnswerThanProvidedThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Drag n Drop Text", "Q1", "Drag [[1]] and [[2]]", 1,
                "A", 1, false);

        DragNDropText question = new DragNDropText();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("as many answers as placeholders"));
    }

    @Test
    void letterGroupWithBooleanInfiniteValueParses() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Drag n Drop Text", "Q1", "Drag [[1]]", 1,
                "A", "A", true);

        DragNDropText question = new DragNDropText();
        question.createFrom(row);
        question.setConfig(new QuestionConfiguration());

        Document doc = XMLUtil.newDocument();
        Element element = question.toXMLElement(doc);
        doc.appendChild(element);

        Element dragBox = (Element) element.getElementsByTagName("dragbox").item(0);
        assertEquals("1", dragBox.getElementsByTagName("group").item(0).getTextContent());
        assertEquals(1, dragBox.getElementsByTagName("infinite").getLength());
    }

    @Test
    void letterGroupWithNumericInfiniteValueParses() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Drag n Drop Text", "Q1", "Drag [[1]]", 1,
                "A", "A", 0);

        DragNDropText question = new DragNDropText();
        question.createFrom(row);
        question.setConfig(new QuestionConfiguration());

        Document doc = XMLUtil.newDocument();
        Element element = question.toXMLElement(doc);
        doc.appendChild(element);

        Element dragBox = (Element) element.getElementsByTagName("dragbox").item(0);
        assertEquals(0, dragBox.getElementsByTagName("infinite").getLength());
    }

    @Test
    void nonStringAnswerTextThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Drag n Drop Text", "Q1", "Drag [[1]]", 1,
                42, "1", false);

        DragNDropText question = new DragNDropText();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("Answer Text"));
    }
}
