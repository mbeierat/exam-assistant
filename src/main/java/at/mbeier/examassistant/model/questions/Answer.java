package at.mbeier.examassistant.model.questions;

public class Answer {

    private final String answer;
    private final boolean correct;
    private final Percentage weight;

    public Answer(String answer, boolean correct, Percentage weight) {
        this.answer = answer;
        this.correct = correct;
        this.weight = weight;
    }
}
