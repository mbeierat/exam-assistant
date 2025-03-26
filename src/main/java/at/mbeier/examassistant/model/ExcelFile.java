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
