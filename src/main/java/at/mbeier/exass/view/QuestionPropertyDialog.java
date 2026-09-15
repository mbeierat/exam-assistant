package at.mbeier.exass.view;

import at.mbeier.exass.model.QuestionConfiguration;
import at.mbeier.exass.model.QuestionProperty;
import at.mbeier.exass.model.QuestionType;
import at.mbeier.exass.model.properties.AnswerNumbering;
import at.mbeier.exass.model.properties.GradingType;
import at.mbeier.exass.model.properties.LayoutType;
import at.mbeier.exass.model.properties.NumberingStyle;
import at.mbeier.exass.model.properties.SelectType;
import at.mbeier.exass.model.properties.ShowGrading;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Lets the user override every {@link QuestionProperty} value for the whole
 * import run. All questions of a run share one {@link QuestionConfiguration}
 * instance, so a change made here is picked up by every compatible question
 * at export time. Fields start out showing that configuration's current
 * values, which are the property defaults until edited.
 */
@SuppressWarnings("serial")
public class QuestionPropertyDialog extends JDialog {

    private record Field(QuestionProperty property, Consumer<String> valueSetter) {}

    private final QuestionConfiguration config;
    private final List<Field> fields = new ArrayList<>();

    public QuestionPropertyDialog(Frame owner, QuestionConfiguration config) {
        super(owner, "Question Properties", true);
        this.config = config;

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        for (QuestionProperty property : QuestionProperty.values()) {
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            JLabel propertyLabel = new JLabel(formatLabel(property));
            propertyLabel.setToolTipText("Applies to: " + compatibleTypeNames(property));
            form.add(propertyLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            form.add(buildEditor(property), gbc);
            row++;
        }

        JButton resetButton = new JButton("Reset to Defaults");
        resetButton.addActionListener(e -> resetToDefaults());

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(resetButton);
        buttons.add(closeButton);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    private JComponent buildEditor(QuestionProperty property) {
        return switch (property) {
            case STANDARD_INSTRUCTION -> booleanEditor(property, "1", "0");
            case SHUFFLE_ANSWERS -> booleanEditor(property, "true", "false");
            case USE_CASE -> booleanEditor(property, "1", "0");
            case RESPONSE_REQUIRED -> booleanEditor(property, "1", "0");
            case RESPONSE_FIELD_LINES, SELECT_COUNT -> numericEditor(property);
            case ANSWER_NUMBERING -> enumEditor(property, AnswerNumbering.values(), AnswerNumbering::getName);
            case LAYOUT_TYPE -> enumEditor(property, LayoutType.values(), LayoutType::getName);
            case SELECT_TYPE -> enumEditor(property, SelectType.values(), SelectType::getName);
            case GRADING_TYPE -> enumEditor(property, GradingType.values(), GradingType::getName);
            case SHOW_GRADING -> enumEditor(property, ShowGrading.values(), ShowGrading::getName);
            case NUMBERING_STYLE -> enumEditor(property, NumberingStyle.values(), NumberingStyle::getName);
            case RESPONSE_FORMAT -> textEditor(property);
        };
    }

    private JComponent booleanEditor(QuestionProperty property, String onValue, String offValue) {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(onValue.equals(this.config.get(property)));
        checkBox.addActionListener(e -> this.config.set(property, checkBox.isSelected() ? onValue : offValue));
        this.fields.add(new Field(property, value -> checkBox.setSelected(onValue.equals(value))));
        return checkBox;
    }

    private JComponent numericEditor(QuestionProperty property) {
        int initial = parseIntOrDefault(this.config.get(property), property);
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(initial, 0, 999, 1));
        spinner.addChangeListener(e -> this.config.set(property, spinner.getValue().toString()));
        this.fields.add(new Field(property, value -> spinner.setValue(parseIntOrDefault(value, property))));
        return spinner;
    }

    private <E> JComponent enumEditor(QuestionProperty property, E[] values, Function<E, String> representation) {
        JComboBox<String> combo = new JComboBox<>();
        for (E value : values) {
            combo.addItem(representation.apply(value));
        }
        combo.setSelectedItem(this.config.get(property));
        combo.addActionListener(e -> this.config.set(property, (String) combo.getSelectedItem()));
        this.fields.add(new Field(property, combo::setSelectedItem));
        return combo;
    }

    private JComponent textEditor(QuestionProperty property) {
        JTextField textField = new JTextField(this.config.get(property), 12);
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { apply(); }

            @Override
            public void removeUpdate(DocumentEvent e) { apply(); }

            @Override
            public void changedUpdate(DocumentEvent e) { apply(); }

            private void apply() {
                QuestionPropertyDialog.this.config.set(property, textField.getText());
            }
        });
        this.fields.add(new Field(property, textField::setText));
        return textField;
    }

    private void resetToDefaults() {
        for (Field field : this.fields) {
            String defaultValue = field.property().getDefaultValue();
            this.config.set(field.property(), defaultValue);
            field.valueSetter().accept(defaultValue);
        }
    }

    private static int parseIntOrDefault(String value, QuestionProperty property) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return Integer.parseInt(property.getDefaultValue());
        }
    }

    private static String formatLabel(QuestionProperty property) {
        String[] words = property.name().toLowerCase().split("_");
        return java.util.Arrays.stream(words)
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    private static String compatibleTypeNames(QuestionProperty property) {
        return java.util.Arrays.stream(property.getCompatibleTypes())
                .map(QuestionType::getId)
                .collect(Collectors.joining(", "));
    }
}
