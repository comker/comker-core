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
@Table(name = "comker_crew_join_global_role")
@AssociationOverrides({
    @AssociationOverride(name = "pk.crew", joinColumns = @JoinColumn(name = "f_crew_id")),
    @AssociationOverride(name = "pk.role", joinColumns = @JoinColumn(name = "f_role_id"))
})
public class ComkerCrewJoinGlobalRoleDPO extends ComkerAbstractDPO {

    public ComkerCrewJoinGlobalRoleDPO() {
        super();
        this.pk = new ComkerCrewJoinGlobalRolePK();
    }

    public ComkerCrewJoinGlobalRoleDPO(ComkerCrewDPO crew, ComkerRoleDPO role) {
        super();
        this.pk = new ComkerCrewJoinGlobalRolePK(crew, role);
    }

    private ComkerCrewJoinGlobalRolePK pk;

    @EmbeddedId
    public ComkerCrewJoinGlobalRolePK getPk() {
        return pk;
    }

    public void setPk(ComkerCrewJoinGlobalRolePK pk) {
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

    public void setRole(ComkerRoleDPO product) {
        getPk().setRole(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerCrewJoinGlobalRoleDPO that = (ComkerCrewJoinGlobalRoleDPO) o;

        if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
