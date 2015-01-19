package net.cokkee.comker.model.msg;

/**
 *
 * @author drupalex
 */
public class ComkerMailMessage extends ComkerMailAddress {
    
    private String subject;
    private String content;

    private boolean htmlContentType = true;
    private boolean contentEmptiable = false;
    
    public String getSubject() {
        return subject;
    }

    public ComkerMailMessage setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getContent() {
        if (content == null && isContentEmptiable()) {
            return "";
        }
        return content;
    }

    public ComkerMailMessage setContent(String content) {
        this.content = content;
        return this;
    }

    public boolean isHtmlContentType() {
        return htmlContentType;
    }

    public ComkerMailMessage setHtmlContentType(boolean htmlContentType) {
        this.htmlContentType = htmlContentType;
        return this;
    }

    public boolean isContentEmptiable() {
        return contentEmptiable;
    }

    public ComkerMailMessage setContentEmptiable(boolean emptiable) {
        this.contentEmptiable = emptiable;
        return this;
    }
}
