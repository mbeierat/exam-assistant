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

class ShortAnswerTest {

    @Test
    void parsesWeightedAnswers() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Kurzantwort", "Q1", "Name a color", 1,
                "Blue", 100, "Green", 50);

        ShortAnswer question = new ShortAnswer();
        question.createFrom(row);

        String gift = question.toGIFTString();
        assertTrue(gift.contains("=%100%Blue"));
        assertTrue(gift.contains("=%50%Green"));
    }

    @Test
    void toXMLElementDiscardsEverythingItBuilt() {
        // Just like FlashCard, toXMLElement here builds up "question" with
        // the standard fields and every answer, but returns a fresh bare
        // <question> element instead - none of that content is exported.
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Kurzantwort", "Q1", "Name a color", 1,
                "Blue", 100);

        ShortAnswer question = new ShortAnswer();
        question.createFrom(row);
        question.setConfig(new QuestionConfiguration());

        Document doc = XMLUtil.newDocument();
        Element element = question.toXMLElement(doc);

        assertEquals("question", element.getTagName());
        assertEquals("", element.getAttribute("type"));
        assertEquals(0, element.getChildNodes().getLength());
    }

    @Test
    void invalidWeightValueThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Kurzantwort", "Q1", "Name a color", 1,
                "Blue", 7);

        ShortAnswer question = new ShortAnswer();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("does not have a valid weight"));
    }

    @Test
    void missingWeightCellThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Kurzantwort", "Q1", "Name a color", 1,
                "Blue");

        ShortAnswer question = new ShortAnswer();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("Answer Correct"));
    }
}
