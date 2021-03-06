package net.cokkee.comker.dao.impl;

import java.text.MessageFormat;
import net.cokkee.comker.dao.*;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.error.ComkerExceptionExtension;

import net.cokkee.comker.model.dpo.ComkerSettingEntryDPO;
import net.cokkee.comker.model.dpo.ComkerSettingEntryPK;
import net.cokkee.comker.model.dpo.ComkerSettingKeyDPO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;
import net.cokkee.comker.model.dpo.ComkerUserDPO;
import net.cokkee.comker.util.ComkerDataUtil;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public class ComkerSettingDaoHibernate extends ComkerAbstractDaoHibernate
        implements ComkerSettingDao {

    boolean errorCatched = false;

    public boolean isErrorCatched() {
        return errorCatched;
    }

    public void setErrorCatched(boolean errorCatched) {
        this.errorCatched = errorCatched;
    }


    private ComkerUserDao userDao = null;

    public ComkerUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(ComkerUserDao userDao) {
        this.userDao = userDao;
    }


    private ComkerSpotDao spotDao = null;

    public ComkerSpotDao getSpotDao() {
        return spotDao;
    }

    public void setSpotDao(ComkerSpotDao spotDao) {
        this.spotDao = spotDao;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerSettingKeyDPO get(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerSettingKeyDPO item = (ComkerSettingKeyDPO) session.get(ComkerSettingKeyDPO.class, id);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerSettingKeyDPO getByCode(String code) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerSettingKeyDPO.class);
        c.add(Restrictions.eq(FIELD_CODE, code));
        return (ComkerSettingKeyDPO) c.uniqueResult();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerSettingKeyDPO create(ComkerSettingKeyDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.save(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerSettingKeyDPO update(ComkerSettingKeyDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(ComkerSettingKeyDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerSettingKeyDPO item = (ComkerSettingKeyDPO) session.get(ComkerSettingKeyDPO.class, id);
        session.delete(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public <T> void define(ComkerSpotDPO spot, ComkerUserDPO user, ComkerSettingKeyDPO key, Class<T> clazz, T defaultValue) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerSettingEntryPK pk = new ComkerSettingEntryPK(key, spot, user);
        ComkerSettingEntryDPO item = (ComkerSettingEntryDPO) session.get(ComkerSettingEntryDPO.class, pk);
        if (item == null) {
            item = new ComkerSettingEntryDPO(key, spot, user);
        }

        setSettingEntryValue(item, clazz, defaultValue);
        
        session.saveOrUpdate(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public <T> T getValue(ComkerSpotDPO spot, ComkerUserDPO user, ComkerSettingKeyDPO key, Class<T> clazz) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerSettingEntryPK pk = new ComkerSettingEntryPK(key, spot, user);
        ComkerSettingEntryDPO item = (ComkerSettingEntryDPO) session.get(ComkerSettingEntryDPO.class, pk);
        if (item == null) {
            if (isErrorCatched()) {
                throw new ComkerObjectNotFoundException(
                    "settingentry_with__key__and__spot__and__user__not_found",
                    new ComkerExceptionExtension(
                            "error.settingentry_with__key__and__spot__and__user__not_found", 
                            new Object[] {key.getCode(), spot.getCode(), user.getUsername()}, 
                            MessageFormat.format(
                                    "Setting entry object identified by:{0}-{1}-{2} not found", 
                                    new Object[] {key.getCode(), spot.getCode(), user.getUsername()})));
            } else {
                return null;
            }
        }
        return getSettingEntryValue(item, clazz);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public <T> void setValue(ComkerSpotDPO spot, ComkerUserDPO user, ComkerSettingKeyDPO key, Class<T> clazz, T value) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerSettingEntryPK pk = new ComkerSettingEntryPK(key, spot, user);
        ComkerSettingEntryDPO item = (ComkerSettingEntryDPO) session.get(ComkerSettingEntryDPO.class, pk);
        if (item == null) {
            if (isErrorCatched()) {
                throw new ComkerObjectNotFoundException(
                    "settingentry_with__key__and__spot__and__user__not_found",
                    new ComkerExceptionExtension(
                            "error.settingentry_with__key__and__spot__and__user__not_found", 
                            new Object[] {key.getCode(), spot.getCode(), user.getUsername()}, 
                            MessageFormat.format(
                                    "Setting entry object identified by:{0}-{1}-{2} not found", 
                                    new Object[] {key.getCode(), spot.getCode(), user.getUsername()})));
            } else {
                return;
            }
        }

        setSettingEntryValue(item, clazz, value);

        session.saveOrUpdate(value);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void define(ComkerSpotDPO spot, ComkerUserDPO user, ComkerSettingKeyDPO key, String defaultValue) {
        define(spot, user, key, String.class, defaultValue);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String getValue(String spotCode, String username, String keyCode) {
        return getValue(getSpotDao().getByCode(spotCode),
                getUserDao().getByUsername(username), getByCode(keyCode));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void setValue(String spotCode, String username, String keyCode, String value) {
        setValue(getSpotDao().getByCode(spotCode),
                getUserDao().getByUsername(username), getByCode(keyCode), value);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String getValue(ComkerSpotDPO spot, ComkerUserDPO user, ComkerSettingKeyDPO key) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerSettingEntryPK pk = new ComkerSettingEntryPK(key, spot, user);
        ComkerSettingEntryDPO item = (ComkerSettingEntryDPO) session.get(ComkerSettingEntryDPO.class, pk);
        if (item == null) {
            if (isErrorCatched()) {
                throw new ComkerObjectNotFoundException("Setting not found");
            } else {
                return null;
            }
        }
        return item.getValue();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void setValue(ComkerSpotDPO spot, ComkerUserDPO user, ComkerSettingKeyDPO key, String value) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerSettingEntryPK pk = new ComkerSettingEntryPK(key, spot, user);
        ComkerSettingEntryDPO item = (ComkerSettingEntryDPO) session.get(ComkerSettingEntryDPO.class, pk);
        if (item == null) {
            if (isErrorCatched()) {
                throw new ComkerObjectNotFoundException("Setting not found");
            } else {
                return;
            }
        }
        item.setValue(value);
        session.saveOrUpdate(value);
    }

    //--------------------------------------------------------------------------
    
    private <T> T getSettingEntryValue(ComkerSettingEntryDPO item, Class<T> clazz) {
        T result = null;
        if (clazz == String.class) {
            result = clazz.cast(item.getValueString());
        } else if (clazz == Double.class) {
            result = clazz.cast(item.getValueDouble());
        } else if (clazz == Integer.class) {
            result = clazz.cast(item.getValueInteger());
        } else if (clazz == Boolean.class) {
            result = clazz.cast(item.getValueBoolean());
        } else {
            result = clazz.cast(ComkerDataUtil.convertXStreamToObject(item.getValueXStream()));
        }
        return result;
    }

    private <T> void setSettingEntryValue(ComkerSettingEntryDPO item, Class<T> clazz, T value) {
        if (clazz == String.class) {
            item.setValueString((String)value);
        } else if (clazz == Double.class) {
            item.setValueDouble((Double)value);
        } else if (clazz == Integer.class) {
            item.setValueInteger((Integer)value);
        } else if (clazz == Boolean.class) {
            item.setValueBoolean((Boolean)value);
        } else {
            item.setValueXStream(ComkerDataUtil.convertObjectToXStream(value));
        }
    }
}
