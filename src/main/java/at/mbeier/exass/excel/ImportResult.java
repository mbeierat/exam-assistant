package at.mbeier.exass.excel;

import at.mbeier.exass.model.Category;

import java.util.List;

/**
 * Outcome of an import attempt: either categories to load, or messages
 * explaining what's wrong with the sheet - never both meaningfully at once,
 * since the view only loads into the table when errors is empty.
 */
public record ImportResult(List<Category> categories, List<String> errors) {

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }
}
