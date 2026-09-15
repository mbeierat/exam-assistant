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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest {

    @Test
    void toGIFTStringIncludesDefaultConfigAndAnswers() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Anordnung", "Q1", "Order these", 1,
                "First", "Second", "Third");

        Order question = new Order();
        question.createFrom(row);
        question.setConfig(new QuestionConfiguration());

        String gift = question.toGIFTString();
        assertTrue(gift.contains(">2 ALL VERTICAL ABSOLUTE_POSITION SHOW none"));
        assertTrue(gift.contains("First"));
        assertTrue(gift.contains("Second"));
        assertTrue(gift.contains("Third"));
    }

    @Test
    void toXMLElementContainsIndexedAnswers() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Anordnung", "Q1", "Order these", 1,
                "First", "Second");

        Order question = new Order();
        question.createFrom(row);
        question.setConfig(new QuestionConfiguration());

        Document doc = XMLUtil.newDocument();
        Element element = question.toXMLElement(doc);
        doc.appendChild(element);

        assertEquals("ordering", element.getAttribute("type"));
        String xml = XMLUtil.toXMLString(doc);
        assertTrue(xml.contains("First"));
        assertTrue(xml.contains("Second"));
    }

    @Test
    void nonStringAnswerThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Anordnung", "Q1", "Order these", 1,
                "First", 42);

        Order question = new Order();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("Answer"));
    }
}
