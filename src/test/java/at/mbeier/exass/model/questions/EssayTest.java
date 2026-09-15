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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EssayTest {

    @Test
    void parsesAllOptionalFields() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Freitext", "Q1", "Explain yourself", 5,
                10, 100, "Grade generously", "Dear student,");

        Essay question = new Essay();
        question.createFrom(row);
        question.setConfig(new QuestionConfiguration());

        Document doc = XMLUtil.newDocument();
        Element element = question.toXMLElement(doc);
        doc.appendChild(element);

        String xml = XMLUtil.toXMLString(doc);
        assertTrue(xml.contains("10"));
        assertTrue(xml.contains("100"));
        assertTrue(xml.contains("Grade generously"));
        assertTrue(xml.contains("Dear student,"));
    }

    @Test
    void omittedOptionalFieldsThrowInsteadOfDefaulting() {
        // Essay's optional columns are documented as "number or blank", but
        // ExcelRow (see ExcelRowTest) never actually produces a BLANK cell -
        // it silently truncates the row at the first blank/missing cell
        // instead. So a row missing column 6 (Minimum Word Amount) blows up
        // with an uncaught IndexOutOfBoundsException rather than defaulting
        // minWords to -1.
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Freitext", "Q1", "Explain yourself", 5);

        Essay question = new Essay();
        assertThrows(IndexOutOfBoundsException.class, () -> question.createFrom(row));
    }

    @Test
    void toGIFTStringContainsTitleAndText() {
        Essay question = new Essay("Q1", "Explain yourself", 5);
        assertTrue(question.toGIFTString().contains("Q1"));
        assertTrue(question.toGIFTString().contains("Explain yourself"));
    }

    @Test
    void nonNumericMinWordsThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Freitext", "Q1", "Explain yourself", 5,
                "not a number");

        Essay question = new Essay();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("Minimum Word Amount"));
    }
}
