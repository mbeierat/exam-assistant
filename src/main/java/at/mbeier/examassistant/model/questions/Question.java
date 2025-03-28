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
        return "";
    }

    @Override
    public void appendXMLElements(Document doc, Element parent) {

    }
}
