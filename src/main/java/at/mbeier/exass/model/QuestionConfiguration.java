package at.mbeier.exass.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QuestionConfiguration {

    public Map<QuestionProperty, String> config;

    public QuestionConfiguration() {
        this.config = new HashMap<>();
        this.setDefault();
    }

    public void set(QuestionProperty property, String value) {
        this.config.put(property, value);
    }

    public String get(QuestionProperty property) {
        return this.config.get(property);
    }

    public Map<QuestionProperty, String> getConfigForType(QuestionType type) {
        Map<QuestionProperty, String> result = new HashMap<>();
        for (Map.Entry<QuestionProperty, String> entry : config.entrySet()) {
            if (Arrays.asList(entry.getKey().getCompatibleTypes()).contains(type)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    public boolean isSet(QuestionProperty property) {
        return this.config.containsKey(property);
    }

    public void setDefault() {
        for (QuestionProperty property : QuestionProperty.values()) {
            this.config.put(property, property.getDefaultValue());
        }
    }
}