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
public class ComkerSettingEntryPk implements Serializable {

    public ComkerSettingEntryPk() {
    }

    public ComkerSettingEntryPk(ComkerSettingKey settingKey, ComkerSpot spot, ComkerUser user) {
        this.settingKey = settingKey;
        this.spot = spot;
        this.user = user;
    }

    private ComkerSettingKey settingKey;
    private ComkerSpot spot;
    private ComkerUser user;

    @ManyToOne
    @JoinColumn(name="f_setting_key_id", nullable=false)
    public ComkerSettingKey getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(ComkerSettingKey settingKey) {
        this.settingKey = settingKey;
    }
    
    @ManyToOne
    @JoinColumn(name="f_spot_id", nullable=true)
    public ComkerSpot getSpot() {
        return spot;
    }

    public void setSpot(ComkerSpot spot) {
        this.spot = spot;
    }

    @ManyToOne
    @JoinColumn(name="f_user_id", nullable=true)
    public ComkerUser getUser() {
        return user;
    }

    public void setUser(ComkerUser user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerSettingEntryPk that = (ComkerSettingEntryPk) o;

        if (settingKey != null ? !settingKey.equals(that.settingKey) : that.settingKey != null) return false;
        if (spot != null ? !spot.equals(that.spot) : that.spot != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (settingKey != null ? settingKey.hashCode() : 0);
        result = 31 * result + (spot != null ? spot.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}