package net.cokkee.comker.model.dpo;

import net.cokkee.comker.model.dpo.ComkerUserDPO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;
import net.cokkee.comker.model.dpo.ComkerAbstractDPO;
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
public class ComkerSettingEntryDPO extends ComkerAbstractDPO {

    public ComkerSettingEntryDPO() {
        super();
        this.pk = new ComkerSettingEntryPK();
    }

    public ComkerSettingEntryDPO(ComkerSettingKeyDPO settingKey, ComkerSpotDPO spot, ComkerUserDPO user) {
        super();
        this.pk = new ComkerSettingEntryPK(settingKey, spot, user);
    }

    private ComkerSettingEntryPK pk;
    private String value;

    private String valueString;
    private Double valueDouble;
    private Integer valueInteger;
    private Boolean valueBoolean;
    private String valueXStream;
    
    @EmbeddedId
    public ComkerSettingEntryPK getPk() {
        return pk;
    }

    public void setPk(ComkerSettingEntryPK pk) {
        this.pk = pk;
    }

    @Transient
    public ComkerSettingKeyDPO getSettingKey() {
        return getPk().getSettingKey();
    }

    public void setSettingKey(ComkerSettingKeyDPO item) {
        getPk().setSettingKey(item);
    }
    
    @Transient
    public ComkerSpotDPO getSpot() {
        return getPk().getSpot();
    }

    public void setSpot(ComkerSpotDPO item) {
        getPk().setSpot(item);
    }

    @Transient
    public ComkerUserDPO getUser() {
        return getPk().getUser();
    }

    public void setUser(ComkerUserDPO product) {
        getPk().setUser(product);
    }

    @Column(name = "f_value", unique = false, nullable = true, length = 1024)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "f_value_string", length = 1024)
    public String getValueString() {
        return valueString;
    }

    public void setValueString(String value) {
        this.valueString = value;
    }

    @Column(name = "f_value_double")
    public Double getValueDouble() {
        return valueDouble;
    }

    public void setValueDouble(Double value) {
        this.valueDouble = value;
    }

    @Column(name = "f_value_integer")
    public Integer getValueInteger() {
        return valueInteger;
    }

    public void setValueInteger(Integer value) {
        this.valueInteger = value;
    }

    @Column(name = "f_value_boolean")
    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    public void setValueBoolean(Boolean value) {
        this.valueBoolean = value;
    }

    @Column(name = "f_value_xstream")
    public String getValueXStream() {
        return valueXStream;
    }

    public void setValueXStream(String valueXStream) {
        this.valueXStream = valueXStream;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerSettingEntryDPO that = (ComkerSettingEntryDPO) o;

        if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
