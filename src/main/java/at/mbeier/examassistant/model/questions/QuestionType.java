package at.mbeier.examassistant.model.questions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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

    private final String identifier;
    private final Class<? extends Question> questionClass;

    QuestionType(String identifier, Class<? extends Question> questionClass) {
        this.identifier = identifier;
        this.questionClass = questionClass;
    }

    @Override
    public String toString() {
        return this.identifier;
    }

    public Question createNewQuestion(String title, String question, String points, String[] answers) {
        try {
            Constructor<? extends Question> constructor = questionClass.getConstructor(String.class, String.class, String.class, String[].class);
            return constructor.newInstance(title, question, points, answers);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("couldn't create new instance of question " + this.identifier, e);
        }
    }

    public static QuestionType getQuestionType(String identifier) {
        for (QuestionType questionType : values()) {
            if (questionType.identifier.equals(identifier)) {
                return questionType;
            }
        }
        return null;
    }
}
