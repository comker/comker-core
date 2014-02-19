package net.cokkee.comker.dao.impl;

import net.cokkee.comker.dao.*;
import net.cokkee.comker.exception.ComkerFieldDuplicatedException;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;

import net.cokkee.comker.model.po.ComkerSettingEntry;
import net.cokkee.comker.model.po.ComkerSettingEntryPk;
import net.cokkee.comker.model.po.ComkerSettingKey;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.model.po.ComkerUser;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public class ComkerSettingDaoHibernate extends ComkerAbstractDao.Hibernate
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
    public ComkerSettingKey get(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerSettingKey item = (ComkerSettingKey) session.get(ComkerSettingKey.class, id);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerSettingKey getByCode(String code) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerSettingKey.class);
        c.add(Restrictions.eq(FIELD_CODE, code));
        return (ComkerSettingKey) c.uniqueResult();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerSettingKey create(ComkerSettingKey item) {
        Session session = this.getSessionFactory().getCurrentSession();
        if (getByCode(item.getCode()) != null) {
            throw new ComkerFieldDuplicatedException(400, "SettingKey has already existed");
        }
        session.save(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerSettingKey update(ComkerSettingKey item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(ComkerSettingKey item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerSettingKey item = (ComkerSettingKey) session.get(ComkerSettingKey.class, id);
        session.delete(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void define(ComkerSpot spot, ComkerUser user, ComkerSettingKey key, String defaultValue) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerSettingEntryPk pk = new ComkerSettingEntryPk(key, spot, user);
        ComkerSettingEntry item = (ComkerSettingEntry) session.get(ComkerSettingEntry.class, pk);
        if (item == null) {
            item = new ComkerSettingEntry(key, spot, user);
        }
        item.setValue(defaultValue);
        session.saveOrUpdate(item);
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
    public String getValue(ComkerSpot spot, ComkerUser user, ComkerSettingKey key) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerSettingEntryPk pk = new ComkerSettingEntryPk(key, spot, user);
        ComkerSettingEntry item = (ComkerSettingEntry) session.get(ComkerSettingEntry.class, pk);
        if (item == null) {
            if (isErrorCatched()) {
                throw new ComkerObjectNotFoundException(401, "Setting not found");
            } else {
                return null;
            }
        }
        return item.getValue();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void setValue(ComkerSpot spot, ComkerUser user, ComkerSettingKey key, String value) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerSettingEntryPk pk = new ComkerSettingEntryPk(key, spot, user);
        ComkerSettingEntry item = (ComkerSettingEntry) session.get(ComkerSettingEntry.class, pk);
        if (item == null) {
            if (isErrorCatched()) {
                throw new ComkerObjectNotFoundException(401, "Setting not found");
            } else {
                return;
            }
        }
        item.setValue(value);
        session.saveOrUpdate(value);
    }
}
