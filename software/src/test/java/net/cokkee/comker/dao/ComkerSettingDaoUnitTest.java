package net.cokkee.comker.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.po.ComkerSettingEntry;
import net.cokkee.comker.model.po.ComkerSettingEntryPk;
import net.cokkee.comker.model.po.ComkerSettingKey;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.model.po.ComkerUser;
import org.hamcrest.CoreMatchers;
import org.hibernate.Criteria;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Assert;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/dao/ComkerSettingDaoUnitTest.xml"})
@Transactional
public class ComkerSettingDaoUnitTest {

    @Autowired
    private SessionFactory testSessionFactory = null;

    @Autowired
    private ComkerSettingDao testSettingDao = null;
    
    @Before
    public void init() {
        // init seed spots
        ComkerSpot spotUnknown = getUnknownSpot();
        ComkerSpot spotDefault = getDefaultSpot();

        // init seed users
        ComkerUser userUnknown = getUnknownUser();
        ComkerUser userDefault = getDefaultUser();

        // init settings
        ComkerSettingKey sKey = null;

        sKey = getOrCreateSettingKey("APPLICATION_SETTING_", ComkerSettingKey.TYPE_STRING, "");
        defineValue(spotUnknown, userUnknown, sKey, "Default Value of Application Setting ");
    }

    @Test
    public void test_something() {

    }

    //--------------------------------------------------------------------------

    private ComkerSpot getOrCreateSpot(String code, String name, String description) {
        Session session = testSessionFactory.getCurrentSession();

        Criteria c = session.createCriteria(ComkerSpot.class);
        c.add(Restrictions.eq("code", code));
        List<ComkerSpot> result = c.list();
        ComkerSpot item = (result.isEmpty()) ? null:result.get(0);

        if (item == null) {
            item = new ComkerSpot(code, name, description);
            session.saveOrUpdate(item);
        }
        return item;
    }

    private ComkerSpot getUnknownSpot() {
        return getOrCreateSpot(ComkerSpot.UNKNOWN, "UNKNOWN SPOT", "");
    }

    private ComkerSpot getDefaultSpot() {
        return getOrCreateSpot(ComkerSpot.DEFAULT, "DEFAULT SPOT", "");
    }


    private ComkerUser getOrCreateUser(String email, String username, String password, String fullname){
        Session session = testSessionFactory.getCurrentSession();

        Criteria c = session.createCriteria(ComkerUser.class);
        c.add(Restrictions.eq("username", username));
        List<ComkerUser> result = c.list();
        ComkerUser item = (result.isEmpty()) ? null:result.get(0);

        if (item == null) {
            item = new ComkerUser(email, username, password, fullname);
            session.saveOrUpdate(item);
        }
        return item;
    }

    private ComkerUser getUnknownUser(){
        return getOrCreateUser("unknown@cokkee.net", ComkerUser.UNKNOWN, "ff808181442e048701442e04aa710008", "UNKNOWN USER");
    }

    private ComkerUser getDefaultUser(){
        return getOrCreateUser("default@cokkee.net", ComkerUser.DEFAULT, "ff808181442e048701442e04aa610006", "DEFAULT USER");
    }


    private ComkerSettingKey getOrCreateSettingKey(String code, String type, String range) {
        Session session = testSessionFactory.getCurrentSession();

        Criteria c = session.createCriteria(ComkerSettingKey.class);
        c.add(Restrictions.eq("code", code));
        List<ComkerSettingKey> result = c.list();
        ComkerSettingKey item = (result.isEmpty()) ? null:result.get(0);

        if (item == null) {
            item = new ComkerSettingKey(code, type, range);
            session.saveOrUpdate(item);
        }
        return item;
    }

    private void defineValue(ComkerSpot spot, ComkerUser user, ComkerSettingKey key, String defaultValue) {
        Session session = testSessionFactory.getCurrentSession();
        ComkerSettingEntryPk pk = new ComkerSettingEntryPk(key, spot, user);
        ComkerSettingEntry item = (ComkerSettingEntry) session.get(ComkerSettingEntry.class, pk);
        if (item == null) {
            item = new ComkerSettingEntry(key, spot, user);
        }
        item.setValue(defaultValue);
        session.saveOrUpdate(item);
    }
}
