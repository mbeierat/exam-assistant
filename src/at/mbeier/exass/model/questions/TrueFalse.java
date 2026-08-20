package at.mbeier.exass.model.questions;

import at.mbeier.exass.excel.ExcelCell;
import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.excel.ImportUtil;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionType;
import org.apache.poi.ss.usermodel.CellType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
            if (!((int) correctCell.getContent() == 1 || (int) correctCell.getContent() == 0))
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column 5 (Correct) needs to be a number (or a boolean) being 1 or 0 as in true or false");
            this.correct = ((int) correctCell.getContent()) == 1;
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
        XMLUtil.append(question,
                XMLUtil.moodleText(doc, "name", super.getTitle()),
                XMLUtil.moodleText(doc, "questiontext", "html", super.getText()),
                XMLUtil.textElement(doc, "defaultgrade", super.getPoints() + ""),
                XMLUtil.textElement(doc, "showstandardinstruction", "0"),
                XMLUtil.answer(doc, this.correct ? "100" : "0", null, "richtig", "html", ""),
                XMLUtil.answer(doc, !this.correct ? "100" : "0", null, "falsch", "html", ""));
        return question;
    }
}
