package at.mbeier.examassistant.model.questions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class AllOrNothingMCQuestion extends Question {

    private final List<Answer> answers;

    public AllOrNothingMCQuestion(String title, String question, String points, String[] answerInput) {
        super(title, question, points);
        this.answers = new ArrayList<>();
        // Checking for correcting values and whether true/false or weighted values are used.
        if (answerInput.length % 2 != 0) {
            throw new IllegalArgumentException("Answers must have an even number of arguments, as every answer needs to indicate a weight or whether it is true or false");
        }
        for (int i = 1; i < answerInput.length; i+=2)
            if (!Answer.isValidTrueFalseOption(answerInput[i]))
                throw new IllegalArgumentException("Answer " + answerInput[i] + " is not a valid true/false option");
        // Creating answer list
        for (int i = 0; i < answerInput.length; i+=2) {
            if (Answer.isOptionTrue(answerInput[i+1]))
                this.answers.add(new Answer(answerInput[i], Percentage.P100));
            else if (Answer.isOptionFalse(answerInput[i+1]))
                this.answers.add(new Answer(answerInput[i], Percentage.P0));
        }
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    @Override
    public String toGIFTString() {
        return "// All Or Nothing MC Questions (" + super.getTitle() + ") are not supported in this format";
    }

    @Override
    public void appendXMLElements(Document doc, Element parent) {
        Element question = doc.createElement("question");
        question.setAttribute("type", "multichoiceset");
        parent.appendChild(question);
        super.appendXMLElements(doc, question);
        this.answers.forEach(answer -> answer.appendXMLElements(doc, question));
        Element single = doc.createElement("single");
        single.setNodeValue("false");
        question.appendChild(single);
        Element shuffleanswers = doc.createElement("shuffleanswers");
        shuffleanswers.setNodeValue("1");
        question.appendChild(shuffleanswers);
        Element answernumbering = doc.createElement("answernumbering");
        answernumbering.setNodeValue("abc");
        question.appendChild(answernumbering);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("AllOrNothingMCQuestion{").append("answers=[");
        answers.forEach(answer -> sb.append(answer.toString()).append(","));
        return sb.substring(0, sb.length()-1) + "]}";
    }
}
