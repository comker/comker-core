package net.cokkee.comker.test.unit.dao;

import java.util.Properties;
import javax.sql.DataSource;

import net.cokkee.comker.dao.ComkerRegistrationDao;
import net.cokkee.comker.dao.impl.ComkerRegistrationDaoHibernate;
import net.cokkee.comker.model.dpo.ComkerRegistrationDPO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
            ComkerRegistrationDaoUnitTest.GeneralConfig.class,
            ComkerRegistrationDaoUnitTest.ContextConfig.class
        }
)
@Transactional
public class ComkerRegistrationDaoUnitTest extends ComkerAbstractDaoUnitTest {

    @Autowired
    @Qualifier("comkerSessionFactory")
    private SessionFactory testSessionFactory;

    @Autowired
    @Qualifier("comkerRegistrationDao")
    private ComkerRegistrationDao testRegistrationDao;

    private String[] registrationIds = new String[5];
    
    @Before
    public void init() {
        Session session = testSessionFactory.getCurrentSession();

        for(int n=0; n<registrationIds.length; n++) {
            registrationIds[n] = (String) session.save(new ComkerRegistrationDPO(
                    "demo" + n + "@gmail.com", 
                    "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + n,
                    "0123456789abcdefghijklmnopqrstuvwxyz" + n));
        }
    }
    
    @Test
    public void test_create() {
        Session session = testSessionFactory.getCurrentSession();
        
        int currentCount = testRegistrationDao.count(null);
        
        ComkerRegistrationDPO item = new ComkerRegistrationDPO(
                "demo@gmail.com", 
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", 
                "0123456789abcdefghijklmnopqrstuvwxyz");
        
        testRegistrationDao.create(item);

        Assert.assertTrue(testRegistrationDao.count(null) == currentCount + 1);
    }

    @Test
    public void test_update() {
        Session session = testSessionFactory.getCurrentSession();
        
        ComkerRegistrationDPO o1 = testRegistrationDao.get(registrationIds[1]);
        o1.setEmail("pnhung177@gmail.com");
        
        testRegistrationDao.update(o1);

        ComkerRegistrationDPO o2 = testRegistrationDao.get(registrationIds[1]);
        Assert.assertEquals("pnhung177@gmail.com", o2.getEmail());
    }

    @Test
    public void test_delete() {
        testRegistrationDao.delete(testRegistrationDao.get(registrationIds[2]));
        Assert.assertNull(testRegistrationDao.get(registrationIds[2]));
        Assert.assertTrue(testRegistrationDao.count(null) == (registrationIds.length - 1));
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
        public ComkerRegistrationDao comkerRegistrationDao(
                @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
            ComkerRegistrationDaoHibernate bean = new ComkerRegistrationDaoHibernate();
            bean.setSessionFactory(sessionFactory);
            return bean;
        }
        
        @Bean
        public AnnotationSessionFactoryBean comkerSessionFactory(
                @Qualifier("comkerDataSource") DataSource dataSource,
                @Qualifier("comkerHibernateProperties") Properties hibernateProperties) {
            AnnotationSessionFactoryBean asfb = new AnnotationSessionFactoryBean();
            asfb.setAnnotatedClasses(
                    net.cokkee.comker.model.dpo.ComkerRegistrationDPO.class);
            asfb.setDataSource(dataSource);
            asfb.setHibernateProperties(hibernateProperties);
            return asfb;
        }
    }
}
