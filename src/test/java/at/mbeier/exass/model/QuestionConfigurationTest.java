package at.mbeier.exass.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionConfigurationTest {

    @Test
    void everyPropertyIsPrepopulatedWithItsDefaultOnConstruction() {
        QuestionConfiguration config = new QuestionConfiguration();

        for (QuestionProperty property : QuestionProperty.values()) {
            assertTrue(config.isSet(property));
            assertEquals(property.getDefaultValue(), config.get(property));
        }
    }

    @Test
    void setOverridesTheStoredValue() {
        QuestionConfiguration config = new QuestionConfiguration();
        config.set(QuestionProperty.SELECT_COUNT, "5");
        assertEquals("5", config.get(QuestionProperty.SELECT_COUNT));
    }

    @Test
    void setDefaultRestoresEveryOverride() {
        QuestionConfiguration config = new QuestionConfiguration();
        config.set(QuestionProperty.SELECT_COUNT, "5");

        config.setDefault();

        assertEquals(QuestionProperty.SELECT_COUNT.getDefaultValue(), config.get(QuestionProperty.SELECT_COUNT));
    }

    @Test
    void getConfigForTypeOnlyReturnsCompatibleProperties() {
        QuestionConfiguration config = new QuestionConfiguration();

        assertTrue(config.getConfigForType(QuestionType.ORDER).containsKey(QuestionProperty.SELECT_COUNT));
        assertFalse(config.getConfigForType(QuestionType.ORDER).containsKey(QuestionProperty.SHUFFLE_ANSWERS));

        assertTrue(config.getConfigForType(QuestionType.SINGLE_CHOICE).containsKey(QuestionProperty.SHUFFLE_ANSWERS));
        assertFalse(config.getConfigForType(QuestionType.SINGLE_CHOICE).containsKey(QuestionProperty.SELECT_COUNT));
    }
}
