package at.mbeier.exass.model.questions;

import at.mbeier.exass.excel.ExcelCell;
import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.excel.ImportUtil;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.AnswerWeight;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionProperty;
import at.mbeier.exass.model.QuestionType;
import org.apache.poi.ss.usermodel.CellType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Map;

public class TrueFalse extends Question {

    private boolean correct;

    public TrueFalse() {
        super();
    }

    public TrueFalse(String title, String text, int points) {
        super(QuestionType.TRUE_FALSE, title, text, points);
    }

    @Override
    public void createFrom(ExcelRow row) {
        ImportUtil.setStandardQuestionFields(this, row);
        ExcelCell correctCell;
        try {
            correctCell = row.getCells().get(5);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Row " + row.getIndex() + " Column 5 (Correct) needs to have a value");
        }
        if (correctCell.getType() == CellType.BOOLEAN) {
            this.correct = (boolean) correctCell.getContent();
        } else if (correctCell.getType() == CellType.NUMERIC) {
            if (!(((Number) correctCell.getContent()).intValue() == 1 || ((Number) correctCell.getContent()).intValue() == 0))
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column 5 (Correct) needs to be a number (or a boolean) being 1 or 0 as in true or false");
            this.correct = ((Number) correctCell.getContent()).intValue() == 1;
        }
    }

    @Override
    public String toGIFTString() {
        return "::" + super.getTitle() + "::" +
                "[html]" + super.getText() +
                "{" + (this.correct ? "T" : "F") + "}";
    }

    @Override
    public Element toXMLElement(Document doc) {
        Element question = XMLUtil.question(doc, "truefalse");
        super.appendStandardXMLChilds(doc, question);
        XMLUtil.append(question,
                XMLUtil.answer(doc, this.correct ? AnswerWeight.P100.getXMLRepresentation() : AnswerWeight.P0.getXMLRepresentation(), null, "richtig", "html", ""),
                XMLUtil.answer(doc, !this.correct ? AnswerWeight.P100.getXMLRepresentation() : AnswerWeight.P0.getXMLRepresentation(), null, "falsch", "html", ""));
        return question;
    }
}
