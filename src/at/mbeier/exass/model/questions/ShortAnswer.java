package at.mbeier.exass.model.questions;

import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ShortAnswer extends Question {

    public ShortAnswer() {
        super();
    }

    public ShortAnswer(String name, String text, int points) {
        super(QuestionType.SHORT_ANSWER, name, text, points);
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
