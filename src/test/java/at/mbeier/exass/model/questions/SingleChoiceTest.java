package at.mbeier.exass.model.questions;

import at.mbeier.exass.TestSupport;
import at.mbeier.exass.excel.ExcelRow;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SingleChoiceTest {

    @Test
    void parsesExactlyOneCorrectAnswer() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Single Choice", "Q1", "Pick the right one", 1,
                "A", false, "B", true, "C", 0);

        SingleChoice question = new SingleChoice();
        question.createFrom(row);

        String gift = question.toGIFTString();
        assertTrue(gift.contains("=B"));
        assertTrue(gift.contains("~A"));
        assertTrue(gift.contains("~C"));
    }

    @Test
    void morethan1CorrectThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Single Choice", "Q1", "Pick the right one", 1,
                "A", true, "B", true);

        SingleChoice question = new SingleChoice();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertEquals("A single choice question must have precisely one correct answer", ex.getMessage());
    }

    @Test
    void zeroCorrectThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Single Choice", "Q1", "Pick the right one", 1,
                "A", false, "B", false);

        SingleChoice question = new SingleChoice();
        assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
    }

    @Test
    void nonStringAnswerTextThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Single Choice", "Q1", "Pick the right one", 1,
                42, true);

        SingleChoice question = new SingleChoice();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("Answer Text"));
    }
}
