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
public class ComkerCrewJoinRoleWithSpotPk implements Serializable {

    public ComkerCrewJoinRoleWithSpotPk() {
    }

    public ComkerCrewJoinRoleWithSpotPk(ComkerCrew crew, ComkerRole role, ComkerSpot spot) {
        this.crew = crew;
        this.role = role;
        this.spot = spot;
    }

    private ComkerCrew crew;
    private ComkerRole role;
    private ComkerSpot spot;

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

    @ManyToOne
    @JoinColumn(name="f_spot_id")
    public ComkerSpot getSpot() {
        return spot;
    }

    public void setSpot(ComkerSpot spot) {
        this.spot = spot;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerCrewJoinRoleWithSpotPk that = (ComkerCrewJoinRoleWithSpotPk) o;

        if (crew != null ? !crew.equals(that.crew) : that.crew != null) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;
        if (spot != null ? !spot.equals(that.spot) : that.spot != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (crew != null ? crew.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (spot != null ? spot.hashCode() : 0);
        return result;
    }
}