package net.cokkee.comker.model.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class ComkerRegistrationDTO extends ComkerAbstractDTO {
    
    public static final int OK = 0;
    public static final int CONFIRMATION_NOT_FOUND = -1;
    public static final int USER_HAS_BEEN_REGISTED = 1;
    
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public ComkerRegistrationDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ComkerRegistrationDTO setPassword(String password) {
        this.password = password;
        return this;
    }
}
