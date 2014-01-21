package net.cokkee.comker.model.po;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author drupalex
 */
@Entity
@Table(name = "comker_crew_join_role_with_spot")
@AssociationOverrides({
    @AssociationOverride(name = "pk.crew", joinColumns = @JoinColumn(name = "f_crew_id")),
    @AssociationOverride(name = "pk.role", joinColumns = @JoinColumn(name = "f_role_id")),
    @AssociationOverride(name = "pk.spot", joinColumns = @JoinColumn(name = "f_spot_id"))
})
public class ComkerCrewJoinRoleWithSpot extends ComkerAbstractItem {

    public ComkerCrewJoinRoleWithSpot() {
        super();
        this.pk = new ComkerCrewJoinRoleWithSpotPk();
    }

    public ComkerCrewJoinRoleWithSpot(ComkerCrew crew, ComkerRole role, ComkerSpot spot) {
        super();
        this.pk = new ComkerCrewJoinRoleWithSpotPk(crew, role, spot);
    }

    private ComkerCrewJoinRoleWithSpotPk pk;

    @EmbeddedId
    public ComkerCrewJoinRoleWithSpotPk getPk() {
        return pk;
    }

    public void setPk(ComkerCrewJoinRoleWithSpotPk pk) {
        this.pk = pk;
    }

    @Transient
    public ComkerCrew getCrew() {
        return getPk().getCrew();
    }

    public void setCrew(ComkerCrew item) {
        getPk().setCrew(item);
    }

    @Transient
    public ComkerRole getRole() {
        return getPk().getRole();
    }

    public void setRole(ComkerRole item) {
        getPk().setRole(item);
    }

    @Transient
    public ComkerSpot getSpot() {
        return getPk().getSpot();
    }

    public void setSpot(ComkerSpot item) {
        getPk().setSpot(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerCrewJoinRoleWithSpot that = (ComkerCrewJoinRoleWithSpot) o;

        if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
