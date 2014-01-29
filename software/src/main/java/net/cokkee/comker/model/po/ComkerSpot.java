package net.cokkee.comker.model.po;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author drupalex
 */
@XmlRootElement
@Entity
@Table(name = "comker_spot")
public class ComkerSpot extends ComkerAbstractItem {

    public ComkerSpot() {
        super();
    }

    public ComkerSpot(String code, String name, String description) {
        super();
        this.code = code;
        this.name = name;
        this.description = description;
    }

    private String id;
    private String code;
    private String name;
    private String description;
    private List<ComkerSpotJoinModule> spotJoinModuleList = 
            new LinkedList<ComkerSpotJoinModule>();
    private List<ComkerCrewJoinRoleWithSpot> crewJoinRoleWithSpotList =
            new LinkedList<ComkerCrewJoinRoleWithSpot>();

    private Set<String> idsOfModuleList = new HashSet<String>();

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

    @Column(name = "f_description", unique = false, nullable = true, length = 1024)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "f_name", unique = false, nullable = false, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pk.spot")
    public List<ComkerSpotJoinModule> getSpotJoinModuleList() {
        return spotJoinModuleList;
    }

    public void setSpotJoinModuleList(List<ComkerSpotJoinModule> spotJoinModule) {
        this.spotJoinModuleList = spotJoinModule;
    }

    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.spot")
    public List<ComkerCrewJoinRoleWithSpot> getCrewJoinRoleWithSpotList() {
        return crewJoinRoleWithSpotList;
    }

    public void setCrewJoinRoleWithSpotList(List<ComkerCrewJoinRoleWithSpot> item) {
        this.crewJoinRoleWithSpotList = item;
    }

    @Transient
    public Set<String> getIdsOfModuleList() {
        return idsOfModuleList;
    }

    public void setIdsOfModuleList(Set<String> idsOfModuleList) {
        this.idsOfModuleList = idsOfModuleList;
    }
}
