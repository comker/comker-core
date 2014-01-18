package net.cokkee.comker.model.po;

import net.cokkee.comker.model.ComkerObject;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class ComkerQuestion extends ComkerObject {

    public static ComkerQuestion BAD = new ComkerQuestion(-1, "Error on load Question");

    public ComkerQuestion() {
        super();
    }

    public ComkerQuestion(int code, String message) {
        super(code, message);
    }

    public ComkerQuestion(String content, String explanation,
            ComkerAnswer[] answerArray) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.explanation = explanation;
        if (answerArray != null) {
            for(ComkerAnswer answer : answerArray) {
                this.answerList.add(answer);
            }
        }
    }

    private String id = null;
    private String content = null;
    private String explanation = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    private Set<ComkerAnswer> answerList = new HashSet<ComkerAnswer>();

    @XmlElement(name = "answerList")
    public Set<ComkerAnswer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(Set<ComkerAnswer> answerList) {
        this.answerList = answerList;
    }
}
