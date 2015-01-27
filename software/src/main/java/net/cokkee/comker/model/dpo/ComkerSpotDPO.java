package net.cokkee.comker.model.dpo;

import java.util.LinkedList;
import java.util.List;
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
@Table(name = "comker_spot")
public class ComkerSpotDPO extends ComkerAbstractDPO {

    public static final String UNKNOWN = "__UNKNOWN_SPOT__";
    public static final String DEFAULT = "__DEFAULT_SPOT__";

    public ComkerSpotDPO() {
        super();
    }

    public ComkerSpotDPO(String code, String name, String description) {
        super();
        this.code = code;
        this.name = name;
        this.description = description;
    }
    
    public ComkerSpotDPO update(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
        return this;
    }

    private String id;
    private String code;
    private String name;
    private String description;
    private List<ComkerSpotJoinModuleDPO> spotJoinModuleList = 
            new LinkedList<ComkerSpotJoinModuleDPO>();

    /*
    private List<ComkerCrewJoinRoleWithSpot> crewJoinRoleWithSpotList =
            new LinkedList<ComkerCrewJoinRoleWithSpot>();
    */

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.spot", cascade=CascadeType.ALL, orphanRemoval=true)
    public List<ComkerSpotJoinModuleDPO> getSpotJoinModuleList() {
        return spotJoinModuleList;
    }

    public void setSpotJoinModuleList(List<ComkerSpotJoinModuleDPO> spotJoinModule) {
        this.spotJoinModuleList = spotJoinModule;
    }

    /*
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.spot", cascade=CascadeType.ALL, orphanRemoval=true)
    public List<ComkerCrewJoinRoleWithSpot> getCrewJoinRoleWithSpotList() {
        return crewJoinRoleWithSpotList;
    }

    public void setCrewJoinRoleWithSpotList(List<ComkerCrewJoinRoleWithSpot> item) {
        this.crewJoinRoleWithSpotList = item;
    }
    */
}
