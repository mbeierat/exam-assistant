package at.mbeier.exass.excel;

import at.mbeier.exass.model.Category;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionType;
import org.apache.poi.ss.usermodel.CellType;

import java.util.ArrayList;
import java.util.List;

public final class ImportUtil {

    private ImportUtil() {}

    public static ImportResult importSheet(ExcelFile file) {
        List<Category> categories = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        List<ExcelRow> rows = file.getRows();
        for (ExcelRow row : rows) {
            try {
                List<ExcelCell> cells = row.getCells();
                if (cells.getFirst().getType() != CellType.STRING) {
                    throw new IllegalArgumentException("Row " + row.getIndex() + " Column 1 (Category Name) needs to be a string");
                }
                String catName = (String) cells.getFirst().getContent();
                if (cells.get(1).getType() != CellType.STRING) {
                    throw new IllegalArgumentException("Row " + row.getIndex() + " Column 2 (Question ID) needs to be a string");
                }
                String questionId = (String) cells.get(1).getContent();
                boolean contains = false;
                for (Category cat : categories) {
                    if (cat.getName().equals(catName)) {
                        cat.addQuestion(QuestionType.fromId(questionId).parse(row));
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    Category category = new Category(catName);
                    category.addQuestion(QuestionType.fromId(questionId).parse(row));
                    categories.add(category);
                }
            } catch (Exception e) {
                errors.add(e.getMessage());
            }
        }
        return new ImportResult(categories, errors);
    }

    public static void setStandardQuestionFields(Question q, ExcelRow row) {
        if (row.getCells().get(2).getType() != CellType.STRING)
            throw new IllegalArgumentException("Row " + row.getIndex() + " Column 3 (Question Name) needs to be a string");
        String qname = (String) row.getCells().get(2).getContent();
        q.setTitle(qname);
        if (row.getCells().get(3).getType() != CellType.STRING)
            throw new IllegalArgumentException("Row " + row.getIndex() + " Column 4 (Question Text) needs to be a string");
        String qtext = (String) row.getCells().get(3).getContent();
        q.setText(qtext);
        if (row.getCells().get(4).getType() != CellType.NUMERIC)
            throw new IllegalArgumentException("Row " + row.getIndex() + " Column 5 (Points) needs to be a number");
        int points = ((Number) row.getCells().get(4).getContent()).intValue();
        q.setPoints(points);
    }
}
