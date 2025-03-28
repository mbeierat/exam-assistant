package at.mbeier.examassistant.model.questions;

public enum QuestionType {
    MULTIPLE_CHOICE_QUESTION("MultipleChoice", MultipleChoiceQuestion.class),
    SINGLE_CHOICE_QUESTION("SingleChoice", SingleChoiceQuestion.class),
    ALL_OR_NOTHING_MC_QUESTION("AllOrNothing", AllOrNothingMCQuestion.class),
    TRUE_FALSE_QUESTION("True/False", TrueFalseQuestion.class),
    SHORT_ANSWER_QUESTION("ShortAnswer", ShortAnswerQuestion.class),
    NUMERICAL_QUESTION("Numerical", NumericalQuestion.class),
    MATCHING_QUESTION("Matching", MatchingQuestion.class),
    ESSAY_QUESTION("Essay", EssayQuestion.class),
    CLOZE_QUESTION("Cloze", ClozeQuestion.class);

    private String identifier;
    private Class<? extends Question> questionClass;

    QuestionType(String identifier, Class<? extends Question> questionClass) {
        this.identifier = identifier;
        this.questionClass = questionClass;
    }
}
