package net.cokkee.comker.test.unit.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.sql.DataSource;

import net.cokkee.comker.dao.ComkerWatchdogDao;
import net.cokkee.comker.dao.impl.ComkerWatchdogDaoHibernate;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.dpo.ComkerWatchdogDPO;

import org.hamcrest.CoreMatchers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {
            ComkerWatchdogDaoUnitTest.GeneralConfig.class,
            ComkerWatchdogDaoUnitTest.ContextConfig.class
        }
)
@Transactional
public class ComkerWatchdogDaoUnitTest extends ComkerAbstractDaoUnitTest {

    @Autowired
    @Qualifier("comkerSessionFactory")
    private SessionFactory testSessionFactory = null;

    @Autowired
    @Qualifier("comkerWatchdogDao")
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
    
    @Configuration
    public static class ContextConfig {
        
        private static final Logger logger = LoggerFactory.getLogger(ContextConfig.class);
    
        public ContextConfig() {
            if (logger.isDebugEnabled()) {
                logger.debug("==@ " + ContextConfig.class.getSimpleName() + " is invoked");
            }
        }
        
        @Bean
        public ComkerWatchdogDao comkerWatchdogDao(
                @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
            ComkerWatchdogDaoHibernate bean = new ComkerWatchdogDaoHibernate();
            bean.setSessionFactory(sessionFactory);
            return bean;
        }
        
        @Bean
        public AnnotationSessionFactoryBean comkerSessionFactory(
                @Qualifier("comkerDataSource") DataSource dataSource,
                @Qualifier("comkerHibernateProperties") Properties hibernateProperties) {
            AnnotationSessionFactoryBean asfb = new AnnotationSessionFactoryBean();
            asfb.setAnnotatedClasses(
                    net.cokkee.comker.model.dpo.ComkerWatchdogDPO.class);
            asfb.setDataSource(dataSource);
            asfb.setHibernateProperties(hibernateProperties);
            return asfb;
        }
    }
}
