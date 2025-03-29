package at.mbeier.examassistant.model.questions;

import at.mbeier.examassistant.model.GIFTFormattable;
import at.mbeier.examassistant.model.MoodleXMLFormattable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Answer implements GIFTFormattable, MoodleXMLFormattable {

    private final String answer;
    private final Percentage weight;

    public Answer(String answer, Percentage weight) {
        this.answer = answer;
        this.weight = weight;
    }

    public String getAnswer() {
        return answer;
    }

    public Percentage getPercentage() {
        return weight;
    }

    @Override
    public String toGIFTString() {
        return "~" + this.getPercentage().getGIFTValue() + "[html]" + this.answer;
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
}
