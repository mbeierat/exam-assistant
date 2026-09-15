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

class AllOrNothingMultipleChoiceTest {

    @Test
    void parsesAnswersAndBuildsXmlElement() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Alles/Nichts MC", "Q1", "Pick all correct", 1,
                "A", true, "B", false);

        AllOrNothingMultipleChoice question = new AllOrNothingMultipleChoice();
        question.createFrom(row);
        question.setConfig(new QuestionConfiguration());

        Document doc = XMLUtil.newDocument();
        Element element = question.toXMLElement(doc);
        doc.appendChild(element);

        assertEquals("multichoiceset", element.getAttribute("type"));
        String xml = XMLUtil.toXMLString(doc);
        assertTrue(xml.contains("A"));
        assertTrue(xml.contains("B"));
    }

    @Test
    void toGIFTStringIsUnsupported() {
        AllOrNothingMultipleChoice question = new AllOrNothingMultipleChoice("Q1", "Text", 1);
        assertThrows(UnsupportedOperationException.class, question::toGIFTString);
    }

    @Test
    void noCorrectAnswerThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Alles/Nichts MC", "Q1", "Pick all correct", 1,
                "A", false, "B", false);

        AllOrNothingMultipleChoice question = new AllOrNothingMultipleChoice();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("at least one correct answer"));
    }

    @Test
    void nonStringAnswerTextThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Alles/Nichts MC", "Q1", "Pick all correct", 1,
                42, true);

        AllOrNothingMultipleChoice question = new AllOrNothingMultipleChoice();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("Answer Text"));
    }
}
