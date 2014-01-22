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
public class ComkerUserJoinCrewPk implements Serializable {

    public ComkerUserJoinCrewPk() {
    }

    public ComkerUserJoinCrewPk(ComkerUser user, ComkerCrew crew) {
        this.user = user;
        this.crew = crew;
    }

    private ComkerUser user;
    private ComkerCrew crew;

    @ManyToOne
    @JoinColumn(name="f_user_id")
    public ComkerUser getUser() {
        return user;
    }

    public void setUser(ComkerUser user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name="f_crew_id")
    public ComkerCrew getCrew() {
        return crew;
    }

    public void setCrew(ComkerCrew crew) {
        this.crew = crew;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerUserJoinCrewPk that = (ComkerUserJoinCrewPk) o;

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