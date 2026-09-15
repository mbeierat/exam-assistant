package at.mbeier.exass.model.questions;

import at.mbeier.exass.model.AnswerWeight;

class MultipleChoiceAnswer {
    private final String text;
    private final boolean correct;
    private AnswerWeight weight;

    public MultipleChoiceAnswer(String text, boolean correct) {
        this(text, correct, null);
    }

    public MultipleChoiceAnswer(String text, boolean correct, AnswerWeight weight) {
        this.text = text;
        this.correct = correct;
        this.weight = weight;
    }

    public void setWeight(AnswerWeight weight) {
        this.weight = weight;
    }

    public String getText() {
        return this.text;
    }

    public boolean isCorrect() {
        return this.correct;
    }

    public AnswerWeight getWeight() {
        return this.weight;
    }
}
