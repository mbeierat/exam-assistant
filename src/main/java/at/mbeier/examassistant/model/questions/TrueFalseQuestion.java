package at.mbeier.examassistant.model.questions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Arrays;

public class TrueFalseQuestion extends Question {

    private final boolean correct;

    public TrueFalseQuestion(String title, String question, String points, String[] answers) {
        super(title, question, points);
        if (answers.length != 1) {
            throw new IllegalArgumentException("Answer values for True/False questions must exactly be 1, namely true (1) or false (0), but was " + Arrays.toString(answers) + '(' + answers.length + ')');
        }
        boolean temp;
        try {
            int iCorrect = Integer.parseInt(answers[0]);
            temp = iCorrect == 1;
        } catch (NumberFormatException e) {
            temp = Boolean.parseBoolean(answers[0]);
        }
        this.correct = temp;
    }

    public boolean getCorrectAnswer() {
        return this.correct;
    }

    @Override
    public String toGIFTString() {
        return super.toGIFTString() + '{' + (this.correct + "").toUpperCase() + '}' ;
    }

    @Override
    public void appendXMLElements(Document doc, Element parent) {
        Element question = doc.createElement("question");
        question.setAttribute("type", "truefalse");
        parent.appendChild(question);
        super.appendXMLElements(doc, question);
        new Answer(this.correct + "", Percentage.P100).appendXMLElements(doc, question);
        new Answer((!this.correct) + "", Percentage.P0).appendXMLElements(doc, question);
    }

    @Override
    public String toString() {
        return "TrueFalseQuestion{" + super.toString() +
                ", correct=" + correct +
                '}';
    }
}
