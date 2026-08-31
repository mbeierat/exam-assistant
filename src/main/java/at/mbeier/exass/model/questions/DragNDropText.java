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

import java.util.ArrayList;
import java.util.List;

public class DragNDropText extends Question {

    private final List<DragNDropTextAnswer> answers;

    public DragNDropText() {
        super();
        this.answers = new ArrayList<>();
    }

    public DragNDropText(String name, String text, int points) {
        super(QuestionType.DRAG_N_DROP_TEXT, name, text, points);
        this.answers = new ArrayList<>();
    }

    @Override
    public void createFrom(ExcelRow row) {
        ImportUtil.setStandardQuestionFields(this, row);
        for (int i = 5; i < row.getCells().size(); i += 3) {
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
            if (group > 8)
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Group) needs to have a value smaller than or equal to 8");
            ExcelCell infinteCell;
            try {
                infinteCell = row.getCells().get(i + 2);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 2) + " (Infinite?) needs to have a value");
            }
            if (groupCell.getType() != CellType.BOOLEAN && groupCell.getType() != CellType.NUMERIC) {
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + i + " (Infinite?) needs to be a boolean (true/false) or number (0/1)");
            }
            boolean infinite = false;
            if (infinteCell.getType() == CellType.BOOLEAN) {
                infinite = (Boolean) infinteCell.getContent();
            } else if (groupCell.getType() == CellType.NUMERIC) {
                infinite = ((Number) infinteCell.getContent()).intValue() == 1;
            }
            DragNDropTextAnswer answer = new DragNDropTextAnswer(answerText, group, infinite);
            this.answers.add(answer);
        }
        if (super.getText().contains("[[" + (this.answers.size() + 1) + "]]"))
            throw new IllegalArgumentException("There must be at least as many answers as placeholders in the question text");
    }

    @Override
    public String toGIFTString() {
        throw new UnsupportedOperationException("The Question Type " + super.getType().getId() + " is not supported in the GIFT-Format");
    }

    @Override
    public Element toXMLElement(Document doc) {
        Element question =  XMLUtil.question(doc, "ddwtos");
        super.appendStandardXMLChilds(doc, question);
        for (DragNDropTextAnswer answer : answers) {
            Element dragBox = XMLUtil.element(doc, "dragbox");
            Element text = XMLUtil.element(doc, "text");
            XMLUtil.append(text,
                    doc.createCDATASection(answer.answer()));
            XMLUtil.append(dragBox,
                    text,
                    XMLUtil.textElement(doc, "group", answer.group() + ""));
            if (answer.infinite())
                XMLUtil.append(dragBox, XMLUtil.element(doc, "infinite"));
        }
        return question;
    }
}

record DragNDropTextAnswer(String answer, int group, boolean infinite) {}
