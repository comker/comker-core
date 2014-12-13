package net.cokkee.comker.model.dpo;

import net.cokkee.comker.model.dpo.ComkerAbstractDPO;
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
public class ComkerCrewJoinRoleWithSpotDPO extends ComkerAbstractDPO {

    public ComkerCrewJoinRoleWithSpotDPO() {
        super();
        this.pk = new ComkerCrewJoinRoleWithSpotPK();
    }

    public ComkerCrewJoinRoleWithSpotDPO(ComkerCrewDPO crew, ComkerRoleDPO role, ComkerSpotDPO spot) {
        super();
        this.pk = new ComkerCrewJoinRoleWithSpotPK(crew, role, spot);
    }

    private ComkerCrewJoinRoleWithSpotPK pk;

    @EmbeddedId
    public ComkerCrewJoinRoleWithSpotPK getPk() {
        return pk;
    }

    public void setPk(ComkerCrewJoinRoleWithSpotPK pk) {
        this.pk = pk;
    }

    @Transient
    public ComkerCrewDPO getCrew() {
        return getPk().getCrew();
    }

    public void setCrew(ComkerCrewDPO item) {
        getPk().setCrew(item);
    }

    @Transient
    public ComkerRoleDPO getRole() {
        return getPk().getRole();
    }

    public void setRole(ComkerRoleDPO item) {
        getPk().setRole(item);
    }

    @Transient
    public ComkerSpotDPO getSpot() {
        return getPk().getSpot();
    }

    public void setSpot(ComkerSpotDPO item) {
        getPk().setSpot(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerCrewJoinRoleWithSpotDPO that = (ComkerCrewJoinRoleWithSpotDPO) o;

        if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
