package net.cokkee.comker.model.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class ComkerRoleDTO extends ComkerAbstractDTO {

    public ComkerRoleDTO() {
        super();
    }
    
    public ComkerRoleDTO(String code, String name, String description, Boolean global) {
        super();
        this.code = code;
        this.name = name;
        this.description = description;
        this.global = global;
    }
    
    public ComkerRoleDTO(String id, String code, String name, String description, Boolean global) {
        this(code, name, description, global);
        this.id = id;
    }
    
    private String id;
    private String code;
    private String name;
    private String description;
    private String[] permissionIds;
    private Boolean global = Boolean.TRUE;
    
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

    @XmlElement(name = "permissionIds")
    public String[] getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(String[] permissionIds) {
        this.permissionIds = permissionIds;
    }

    public Boolean isGlobal() {
        return global;
    }

    public void setGlobal(Boolean global) {
        this.global = global;
    }
    
    @XmlRootElement
    public static class Pack {

        public Pack() {
        }

        public Pack(Integer total, List<ComkerRoleDTO> collection) {
            this();
            this.total = total;
            this.collection = collection;
        }

        private Integer total = null;

        private List<ComkerRoleDTO> collection = null;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<ComkerRoleDTO> getCollection() {
            return collection;
        }

        public void setCollection(List<ComkerRoleDTO> collection) {
            this.collection = collection;
        }
    }
}
