package net.cokkee.comker.model.dpo;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author drupalex
 */
@Entity
@Table(name = "comker_user")
public class ComkerUserDPO extends ComkerAbstractDPO {

    public static final String UNKNOWN = "__UNKNOWN_USER__";
    public static final String DEFAULT = "__DEFAULT_USER__";

    public ComkerUserDPO() {
        super();
    }

    public ComkerUserDPO(String email, String username, String password, String fullname) {
        super();
        this.email = email;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
    }

    private String id;
    private String email;
    private String username;
    private String password;
    private String fullname;
    private boolean enabled = true;
    private boolean accountExpired = false;
    private boolean accountLocked = false;
    private boolean passwordExpired = false;

    private List<ComkerUserJoinCrewDPO> userJoinCrewList =
            new LinkedList<ComkerUserJoinCrewDPO>();

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

    @Column(name = "f_email", unique = true, nullable = false, length = 64)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "f_username", unique = true, nullable = false, length = 64)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "f_password", unique = false, nullable = false, length = 64)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "f_fullname", unique = false, nullable = false, length = 64)
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Column(name = "f_enabled", unique = false, nullable = false)
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.user", cascade=CascadeType.ALL, orphanRemoval=true)
    public List<ComkerUserJoinCrewDPO> getUserJoinCrewList() {
        return userJoinCrewList;
    }

    public void setUserJoinCrewList(List<ComkerUserJoinCrewDPO> list) {
        this.userJoinCrewList = list;
    }
}
