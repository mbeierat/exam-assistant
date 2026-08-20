package at.mbeier.exass;

import at.mbeier.exass.excel.ExcelRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * In-memory row/workbook builders shared by tests, so no test needs a
 * checked-in .xlsx fixture file just to exercise ExcelRow-based parsing.
 */
public final class TestSupport {

    private TestSupport() {
    }

    public static Workbook newWorkbook() {
        return new XSSFWorkbook();
    }

    /** Builds a raw POI row with typed cells: String -> STRING, Boolean -> BOOLEAN, Number -> NUMERIC. */
    public static Row rawRow(Sheet sheet, int rowNum, Object... values) {
        Row row = sheet.createRow(rowNum);
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            Object value = values[i];
            if (value instanceof String s) {
                cell.setCellValue(s);
            } else if (value instanceof Boolean b) {
                cell.setCellValue(b);
            } else if (value instanceof Number n) {
                cell.setCellValue(n.doubleValue());
            } else if (value != null) {
                throw new IllegalArgumentException("Unsupported cell value type: " + value.getClass());
            }
        }
        return row;
    }

    public static ExcelRow row(Sheet sheet, int rowNum, Object... values) {
        return new ExcelRow(rawRow(sheet, rowNum, values));
    }
}
