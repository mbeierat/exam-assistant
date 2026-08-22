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

public class AllOrNothingMultipleChoice extends Question {

    private final List<MultipleChoiceAnswer> answers;

    public AllOrNothingMultipleChoice() {
        super();
        this.answers = new ArrayList<>();
    }

    public AllOrNothingMultipleChoice(String name, String text, int points) {
        super(QuestionType.ALL_OR_NOTHING_MULTI_CHOICE, name, text, points);
        this.answers = new ArrayList<>();
    }

    @Override
    public void createFrom(ExcelRow row) {
        ImportUtil.setStandardQuestionFields(this, row);
        int amountCorrect = 0;
        for (int i = 5; i < row.getCells().size(); i += 2) {
            if (row.getCells().get(i).getType() != CellType.STRING)
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + i + " (Answer Text) needs to be a string");
            String answertext = (String) row.getCells().get(i).getContent();
            boolean correct = false;
            ExcelCell correctCell;
            try {
                correctCell = row.getCells().get(i + 1);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Correct) needs to have a value");
            }
            if (correctCell.getType() == CellType.BOOLEAN) {
                correct = (boolean) correctCell.getContent();
            } else if (correctCell.getType() == CellType.NUMERIC) {
                if (!(((Number) correctCell.getContent()).intValue() == 1 || ((Number) correctCell.getContent()).intValue() == 0))
                    throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Correct) needs to be a number (or a boolean) being 1 or 0 as in true/false mode");
                correct = ((Number) correctCell.getContent()).intValue() == 1;
            }
            MultipleChoiceAnswer answer = new MultipleChoiceAnswer(answertext, correct, correct ? AnswerWeight.P100 : AnswerWeight.P0);
            this.answers.add(answer);
            amountCorrect += (correct ? 1 : 0);
        }
        if (amountCorrect < 1)
            throw new IllegalArgumentException("A multiple choice question must have at least one correct answer");
    }

    @Override
    public String toGIFTString() {
        throw new UnsupportedOperationException("The Question Type " + super.getType().getId() + " is not supported in the GIFT-Format");
    }

    @Override
    public Element toXMLElement(Document doc) {
        Element question = XMLUtil.question(doc,"multichoiceset");
        XMLUtil.append(question,
                XMLUtil.moodleText(doc, "name", super.getTitle()),
                XMLUtil.moodleText(doc, "questiontext", "html", super.getText()),
                XMLUtil.textElement(doc, "defaultgrade", super.getPoints() + ""),
                XMLUtil.textElement(doc, "shuffleanswers", "true"),
                XMLUtil.textElement(doc, "showstandardinstruction", "0"),
                XMLUtil.textElement(doc, "answernumbering", "abc"));
        for (MultipleChoiceAnswer answer : this.answers) {
            XMLUtil.append(question,
                    XMLUtil.answer(doc, answer.getWeight().getXMLRepresentation(), "html", answer.getText(), "html", ""));
        }
        return question;
    }
}
