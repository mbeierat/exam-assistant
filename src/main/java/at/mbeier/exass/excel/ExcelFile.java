package at.mbeier.exass.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelFile implements AutoCloseable {

    private final Workbook wb;
    private final List<Sheet> sheets;

    public ExcelFile(String path, String... names) {
        try (FileInputStream fis = new FileInputStream(path)) {
            boolean xlsx = path.contains(".xlsx");
            this.wb = xlsx ? new XSSFWorkbook(fis) : new HSSFWorkbook(fis);
        } catch (IOException ex) {
            throw new RuntimeException("Couldnt read file");
        }
        this.sheets = new ArrayList<>();
        for (String n : names) {
            if (this.wb.getSheet(n) == null) {
                throw new IllegalArgumentException("No sheet named '" + n + "' in this workbook");
            }
            this.sheets.add(this.wb.getSheet(n));
        }
    }

    public List<Sheet> getSheets() {
        return this.sheets;
    }

    public Workbook getWorkbook() {
        return this.wb;
    }

    public List<ExcelRow> getRows() {
        List<ExcelRow> rows = new ArrayList<>();
        for (Sheet sheet : this.sheets) {
            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                ExcelRow er = new ExcelRow(sheet.getRow(i));
                if (er.isEmpty()) continue;
                rows.add(er);
            }
        }
        return rows;
    }

    @Override
    public void close() throws IOException {
        this.wb.close();
    }
}
