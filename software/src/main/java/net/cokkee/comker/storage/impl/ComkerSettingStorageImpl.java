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
        settingDao.define(getSpotNotNullByCode(spotCode), 
                getUserNotNullByUsername(username), 
                getNotNullByCode(keyCode), clazz, value);
    }

    @Override
    public <T> T getValue(String spotCode, String username, String keyCode, Class<T> clazz) {
        return settingDao.getValue(getSpotNotNullByCode(spotCode), 
                getUserNotNullByUsername(username), 
                getNotNullByCode(keyCode), clazz);
    }

    @Override
    public <T> void setValue(String spotCode, String username, String keyCode, Class<T> clazz, T value) {
        settingDao.setValue(getSpotNotNullByCode(spotCode), 
                getUserNotNullByUsername(username), 
                getNotNullByCode(keyCode), clazz, value);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private ComkerSpot getSpotNotNullByCode(String code) {
        ComkerSpot dbItem = spotDao.getByCode(code);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    "spot_with__code__not_found",
                    new ComkerExceptionExtension("error.spot_with__code__not_found", 
                            new Object[] {code}, 
                            MessageFormat.format("Spot object with code:{0} not found", 
                                    new Object[] {code})));
        }
        return dbItem;
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private ComkerUser getUserNotNullByUsername(String username) {
        ComkerUser dbItem = userDao.getByUsername(username);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    "user_with__username__not_found",
                    new ComkerExceptionExtension("error.user_with__username__not_found", 
                            new Object[] {username}, 
                            MessageFormat.format("User object with username:{0} not found", 
                                    new Object[] {username})));
        }
        return dbItem;
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private ComkerSettingKey getNotNullByCode(String code) {
        ComkerSettingKey dbItem = settingDao.getByCode(code);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    "setting_key_with__code__not_found",
                    new ComkerExceptionExtension("error.setting_key_with__code__not_found", 
                            new Object[] {code}, 
                            MessageFormat.format("Setting Key object with code:{0} not found", 
                                    new Object[] {code})));
        }
        return dbItem;
    }
}
