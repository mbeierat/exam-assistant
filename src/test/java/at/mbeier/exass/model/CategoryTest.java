package at.mbeier.exass.model;

import at.mbeier.exass.model.questions.Description;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryTest {

    @Test
    void startsEmptyAndTracksAddedQuestions() {
        Category category = new Category("Chapter1");
        Question question = new Description("Q1", "Some text");

        category.addQuestion(question);

        assertEquals("Chapter1", category.getName());
        assertEquals(1, category.getQuestions().size());
        assertTrue(category.getQuestions().contains(question));
    }

    @Test
    void removeQuestionReturnsWhetherItWasPresent() {
        Category category = new Category("Chapter1");
        Question question = new Description("Q1", "Some text");
        category.addQuestion(question);

        assertTrue(category.removeQuestion(question));
        assertFalse(category.removeQuestion(question));
        assertEquals(0, category.getQuestions().size());
    }

    @Test
    void setNameChangesTheName() {
        Category category = new Category("Chapter1");
        category.setName("Chapter2");
        assertEquals("Chapter2", category.getName());
    }
}
