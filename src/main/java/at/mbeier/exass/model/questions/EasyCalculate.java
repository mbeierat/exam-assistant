package at.mbeier.exass.model.questions;

import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EasyCalculate extends Question {

    public EasyCalculate() {
        super();
    }

    public EasyCalculate(String name, String text, int points) {
        super(QuestionType.EASY_CALCULATE, name, text, points);
    }

    @Override
    public void createFrom(ExcelRow row) {
        throw new UnsupportedOperationException("This question type is not supported yet.");
    }

    @Override
    public String toGIFTString() {
        throw new UnsupportedOperationException("This question type is not supported yet.");
    }

    @Override
    public Element toXMLElement(Document doc) {
        throw new UnsupportedOperationException("This question type is not supported yet.");
    }
}
