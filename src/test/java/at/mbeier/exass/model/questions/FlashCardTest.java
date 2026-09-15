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

class FlashCardTest {

    @Test
    void parsesAnswerText() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Lernkarte", "Q1", "What is 2+2?", 1,
                "4");

        FlashCard question = new FlashCard();
        question.createFrom(row);

        assertEquals("Q1", question.getTitle());
    }

    @Test
    void toXMLElementDiscardsEverythingItBuilt() {
        // toXMLElement builds up the "flashcard" question element with the
        // standard fields and the answer, but then returns a brand new bare
        // <question> element instead of the one it just built - none of that
        // content ever makes it into the export.
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Lernkarte", "Q1", "What is 2+2?", 1,
                "4");

        FlashCard question = new FlashCard();
        question.createFrom(row);
        question.setConfig(new QuestionConfiguration());

        Document doc = XMLUtil.newDocument();
        Element element = question.toXMLElement(doc);

        assertEquals("question", element.getTagName());
        assertEquals("", element.getAttribute("type"));
        assertEquals(0, element.getChildNodes().getLength());
    }

    @Test
    void toGIFTStringIsUnsupported() {
        FlashCard question = new FlashCard("Q1", "Text", 1);
        assertThrows(UnsupportedOperationException.class, question::toGIFTString);
    }

    @Test
    void nonStringAnswerThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Lernkarte", "Q1", "What is 2+2?", 1,
                4);

        FlashCard question = new FlashCard();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("Answer Text"));
    }
}
