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
public class ComkerUserJoinCrewPK implements Serializable {

    public ComkerUserJoinCrewPK() {
    }

    public ComkerUserJoinCrewPK(ComkerUserDPO user, ComkerCrewDPO crew) {
        if (user == null || crew == null) {
            throw new ComkerInvalidParameterException(
                "ComkerUserJoinCrewPk_user_or_crew_is_null",
                new ComkerExceptionExtension(
                    "error.ComkerUserJoinCrewPk_user_or_crew_is_null", null, 
                    "Error on creating ComkerUserJoinCrewPk: User or Crew is NULL."));
        }
        this.user = user;
        this.crew = crew;
    }

    private ComkerUserDPO user;
    private ComkerCrewDPO crew;

    @ManyToOne
    @JoinColumn(name="f_user_id")
    public ComkerUserDPO getUser() {
        return user;
    }

    public void setUser(ComkerUserDPO user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name="f_crew_id")
    public ComkerCrewDPO getCrew() {
        return crew;
    }

    public void setCrew(ComkerCrewDPO crew) {
        this.crew = crew;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerUserJoinCrewPK that = (ComkerUserJoinCrewPK) o;

        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (crew != null ? !crew.equals(that.crew) : that.crew != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (user != null ? user.hashCode() : 0);
        result = 31 * result + (crew != null ? crew.hashCode() : 0);
        return result;
    }
}