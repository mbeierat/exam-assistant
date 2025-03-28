package at.mbeier.examassistant.model;

import at.mbeier.examassistant.model.questions.Question;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class Category implements GIFTFormattable, MoodleXMLFormattable {

    private final String name;
    private final List<Question> questions;

    public Category(String name) {
        this.name = name;
        this.questions = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    @Override
    public String toGIFTString() {
        StringBuilder sb = new StringBuilder("$CATEGORY: $course$/top/").append(this.name);
        sb.append(GIFTFormattable.GIFT_CATEGORY_SEPERATOR);
        this.questions.forEach(question -> sb.append(question.toGIFTString()).append(GIFTFormattable.GIFT_CATEGORY_SEPERATOR));
        return sb.toString();
    }

    @Override
    public void appendXMLElements(Document doc, Element parent) {
        Element question = doc.createElement("question");
        question.setAttribute("type", "category");
        Element category = doc.createElement("category");
        Element text = doc.createElement("text");
        text.setNodeValue("$course$/top/" + this.name);
        question.appendChild(category);
        category.appendChild(text);
        Element info = doc.createElement("info");
        info.setAttribute("format", "html");
        Element text1 = doc.createElement("text");
        question.appendChild(info);
        info.appendChild(text1);
        for (Question q : this.questions) {
            q.appendXMLElements(doc, parent);
        }
    }
}
