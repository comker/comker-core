package net.cokkee.comker.model.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class ComkerNavNodeFormDTO extends ComkerNavNodeDTO {

    public ComkerNavNodeFormDTO() {
        super();
    }

    public ComkerNavNodeFormDTO(String code, String url, String[] permissions) {
        super(code, url, permissions);
    }

    public ComkerNavNodeFormDTO(String code, String url, String[] permissions,
            String label, String description) {
        super(code, url, permissions, label, description);
    }

    private String parentId = null;
    
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
