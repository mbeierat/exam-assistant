package at.mbeier.exass.model;

import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.model.questions.*;

public enum QuestionType {

    MULTI_CHOICE("Multiple Choice", MultipleChoice.class),
    SINGLE_CHOICE("Single Choice", SingleChoice.class),
    TRUE_FALSE("Wahr/Falsch", TrueFalse.class),
    SHORT_ANSWER("Kurzantwort", ShortAnswer.class),
    NUMERICAL("Numerisch", Numerical.class),
    ESSAY("Freitext", Essay.class),
    ALL_OR_NOTHING_MULTI_CHOICE("Alles/Nichts MC", AllOrNothingMultipleChoice.class),
    CALCULATE("Berechnen", Calculate.class),
    MATCH("Zuordnen", Match.class),
    RANDOM_SHORT_ANSWER_MATCH("Zufällige Kurzantwortzuordnung", RandomShortAnswerMatch.class),
    CLOZE("Lückentext", Cloze.class),
    ORDER("Anordnung", Order.class),
    CALCULATE_MULTI_CHOICE("Multiple Choice Berechnen", CalculateMultipleChoice.class),
    DRAG_N_DROP_PIC("Drag n Drop Bild", DragNDropPicture.class),
    DRAG_N_DROP_TEXT("Drag n Drop Text", DragNDropText.class),
    DRAG_N_DROP_MARKINGS("Drag n Drop Markierungen", DragNDropMarkings.class),
    EASY_CALCULATE("Einfach Berechnen", EasyCalculate.class),
    LEARNCARD("Lernkarte", LearnCard.class),
    TEXT_DROP_DOWN("Lückentextauswahl", TextDropDown.class),
    DESCRIPTION("DESCRIPTION", Description.class);

    private final String id;
    private final Class<? extends Question> clazz;

    QuestionType(String id, Class<? extends Question> clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    public String getId() {
        return this.id;
    }

    /**
     * Instantiates this type's Question subclass via its no-arg constructor
     * and has it create itself from the row. Question parent type enforces the existence
     * of a createFrom method. Reflections are contained to this method itself.
     */
    public Question parse(ExcelRow row) {
        if (this.clazz == null) {
            throw new UnsupportedOperationException(this + " is not implemented yet");
        }
        try {
            Question question = this.clazz.getDeclaredConstructor().newInstance();
            question.setType(this);
            question.createFrom(row);
            return question;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot instantiate " + this.clazz.getSimpleName() + " for " + this, e);
        }
    }

    public static QuestionType fromId(String id) {
        for (QuestionType type : values()) {
            if (type.id.equalsIgnoreCase(id.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown question type: " + id);
    }
}
