package net.cokkee.comker.model.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import net.cokkee.comker.structure.ComkerKeyAndValueSet;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class ComkerCrewDTO extends ComkerAbstractDTO {

    public ComkerCrewDTO() {
        super();
    }

    public ComkerCrewDTO(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }
    
    private String id;
    private String name;
    private String description;
    
    private String[] globalRoleIds;
    private ComkerKeyAndValueSet[] scopedRoleIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String[] getGlobalRoleIds() {
        return globalRoleIds;
    }

    public void setGlobalRoleIds(String[] globalRoleIds) {
        this.globalRoleIds = globalRoleIds;
    }

    public ComkerKeyAndValueSet[] getScopedRoleIds() {
        return scopedRoleIds;
    }

    public void setScopedRoleIds(ComkerKeyAndValueSet[] scopedRoleIds) {
        this.scopedRoleIds = scopedRoleIds;
    }

    @XmlRootElement
    public static class Filter extends ComkerAbstractDTO.Filter {
        public Filter() {
            super();
        }
        public Filter(String queryString) {
            super(queryString);
        }
    }

    @XmlRootElement
    public static class Pack {

        public Pack() {
        }

        public Pack(Integer total, List<ComkerCrewDTO> collection) {
            this();
            this.total = total;
            this.collection = collection;
        }

        private Integer total = null;

        private List<ComkerCrewDTO> collection = null;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<ComkerCrewDTO> getCollection() {
            return collection;
        }

        public void setCollection(List<ComkerCrewDTO> collection) {
            this.collection = collection;
        }
    }
}
