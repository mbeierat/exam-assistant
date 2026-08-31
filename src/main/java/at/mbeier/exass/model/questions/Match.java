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

public class Match extends Question {

    private final List<MatchingPair> matchingItems;

    public Match() {
        super();
        this.matchingItems = new ArrayList<>();
    }

    public Match(String title, String text, int points) {
        super(QuestionType.MATCH, title, text, points);
        this.matchingItems = new ArrayList<>();
    }

    @Override
    public void createFrom(ExcelRow row) {
        ImportUtil.setStandardQuestionFields(this, row);
        for (int i = 5; i < row.getCells().size(); i += 2) {
            if (row.getCells().get(i).getType() != CellType.STRING && row.getCells().get(i).getType() != CellType.BLANK)
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + i + " (Prompt Text) needs to be a string (or blank if extra answers are introduced)");
            ExcelCell answerCell;
            try {
                answerCell = row.getCells().get(i + 1);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + (i + 1) + " (Answer Text) needs to have a value");
            }
            if (answerCell.getType() != CellType.STRING) {
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column " + i + " (Answer Text) needs to be a string");
            }
            MatchingPair pair = new MatchingPair((String) row.getCells().get(i).getContent(), (String) answerCell.getContent());
            this.matchingItems.add(pair);
        }
        int answers = this.matchingItems.size();
        int unmatched = 0;
        for (MatchingPair pair : this.matchingItems) {
            if (pair.isAnswerUnmatched()) unmatched++;
        }
        int prompts = answers - unmatched;
        if (!(prompts >= 2 && answers >=3))
            throw new IllegalArgumentException("A matching question must have at least two prompts and three answers");
    }

    @Override
    public String toGIFTString() {
        StringBuilder builder = new StringBuilder("::" + super.getTitle() + "::" +
                "[html]" + super.getText() + "{");
        for (MatchingPair pair : this.matchingItems) {
            builder.append("=")
                    .append(pair.isAnswerUnmatched() ? "" : pair.prompt())
                    .append(" -> ")
                    .append(pair.answer());
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public Element toXMLElement(Document doc) {
        Element question = XMLUtil.question(doc, "matching");
        super.appendStandardXMLChilds(doc, question);
        for (MatchingPair pair : this.matchingItems) {
            Element subQ = XMLUtil.moodleText(doc, "subquestion", "html", pair.isAnswerUnmatched() ? "" : pair.prompt());
            XMLUtil.append(subQ, XMLUtil.moodleText(doc, "answer", pair.answer()));
            XMLUtil.append(question, subQ);
        }
        return question;
    }
}

record MatchingPair(String prompt, String answer) {
    public boolean isAnswerUnmatched() {
        return this.prompt == null || this.prompt.trim().isEmpty();
    }
}