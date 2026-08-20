package at.mbeier.exass.model.questions;

import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TextDropDown extends Question {

    public TextDropDown() {
        super();
    }

    public TextDropDown(String name, String text, int points) {
        super(QuestionType.TEXT_DROP_DOWN, name, text, points);
    }

    @Override
    public void createFrom(ExcelRow row) {
    }

    @Override
    public String toGIFTString() {
        return "";
    }

    @Override
    public Element toXMLElement(Document doc) {
        return XMLUtil.element(doc, "question");
    }
}
