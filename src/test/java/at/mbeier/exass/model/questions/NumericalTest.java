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

class NumericalTest {

    @Test
    void withNoAnswersProducesAnEmptyAnswerSet() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Numerisch", "Q1", "What is pi?", 1);

        Numerical question = new Numerical();
        question.createFrom(row);

        assertEquals("::Q1::[html]What is pi?{#}", question.toGIFTString());
    }

    @Test
    void parsesAnswerWithToleranceAndWeight() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Numerisch", "Q1", "What is pi?", 1,
                3, 0, 100);

        Numerical question = new Numerical();
        question.createFrom(row);

        assertTrue(question.toGIFTString().contains("=%100%3.0:0.0#"));
    }

    @Test
    void nonNumericAnswerValueThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Numerisch", "Q1", "What is pi?", 1,
                "not a number", 0, 100);

        Numerical question = new Numerical();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("Answer Value"));
    }

    @Test
    void toXMLElementOnEmptyAnswerSet() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Numerisch", "Q1", "What is pi?", 1);

        Numerical question = new Numerical();
        question.createFrom(row);
        question.setConfig(new QuestionConfiguration());

        Document doc = XMLUtil.newDocument();
        Element element = question.toXMLElement(doc);
        assertEquals("numerical", element.getAttribute("type"));
    }
}
