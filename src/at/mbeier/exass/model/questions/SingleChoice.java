package at.mbeier.exass.model.questions;

import at.mbeier.exass.excel.ExcelCell;
import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.excel.ImportUtil;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionType;
import org.apache.poi.ss.usermodel.CellType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class SingleChoice extends Question {

    private List<MultipleChoiceAnswer> answers;

    public SingleChoice() {
        super();
        this.answers = new ArrayList<>();
    }

    public SingleChoice(String title, String text, int points) {
        super(QuestionType.SINGLE_CHOICE, title, text, points);
        this.answers = new ArrayList<>();
    }

    @Override
    public void createFrom(ExcelRow row) {
        ImportUtil.setStandardQuestionFields(this, row);
        int amountCorrect = 0;
        for (int i = 5; i < row.getCells().size(); i += 2) {
            if (row.getCells().get(i).getType() != CellType.STRING)
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Text) needs to be a string");
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
                if (!((int) correctCell.getContent() == 1 || (int) correctCell.getContent() == 0))
                    throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Correct) needs to be a number (or a boolean) being 1 or 0 as in true or false");
                correct = ((int) correctCell.getContent()) == 1;
            }
            this.answers.add(new MultipleChoiceAnswer(answertext, correct, null));
            amountCorrect++;
        }
        if (amountCorrect != 1)
            throw new IllegalArgumentException("A single choice question must have precisely one correct answer");
    }

    @Override
    public String toGIFTString() {
        StringBuilder builder = new StringBuilder("::" + super.getTitle() + "::" +
                "[html]" + super.getText() + "{");
        for (MultipleChoiceAnswer answer : this.answers) {
            builder.append(answer.isCorrect() ? "=" : "~").append(answer.getText());
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public Element toXMLElement(Document doc) {
        Element question = XMLUtil.question(doc, "multichoice");
        XMLUtil.append(question,
                XMLUtil.moodleText(doc, "name", super.getTitle()),
                XMLUtil.moodleText(doc, "questiontext", "html", super.getText()),
                XMLUtil.textElement(doc, "defaultgrade", super.getPoints() + ""),
                XMLUtil.textElement(doc, "single", "true"),
                XMLUtil.textElement(doc, "shuffleanswers", "true"),
                XMLUtil.textElement(doc, "showstandardinstruction", "0"),
                XMLUtil.textElement(doc, "answernumbering", "abc"));
        for (MultipleChoiceAnswer answer : this.answers) {
            XMLUtil.append(question,
                    XMLUtil.answer(doc, answer.isCorrect() ? "100" : "0", "html", answer.getText(), "html", ""));
        }
        return question;
    }
}
