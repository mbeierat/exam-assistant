package at.mbeier.exass.model.questions;

import at.mbeier.exass.TestSupport;
import at.mbeier.exass.excel.ExcelRow;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MultipleChoiceTest {

    @Test
    void trueFalseInputModeSplitsWeightEvenlyAcrossCorrectAnswers() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        // firstCorrect (column 7 / index 6) is a boolean -> true/false input mode
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Multiple Choice", "Q1", "Pick two", 2,
                "A", true, "B", true, "C", false, "D", false);

        MultipleChoice question = new MultipleChoice();
        question.createFrom(row);

        String gift = question.toGIFTString();
        assertTrue(gift.contains("~%50%A"));
        assertTrue(gift.contains("~%50%B"));
        assertTrue(gift.contains("~%-50%C"));
        assertTrue(gift.contains("~%-50%D"));
    }

    @Test
    void weightedInputModeUsesGivenPercentages() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        // firstCorrect (index 6) is numeric but not 1/0 -> weighted input mode.
        // Current parsing treats a NEGATIVE value as "correct" (sign convention
        // mirrors how toGIFTString later prints it, but is worth double-checking
        // against intent since it's the opposite of what GIFT's own %-50% syntax
        // means for an *incorrect* option).
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Multiple Choice", "Q2", "Weighted", 3,
                "A", -100, "B", 100);

        MultipleChoice question = new MultipleChoice();
        question.createFrom(row);

        String gift = question.toGIFTString();
        assertTrue(gift.contains("~%100%A"));
        assertTrue(gift.contains("~%-100%B"));
    }

    @Test
    void nonStringAnswerTextThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Multiple Choice", "Q3", "Bad", 1,
                5, true);

        MultipleChoice question = new MultipleChoice();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("Answer Text"));
    }
}
