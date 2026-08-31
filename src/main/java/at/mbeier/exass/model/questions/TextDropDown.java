package at.mbeier.exass.model.questions;

import at.mbeier.exass.excel.ExcelCell;
import at.mbeier.exass.excel.ExcelRow;
import at.mbeier.exass.excel.ImportUtil;
import at.mbeier.exass.exporter.XMLUtil;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionProperty;
import at.mbeier.exass.model.QuestionType;
import org.apache.poi.ss.usermodel.CellType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextDropDown extends Question {

    private final List<TextDropDownAnswer> answers;

    public TextDropDown() {
        super();
        this.answers = new ArrayList<>();
    }

    public TextDropDown(String title, String text, int points) {
        super(QuestionType.TEXT_DROP_DOWN, title, text, points);
        this.answers = new ArrayList<>();
    }

    @Override
    public void createFrom(ExcelRow row) {
        ImportUtil.setStandardQuestionFields(this, row);
        for (int i = 5; i < row.getCells().size(); i += 2) {
            if (row.getCells().get(i).getType() != CellType.STRING)
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + i + " (Answer Text) needs to be a string");
            String answerText = (String) row.getCells().get(i).getContent();
            ExcelCell groupCell;
            try {
                groupCell = row.getCells().get(i + 1);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Group) needs to have a value");
            }
            if (groupCell.getType() != CellType.STRING && groupCell.getType() != CellType.NUMERIC) {
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + i + " (Group) needs to be a string (group identified by character, i.e. A, B, C) or number (group identified by number, i.e. 1, 2, 3)");
            }
            int group = 0;
            if (groupCell.getType() == CellType.STRING) {
                char g = ((String) groupCell.getContent()).toUpperCase().charAt(0);
                group = g - 'A' + 1;
            } else if (groupCell.getType() == CellType.NUMERIC) {
                group = ((Number) groupCell.getContent()).intValue();
            }
            TextDropDownAnswer answer = new TextDropDownAnswer(answerText, group);
            this.answers.add(answer);
        }
        if (super.getText().contains("[[" + this.answers.size() + "]]"))
            throw new IllegalArgumentException("There must be at least as many answers as placeholders in the question text");
    }

    @Override
    public String toGIFTString() {
        throw new UnsupportedOperationException("The Question Type " + super.getType().getId() + " is not supported in the GIFT-Format");
    }

    @Override
    public Element toXMLElement(Document doc) {
        Element question = XMLUtil.question(doc, "gapselect");
        super.appendStandardXMLChilds(doc, question);
        for (TextDropDownAnswer answer : this.answers) {
            Element selectOption = XMLUtil.moodleText(doc, "selectoption", answer.answer());
            Element group = XMLUtil.element(doc, "group");
            group.appendChild(doc.createTextNode(answer.group() + ""));
            XMLUtil.append(question, XMLUtil.append(selectOption, group));
        }
        return question;
    }
}

record TextDropDownAnswer(String answer, int group) {}
