package at.mbeier.exass.model.questions;

import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.excel.ImportUtil;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionType;
import org.apache.poi.ss.usermodel.CellType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Cloze extends Question {

    public Cloze() {
        super();
    }

    public Cloze(String name, String text) {
        super(QuestionType.CLOZE, name, text, 0);
    }

    @Override
    public void createFrom(ExcelRow row) {
        if (row.getCells().get(2).getType() != CellType.STRING)
            throw new IllegalArgumentException("Row " + row.getIndex() + " Column 3 (Question Name) needs to be a string");
        String qname = (String) row.getCells().get(2).getContent();
        this.setTitle(qname);
        if (row.getCells().get(3).getType() != CellType.STRING)
            throw new IllegalArgumentException("Row " + row.getIndex() + " Column 4 (Question Text) needs to be a string");
        String qtext = (String) row.getCells().get(3).getContent();
        this.setText(qtext);
    }

    @Override
    public String toGIFTString() {
        throw new UnsupportedOperationException("The Question Type " + super.getType().getId() + " is not supported in the GIFT-Format");
    }

    @Override
    public Element toXMLElement(Document doc) {
        Element question = XMLUtil.question(doc, "cloze");
        XMLUtil.append(question,
                XMLUtil.moodleText(doc, "name", this.getTitle()),
                XMLUtil.moodleText(doc, "questiontext", "html", this.getText()));
        return question;
    }
}
