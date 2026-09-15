package at.mbeier.exass.model.questions;

import at.mbeier.exass.TestSupport;
import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.exporter.XMLUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClozeTest {

    @Test
    void parsesTitleAndText() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Lückentext", "Q1", "Fill in the {gap}");

        Cloze question = new Cloze();
        question.createFrom(row);

        assertEquals("Q1", question.getTitle());
        assertEquals("Fill in the {gap}", question.getText());
    }

    @Test
    void toXMLElementContainsNameAndText() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Lückentext", "Q1", "Fill in the gap");

        Cloze question = new Cloze();
        question.createFrom(row);

        Document doc = XMLUtil.newDocument();
        Element element = question.toXMLElement(doc);
        assertEquals("cloze", element.getAttribute("type"));
    }

    @Test
    void toGIFTStringIsUnsupported() {
        Cloze question = new Cloze("Q1", "Fill in the gap");
        assertThrows(UnsupportedOperationException.class, question::toGIFTString);
    }

    @Test
    void nonStringNameThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Lückentext", 42, "Some text");

        Cloze question = new Cloze();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("Question Name"));
    }

    @Test
    void nonStringTextThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Lückentext", "Q1", 42);

        Cloze question = new Cloze();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("Question Text"));
    }
}
