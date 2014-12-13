package net.cokkee.comker.model.dpo;

import net.cokkee.comker.model.dpo.ComkerModuleDPO;
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
public class ComkerSpotJoinModulePK implements Serializable {

    public ComkerSpotJoinModulePK() {
    }

    public ComkerSpotJoinModulePK(ComkerSpotDPO spot, ComkerModuleDPO module) {
        if (spot == null || module == null) {
            throw new ComkerInvalidParameterException(
                "ComkerSpotJoinModulePk_spot_or_module_is_null",
                new ComkerExceptionExtension(
                    "error.ComkerSpotJoinModulePk_spot_or_module_is_null", null, 
                    "Error on creating ComkerSpotJoinModulePk: Spot or Module is NULL."));
        }
        this.spot = spot;
        this.module = module;
    }

    private ComkerSpotDPO spot;
    private ComkerModuleDPO module;

    @ManyToOne
    @JoinColumn(name="f_spot_id")
    public ComkerSpotDPO getSpot() {
        return spot;
    }

    public void setSpot(ComkerSpotDPO spot) {
        this.spot = spot;
    }

    @ManyToOne
    @JoinColumn(name="f_module_id")
    public ComkerModuleDPO getModule() {
        return module;
    }

    public void setModule(ComkerModuleDPO module) {
        this.module = module;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerSpotJoinModulePK that = (ComkerSpotJoinModulePK) o;

        if (spot != null ? !spot.equals(that.spot) : that.spot != null) return false;
        if (module != null ? !module.equals(that.module) : that.module != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (spot != null ? spot.hashCode() : 0);
        result = 31 * result + (module != null ? module.hashCode() : 0);
        return result;
    }
}