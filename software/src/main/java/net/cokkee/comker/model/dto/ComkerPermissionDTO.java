package net.cokkee.comker.model.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class ComkerPermissionDTO extends ComkerAbstractDTO {

    public ComkerPermissionDTO() {
        super();
    }

    public ComkerPermissionDTO(String authority) {
        super();
        this.authority = authority;
    }

    private String id;
    private String authority;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
