package net.cokkee.comker.model.po;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
@Entity
@Table(name = "comker_permission",
uniqueConstraints = @UniqueConstraint(columnNames = "f_authority"))
public class ComkerPermission extends ComkerAbstractItem {

    private String id;
    private String authority;
    private List<ComkerRoleJoinPermission> roleJoinPermission = new LinkedList<ComkerRoleJoinPermission>();

    @Id
	@Column(name = "f_id", unique = true, nullable = false, length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name = "f_authority", unique = true, nullable = false, length = 36)
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.permission")
    public List<ComkerRoleJoinPermission> getRoleJoinPermission() {
        return roleJoinPermission;
    }

    public void setRoleJoinPermission(List<ComkerRoleJoinPermission> roleJoinPermission) {
        this.roleJoinPermission = roleJoinPermission;
    }

    public interface Dao {

        Integer count();

        Integer count(String query, Map params);

        Integer count(ComkerPermission.Criteria criteria);

        Collection findAll(ComkerPermission.Filter filter);
        
        Collection findAll(String query, Map params, ComkerPermission.Filter filter);

        Collection findAll(ComkerPermission.Criteria criteria, ComkerPermission.Filter filter);

        ComkerPermission load(String id);

        void save(ComkerPermission item);

        void create(ComkerPermission item);

        void delete(String id);
    }
}
