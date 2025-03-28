package at.mbeier.examassistant.model;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelFile {

    public static final int CATEGORY_COLUMN = 0;
    public static final int QUESTION_TYPE_COLUMN = 1;
    public static final int TITLE_COLUMN = 2;
    public static final int QUESTION_COLUMN = 3;
    public static final int POINT_COLUMN = 4;
    public static final int CHAPTER_COLUMN = 5;
    public static final int COMMENT_COLUMN = 6;
    public static final int ANSWER_COLUMN = 7;

    private final List<Row> questionRows;

    public ExcelFile(String filePath) {
        this.questionRows = new ArrayList<>();
        try (Workbook wb = WorkbookFactory.create(new File(filePath))){
            Sheet sheet = wb.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                if (row.getCell(0).getStringCellValue().charAt(0) != '#') continue;
                this.questionRows.add(row);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not open excel file: " + filePath, e);
        }
    }

    public List<Row> getQuestionRows() {
        return this.questionRows;
    }
}
