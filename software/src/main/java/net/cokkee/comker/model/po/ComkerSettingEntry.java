package net.cokkee.comker.model.po;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
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
@Table(name = "comker_setting_entry")
@AssociationOverrides({
    @AssociationOverride(name = "pk.settingKey", joinColumns = @JoinColumn(name = "f_setting_key_id")),
    @AssociationOverride(name = "pk.spot", joinColumns = @JoinColumn(name = "f_spot_id", nullable=true)),
    @AssociationOverride(name = "pk.user", joinColumns = @JoinColumn(name = "f_user_id", nullable=true))
})
public class ComkerSettingEntry extends ComkerAbstractItem {

    public ComkerSettingEntry() {
        super();
        this.pk = new ComkerSettingEntryPk();
    }

    public ComkerSettingEntry(ComkerSettingKey settingKey, ComkerSpot spot, ComkerUser user) {
        super();
        this.pk = new ComkerSettingEntryPk(settingKey, spot, user);
    }

    private ComkerSettingEntryPk pk;
    private String value;

    @EmbeddedId
    public ComkerSettingEntryPk getPk() {
        return pk;
    }

    public void setPk(ComkerSettingEntryPk pk) {
        this.pk = pk;
    }

    @Transient
    public ComkerSettingKey getSettingKey() {
        return getPk().getSettingKey();
    }

    public void setSettingKey(ComkerSettingKey item) {
        getPk().setSettingKey(item);
    }
    
    @Transient
    public ComkerSpot getSpot() {
        return getPk().getSpot();
    }

    public void setSpot(ComkerSpot item) {
        getPk().setSpot(item);
    }

    @Transient
    public ComkerUser getUser() {
        return getPk().getUser();
    }

    public void setUser(ComkerUser product) {
        getPk().setUser(product);
    }

    @Column(name = "f_value", unique = false, nullable = true, length = 1024)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerSettingEntry that = (ComkerSettingEntry) o;

        if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
