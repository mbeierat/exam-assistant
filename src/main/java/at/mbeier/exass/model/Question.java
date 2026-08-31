package at.mbeier.exass.model;

import at.mbeier.exass.excel.ExcelRow;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Objects;

/**
 * Class representing one question to be exported for Moodle
 *
 * @author Michael Beier @mbeierat
 * @version 2026-08-20
 */
public abstract class Question {

    private QuestionType type;
    private String title;
    private String text;
    private int points;

    private QuestionConfiguration config;

    /**
     * No-arg constructor used by QuestionType#parse, which instantiates the
     * subclass reflectively and then calls createFrom(ExcelRow) to fill it in.
     */
    public Question() {}

    public Question(QuestionType type, String title, String text, int points) {
        this.type = type;
        this.title = title;
        this.text = text;
        this.points = points;
    }

    public QuestionType getType() {
        return this.type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public QuestionConfiguration getConfig() {
        return this.config;
    }

    public void setConfig(QuestionConfiguration config) {
        this.config = config;
    }

    public abstract String toGIFTString();

    /**
     * Builds this question's element into the given document. The caller
     * (XMLExporter) owns the document, appends the returned element into its
     * tree, and only serializes to a string once at the end, after every
     * question in the export has been added.
     */
    public abstract Element toXMLElement(Document doc);

    /**
     * Fills this question's type-specific fields from a parsed Excel row.
     * @param row the row of the Excel
     */
    public abstract void createFrom(ExcelRow row);

    @Override
    public String toString() {
        return "Question{" +
                "type=" + this.type +
                ", name='" + this.title + '\'' +
                ", text='" + this.text + '\'' +
                ", points=" + this.points +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return this.type == question.type && Objects.equals(this.title, question.title)
                && Objects.equals(this.text, question.text) && this.points == question.points;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.title, this.text, this.points);
    }
}
