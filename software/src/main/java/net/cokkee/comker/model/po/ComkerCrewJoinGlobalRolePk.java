package net.cokkee.comker.model.po;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author drupalex
 */
@Embeddable
public class ComkerCrewJoinGlobalRolePk implements Serializable {

    public ComkerCrewJoinGlobalRolePk() {
    }

    public ComkerCrewJoinGlobalRolePk(ComkerCrew crew, ComkerRole role) {
        this.crew = crew;
        this.role = role;
    }

    private ComkerCrew crew;
    private ComkerRole role;

    @ManyToOne
    @JoinColumn(name="f_crew_id")
    public ComkerCrew getCrew() {
        return crew;
    }

    public void setCrew(ComkerCrew crew) {
        this.crew = crew;
    }

    @ManyToOne
    @JoinColumn(name="f_role_id")
    public ComkerRole getRole() {
        return role;
    }

    public void setRole(ComkerRole role) {
        this.role = role;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerCrewJoinGlobalRolePk that = (ComkerCrewJoinGlobalRolePk) o;

        if (crew != null ? !crew.equals(that.crew) : that.crew != null) return false;
        if (role != null ? !role.equals(that.role) : that.role != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (crew != null ? crew.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}