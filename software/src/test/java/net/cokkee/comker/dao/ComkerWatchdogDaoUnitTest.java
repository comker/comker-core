package net.cokkee.comker.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.dpo.ComkerWatchdogDPO;
import org.hamcrest.CoreMatchers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
@ContextConfiguration(locations = {"classpath:/dao/ComkerWatchdogDaoUnitTest.xml"})
@Transactional
public class ComkerWatchdogDaoUnitTest {

    @Autowired
    private SessionFactory testSessionFactory = null;

    @Autowired
    private ComkerWatchdogDao testWatchdogDao = null;

    private List<String> watchdogIds = new ArrayList<String>();
    
    @Before
    public void init() {
        ComkerWatchdogDPO item = null;
        Session session = testSessionFactory.getCurrentSession();

        Random random = new Random();
        for(int i=0; i<20; i++) {
            String prefix = (i<10)?"0":"";
            String username = (i/2 == 0) ? "pnhung177":"someone";

            item = new ComkerWatchdogDPO(username, "method_" + prefix, "{}",
                    Calendar.getInstance().getTime(), random.nextLong(),
                    ComkerWatchdogDPO.HIT_STATE_SUCCESS);

            //item = new ComkerWatchdogDPO(username, "method_" + prefix);
            session.saveOrUpdate(item);
            watchdogIds.add(item.getId());
        }
    }

    @Test
    public void test_count() {
        Assert.assertTrue(testWatchdogDao.count(null) == watchdogIds.size());
    }

    @Test
    public void test_find_all() {
        List list = testWatchdogDao.findAll(null, null);
        List<String> resultSet = new ArrayList<String>();
        for(Object item:list) {
            ComkerWatchdogDPO permission = (ComkerWatchdogDPO) item;
            resultSet.add(permission.getId());
        }
        Assert.assertThat(resultSet, CoreMatchers.is(watchdogIds));
    }

    @Test
    public void test_find_all_with_pager() {
        List list = testWatchdogDao.findAll(null,new ComkerQueryPager());
        Assert.assertTrue(list.size() == ComkerQueryPager.DEFAULT_LIMIT);
    }

    @Test
    public void test_get_by_id() {
        for(int i=0; i<watchdogIds.size(); i++) {
            ComkerWatchdogDPO item = testWatchdogDao.get(watchdogIds.get(i));
            Assert.assertNotNull(item);
        }
    }

    @Test
    public void test_get_by_id_with_invalid_id() {
        ComkerWatchdogDPO item = testWatchdogDao.get("ID_NOT_FOUND");
        Assert.assertNull(item);
    }
}
