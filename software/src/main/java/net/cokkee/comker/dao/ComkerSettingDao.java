package net.cokkee.comker.dao;

import net.cokkee.comker.model.dpo.ComkerSettingKeyDPO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;
import net.cokkee.comker.model.dpo.ComkerUserDPO;

/**
 *
 * @author drupalex
 */
public interface ComkerSettingDao {

    ComkerSettingKeyDPO get(String id);

    ComkerSettingKeyDPO getByCode(String code);

    ComkerSettingKeyDPO create(ComkerSettingKeyDPO item);

    ComkerSettingKeyDPO update(ComkerSettingKeyDPO item);

    void delete(ComkerSettingKeyDPO item);

    @Deprecated
    void delete(String id);

    <T> void define(ComkerSpotDPO spot, ComkerUserDPO user, ComkerSettingKeyDPO key, Class<T> clazz, T defaultValue);

    <T> T getValue(ComkerSpotDPO spot, ComkerUserDPO user, ComkerSettingKeyDPO key, Class<T> clazz);

    <T> void setValue(ComkerSpotDPO spot, ComkerUserDPO user, ComkerSettingKeyDPO key, Class<T> clazz, T value);

    @Deprecated
    void define(ComkerSpotDPO spot, ComkerUserDPO user, ComkerSettingKeyDPO key, String defaultValue);

    @Deprecated
    String getValue(String spotCode, String username, String keyCode);

    @Deprecated
    void setValue(String spotCode, String username, String keyCode, String value);

    @Deprecated
    String getValue(ComkerSpotDPO spot, ComkerUserDPO user, ComkerSettingKeyDPO key);

    @Deprecated
    void setValue(ComkerSpotDPO spot, ComkerUserDPO user, ComkerSettingKeyDPO key, String value);
}
