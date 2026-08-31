package at.mbeier.exass.model;

public enum QuestionProperty {
    STANDARD_INSTRUCTION("showstandardinstruction", "0", QuestionType.TRUE_FALSE, QuestionType.SINGLE_CHOICE, QuestionType.MULTI_CHOICE, QuestionType.ALL_OR_NOTHING_MULTI_CHOICE),
    SHUFFLE_ANSWERS("shuffleanswers", "true", QuestionType.TEXT_DROP_DOWN, QuestionType.SINGLE_CHOICE, QuestionType.MULTI_CHOICE, QuestionType.MATCH, QuestionType.ALL_OR_NOTHING_MULTI_CHOICE, QuestionType.DRAG_N_DROP_TEXT),
    ANSWER_NUMBERING("answernumbering", "abc", QuestionType.SINGLE_CHOICE, QuestionType.MULTI_CHOICE, QuestionType.ALL_OR_NOTHING_MULTI_CHOICE),
    USE_CASE("usecase", "0", QuestionType.SHORT_ANSWER),
    RESPONSE_FORMAT("responseformat", "editor", QuestionType.ESSAY),
    RESPONSE_REQUIRED("responserequired", "1", QuestionType.ESSAY),
    RESPONSE_FIELD_LINES("responsefieldlines", "20", QuestionType.ESSAY),
    LAYOUT_TYPE("layouttype", "VERTICAL", QuestionType.ORDER),
    SELECT_TYPE("selecttype", "ALL", QuestionType.ORDER),
    SELECT_COUNT("selectcount", "2", QuestionType.ORDER),
    GRADING_TYPE("gradingstyle", "ABSOLUTE_POSITION", QuestionType.ORDER),
    SHOW_GRADING("showgrading", "SHOW", QuestionType.ORDER),
    NUMBERING_STYLE("numberingstyle", "none", QuestionType.ORDER);


    private final String representation;
    private final String defaultValue;
    private final QuestionType[] compTypes;

    QuestionProperty(String representation, String defaultValue, QuestionType... compTypes) {
        this.representation = representation;
        this.defaultValue = defaultValue;
        this.compTypes = compTypes;
    }

    public String getRepresentation() {
        return this.representation;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public QuestionType[] getCompatibleTypes() {
        return this.compTypes;
    }
}
