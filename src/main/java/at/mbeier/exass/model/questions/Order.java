package at.mbeier.exass.model.questions;

import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.excel.ImportUtil;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class Order extends Question {

    private List<String> answers;

    public Order() {
        super();
        this.answers = new ArrayList<>();
    }

    public Order(String name, String text, int points) {
        super(QuestionType.ORDER, name, text, points);
        this.answers = new ArrayList<>();
    }

    @Override
    public void createFrom(ExcelRow row) {
        ImportUtil.setStandardQuestionFields(this, row);

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
