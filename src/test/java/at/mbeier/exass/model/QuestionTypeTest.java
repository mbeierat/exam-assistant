package at.mbeier.exass.model;

import at.mbeier.exass.TestSupport;
import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.model.questions.TrueFalse;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionTypeTest {

    @Test
    void fromIdResolvesCaseInsensitivelyAndTrimmed() {
        assertEquals(QuestionType.TRUE_FALSE, QuestionType.fromId(" wahr/falsch "));
        assertEquals(QuestionType.TRUE_FALSE, QuestionType.fromId("WAHR/FALSCH"));
    }

    @Test
    void fromIdThrowsOnUnknownId() {
        assertThrows(IllegalArgumentException.class, () -> QuestionType.fromId("not-a-real-type"));
    }

    @Test
    void allConstantsHaveUniqueIds() {
        Set<String> ids = new HashSet<>();
        for (QuestionType type : QuestionType.values()) {
            assertTrue(ids.add(type.getId()), "Duplicate id '" + type.getId() + "' on " + type);
        }
    }

    @Test
    void parseInstantiatesTheRightSubclassAndSetsType() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Wahr/Falsch", "Q1", "Is the sky blue?", 1, true);

        Question question = QuestionType.TRUE_FALSE.parse(row);

        assertInstanceOf(TrueFalse.class, question);
        assertEquals(QuestionType.TRUE_FALSE, question.getType());
        assertEquals("Q1", question.getTitle());
    }
}
