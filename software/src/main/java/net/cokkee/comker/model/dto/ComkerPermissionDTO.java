package net.cokkee.comker.model.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class ComkerPermissionDTO extends ComkerAbstractDTO {

    public ComkerPermissionDTO() {
        super();
    }

    public ComkerPermissionDTO(String authority) {
        super();
        this.authority = authority;
    }

    private String id;
    private String authority;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
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

        public Pack(Integer total, List<ComkerPermissionDTO> collection) {
            this();
            this.total = total;
            this.collection = collection;
        }

        private Integer total = null;

        private List<ComkerPermissionDTO> collection = null;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<ComkerPermissionDTO> getCollection() {
            return collection;
        }

        public void setCollection(List<ComkerPermissionDTO> collection) {
            this.collection = collection;
        }
    }
}
