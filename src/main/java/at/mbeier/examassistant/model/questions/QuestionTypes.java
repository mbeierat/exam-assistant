package at.mbeier.examassistant.model.questions;

public enum QuestionTypes {
    MULTIPLE_CHOICE_QUESTION("MultipleChoice"),
    SINGLE_CHOICE_QUESTION("SingleChoice"),
    ALL_OR_NOTHING_QUESTION("AllOrNothing"),
    TRUE_FALSE_QUESTION("True/False"),
    SHORT_ANSWER_QUESTION("ShortAnswer"),
    NUMERICAL_QUESTION("Numerical"),
    MATCHING_QUESTION("Matching"),
    ESSAY_QUESTION("Essay"),
    CLOZE_QUESTION("Cloze"),;

    private String identifier;

    QuestionTypes(String identifier) {
        this.identifier = identifier;
    }
}
