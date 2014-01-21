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
@Table(name = "comker_spot_join_module")
@AssociationOverrides({
    @AssociationOverride(name = "pk.spot", joinColumns = @JoinColumn(name = "f_spot_id")),
    @AssociationOverride(name = "pk.module", joinColumns = @JoinColumn(name = "f_module_id"))
})
public class ComkerSpotJoinModule extends ComkerAbstractItem {

    public ComkerSpotJoinModule() {
        super();
        this.pk = new ComkerSpotJoinModulePk();
    }

    public ComkerSpotJoinModule(ComkerSpot spot, ComkerModule module) {
        super();
        this.pk = new ComkerSpotJoinModulePk(spot, module);
    }

    private ComkerSpotJoinModulePk pk;

    @EmbeddedId
    public ComkerSpotJoinModulePk getPk() {
        return pk;
    }

    public void setPk(ComkerSpotJoinModulePk pk) {
        this.pk = pk;
    }

    @Transient
    public ComkerSpot getSpot() {
        return getPk().getSpot();
    }

    public void setSpot(ComkerSpot item) {
        getPk().setSpot(item);
    }

    @Transient
    public ComkerModule getModule() {
        return getPk().getModule();
    }

    public void setModule(ComkerModule product) {
        getPk().setModule(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerSpotJoinModule that = (ComkerSpotJoinModule) o;

        if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
