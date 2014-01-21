package net.cokkee.comker.model.po;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author drupalex
 */
@XmlRootElement
@Entity
@Table(name = "comker_role")
public class ComkerRole extends ComkerAbstractItem {

    private String id;
    private String code;
    private String name;
    private String description;
    private List<ComkerRoleJoinPermission> roleJoinPermissionList = new LinkedList<ComkerRoleJoinPermission>();

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "f_id", unique = true, nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name = "f_code", unique = true, nullable = false, length = 255)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "f_description", unique = false, nullable = true, length = 1024)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "f_name", unique = false, nullable = false, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.role")
    public List<ComkerRoleJoinPermission> getRoleJoinPermissionList() {
        return roleJoinPermissionList;
    }

    public void setRoleJoinPermissionList(List<ComkerRoleJoinPermission> roleJoinPermission) {
        this.roleJoinPermissionList = roleJoinPermission;
    }

    // addPermission sets up bidirectional relationship
    public void addPermission(ComkerPermission permission) {
        ComkerRoleJoinPermission roleJoinPermission = new ComkerRoleJoinPermission();

        roleJoinPermission.setPk(new ComkerRoleJoinPermissionPk(this, permission));
        roleJoinPermission.setRole(this);
        roleJoinPermission.setPermission(permission);
        
        roleJoinPermissionList.add(roleJoinPermission);
    }
}
