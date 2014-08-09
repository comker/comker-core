package net.cokkee.comker.model.po;

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
@Table(name = "comker_crew")
public class ComkerCrew extends ComkerAbstractItem {

    public ComkerCrew() {
        super();
    }

    public ComkerCrew(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }
    
    private String id;
    private String name;
    private String description;
    private List<ComkerCrewJoinGlobalRole> crewJoinGlobalRoleList =
            new LinkedList<ComkerCrewJoinGlobalRole>();
    private List<ComkerCrewJoinRoleWithSpot> crewJoinRoleWithSpotList =
            new LinkedList<ComkerCrewJoinRoleWithSpot>();
    
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.crew", cascade=CascadeType.ALL, orphanRemoval=true)
    public List<ComkerCrewJoinGlobalRole> getCrewJoinGlobalRoleList() {
        return crewJoinGlobalRoleList;
    }

    public void setCrewJoinGlobalRoleList(List<ComkerCrewJoinGlobalRole> item) {
        this.crewJoinGlobalRoleList = item;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.crew", cascade=CascadeType.ALL, orphanRemoval=true)
    public List<ComkerCrewJoinRoleWithSpot> getCrewJoinRoleWithSpotList() {
        return crewJoinRoleWithSpotList;
    }

    public void setCrewJoinRoleWithSpotList(List<ComkerCrewJoinRoleWithSpot> item) {
        this.crewJoinRoleWithSpotList = item;
    }
}
