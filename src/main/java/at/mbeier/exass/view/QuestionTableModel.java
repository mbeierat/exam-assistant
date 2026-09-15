package at.mbeier.exass.view;

import at.mbeier.exass.model.Category;
import at.mbeier.exass.model.Question;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class QuestionTableModel extends AbstractTableModel {

    private static final String[] COLUMNS = {"Category", "Type", "Title", "Points"};

    private record Row(String category, String type, String title, int points) {}

    private final List<Row> rows = new ArrayList<>();

    public void setCategories(List<Category> categories) {
        this.rows.clear();
        for (Category category : categories) {
            for (Question question : category.getQuestions()) {
                this.rows.add(new Row(
                        category.getName(),
                        question.getType() == null ? "" : question.getType().getId(),
                        question.getTitle(),
                        question.getPoints()
                ));
            }
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return this.rows.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Row row = this.rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> row.category();
            case 1 -> row.type();
            case 2 -> row.title();
            case 3 -> row.points();
            default -> null;
        };
    }
}
