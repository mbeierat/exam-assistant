package at.mbeier.exass.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;

public class ExcelRow {

    private List<ExcelCell> cells;
    private int index;

    public ExcelRow(Row row) {
        this.cells = new ArrayList<>();
        this.index = row.getRowNum();
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell c = row.getCell(i);
            if (c == null) {
                this.cells.add(new ExcelCell(i, null, CellType.BLANK));
                continue;
            }
            Object content = switch (c.getCellType()) {
                case NUMERIC -> c.getNumericCellValue();
                case STRING -> c.getStringCellValue();
                case BOOLEAN -> c.getBooleanCellValue();
                default -> null;
            };
            this.cells.add(new ExcelCell(i, content, c.getCellType()));
        }
    }

    public List<ExcelCell> getCells() {
        return cells;
    }

    public int getIndex() {
        return index;
    }

    public boolean isEmpty() {
        return this.cells.isEmpty();
    }
}

