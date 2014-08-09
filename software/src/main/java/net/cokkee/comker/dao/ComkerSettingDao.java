package net.cokkee.comker.dao;

import net.cokkee.comker.model.po.ComkerSettingKey;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.model.po.ComkerUser;

/**
 *
 * @author drupalex
 */
public interface ComkerSettingDao {

    ComkerSettingKey get(String id);

    ComkerSettingKey getByCode(String code);

    ComkerSettingKey create(ComkerSettingKey item);

    ComkerSettingKey update(ComkerSettingKey item);

    void delete(ComkerSettingKey item);

    @Deprecated
    void delete(String id);

    <T> void define(ComkerSpot spot, ComkerUser user, ComkerSettingKey key, Class<T> clazz, T defaultValue);

    <T> T getValue(ComkerSpot spot, ComkerUser user, ComkerSettingKey key, Class<T> clazz);

    <T> void setValue(ComkerSpot spot, ComkerUser user, ComkerSettingKey key, Class<T> clazz, T value);

    @Deprecated
    void define(ComkerSpot spot, ComkerUser user, ComkerSettingKey key, String defaultValue);

    @Deprecated
    String getValue(String spotCode, String username, String keyCode);

    @Deprecated
    void setValue(String spotCode, String username, String keyCode, String value);

    @Deprecated
    String getValue(ComkerSpot spot, ComkerUser user, ComkerSettingKey key);

    @Deprecated
    void setValue(ComkerSpot spot, ComkerUser user, ComkerSettingKey key, String value);
}
