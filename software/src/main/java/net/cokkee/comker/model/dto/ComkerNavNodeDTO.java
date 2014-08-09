package net.cokkee.comker.model.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class ComkerNavNodeDTO extends ComkerAbstractDTO {

    public ComkerNavNodeDTO() {
        super();
        this.enabled = true;
    }

    public ComkerNavNodeDTO(String code, String url, String[] permissions) {
        this();
        this.code = code;
        this.url = url;
        this.permissions = permissions;
    }

    public ComkerNavNodeDTO(String code, String url, String[] permissions,
            String label, String description) {
        this(code, url, permissions);
        this.label = label;
        this.description = description;
    }

    private String id;
    private String code;
    private String url;
    private Boolean enabled;
    private String label;
    private String description;

    private String[] permissions = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }
}
