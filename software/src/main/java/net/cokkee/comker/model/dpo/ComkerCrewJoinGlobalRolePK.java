package net.cokkee.comker.model.dpo;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.model.error.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
@Embeddable
public class ComkerCrewJoinGlobalRolePK implements Serializable {

    public ComkerCrewJoinGlobalRolePK() {
    }

    public ComkerCrewJoinGlobalRolePK(ComkerCrewDPO crew, ComkerRoleDPO role) {
        if (crew == null || role == null) {
            throw new ComkerInvalidParameterException(
                    "ComkerCrewJoinGlobalRolePk_crew_or_role_is_null",
                new ComkerExceptionExtension(
                    "ComkerCrewJoinGlobalRolePk_crew_or_role_is_null", null, 
                    "Error on creating ComkerCrewJoinGlobalRolePk: Crew or Role is NULL."));
        }
        this.crew = crew;
        this.role = role;
    }

    private ComkerCrewDPO crew;
    private ComkerRoleDPO role;

    @ManyToOne
    @JoinColumn(name="f_crew_id")
    public ComkerCrewDPO getCrew() {
        return crew;
    }

    public void setCrew(ComkerCrewDPO crew) {
        this.crew = crew;
    }

    @ManyToOne
    @JoinColumn(name="f_role_id")
    public ComkerRoleDPO getRole() {
        return role;
    }

    public void setRole(ComkerRoleDPO role) {
        this.role = role;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerCrewJoinGlobalRolePK that = (ComkerCrewJoinGlobalRolePK) o;

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