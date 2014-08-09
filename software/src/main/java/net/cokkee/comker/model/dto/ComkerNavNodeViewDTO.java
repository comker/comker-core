package net.cokkee.comker.model.dto;

import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class ComkerNavNodeViewDTO extends ComkerNavNodeDTO {

    public ComkerNavNodeViewDTO() {
        super();
    }

    public ComkerNavNodeViewDTO(String code, String url, String[] permissions) {
        super(code, url, permissions);
    }

    public ComkerNavNodeViewDTO(String code, String url, String[] permissions,
            String label, String description) {
        super(code, url, permissions, label, description);
    }

    private Set<ComkerNavNodeViewDTO> children = new HashSet<ComkerNavNodeViewDTO>();

    private String treeId = null;
    private String treeIndent = null;
    
    public Set<ComkerNavNodeViewDTO> getChildren() {
        return children;
    }

    public void setChildren(Set<ComkerNavNodeViewDTO> children) {
        this.children = children;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public String getTreeIndent() {
        return treeIndent;
    }

    public void setTreeIndent(String treeIndent) {
        this.treeIndent = treeIndent;
    }
}
