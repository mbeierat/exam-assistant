package at.mbeier.examassistant.model.questions;

public enum QuestionTypes {
    MULTIPLE_CHOICE_QUESTION("multipleChoiceQuestion"),
    SINGLE_CHOICE_QUESTION("singleChoiceQuestion"),
    ALL_OR_NOTHING_QUESTION("allOrNothingQuestion"),
    TRUE_FALSE_QUESTION("trueFalseQuestion"),
    SHORT_ANSWER_QUESTION("shortAnswerQuestion"),
    NUMERICAL_QUESTION("numericQuestion"),
    MATCHING_QUESTION("matchingQuestion"),
    ESSAY_QUESTION("essayQuestion"),
    CLOZE_QUESTION("clozeQuestion"),;

    private String identifier;

    QuestionTypes(String identifier) {
        this.identifier = identifier;
    }
}
