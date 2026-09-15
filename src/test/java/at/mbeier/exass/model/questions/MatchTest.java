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

class MatchTest {

    @Test
    void parsesThreeFullyMatchedPairs() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Zuordnen", "Q1", "Match them", 1,
                "P1", "A1", "P2", "A2", "P3", "A3");

        Match question = new Match();
        question.createFrom(row);

        String gift = question.toGIFTString();
        assertTrue(gift.contains("=P1 -> A1"));
        assertTrue(gift.contains("=P2 -> A2"));
        assertTrue(gift.contains("=P3 -> A3"));
    }

    @Test
    void toXMLElementContainsSubquestions() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Zuordnen", "Q1", "Match them", 1,
                "P1", "A1", "P2", "A2", "P3", "A3");

        Match question = new Match();
        question.createFrom(row);
        question.setConfig(new QuestionConfiguration());

        Document doc = XMLUtil.newDocument();
        Element element = question.toXMLElement(doc);
        doc.appendChild(element);

        assertEquals("matching", element.getAttribute("type"));
        String xml = XMLUtil.toXMLString(doc);
        assertTrue(xml.contains("A1"));
        assertTrue(xml.contains("A3"));
    }

    @Test
    void blankPromptForUnmatchedAnswerIsSilentlyLostInstead() {
        // A blank prompt cell is meant to introduce an extra, unmatched
        // answer (see the error message below). But ExcelRow (see
        // ExcelRowTest) never actually produces a BLANK cell - it silently
        // truncates the row at the first blank cell instead. So the third
        // pair here (the intended unmatched "A3") never even makes it into
        // row.getCells(), and createFrom's loop bound (which reads up to
        // row.getCells().size()) just stops one pair early instead of
        // failing loudly - leaving only 2 prompts/2 answers, which then
        // fails the "two prompts, three answers" check for an unrelated
        // reason: not because too little was provided, but because a whole
        // answer silently disappeared.
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Zuordnen", "Q1", "Match them", 1,
                "P1", "A1", "P2", "A2", null, "A3");

        Match question = new Match();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("at least two prompts and three answers"));
    }

    @Test
    void tooFewPromptsAndAnswersThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Zuordnen", "Q1", "Match them", 1,
                "P1", "A1", "P2", "A2");

        Match question = new Match();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("at least two prompts and three answers"));
    }

    @Test
    void nonStringAnswerTextThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Zuordnen", "Q1", "Match them", 1,
                "P1", 42);

        Match question = new Match();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("Answer Text"));
    }
}
