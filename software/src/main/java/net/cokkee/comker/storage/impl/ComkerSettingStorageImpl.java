package net.cokkee.comker.storage.impl;

import java.text.MessageFormat;
import net.cokkee.comker.dao.ComkerSettingDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.storage.ComkerSettingStorage;
import net.cokkee.comker.model.ComkerExceptionExtension;
import net.cokkee.comker.model.po.ComkerSettingKey;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.model.po.ComkerUser;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public class ComkerSettingStorageImpl implements ComkerSettingStorage {

    private ComkerSettingDao settingDao;

    public void setSettingDao(ComkerSettingDao settingDao) {
        this.settingDao = settingDao;
    }

    private ComkerSpotDao spotDao;

    public void setSpotDao(ComkerSpotDao spotDao) {
        this.spotDao = spotDao;
    }

    private ComkerUserDao userDao;

    public void setUserDao(ComkerUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public <T> void define(String spotCode, String username, String keyCode, Class<T> clazz, T value) {
        ComkerSpot spot = spotDao.getByCode(spotCode);
        ComkerUser user = userDao.getByUsername(username);

        if (spot == null || user == null) {
            throw new ComkerObjectNotFoundException("spot_or_user__not_found");
        }

        settingDao.define(spot, user, getNotNullByCode(keyCode), clazz, value);
    }

    @Override
    public <T> T getValue(String spotCode, String username, String keyCode, Class<T> clazz) {
        ComkerSpot spot = spotDao.getByCode(spotCode);
        ComkerUser user = userDao.getByUsername(username);

        if (spot == null || user == null) {
            throw new ComkerObjectNotFoundException("spot_or_user__not_found");
        }
        
        return settingDao.getValue(spot, user, getNotNullByCode(keyCode), clazz);
    }

    @Override
    public <T> void setValue(String spotCode, String username, String keyCode, Class<T> clazz, T value) {
        ComkerSpot spot = spotDao.getByCode(spotCode);
        ComkerUser user = userDao.getByUsername(username);

        if (spot == null || user == null) {
            throw new ComkerObjectNotFoundException("spot_or_user_not_found");
        }

        settingDao.setValue(spot, user, getNotNullByCode(keyCode), clazz, value);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private ComkerSettingKey getNotNull(String id) {
        ComkerSettingKey dbItem = settingDao.get(id);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    MessageFormat.format("Setting Key object with id:{0} not found", new Object[] {id}),
                    new ComkerExceptionExtension("setting_key_with_id_not_found", new Object[] {id}));
        }
        return dbItem;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private ComkerSettingKey getNotNullByCode(String code) {
        ComkerSettingKey dbItem = settingDao.getByCode(code);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    MessageFormat.format("Setting Key object with code:{0} not found", new Object[] {code}),
                    new ComkerExceptionExtension("setting_key_with_code_not_found", new Object[] {code}));
        }
        return dbItem;
    }
}
