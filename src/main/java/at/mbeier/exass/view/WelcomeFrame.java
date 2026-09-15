package at.mbeier.exass.view;

import at.mbeier.exass.controller.ExportController;
import at.mbeier.exass.controller.ImportController;
import at.mbeier.exass.excel.ImportResult;
import at.mbeier.exass.model.Category;
import at.mbeier.exass.model.Question;
import at.mbeier.exass.model.QuestionConfiguration;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class WelcomeFrame extends JFrame {

    private final ImportController importController = new ImportController();
    private final ExportController exportController = new ExportController();
    private final QuestionTableModel tableModel = new QuestionTableModel();

    /**
     * Shared across the whole import run: every question loaded from the
     * Excel file is pointed at this same instance, so editing a value in the
     * QuestionPropertyDialog is immediately reflected for every question of
     * a compatible type at export time.
     */
    private final QuestionConfiguration questionConfiguration = new QuestionConfiguration();

    private List<Category> loadedCategories = List.of();

    private final JRadioButton giftRadio = new JRadioButton("GIFT", true);
    private final JRadioButton xmlRadio = new JRadioButton("Moodle XML");
    private final JButton exportButton = new JButton("Export...");
    private final JTextArea statusArea = new JTextArea(6, 40);

    public WelcomeFrame() {
        super("Moodle Exam Assistant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildToolbar(), BorderLayout.NORTH);
        add(new JScrollPane(new JTable(this.tableModel)), BorderLayout.CENTER);
        add(buildStatusArea(), BorderLayout.SOUTH);

        this.exportButton.setEnabled(false);

        setSize(800, 500);
        setLocationRelativeTo(null);
    }

    private JComponent buildToolbar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton openButton = new JButton("Open Excel...");
        openButton.addActionListener(e -> onOpen());
        panel.add(openButton);

        ButtonGroup group = new ButtonGroup();
        group.add(this.giftRadio);
        group.add(this.xmlRadio);
        panel.add(this.giftRadio);
        panel.add(this.xmlRadio);

        this.exportButton.addActionListener(e -> onExport());
        panel.add(this.exportButton);

        JButton propertiesButton = new JButton("Question Properties...");
        propertiesButton.addActionListener(e -> onEditQuestionProperties());
        panel.add(propertiesButton);

        return panel;
    }

    private void onEditQuestionProperties() {
        new QuestionPropertyDialog(this, this.questionConfiguration).setVisible(true);
    }

    private JComponent buildStatusArea() {
        this.statusArea.setEditable(false);
        return new JScrollPane(this.statusArea);
    }

    private void onOpen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Excel files", "xlsx", "xls"));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = chooser.getSelectedFile();
        List<String> sheetNames;
        try {
            sheetNames = this.importController.listSheetNames(file);
        } catch (IOException ex) {
            showErrors(List.of("Could not open file: " + ex.getMessage()));
            return;
        }
        String sheetName = sheetNames.size() == 1
                ? sheetNames.getFirst()
                : chooseSheetName(sheetNames);
        if (sheetName == null) {
            return;
        }

        ImportResult result = this.importController.load(file, sheetName);
        if (result.hasErrors()) {
            showErrors(result.errors());
        } else {
            showSuccess(result.categories());
        }
    }

    /**
     * Only called when the import produced zero errors - this is the "only
     * load into the table if there are no errors" rule.
     */
    private void showSuccess(List<Category> categories) {
        for (Category category : categories) {
            for (Question question : category.getQuestions()) {
                question.setConfig(this.questionConfiguration);
            }
        }
        this.loadedCategories = categories;
        this.tableModel.setCategories(categories);
        this.exportButton.setEnabled(!categories.isEmpty());
        int total = categories.stream().mapToInt(c -> c.getQuestions().size()).sum();
        this.statusArea.setText(total + " questions across " + categories.size() + " categories loaded.");
    }

    /**
     * Called whenever the import found one or more problems - nothing gets
     * loaded into the table, every collected message is shown together.
     */
    private void showErrors(List<String> errors) {
        this.loadedCategories = List.of();
        this.tableModel.setCategories(this.loadedCategories);
        this.exportButton.setEnabled(false);
        this.statusArea.setText(errors.size() + " error(s) found, nothing loaded:\n" + String.join("\n", errors));
    }

    private String chooseSheetName(List<String> sheetNames) {
        return (String) JOptionPane.showInputDialog(this, "Choose a sheet", "Select Sheet",
                JOptionPane.QUESTION_MESSAGE, null, sheetNames.toArray(), sheetNames.getFirst());
    }

    private void onExport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showDialog(this, "Export") != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File folder = chooser.getSelectedFile();
        try {
            this.exportController.export(this.loadedCategories, folder.getPath(), this.xmlRadio.isSelected());
            this.statusArea.setText("Exported to " + folder.getPath());
        } catch (IOException ex) {
            this.statusArea.setText("Failed to export: " + ex.getMessage());
        }
    }
}
