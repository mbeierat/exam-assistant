package at.mbeier.exass.model;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String name;
    private final List<Question> questions;

    public Category(String name) {
        this(name, new ArrayList<>());
    }

    public Category(String name, List<Question> questions) {
        this.name = name;
        this.questions = questions;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addQuestion(Question q) {
        this.questions.add(q);
    }

    public boolean removeQuestion(Question q) {
        return this.questions.remove(q);
    }

    public List<Question> getQuestions() {
        return this.questions;
    }
}
