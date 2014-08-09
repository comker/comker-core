package net.cokkee.comker.model.po;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import net.cokkee.comker.exception.ComkerInvalidParameterException;

/**
 *
 * @author drupalex
 */
@Embeddable
public class ComkerSpotJoinModulePk implements Serializable {

    public ComkerSpotJoinModulePk() {
    }

    public ComkerSpotJoinModulePk(ComkerSpot spot, ComkerModule module) {
        if (spot == null || module == null) {
            throw new ComkerInvalidParameterException("spot_or_module_should_not_be_null");
        }
        this.spot = spot;
        this.module = module;
    }

    private ComkerSpot spot;
    private ComkerModule module;

    @ManyToOne
    @JoinColumn(name="f_spot_id")
    public ComkerSpot getSpot() {
        return spot;
    }

    public void setSpot(ComkerSpot spot) {
        this.spot = spot;
    }

    @ManyToOne
    @JoinColumn(name="f_module_id")
    public ComkerModule getModule() {
        return module;
    }

    public void setModule(ComkerModule module) {
        this.module = module;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerSpotJoinModulePk that = (ComkerSpotJoinModulePk) o;

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