package net.cokkee.comker.model.dpo;

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
public class ComkerSpotJoinModuleDPO extends ComkerAbstractDPO {

    public ComkerSpotJoinModuleDPO() {
        super();
        this.pk = new ComkerSpotJoinModulePK();
    }

    public ComkerSpotJoinModuleDPO(ComkerSpotDPO spot, ComkerModuleDPO module) {
        super();
        this.pk = new ComkerSpotJoinModulePK(spot, module);
    }

    private ComkerSpotJoinModulePK pk;

    @EmbeddedId
    public ComkerSpotJoinModulePK getPk() {
        return pk;
    }

    public void setPk(ComkerSpotJoinModulePK pk) {
        this.pk = pk;
    }

    @Transient
    public ComkerSpotDPO getSpot() {
        return getPk().getSpot();
    }

    public void setSpot(ComkerSpotDPO item) {
        getPk().setSpot(item);
    }

    @Transient
    public ComkerModuleDPO getModule() {
        return getPk().getModule();
    }

    public void setModule(ComkerModuleDPO product) {
        getPk().setModule(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerSpotJoinModuleDPO that = (ComkerSpotJoinModuleDPO) o;

        if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
