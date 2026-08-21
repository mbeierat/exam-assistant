package at.mbeier.exass.model.questions;

import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.excel.ImportUtil;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionType;
import org.apache.poi.ss.usermodel.CellType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Description extends Question {

    public Description() {
        super();
    }

    public Description(String title, String text) {
        super(QuestionType.DESCRIPTION, title, text, 0);
    }

    @Override
    public void createFrom(ExcelRow row) {
        if (row.getCells().get(2).getType() != CellType.STRING)
            throw new IllegalArgumentException("Row " + row.getIndex() + " Column 3 (Question Name) needs to be a string");
        this.setTitle((String) row.getCells().get(2).getContent());
        if (row.getCells().get(3).getType() != CellType.STRING)
            throw new IllegalArgumentException("Row " + row.getIndex() + " Column 4 (Question Text) needs to be a string");
        this.setText((String) row.getCells().get(3).getContent());
    }

    @Override
    public String toGIFTString() {
        return "::" + super.getTitle() + "::" +
                "[html]" + super.getText();
    }

    @Override
    public Element toXMLElement(Document doc) {
        Element desc = XMLUtil.question(doc, "description");
        XMLUtil.append(desc,
                XMLUtil.moodleText(doc, "name", super.getTitle()),
                XMLUtil.moodleText(doc, "questiontext", "html", super.getText()));
        return desc;
    }
}
