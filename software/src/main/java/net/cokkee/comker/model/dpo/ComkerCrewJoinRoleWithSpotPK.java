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
public class ComkerCrewJoinRoleWithSpotPK implements Serializable {

    public ComkerCrewJoinRoleWithSpotPK() {
    }

    public ComkerCrewJoinRoleWithSpotPK(ComkerCrewDPO crew, ComkerRoleDPO role, ComkerSpotDPO spot) {
        if (crew == null || role == null || spot == null) {
            throw new ComkerInvalidParameterException(
                    "ComkerCrewJoinRoleWithSpotPk_crew_or_role_or_spot_is_null",
            new ComkerExceptionExtension(
                    "ComkerCrewJoinRoleWithSpotPk_crew_or_role_or_spot_is_null", null, 
                    "Error on creating ComkerCrewJoinRoleWithSpotPk: Crew or Role or Spot is NULL."));
        }
        this.crew = crew;
        this.role = role;
        this.spot = spot;
    }

    private ComkerCrewDPO crew;
    private ComkerRoleDPO role;
    private ComkerSpotDPO spot;

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

    @ManyToOne
    @JoinColumn(name="f_spot_id")
    public ComkerSpotDPO getSpot() {
        return spot;
    }

    public void setSpot(ComkerSpotDPO spot) {
        this.spot = spot;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerCrewJoinRoleWithSpotPK that = (ComkerCrewJoinRoleWithSpotPK) o;

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