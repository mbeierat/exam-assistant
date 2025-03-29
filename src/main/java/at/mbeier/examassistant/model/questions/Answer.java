package at.mbeier.examassistant.model.questions;

import at.mbeier.examassistant.model.MoodleXMLFormattable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Answer implements MoodleXMLFormattable {

    private final String answer;
    private final Percentage weight;

    public Answer(String answer, Percentage weight) {
        this.answer = answer;
        this.weight = weight;
    }

    public String getAnswer() {
        return this.answer;
    }

    public Percentage getPercentage() {
        return this.weight;
    }

    public String toMultipleChoiceGIFTString() {
        return "~" + this.getPercentage().getGIFTValue() + "[html]" + this.getAnswer();
    }

    public String toSingleChoiceGIFTString() {
        return (this.getPercentage().isCorrect() ? "=" : "~") + "[html]" + this.getAnswer();
    }

    @Override
    public void appendXMLElements(Document doc, Element parent) {
        Element answer = doc.createElement("answer");
        answer.setAttribute("fraction", this.getPercentage().getXMLValue());
        answer.setAttribute("format", "html");
        parent.appendChild(answer);
        Element text = doc.createElement("text");
        text.setNodeValue(this.getAnswer());
        answer.appendChild(text);
        Element feedback = doc.createElement("feedback");
        feedback.setAttribute("format", "html");
        answer.appendChild(feedback);
        Element feedbackText = doc.createElement("text");
        feedback.appendChild(feedbackText);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answer='" + this.getAnswer() + '\'' +
                ", weight=" + this.getPercentage() +
                '}';
    }

    public static boolean isValidTrueFalseOption(String option) {
        return option.equals("0") || option.equals("1") ||
                option.equalsIgnoreCase("true") || option.equalsIgnoreCase("false") ||
                option.equalsIgnoreCase("t") || option.equalsIgnoreCase("f");
    }

    public static boolean isOptionTrue(String option) {
        return option.equals("1") || option.equalsIgnoreCase("true") || option.equalsIgnoreCase("t");
    }

    public static boolean isOptionFalse(String option) {
        return option.equals("0") || option.equalsIgnoreCase("false") || option.equalsIgnoreCase("f");
    }
}
