package at.mbeier.exass.model.questions;

import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.excel.ImportUtil;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Essay extends Question {

    public Essay() {
        super();
    }

    public Essay(String title, String text, int points) {
        super(QuestionType.ESSAY, title, text, points);
    }

    @Override
    public void createFrom(ExcelRow row) {
        ImportUtil.setStandardQuestionFields(this, row);
    }

    @Override
    public String toGIFTString() {
        return "::" + super.getTitle() + "::" +
                "[html]" + super.getText() + " {}";
    }

    @Override
    public Element toXMLElement(Document doc) {
        Element question = XMLUtil.question(doc, "essay");
        XMLUtil.append(question,
                XMLUtil.moodleText(doc, "name", super.getTitle()),
                XMLUtil.moodleText(doc, "questiontext", "html", super.getText()),
                XMLUtil.moodleText(doc, "defaultgrade", super.getPoints() + ""),
                XMLUtil.moodleText(doc, "responseformat", "editor"),
                XMLUtil.moodleText(doc, "responserequired", "1"),
                XMLUtil.moodleText(doc, "responsefieldlines", "20"),
                XMLUtil.moodleText(doc, "minwordlimit", ""),
                XMLUtil.moodleText(doc, "maxwordlimit", ""),
                XMLUtil.moodleText(doc, "graderinfo", "html", ""),
                XMLUtil.moodleText(doc, "responsetemplate", "html", ""));
        return question;
    }
}
