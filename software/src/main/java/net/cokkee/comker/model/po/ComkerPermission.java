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

    public ComkerPermission() {
        super();
    }

    public ComkerPermission(String authority) {
        super();
        this.authority = authority;
    }

    private String id;
    private String authority;

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
    
    @Column(name = "f_authority", unique = true, nullable = false, length = 36)
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
