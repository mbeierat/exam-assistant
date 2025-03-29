package at.mbeier.examassistant.model.questions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EssayQuestion extends Question {

    public EssayQuestion(String title, String question, String points, String[] answers) {
        super(title, question, points);
        if (answers != null) {
            if (answers.length > 0) throw new IllegalArgumentException("Answer values for Essay Questions are not supported.");
        }
    }

    @Override
    public String toGIFTString() {
        return super.toGIFTString() + "{}";
    }

    @Override
    public void appendXMLElements(Document doc, Element parent) {
        Element question = doc.createElement("question");
        question.setAttribute("type", "essay");
        parent.appendChild(question);
        super.appendXMLElements(doc, question);
        Element answer = doc.createElement("answer");
        answer.setAttribute("fraction", "0");
        question.appendChild(answer);
        Element answerText = doc.createElement("text");
        answer.appendChild(answerText);
    }

    @Override
    public String toString() {
        return "EssayQuestion{" + super.toString() + '}';
    }
}
