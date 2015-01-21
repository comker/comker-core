package net.cokkee.comker.test.unit.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.sql.DataSource;

import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.dao.impl.ComkerPermissionDaoHibernate;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.dpo.ComkerPermissionDPO;

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
            ComkerPermissionDaoUnitTest.GeneralConfig.class,
            ComkerPermissionDaoUnitTest.ContextConfig.class
        }
)
@Transactional
public class ComkerPermissionDaoUnitTest extends ComkerAbstractDaoUnitTest {

    @Autowired
    @Qualifier("comkerSessionFactory")
    private SessionFactory testSessionFactory = null;

    @Autowired
    @Qualifier("comkerPermissionDao")
    private ComkerPermissionDao testPermissionDao = null;

    private List<String> permissionIdx = new ArrayList<String>();

    private Set<String> authorities = new HashSet<String>();
    
    @Before
    public void init() {
        ComkerPermissionDPO item = null;
        Session session = testSessionFactory.getCurrentSession();
        
        for(int i=0; i<20; i++) {
            String authorityString = "PERMISSION_" + i;
            authorities.add(authorityString);

            item = new ComkerPermissionDPO(authorityString);
            session.saveOrUpdate(item);
            permissionIdx.add(item.getId());
        }
    }

    @Test
    public void test_count_permissions() {
        Assert.assertTrue(testPermissionDao.count(null) == 20);
    }

    @Test
    public void test_find_all_permissions() {
        List list = testPermissionDao.findAll(null, null);
        Set<String> resultSet = new HashSet<String>();
        for(Object item:list) {
            ComkerPermissionDPO permission = (ComkerPermissionDPO) item;
            resultSet.add(permission.getAuthority());
        }
        Assert.assertThat(resultSet, CoreMatchers.is(authorities));
    }

    @Test
    public void test_find_all_permissions_with_pager() {
        List list = testPermissionDao.findAll(null,new ComkerQueryPager());
        Assert.assertTrue(list.size() == ComkerQueryPager.DEFAULT_LIMIT);
    }

    @Test
    public void test_get_by_authority() {
        ComkerPermissionDPO item = testPermissionDao.getByAuthority("PERMISSION_1");
        Assert.assertNotNull(item);
    }

    @Test
    public void test_get_by_authority_with_invalid_authority_code() {
        ComkerPermissionDPO item = testPermissionDao.getByAuthority("PERMISSION_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_get_by_id() {
        ComkerPermissionDPO item = testPermissionDao.getByAuthority("PERMISSION_1");
        ComkerPermissionDPO item2 = testPermissionDao.get(item.getId());
        Assert.assertEquals(item, item2);
    }

    @Test
    public void test_get_by_id_with_invalid_id() {
        ComkerPermissionDPO item = testPermissionDao.get("ID_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_exists_by_id() {
        for(int i=0; i<permissionIdx.size(); i++) {
            Assert.assertTrue(testPermissionDao.exists(permissionIdx.get(i)));
        }
    }

    @Test
    public void test_exists_by_invalid_id() {
        Assert.assertFalse(testPermissionDao.exists(null));
        
        for(int i=0; i<10; i++) {
            Assert.assertFalse(testPermissionDao.exists("permission_id_" + i));
        }
    }

    @Test
    public void test_create() {
        int currentCount = testPermissionDao.count(null);

        ComkerPermissionDPO item = new ComkerPermissionDPO("PERMISSION_A");
        testPermissionDao.save(item);

        Assert.assertTrue(testPermissionDao.count(null) == currentCount + 1);
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
        public ComkerPermissionDao comkerPermissionDao(
                @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
            ComkerPermissionDaoHibernate bean = new ComkerPermissionDaoHibernate();
            bean.setSessionFactory(sessionFactory);
            return bean;
        }
        
        @Bean
        public AnnotationSessionFactoryBean comkerSessionFactory(
                @Qualifier("comkerDataSource") DataSource dataSource,
                @Qualifier("comkerHibernateProperties") Properties hibernateProperties) {
            AnnotationSessionFactoryBean asfb = new AnnotationSessionFactoryBean();
            asfb.setAnnotatedClasses(
                    net.cokkee.comker.model.dpo.ComkerPermissionDPO.class);
            asfb.setDataSource(dataSource);
            asfb.setHibernateProperties(hibernateProperties);
            return asfb;
        }
    }
}
