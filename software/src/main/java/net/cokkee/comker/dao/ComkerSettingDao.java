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

    void delete(String id);

    void define(ComkerSpot spot, ComkerUser user, ComkerSettingKey key, String defaultValue);

    String getValue(String spotCode, String username, String keyCode);

    void setValue(String spotCode, String username, String keyCode, String value);

    String getValue(ComkerSpot spot, ComkerUser user, ComkerSettingKey key);

    void setValue(ComkerSpot spot, ComkerUser user, ComkerSettingKey key, String value);
}
