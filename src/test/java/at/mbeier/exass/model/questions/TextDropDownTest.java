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

class TextDropDownTest {

    @Test
    void parsesAnswerGroupsAndBuildsXmlElement() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Lückentextauswahl", "Q1", "Pick [[1]] and [[2]]", 1,
                "A", "A", "B", 2);

        TextDropDown question = new TextDropDown();
        question.createFrom(row);
        question.setConfig(new QuestionConfiguration());

        Document doc = XMLUtil.newDocument();
        Element element = question.toXMLElement(doc);
        doc.appendChild(element);

        assertEquals("gapselect", element.getAttribute("type"));
        String xml = XMLUtil.toXMLString(doc);
        assertTrue(xml.contains("selectoption"));
        assertTrue(xml.contains(">A<") || xml.contains("A"));
        assertTrue(xml.contains(">B<") || xml.contains("B"));
    }

    @Test
    void toGIFTStringIsUnsupported() {
        TextDropDown question = new TextDropDown("Q1", "Text", 1);
        assertThrows(UnsupportedOperationException.class, question::toGIFTString);
    }

    @Test
    void placeholderForOneMoreAnswerThanProvidedThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Lückentextauswahl", "Q1", "Pick [[1]] and [[2]]", 1,
                "A", "A");

        TextDropDown question = new TextDropDown();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("as many answers as placeholders"));
    }

    @Test
    void invalidGroupTypeThrows() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0,
                "Chapter1", "Lückentextauswahl", "Q1", "Pick [[1]]", 1,
                "A", true);

        TextDropDown question = new TextDropDown();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> question.createFrom(row));
        assertTrue(ex.getMessage().contains("Group"));
    }
}
