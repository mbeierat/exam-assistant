package at.mbeier.exass.model.questions;

import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.excel.ImportUtil;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionType;
import org.apache.poi.ss.usermodel.CellType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Essay extends Question {

    private int minWords;
    private int maxWords;
    private String graderInfo;
    private String responseTemplate;

    public Essay() {
        super();
    }

    public Essay(String title, String text, int points) {
        super(QuestionType.ESSAY, title, text, points);
        this.minWords = -1;
        this.maxWords = -1;
        this.graderInfo = null;
        this.responseTemplate = null;
    }

    @Override
    public void createFrom(ExcelRow row) {
        ImportUtil.setStandardQuestionFields(this, row);
        if (row.getCells().get(5).getType() != CellType.NUMERIC && row.getCells().get(5).getType() != CellType.BLANK)
            throw new IllegalArgumentException("Row " + row.getIndex() + " Column 6 (Minimum Word Amount) needs to be a number or blank");
        else if (row.getCells().get(5).getType() == CellType.NUMERIC)
            this.minWords = ((Number) row.getCells().get(5).getContent()).intValue();

        if (row.getCells().get(6).getType() != CellType.NUMERIC && row.getCells().get(6).getType() != CellType.BLANK)
            throw new IllegalArgumentException("Row " + row.getIndex() + " Column 7 (Maximum Word Amount) needs to be a number or blank");
        else if (row.getCells().get(6).getType() == CellType.NUMERIC)
            this.maxWords = ((Number) row.getCells().get(6).getContent()).intValue();

        if (row.getCells().get(7).getType() != CellType.STRING && row.getCells().get(7).getType() != CellType.BLANK)
            throw new IllegalArgumentException("Row " + row.getIndex() + " Column 8 (Grader Info) needs to be a string or blank");
        else if (row.getCells().get(7).getType() == CellType.STRING)
            this.graderInfo = ((String) row.getCells().get(7).getContent());

        if (row.getCells().get(8).getType() != CellType.STRING && row.getCells().get(8).getType() != CellType.BLANK)
            throw new IllegalArgumentException("Row " + row.getIndex() + " Column 9 (Response Template) needs to be a string or blank");
        else if (row.getCells().get(8).getType() == CellType.STRING)
            this.responseTemplate = ((String) row.getCells().get(8).getContent());
    }

    @Override
    public String toGIFTString() {
        return "::" + super.getTitle() + "::" +
                "[html]" + super.getText() + " {}";
    }

    @Override
    public Element toXMLElement(Document doc) {
        Element question = XMLUtil.question(doc, "essay");
        super.appendStandardXMLChilds(doc, question);
        XMLUtil.append(question,
                XMLUtil.moodleText(doc, "minwordlimit", this.minWords == -1 ? "" : this.minWords + ""),
                XMLUtil.moodleText(doc, "maxwordlimit", this.maxWords == -1 ? "" : this.maxWords + ""),
                XMLUtil.moodleText(doc, "graderinfo", "html", this.graderInfo == null ? "" : this.graderInfo),
                XMLUtil.moodleText(doc, "responsetemplate", "html", this.responseTemplate == null ? "" : this.responseTemplate));
        return question;
    }
}
