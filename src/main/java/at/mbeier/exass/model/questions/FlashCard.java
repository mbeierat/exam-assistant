package at.mbeier.exass.model.questions;

import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.excel.ImportUtil;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.AnswerWeight;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionType;
import org.apache.poi.ss.usermodel.CellType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FlashCard extends Question {

    private String answer;

    public FlashCard() {
        super();
    }

    public FlashCard(String title, String text, int points) {
        super(QuestionType.FLASHCARD, title, text, points);
    }

    @Override
    public void createFrom(ExcelRow row) {
        ImportUtil.setStandardQuestionFields(this, row);
        if (row.getCells().get(5).getType() != CellType.STRING)
            throw new IllegalArgumentException("Row " + row.getIndex() + " Column 5 (Answer Text) needs to be a string");
        this.answer = (String) row.getCells().get(5).getContent();
    }

    @Override
    public String toGIFTString() {
        throw new UnsupportedOperationException("The Question Type " + super.getType().getId() + " is not supported in the GIFT-Format");
    }

    @Override
    public Element toXMLElement(Document doc) {
        Element question = XMLUtil.question(doc,"flashcard");
        super.appendStandardXMLChilds(doc, question);
        XMLUtil.append(question,
                XMLUtil.answer(doc, AnswerWeight.P0.getXMLRepresentation(), "html", this.answer, "html", ""));
        return XMLUtil.element(doc, "question");
    }
}
