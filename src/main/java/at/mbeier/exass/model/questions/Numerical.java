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

import java.util.ArrayList;
import java.util.List;

public class Numerical extends Question {

    private final List<NumericalAnswer> answers;

    public Numerical() {
        super();
        this.answers = new ArrayList<>();
    }

    public Numerical(String name, String text, int points) {
        super(QuestionType.NUMERICAL, name, text, points);
        this.answers = new ArrayList<>();
    }

    @Override
    public void createFrom(ExcelRow row) {
        ImportUtil.setStandardQuestionFields(this, row);
        for (int i = 5; i < row.getCells().size(); i += 3) {
            if (row.getCells().get(i).getType() != CellType.NUMERIC)
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + i + " (Answer Value) needs to be a number");
            double answer = ((Number) row.getCells().get(i).getContent()).doubleValue();
            if (row.getCells().get(i + 1).getType() != CellType.NUMERIC || row.getCells().get(i + 1).getType() != CellType.BLANK)
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Tolerance) needs to be a number (or blank)");
            double tolerance = 0.0;
            if (row.getCells().get(i + 1).getType() == CellType.NUMERIC)
                 tolerance = ((Number) row.getCells().get(i + 1).getContent()).doubleValue();
            AnswerWeight weight = AnswerWeight.P100;
            if (row.getCells().get(i + 2).getType() != CellType.NUMERIC || row.getCells().get(i + 2).getType() != CellType.BLANK)
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 2) + " (Answer Weight) needs to be a number (or blank)");
            if (row.getCells().get(i + 2).getType() == CellType.NUMERIC)
                weight = AnswerWeight.parse(((Number) row.getCells().get(i + 2).getContent()).doubleValue());
            this.answers.add(new NumericalAnswer(answer, tolerance, weight));
        }
    }

    @Override
    public String toGIFTString() {
        StringBuilder builder = new StringBuilder("::" + super.getTitle() + "::" +
                "[html]" + super.getText() + "{#");
        for (NumericalAnswer answer : this.answers) {
            builder.append("=%")
                    .append(answer.weight().getRepresentation())
                    .append("%")
                    .append(answer.answer())
                    .append(":")
                    .append(answer.tolerance())
                    .append("#");
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public Element toXMLElement(Document doc) {
        Element question = XMLUtil.question(doc, "numerical");
        super.appendStandardXMLChilds(doc, question);
        for (NumericalAnswer answer : answers) {
            Element answerElement = XMLUtil.answer(doc, answer.weight().getXMLRepresentation(),
                    "moodle_auto_format", String.valueOf(answer.answer()), "", "");
            Element tolerance = XMLUtil.element(doc, "tolerance");
            tolerance.setNodeValue(String.valueOf(answer.tolerance()));
            answerElement.appendChild(tolerance);
            XMLUtil.append(question, answerElement);
        }
        XMLUtil.append(question,
                XMLUtil.textElement(doc, "unitgradingtype", "0"),
                XMLUtil.textElement(doc, "showunits", "3"),
                XMLUtil.textElement(doc, "unitsleft", "0"));
        return question;
    }
}

record NumericalAnswer(double answer, double tolerance, AnswerWeight weight) {}