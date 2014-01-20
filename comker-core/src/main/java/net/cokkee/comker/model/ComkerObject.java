package net.cokkee.comker.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */

@XmlRootElement
public class ComkerObject implements Serializable {

    public ComkerObject() {
        super();
    }

    public ComkerObject(int code) {
        this();
        this.initStatus().setCode(code);
    }

    public ComkerObject(int code, String message) {
        this();
        this.initStatus().setCode(code);
        this.initStatus().setMessage(message);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    private ComkerStatus status = null;

    public ComkerStatus getStatus() {
        return status;
    }

    public void setStatus(ComkerStatus status) {
        this.status = status;
    }

    public final ComkerStatus initStatus() {
        if (status == null) {
            status = new ComkerStatus();
        }
        return status;
    }
}
