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

public class MultipleChoice extends Question {

    private List<MultipleChoiceAnswer> answers;

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
                        ((int) firstCorrect.getContent() == 1 || (int) firstCorrect.getContent() == 0))) {
            mode = InputMode.TRUE_FALSE;
        } else {
            mode = InputMode.WEIGHTED;
        }
        int total = 0;
        int amountCorrect = 0;
        for (int i = 5; i < row.getCells().size(); i += 2) {
            if (row.getCells().get(i).getType() != CellType.STRING)
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Text) needs to be a string");
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
                    if (!((int) correctCell.getContent() == 1 || (int) correctCell.getContent() == 0))
                        throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Correct) needs to be a number (or a boolean) being 1 or 0 as in true/false mode");
                    correct = ((int) correctCell.getContent()) == 1;
                }
            } else {
                if (correctCell.getType() != CellType.NUMERIC)
                    throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Correct) needs to be a number, as question is in weighted input mode");
                int value = (int) correctCell.getContent();
                correct = value < 0;
                weight = AnswerWeight.parse(Math.abs(value));
                if (weight == null)
                    throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Correct) does not have a valid weight");
            }
            
            MultipleChoiceAnswer answer = (mode == InputMode.WEIGHTED) ?
                    new MultipleChoiceAnswer(answertext, correct, weight) :
                    new MultipleChoiceAnswer(answertext, correct);
            this.answers.add(answer);
            total++;
            amountCorrect += (correct ? 1 : 0);
        }
        if (amountCorrect < 1)
            throw new IllegalArgumentException("A multiple choice question must have at least one correct answer");
        double totalWeight = 0.0;
        AnswerWeight weight = AnswerWeight.calculate(total, amountCorrect);
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
            builder.append("~%").
                    append(answer.isCorrect() ? "" : "-").
                    append(answer.getWeight().getRepresentation()).
                    append("%").
                    append(answer.getText());
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
                 XMLUtil.textElement(doc, "single", "false"),
                 XMLUtil.textElement(doc, "shuffleanswers", "true"),
                 XMLUtil.textElement(doc, "showstandardinstruction", "0"),
                 XMLUtil.textElement(doc, "answernumbering", "abc"));
        for (MultipleChoiceAnswer answer : this.answers) {
            XMLUtil.append(question,
                    XMLUtil.answer(doc, (answer.isCorrect() ? "" : "-") + answer.getWeight().getRepresentation(), "html", answer.getText(), "html", ""));
        }
        return question;
    }
}

class MultipleChoiceAnswer {
    private final String text;
    private final boolean correct;
    private AnswerWeight weight;

    public MultipleChoiceAnswer(String text, boolean correct) {
        this(text, correct, null);
    }
    
    public MultipleChoiceAnswer(String text, boolean correct, AnswerWeight weight) {
        this.text = text;
        this.correct = correct;
        this.weight = weight;
    }

    public void setWeight(AnswerWeight weight) {
        this.weight = weight;
    }

    public String getText() {
        return this.text;
    }

    public boolean isCorrect() {
        return this.correct;
    }

    public AnswerWeight getWeight() {
        return this.weight;
    }
}

enum AnswerWeight {
    P100("100", 100.0),
    P90("90", 90.0),
    P83("83.33333", 100.0*5.0/6.0),
    P80("80", 80.0),
    P75("75", 75.0),
    P70("70", 70.0),
    P66("66.66667", 100.0*2.0/3.0),
    P60("60", 60.0),
    P50("50", 50.0),
    P40("40", 40.0),
    P33("33.33333", 100.0/3.0),
    P30("30", 30.0),
    P25("25", 25.0),
    P20("20", 20.0),
    P16("16.66667", 100.0/6.0),
    P14("14.28571", 100.0/7.0),
    P12("12.5", 12.5),
    P11("11.11111", 100.0/9.0),
    P10("10", 10.0),
    P5("5",5.0),
    P0("", 0.0);

    private final String representation;
    private final double value;

    AnswerWeight(String representation, double value) {
        this.representation = representation;
        this.value = value;
    }


    public double getValue() {
        return this.value;
    }

    public String getRepresentation() {
        return this.representation;
    }

    public static AnswerWeight calculate(int total, int amountCorrect) {
        return AnswerWeight.parse((double) amountCorrect / (double) total * 100.0);
    }

    public static AnswerWeight parse(double value) {
        for (AnswerWeight weight : AnswerWeight.values()) {
            if (weight.value == value) {
                return weight;
            }
        }
        return null;
    }
}

enum InputMode {
    TRUE_FALSE, WEIGHTED;
}