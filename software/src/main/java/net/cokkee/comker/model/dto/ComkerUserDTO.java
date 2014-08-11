package net.cokkee.comker.model.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class ComkerUserDTO extends ComkerAbstractDTO {

    public static final String UNKNOWN = "__UNKNOWN_USER__";
    public static final String DEFAULT = "__DEFAULT_USER__";

    public ComkerUserDTO() {
        super();
    }

    public ComkerUserDTO(String email, String username, String password, String fullname) {
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
    private boolean enabled;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean passwordExpired;

    private String[] crewIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isPasswordExpired() {
        return passwordExpired;
    }

    public void setPasswordExpired(boolean passwordExpired) {
        this.passwordExpired = passwordExpired;
    }

    public String[] getCrewIds() {
        return crewIds;
    }

    public void setCrewIds(String[] crewIds) {
        this.crewIds = crewIds;
    }

    @XmlRootElement
    public static class Pack {

        public Pack() {
        }

        public Pack(Integer total, List<ComkerUserDTO> collection) {
            this();
            this.total = total;
            this.collection = collection;
        }

        private Integer total = null;

        private List<ComkerUserDTO> collection = null;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<ComkerUserDTO> getCollection() {
            return collection;
        }

        public void setCollection(List<ComkerUserDTO> collection) {
            this.collection = collection;
        }
    }
}
