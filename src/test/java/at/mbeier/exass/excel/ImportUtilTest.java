package at.mbeier.exass.excel;

import at.mbeier.exass.TestSupport;
import at.mbeier.exass.model.Category;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImportUtilTest {

    @TempDir
    Path tempDir;

    private ExcelFile writeAndOpen(Workbook wb, String sheetName) throws IOException {
        Path file = tempDir.resolve("import-" + System.nanoTime() + ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(file.toFile())) {
            wb.write(fos);
        }
        wb.close();
        return new ExcelFile(file.toString(), sheetName);
    }

    @Test
    void collectsAllRowErrorsAndGroupsValidRowsByCategory() throws IOException {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        TestSupport.rawRow(sheet, 0, "Chapter1", "Wahr/Falsch", "Q1", "Sky blue?", 1, true);
        TestSupport.rawRow(sheet, 1, "Chapter1", "Wahr/Falsch", "Q2", "Grass green?", 1, false);
        TestSupport.rawRow(sheet, 2, "#a commented-out row, must be skipped entirely");
        TestSupport.rawRow(sheet, 3, 123, "Wahr/Falsch", "Q3", "Bad category", 1, true);
        TestSupport.rawRow(sheet, 4, "Chapter2", "NotARealType", "Q4", "Bad type", 1, true);
        // Sacrificial: ExcelFile#getRows loops "i < sheet.getLastRowNum()",
        // which excludes the sheet's actual last row - so this row (and only
        // this row) is silently never read. See sheetsLastDataRowIsDropped.
        TestSupport.rawRow(sheet, 5);

        try (ExcelFile file = writeAndOpen(wb, "Sheet1")) {
            ImportResult result = ImportUtil.importSheet(file);

            assertEquals(1, result.categories().size());
            Category chapter1 = result.categories().getFirst();
            assertEquals("Chapter1", chapter1.getName());
            assertEquals(2, chapter1.getQuestions().size());

            assertEquals(2, result.errors().size());
            assertTrue(result.errors().stream().anyMatch(e -> e.contains("Category Name")));
            assertTrue(result.errors().stream().anyMatch(e -> e.contains("Unknown question type")));
        }
    }

    @Test
    void sheetsLastDataRowIsDropped() throws IOException {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        TestSupport.rawRow(sheet, 0, "Chapter1", "Wahr/Falsch", "Q1", "Sky blue?", 1, true);

        try (ExcelFile file = writeAndOpen(wb, "Sheet1")) {
            ImportResult result = ImportUtil.importSheet(file);

            assertEquals(0, result.categories().size());
            assertEquals(0, result.errors().size());
        }
    }

    @Test
    void allBadRowsYieldNoCategoriesOnlyErrors() throws IOException {
        Workbook wb = TestSupport.newWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        TestSupport.rawRow(sheet, 0, 123, "Wahr/Falsch", "Q1", "Bad category", 1, true);
        TestSupport.rawRow(sheet, 1);

        try (ExcelFile file = writeAndOpen(wb, "Sheet1")) {
            ImportResult result = ImportUtil.importSheet(file);

            assertEquals(0, result.categories().size());
            assertEquals(1, result.errors().size());
        }
    }
}
