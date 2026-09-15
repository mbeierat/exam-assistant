package at.mbeier.exass.excel;

import at.mbeier.exass.TestSupport;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExcelRowTest {

    @Test
    void readsTypedCellsInOrder() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0, "A", 1, true);

        assertEquals(3, row.getCells().size());
        assertEquals(CellType.STRING, row.getCells().get(0).getType());
        assertEquals(CellType.NUMERIC, row.getCells().get(1).getType());
        assertEquals(CellType.BOOLEAN, row.getCells().get(2).getType());
    }

    @Test
    void aBlankCellIsKeptAndDoesNotTruncateLaterCells() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0, "A", "B", null, "D");

        assertEquals(4, row.getCells().size());
        assertEquals(CellType.BLANK, row.getCells().get(2).getType());
        assertNull(row.getCells().get(2).getContent());
        assertEquals("D", row.getCells().get(3).getContent());
    }

    @Test
    void aRowWithOnlyOneBlankCellIsNotEmpty() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0, (Object) null);

        assertEquals(1, row.getCells().size());
        assertEquals(CellType.BLANK, row.getCells().getFirst().getType());
        assertFalse(row.isEmpty());
    }

    @Test
    void aRowWithNoCellsAtAllIsEmpty() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0);

        assertTrue(row.isEmpty());
    }
}
