package net.cokkee.comker.model.msg;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import net.cokkee.comker.model.error.ComkerResolvableMessage;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class ComkerInformationResponse implements Serializable {

    private String message;
    private ComkerResolvableMessage messageOrigin;

    public String getMessage() {
        return message;
    }

    public ComkerInformationResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public ComkerResolvableMessage getMessageOrigin() {
        return messageOrigin;
    }

    public ComkerInformationResponse setMessageOrigin(ComkerResolvableMessage messageOrigin) {
        this.messageOrigin = messageOrigin;
        return this;
    }
}
