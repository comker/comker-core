package net.cokkee.comker.model.dpo;

import net.cokkee.comker.model.dpo.ComkerCrewDPO;
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
@Table(name = "comker_user_join_crew")
@AssociationOverrides({
    @AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = "f_user_id")),
    @AssociationOverride(name = "pk.crew", joinColumns = @JoinColumn(name = "f_crew_id"))
})
public class ComkerUserJoinCrewDPO extends ComkerAbstractDPO {

    public ComkerUserJoinCrewDPO() {
        super();
        this.pk = new ComkerUserJoinCrewPK();
    }

    public ComkerUserJoinCrewDPO(ComkerUserDPO user, ComkerCrewDPO crew) {
        super();
        this.pk = new ComkerUserJoinCrewPK(user, crew);
    }

    private ComkerUserJoinCrewPK pk;

    @EmbeddedId
    public ComkerUserJoinCrewPK getPk() {
        return pk;
    }

    public void setPk(ComkerUserJoinCrewPK pk) {
        this.pk = pk;
    }

    @Transient
    public ComkerUserDPO getUser() {
        return getPk().getUser();
    }

    public void setUser(ComkerUserDPO item) {
        getPk().setUser(item);
    }

    @Transient
    public ComkerCrewDPO getCrew() {
        return getPk().getCrew();
    }

    public void setCrew(ComkerCrewDPO item) {
        getPk().setCrew(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerUserJoinCrewDPO that = (ComkerUserJoinCrewDPO) o;

        if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
