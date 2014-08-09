package net.cokkee.comker.model.dto;

import java.util.ArrayList;
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

    public ComkerRoleDTO(String code, String name, String description) {
        super();
        this.code = code;
        this.name = name;
        this.description = description;
    }
    private String id;
    private String code;
    private String name;
    private String description;
    private String[] permissionIds;
    private List<String> idsOfPermissionList = new ArrayList<String>();

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

    public List<String> getIdsOfPermissionList() {
        return idsOfPermissionList;
    }

    public void setIdsOfPermissionList(List<String> idsOfPermissionList) {
        this.idsOfPermissionList = idsOfPermissionList;
    }
}
