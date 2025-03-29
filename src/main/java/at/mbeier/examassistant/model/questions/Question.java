package at.mbeier.examassistant.model.questions;

import at.mbeier.examassistant.model.GIFTFormattable;
import at.mbeier.examassistant.model.MoodleXMLFormattable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class Question implements GIFTFormattable, MoodleXMLFormattable {
    private final String title;
    private final String question;
    private final int points;

    public Question(String title, String question, String points) {
        this.title = title;
        this.question = question;
        this.points = Integer.parseInt(points);
    }

    public int getPoints() {
        return this.points;
    }

    public String getQuestion() {
        return this.question;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public String toGIFTString() {
        return "::" + this.title + "::" + "[html]" + this.question;
    }

    @Override
    public void appendXMLElements(Document doc, Element parent) {
        Element name = doc.createElement("name");
        Element nameText = doc.createElement("text");
        nameText.setNodeValue(this.title);
        name.appendChild(nameText);
        parent.appendChild(name);
        Element questiontext = doc.createElement("questiontext");
        questiontext.setAttribute("format", "html");
        Element questiontextText = doc.createElement("text");
        questiontextText.setNodeValue(this.question);
        questiontext.appendChild(questiontextText);
        parent.appendChild(questiontext);
        Element generalfeedback = doc.createElement("generalfeedback");
        generalfeedback.setAttribute("format", "html");
        Element generalfeedbackText = doc.createElement("text");
        generalfeedback.appendChild(generalfeedbackText);
        parent.appendChild(generalfeedback);
        Element defaultgrade = doc.createElement("defaultgrade");
        defaultgrade.setNodeValue(this.points + ".0000000");
        parent.appendChild(defaultgrade);
        Element penalty = doc.createElement("penalty");
        penalty.setNodeValue("1.0000000");
        parent.appendChild(penalty);
        Element hidden = doc.createElement("hidden");
        hidden.setNodeValue("0");
        parent.appendChild(hidden);
        Element idnumber = doc.createElement("idnumber");
        parent.appendChild(idnumber);

    }

    @Override
    public String toString() {
        return "title='" + title + '\'' +
                ", question='" + question + '\'' +
                ", points=" + points;
    }
}
