package net.cokkee.comker.model.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class ComkerSpotDTO extends ComkerAbstractDTO {

    public static final String UNKNOWN = "__UNKNOWN_SPOT__";
    public static final String DEFAULT = "__DEFAULT_SPOT__";

    public ComkerSpotDTO() {
        super();
    }

    public ComkerSpotDTO(String code, String name, String description) {
        super();
        this.code = code;
        this.name = name;
        this.description = description;
    }

    private String id;
    private String code;
    private String name;
    private String description;
    private String[] moduleIds;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getModuleIds() {
        return moduleIds;
    }

    public void setModuleIds(String[] moduleIds) {
        this.moduleIds = moduleIds;
    }
}
