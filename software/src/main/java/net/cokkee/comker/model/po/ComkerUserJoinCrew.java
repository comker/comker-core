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
@Table(name = "comker_user_join_crew")
@AssociationOverrides({
    @AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = "f_user_id")),
    @AssociationOverride(name = "pk.crew", joinColumns = @JoinColumn(name = "f_crew_id"))
})
public class ComkerUserJoinCrew extends ComkerAbstractItem {

    public ComkerUserJoinCrew() {
        super();
        this.pk = new ComkerUserJoinCrewPk();
    }

    public ComkerUserJoinCrew(ComkerUser user, ComkerCrew crew) {
        super();
        this.pk = new ComkerUserJoinCrewPk(user, crew);
    }

    private ComkerUserJoinCrewPk pk;

    @EmbeddedId
    public ComkerUserJoinCrewPk getPk() {
        return pk;
    }

    public void setPk(ComkerUserJoinCrewPk pk) {
        this.pk = pk;
    }

    @Transient
    public ComkerUser getUser() {
        return getPk().getUser();
    }

    public void setUser(ComkerUser item) {
        getPk().setUser(item);
    }

    @Transient
    public ComkerCrew getCrew() {
        return getPk().getCrew();
    }

    public void setCrew(ComkerCrew item) {
        getPk().setCrew(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerUserJoinCrew that = (ComkerUserJoinCrew) o;

        if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
