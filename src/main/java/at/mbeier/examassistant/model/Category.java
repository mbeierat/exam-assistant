package at.mbeier.examassistant.model;

import at.mbeier.examassistant.model.questions.Question;

import java.util.ArrayList;
import java.util.List;

public class Category implements GIFTFormattable, MoodleXMLFormattable {

    private final String name;
    private final List<Question> questions;

    public Category(String name) {
        this.name = name;
        this.questions = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

}
