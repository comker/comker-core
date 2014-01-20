package net.cokkee.comker.model.po;

import net.cokkee.comker.model.ComkerObject;
import java.util.UUID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @mark drupalex
 */
@XmlRootElement
public class ComkerAnswer extends ComkerObject {

    public ComkerAnswer() {
        super();
    }

    public ComkerAnswer(String content, Integer mark, String note) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.mark = mark;
        this.note = note;
    }

    private String id = null;
    private String content = null;
    private Integer mark = null;
    private String note = null;

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
