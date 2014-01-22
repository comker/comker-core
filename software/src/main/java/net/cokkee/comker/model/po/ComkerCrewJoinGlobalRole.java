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
@Table(name = "comker_crew_join_global_role")
@AssociationOverrides({
    @AssociationOverride(name = "pk.crew", joinColumns = @JoinColumn(name = "f_crew_id")),
    @AssociationOverride(name = "pk.role", joinColumns = @JoinColumn(name = "f_role_id"))
})
public class ComkerCrewJoinGlobalRole extends ComkerAbstractItem {

    public ComkerCrewJoinGlobalRole() {
        super();
        this.pk = new ComkerCrewJoinGlobalRolePk();
    }

    public ComkerCrewJoinGlobalRole(ComkerCrew crew, ComkerRole role) {
        super();
        this.pk = new ComkerCrewJoinGlobalRolePk(crew, role);
    }

    private ComkerCrewJoinGlobalRolePk pk;

    @EmbeddedId
    public ComkerCrewJoinGlobalRolePk getPk() {
        return pk;
    }

    public void setPk(ComkerCrewJoinGlobalRolePk pk) {
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

    public void setRole(ComkerRole product) {
        getPk().setRole(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerCrewJoinGlobalRole that = (ComkerCrewJoinGlobalRole) o;

        if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
