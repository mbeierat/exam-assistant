package at.mbeier.exass.model.questions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculateMultipleChoiceTest {

    @Test
    void everyOperationIsUnsupported() {
        CalculateMultipleChoice question = new CalculateMultipleChoice();
        assertThrows(UnsupportedOperationException.class, () -> question.createFrom(null));
        assertThrows(UnsupportedOperationException.class, question::toGIFTString);
        assertThrows(UnsupportedOperationException.class, () -> question.toXMLElement(null));
    }
}
