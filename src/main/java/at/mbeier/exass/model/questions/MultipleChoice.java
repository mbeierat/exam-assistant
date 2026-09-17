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

public class MultipleChoice extends Question {

    private final List<MultipleChoiceAnswer> answers;

    public MultipleChoice() {
        super();
        this.answers = new ArrayList<>();
    }

    public MultipleChoice(String title, String text, int points) {
        super(QuestionType.MULTI_CHOICE, title, text, points);
        this.answers = new ArrayList<>();
    }

    @Override
    public void createFrom(ExcelRow row) {
        ImportUtil.setStandardQuestionFields(this, row);
        InputMode mode;
        ExcelCell firstCorrect = row.getCells().get(6);
        if (firstCorrect.getType() == CellType.BOOLEAN ||
                (firstCorrect.getType() == CellType.NUMERIC &&
                        (((Number) firstCorrect.getContent()).intValue() == 1 || ((Number) firstCorrect.getContent()).intValue() == 0))) {
            mode = InputMode.TRUE_FALSE;
        } else {
            mode = InputMode.WEIGHTED;
        }
        int amountCorrect = 0;
        for (int i = 5; i < row.getCells().size(); i += 2) {
            if (row.getCells().get(i).getType() != CellType.STRING)
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + i + " (Answer Text) needs to be a string");
            String answertext = (String) row.getCells().get(i).getContent();
            boolean correct = false;
            AnswerWeight weight = null;
            ExcelCell correctCell;
            try {
                correctCell = row.getCells().get(i + 1);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Correct) needs to have a value");
            }
            if (mode == InputMode.TRUE_FALSE) {
                if (correctCell.getType() == CellType.BOOLEAN) {
                    correct = (boolean) correctCell.getContent();
                } else if (correctCell.getType() == CellType.NUMERIC) {
                    if (!(((Number) correctCell.getContent()).intValue() == 1 || ((Number) correctCell.getContent()).intValue() == 0))
                        throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Correct) needs to be a number (or a boolean) being 1 or 0 as in true/false mode");
                    correct = ((Number) correctCell.getContent()).intValue() == 1;
                }
            } else {
                if (correctCell.getType() != CellType.NUMERIC)
                    throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Correct) needs to be a number, as question is in weighted input mode");
                double value = ((Number) correctCell.getContent()).doubleValue();
                correct = value < 0;
                weight = AnswerWeight.parse(Math.abs(value));
                if (weight == null)
                    throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Correct) does not have a valid weight");
            }
            
            MultipleChoiceAnswer answer = (mode == InputMode.WEIGHTED) ?
                    new MultipleChoiceAnswer(answertext, correct, weight) :
                    new MultipleChoiceAnswer(answertext, correct);
            this.answers.add(answer);
            amountCorrect += (correct ? 1 : 0);
        }
        if (amountCorrect < 1)
            throw new IllegalArgumentException("A multiple choice question must have at least one correct answer");
        double totalWeight = 0.0;
        AnswerWeight weight = AnswerWeight.calculate(this.answers.size(), amountCorrect);
        for (MultipleChoiceAnswer answer : answers) {
            if (mode == InputMode.TRUE_FALSE)
                answer.setWeight(weight);
            totalWeight += answer.isCorrect() ? answer.getWeight().getValue() : 0.0;
        }
        if (Math.round(totalWeight) != 100)
            throw new IllegalArgumentException("Total weight of correct answers must be 100%");
    }

    @Override
    public String toGIFTString() {
        StringBuilder builder = new StringBuilder("::" + super.getTitle() + "::" +
                "[html]" + super.getText() + "{");
        for (MultipleChoiceAnswer answer : this.answers) {
            builder.append("~%")
                    .append(answer.isCorrect() ? "" : "-")
                    .append(answer.getWeight().getRepresentation())
                    .append("%")
                    .append(answer.getText());
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public Element toXMLElement(Document doc) {
        Element question = XMLUtil.question(doc, "multichoice");
        super.appendStandardXMLChilds(doc, question);
        XMLUtil.append(question,
                XMLUtil.textElement(doc, "single", "false"));
        for (MultipleChoiceAnswer answer : this.answers) {
            XMLUtil.append(question,
                    XMLUtil.answer(doc, (answer.isCorrect() ? "" : "-") + answer.getWeight().getXMLRepresentation(), "html", answer.getText(), "html", ""));
        }
        return question;
    }
}

enum InputMode {
    TRUE_FALSE, WEIGHTED
}