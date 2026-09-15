package at.mbeier.exass.excel;

import at.mbeier.exass.TestSupport;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    void aBlankCellSilentlyTruncatesEverythingAfterIt() {
        // A blank cell maps to a null "content" in the constructor's switch,
        // and that null makes the scan break instead of just skipping this
        // one cell - so the blank cell itself, and every real value after
        // it, never make it into getCells() at all. There is no way to
        // produce a CellType.BLANK ExcelCell this way; callers that check
        // for one (Essay, Numerical, Match's prompt column, ...) can never
        // actually see it from a real Excel-sourced row.
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0, "A", "B", null, "D");

        assertEquals(2, row.getCells().size());
        assertFalse(row.getCells().stream().anyMatch(c -> c.getType() == CellType.BLANK));
    }

    @Test
    void isEmptyWhenTheVeryFirstCellIsBlank() {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet();
        ExcelRow row = TestSupport.row(sheet, 0, (Object) null);

        assertEquals(0, row.getCells().size());
    }
}
