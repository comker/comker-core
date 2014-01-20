package net.cokkee.comker.model.po;

import java.util.Collection;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author drupalex
 */
@XmlRootElement
@Entity
@Table(name = "comker_permission",
uniqueConstraints = @UniqueConstraint(columnNames = "f_authority"))
public class ComkerPermission extends ComkerAbstractItem {

    private Long id;

    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "f_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    private String authority;

    @Column(name = "f_authority", unique = true, nullable = false, length = 36)
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
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
