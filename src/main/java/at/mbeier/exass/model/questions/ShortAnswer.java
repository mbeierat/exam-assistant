package at.mbeier.exass.model.questions;

import at.mbeier.exass.excel.ExcelCell;
import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.excel.ImportUtil;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.AnswerWeight;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionType;
import org.apache.poi.ss.usermodel.CellType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class ShortAnswer extends Question {

    private final List<WeightedShortAnswer> answers;

    public ShortAnswer() {
        super();
        this.answers = new ArrayList<>();
    }

    public ShortAnswer(String title, String text, int points) {
        super(QuestionType.SHORT_ANSWER, title, text, points);
        this.answers = new ArrayList<>();
    }

    @Override
    public void createFrom(ExcelRow row) {
        ImportUtil.setStandardQuestionFields(this, row);
        for (int i = 5; i < row.getCells().size(); i += 2) {
            if (row.getCells().get(i).getType() != CellType.STRING)
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Text) needs to be a string");
            String answertext = (String) row.getCells().get(i).getContent();
            AnswerWeight weight;
            ExcelCell correctCell;
            try {
                correctCell = row.getCells().get(i + 1);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Correct) needs to have a value");
            }
            if (correctCell.getType() != CellType.NUMERIC)
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Correct) needs to be a number, as question is in weighted input mode");
            int value = ((Number) correctCell.getContent()).intValue();
            weight = AnswerWeight.parse(value);
            if (weight == null)
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Correct) does not have a valid weight");
            this.answers.add(new WeightedShortAnswer(answertext, weight));
        }
    }

    @Override
    public String toGIFTString() {
        StringBuilder builder = new StringBuilder("::" + super.getTitle() + "::" +
                "[html]" + super.getText() + "{");
        for (WeightedShortAnswer wsa : this.answers) {
            builder.append("=%")
                    .append(wsa.weight().getRepresentation())
                    .append("%")
                    .append(wsa.answer());
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public Element toXMLElement(Document doc) {
        Element question = XMLUtil.question(doc, "shortanswer");
        XMLUtil.append(question,
                XMLUtil.moodleText(doc, "name", super.getTitle()),
                XMLUtil.moodleText(doc, "questiontext", "html", super.getText()),
                XMLUtil.textElement(doc, "defaultgrade", super.getPoints() + ""),
                XMLUtil.textElement(doc, "usecase", "0"));
        for (WeightedShortAnswer answer : this.answers) {
            XMLUtil.append(question, XMLUtil.answer(doc, answer.weight().getRepresentation(), null, answer.answer(), "html", ""));
        }
        return XMLUtil.element(doc, "question");
    }
}

record WeightedShortAnswer(String answer, AnswerWeight weight) {}