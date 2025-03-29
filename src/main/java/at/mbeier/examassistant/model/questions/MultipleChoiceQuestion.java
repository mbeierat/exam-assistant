package at.mbeier.examassistant.model.questions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceQuestion extends Question {

    private final List<Answer> answers;
    private InputMode mode;

    public MultipleChoiceQuestion(String title, String question, String points, String[] answerInput) {
        super(title, question, points);
        this.answers = new ArrayList<>();
        // Checking for correct values and whether true/false or weighted values are used.
        if (answerInput.length % 2 != 0) {
            throw new IllegalArgumentException("Answers must have an even number of arguments, as every answer needs to indicate a weight or whether it is true or false");
        }
        for (int i = 1; i < answerInput.length; i+=2) {
            if (answerInput[i].contains("%")) {
                if (this.mode == InputMode.TRUE_FALSE)
                    throw new IllegalArgumentException("Multiple choice answers must not contain mixed weighted answers and true/false answers");
                if (Percentage.getPercentage(answerInput[i]) == null)
                    throw new IllegalArgumentException("The weight " + answerInput[i] + " is not a valid weight");
                this.mode = InputMode.WEIGHTED;
            } else if (Answer.isValidTrueFalseOption(answerInput[i])) {
                if (this.mode == InputMode.WEIGHTED)
                    throw new IllegalArgumentException("Multiple choice answers must not contain mixed weighted answers and true/false answers");
                this.mode = InputMode.TRUE_FALSE;
            } else {
                throw new IllegalArgumentException("Cannot determine whether " + answerInput[i] + " is a weight or true/false statement");
            }
        }
        // Creating answers list
        if (this.mode == InputMode.TRUE_FALSE) {
            int correct = 0;
            int wrong = 0;
            for (int i = 1; i < answerInput.length; i+=2) {
                if (Answer.isOptionTrue(answerInput[i])) correct++;
                else if (Answer.isOptionFalse(answerInput[i])) wrong++;
            }
            Percentage p = Percentage.calculateCorrectPercentage(correct, wrong);
            Percentage n = Percentage.switchSign(p);
            for (int i = 0; i < answerInput.length; i+=2) {
                if (Answer.isOptionTrue(answerInput[i+1]))
                    this.answers.add(new Answer(answerInput[i], p));
                else if (Answer.isOptionFalse(answerInput[i+1]))
                    this.answers.add(new Answer(answerInput[i], n));
            }
        } else if (this.mode == InputMode.WEIGHTED) {
            double total = 0;
            for (int i = 0; i < answerInput.length; i+=2) {
                Percentage p = Percentage.getPercentage(answerInput[i+1]);
                if (p != null && p.isCorrect()) {
                    total += p.getPercentage();
                }
                this.answers.add(new Answer(answerInput[i], p));
            }
            if (Math.round(total) != 100) {
                throw new IllegalArgumentException("Multiple choice answers, that are true, must have a cumulative weight of 100%");
            }
        }
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    @Override
    public String toGIFTString() {
        StringBuilder builder = new StringBuilder(super.toGIFTString()).append("{");
        this.answers.forEach(answer -> builder.append(answer.toMultipleChoiceGIFTString()));
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
        single.setNodeValue("false");
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
        sb.append("MultipleChoiceQuestion{").append("answers=[");
        answers.forEach(answer -> sb.append(answer.toString()).append(","));
        return sb.substring(0, sb.length()-1) + "]}";
    }
}

enum InputMode {
    TRUE_FALSE, WEIGHTED
}
