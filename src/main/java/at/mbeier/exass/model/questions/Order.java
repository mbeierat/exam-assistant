package at.mbeier.exass.model.questions;

import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.excel.ImportUtil;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionProperty;
import at.mbeier.exass.model.QuestionType;
import org.apache.poi.ss.usermodel.CellType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class Order extends Question {

    private final List<String> answers;

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
        for (int i = 5; i < row.getCells().size(); i++) {
            if (row.getCells().get(i).getType() != CellType.STRING)
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer) needs to be a string");
            this.answers.add((String) row.getCells().get(i).getContent());
        }
    }

    @Override
    public String toGIFTString() {
        StringBuilder builder = new StringBuilder("::" + super.getTitle() + "::" +
                "[html]" + super.getText() + "{" +
                ">" + super.getConfig().get(QuestionProperty.SELECT_COUNT) + " " +
                super.getConfig().get(QuestionProperty.SELECT_TYPE) + " " +
                super.getConfig().get(QuestionProperty.LAYOUT_TYPE) + " " +
                super.getConfig().get(QuestionProperty.GRADING_TYPE) + " " +
                super.getConfig().get(QuestionProperty.SHOW_GRADING) + " " +
                super.getConfig().get(QuestionProperty.NUMBERING_STYLE) + "\n");
        for (String answer : this.answers) {
            builder.append(answer).append("\n");
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public Element toXMLElement(Document doc) {
        Element question = XMLUtil.question(doc, "ordering");
        super.appendStandardXMLChilds(doc, question);
        for (int i = 0; i < answers.size(); i++) {
            XMLUtil.append(question,
                    XMLUtil.answer(doc, i + "", "html", answers.get(i), "html", ""));
        }
        return question;
    }
}
