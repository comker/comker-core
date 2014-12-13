package net.cokkee.comker.model.dpo;

import net.cokkee.comker.model.dpo.ComkerAbstractDPO;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import net.cokkee.comker.util.ComkerDataUtil;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author drupalex
 */
@XmlRootElement
@Entity
@Table(name = "comker_navbar_node")
public class ComkerNavbarNodeDPO extends ComkerAbstractDPO {

    public static final String ROOT_CODE = "";

    public ComkerNavbarNodeDPO() {
        super();
        this.parent = null;
        this.enabled = true;
    }

    public ComkerNavbarNodeDPO(String code, String url, String[] permissions) {
        this();
        this.code = code;
        this.url = url;
        this.permissions = permissions;
    }

    public ComkerNavbarNodeDPO(String code, String url, String[] permissions, String label, String description) {
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
    private String permissionStore = null;

    private ComkerNavbarNodeDPO parent = null;
    private Set<ComkerNavbarNodeDPO> children = new HashSet<ComkerNavbarNodeDPO>();

    private String treeId = null;
    private String treeIndent = null;
    
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

    @Column(name = "f_url", unique = false, nullable = true, length = 255)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "f_enabled", unique = false, nullable = true)
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Column(name = "f_label", unique = false, nullable = true, length = 255)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Column(name = "f_description", unique = false, nullable = true, length = 1024)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name = "permissions")
    @Transient
    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    @XmlTransient
    @Column(name = "f_permissions", unique = false, nullable = true, length = 1024)
    public String getPermissionStore() {
        this.permissionStore = ComkerDataUtil.mergeStringArray(permissions);
        return this.permissionStore;
    }

    public void setPermissionStore(String permissionStore) {
        this.permissionStore = permissionStore;
        this.permissions = ComkerDataUtil.splitStringArray(permissionStore);
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "f_parent_id", nullable=true)
    public ComkerNavbarNodeDPO getParent() {
        return parent;
    }

    public void setParent(ComkerNavbarNodeDPO parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy="parent", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
    public Set<ComkerNavbarNodeDPO> getChildren() {
        return children;
    }

    public void setChildren(Set<ComkerNavbarNodeDPO> children) {
        this.children = children;
    }

    @Column(name = "f_tree_id", unique = false, nullable = true, length = 1024)
    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    @Column(name = "f_tree_indent", unique = false, nullable = true, length = 1024)
    public String getTreeIndent() {
        return treeIndent;
    }

    public void setTreeIndent(String treeIndent) {
        this.treeIndent = treeIndent;
    }
}
