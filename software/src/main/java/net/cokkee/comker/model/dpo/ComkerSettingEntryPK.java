package net.cokkee.comker.model.dpo;

import net.cokkee.comker.model.dpo.ComkerUserDPO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author drupalex
 */
@Embeddable
public class ComkerSettingEntryPK implements Serializable {

    public ComkerSettingEntryPK() {
    }

    public ComkerSettingEntryPK(ComkerSettingKeyDPO settingKey, ComkerSpotDPO spot, ComkerUserDPO user) {
        this.settingKey = settingKey;
        this.spot = spot;
        this.user = user;
    }

    private ComkerSettingKeyDPO settingKey;
    private ComkerSpotDPO spot;
    private ComkerUserDPO user;

    @ManyToOne
    @JoinColumn(name="f_setting_key_id", nullable=false)
    public ComkerSettingKeyDPO getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(ComkerSettingKeyDPO settingKey) {
        this.settingKey = settingKey;
    }
    
    @ManyToOne
    @JoinColumn(name="f_spot_id", nullable=true)
    public ComkerSpotDPO getSpot() {
        return spot;
    }

    public void setSpot(ComkerSpotDPO spot) {
        this.spot = spot;
    }

    @ManyToOne
    @JoinColumn(name="f_user_id", nullable=true)
    public ComkerUserDPO getUser() {
        return user;
    }

    public void setUser(ComkerUserDPO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComkerSettingEntryPK that = (ComkerSettingEntryPK) o;

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