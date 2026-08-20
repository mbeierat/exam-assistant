package at.mbeier.exass.model.questions;

import at.mbeier.exass.TestSupport;
import at.mbeier.exass.excel.ExcelRow;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrueFalseTest {

    @Test
    void parsesBooleanCorrectCell() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Wahr/Falsch", "Q1", "Is the sky blue?", 1, true);

        TrueFalse question = new TrueFalse();
        question.createFrom(row);

        assertEquals("Q1", question.getTitle());
        assertEquals(1, question.getPoints());
        assertTrue(question.toGIFTString().endsWith("{T}"));
    }

    @Test
    void parsesNumericCorrectCell() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Wahr/Falsch", "Q1", "Is the sky blue?", 1, 0);

        TrueFalse question = new TrueFalse();
        question.createFrom(row);

        assertTrue(question.toGIFTString().endsWith("{F}"));
    }

    @Test
    void badPointsColumnThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Wahr/Falsch", "Q1", "Is the sky blue?", "not a number", true);

        TrueFalse question = new TrueFalse();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("Points"));
    }
}
