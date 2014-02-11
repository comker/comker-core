package net.cokkee.comker.model.po;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "comker_navbar_node")
public class ComkerNavbarNode extends ComkerAbstractItem {

    public ComkerNavbarNode() {
        super();
        this.parent = null;
        this.enabled = true;
    }

    public ComkerNavbarNode(String code, String url, String label, String description) {
        super();
        this.parent = null;
        this.code = code;
        this.url = url;
        this.enabled = true;
        this.label = label;
        this.description = description;
    }

    private String id;
    private String code;
    private String url;
    private Boolean enabled;
    private String label;
    private String description;

    private ComkerNavbarNode parent = null;
    private Set<ComkerNavbarNode> children = new HashSet<ComkerNavbarNode>();

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

    @Column(name = "f_code", unique = false, nullable = false, length = 255)
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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "f_parent_id", insertable=false, updatable=false)
    public ComkerNavbarNode getParent() {
        return parent;
    }

    private void setParent(ComkerNavbarNode parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy="parent", fetch=FetchType.LAZY)
    public Set<ComkerNavbarNode> getChildren() {
        return children;
    }

    public void setChildren(Set<ComkerNavbarNode> children) {
        this.children = children;
    }

    @Column(name = "f_tree_id", unique = false, nullable = true, length = 1024)
    public String getTreeId() {
        return treeId;
    }

    private void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    @Column(name = "f_tree_indent", unique = false, nullable = true, length = 1024)
    public String getTreeIndent() {
        return treeIndent;
    }

    private void setTreeIndent(String treeIndent) {
        this.treeIndent = treeIndent;
    }
    
    //--------------------------------------------------------------------------
    
    public void changeParent(ComkerNavbarNode newParent) {
        if (validateTreeData(newParent)) {
            this.parent = newParent;
            updateTreeData();
        }
    }

    private boolean validateTreeData(ComkerNavbarNode newParent) {
        if (newParent == null) return true;
        if (newParent.getTreeId().contains(this.treeId)) return false;
        return true;
    }

    private void updateTreeData() {
        if (this.parent == null) {
            this.treeId = this.id;
            this.treeIndent = "";
        } else {
            this.treeId = this.parent.getTreeId() + ">" + this.id;
            this.treeIndent = "----" + this.parent.getTreeIndent();
        }
    }
}
