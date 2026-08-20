package at.mbeier.exass.controller;

import at.mbeier.exass.excel.ExcelFile;
import at.mbeier.exass.excel.ImportResult;
import at.mbeier.exass.excel.ImportUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImportController {

    public List<String> listSheetNames(File excelFile) throws IOException {
        List<String> names = new ArrayList<>();
        try (Workbook wb = WorkbookFactory.create(excelFile)) {
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                names.add(wb.getSheetName(i));
            }
        }
        return names;
    }

    public ImportResult load(File excelFile, String sheetName) {
        try (ExcelFile file = new ExcelFile(excelFile.getPath(), sheetName)) {
            return ImportUtil.importSheet(file);
        } catch (IOException e) {
            return new ImportResult(List.of(), List.of("Could not read file: " + e.getMessage()));
        }
    }
}
