package at.mbeier.exass.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;

public class ExcelRow {

    private List<CellValue> cells;
    private int index;

    public ExcelRow(Row row) {
        this.cells = new ArrayList<>();
        this.index = row.getRowNum();
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell c = row.getCell(i);
            if (c == null) {
                break;
            }
            Object content = switch (c.getCellType()) {
                case NUMERIC -> c.getNumericCellValue();
                case STRING -> c.getStringCellValue();
                case BOOLEAN -> c.getBooleanCellValue();
                default -> null;
            };
            if (content == null) {
                break;
            }
            this.cells.add(new CellValue(i, content, c.getCellType()));
        }
    }

    public List<CellValue> getCells() {
        return cells;
    }

    public int getIndex() {
        return index;
    }

    public boolean isEmpty() {
        return this.cells.isEmpty();
    }
}

