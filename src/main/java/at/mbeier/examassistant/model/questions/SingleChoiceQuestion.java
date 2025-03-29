package at.mbeier.examassistant.model.questions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class SingleChoiceQuestion extends Question {

    private final List<Answer> answers;

    public SingleChoiceQuestion(String title, String question, String points, String[] answerInput) {
        super(title, question, points);
        this.answers = new ArrayList<>();
        // Check for correct input variables (only one being correct and true/false fomrat)
        if (answerInput.length % 2 != 0)
            throw new IllegalArgumentException("Answers must have an even number of arguments, as every answer needs to indicate a weight or whether it is true or false");
        for (int i = 1; i < answerInput.length; i+=2)
            if (!Answer.isValidTrueFalseOption(answerInput[i]))
                throw new IllegalArgumentException("Answer " + answerInput[i] + " is not a valid true/false option");
        int correct = 0;
        for (int i = 1; i < answerInput.length; i+=2) {
            if (Answer.isOptionTrue(answerInput[i])) correct++;
        }
        if (correct != 1)
            throw new IllegalArgumentException("Answers of Single Choice Questions must have exactly one correct answer, all other must be false");
        // Creating answer list
        for (int i = 0; i < answerInput.length; i+=2) {
            this.answers.add(new Answer(answerInput[i], (Answer.isOptionTrue(answerInput[i+1]) ? Percentage.P100 : Percentage.M100)));
        }
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    @Override
    public String toGIFTString() {
        StringBuilder builder = new StringBuilder(super.toGIFTString()).append("{");
        this.answers.forEach(answer -> builder.append(answer.toSingleChoiceGIFTString()));
        builder.append('}');
        return builder.toString();
    }

    @Override
    public void appendXMLElements(Document doc, Element parent) {
        Element question = doc.createElement("question");
        question.setAttribute("type", "multichoice");
        parent.appendChild(question);
        super.appendXMLElements(doc, question);
        this.answers.forEach(answer -> answer.appendXMLElements(doc, question));
        Element single = doc.createElement("single");
        single.setNodeValue("true");
        question.appendChild(single);
        Element shuffleanswers = doc.createElement("shuffleanswers");
        shuffleanswers.setNodeValue("1");
        question.appendChild(shuffleanswers);
        Element answernumbering = doc.createElement("answernumbering");
        answernumbering.setNodeValue("abc");
        question.appendChild(answernumbering);
        Element showstandardinstruction = doc.createElement("showstandardinstruction");
        showstandardinstruction.setNodeValue("0");
        question.appendChild(showstandardinstruction);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SingleChoiceQuestion{").append("answers=[");
        answers.forEach(answer -> sb.append(answer.toString()).append(","));
        return sb.substring(0, sb.length()-1) + "]}";
    }
}
