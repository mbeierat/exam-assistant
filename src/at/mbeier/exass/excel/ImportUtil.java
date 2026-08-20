package at.mbeier.exass.excel;

import at.mbeier.exass.model.Category;
import at.mbeier.exass.model.QuestionType;
import org.apache.poi.ss.usermodel.CellType;

import java.util.ArrayList;
import java.util.List;

public final class ImportUtil {

    private ImportUtil() {}

    public static List<Category> importSheet(ExcelFile file) {
        List<Category> categories = new ArrayList<>();
        List<ExcelRow> rows = file.getRows();
        for (ExcelRow row : rows) {
            List<CellValue> cells = row.getCells();
            if (cells.getFirst().getType() != CellType.STRING) {
                throw new IllegalArgumentException("Row " + row.getIndex() + " Column 1 (Category Name) needs to be a string");
            }
            String catName = (String) cells.getFirst().getContent();
            if (cells.getFirst().getType() != CellType.STRING) {
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
        }
        return categories;
    }
}
