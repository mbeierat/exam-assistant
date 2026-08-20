package at.mbeier.exass.excel;

import org.apache.poi.ss.usermodel.CellType;

public class CellValue {
    private int index;
    private Object content;
    private CellType type;

    public CellValue(int index, Object content, CellType type) {
        this.index = index;
        this.content = content;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public Object getContent() {
        return this.content;
    }

    public CellType getType() {
        return this.type;
    }
}
