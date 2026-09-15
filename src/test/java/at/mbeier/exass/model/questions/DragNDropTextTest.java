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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DragNDropTextTest {

    @Test
    void parsesAnswerGroupsAndBuildsXmlElement() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        // Numeric groups, since a letter group (e.g. "A") trips
        // createFrom's "Infinite?" check regardless of the infinite value -
        // see letterGroupAlwaysFailsInfiniteCheck below.
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
        // The per-answer <dragbox> elements are built but never appended to
        // the returned <question> element, so none of that content actually
        // makes it into the exported XML.
        assertFalse(XMLUtil.toXMLString(doc).contains("dragbox"));
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
    void letterGroupAlwaysFailsInfiniteCheck() {
        // createFrom's "Infinite?" validation checks groupCell's type
        // instead of infinteCell's, so a letter-based group (a STRING cell)
        // always fails it, no matter what the infinite cell actually holds.
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Drag n Drop Text", "Q1", "Drag [[1]]", 1,
                "A", "A", true);

        DragNDropText question = new DragNDropText();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("Infinite?"));
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
