package at.mbeier.examassistant.model.questions;

import at.mbeier.examassistant.model.GIFTFormattable;
import at.mbeier.examassistant.model.MoodleXMLFormattable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class Question implements GIFTFormattable, MoodleXMLFormattable {
    private String title;
    private String question;
    private int points;

    public Question(String title, String question, int points) {
        this.title = title;
        this.question = question;
        this.points = points;
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
