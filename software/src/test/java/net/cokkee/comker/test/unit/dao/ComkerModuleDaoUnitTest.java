package net.cokkee.comker.test.unit.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.sql.DataSource;
import net.cokkee.comker.dao.ComkerModuleDao;
import net.cokkee.comker.dao.impl.ComkerModuleDaoHibernate;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.dpo.ComkerModuleDPO;
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
            ComkerModuleDaoUnitTest.GeneralConfig.class,
            ComkerModuleDaoUnitTest.ContextConfig.class
        }
)
@Transactional
public class ComkerModuleDaoUnitTest extends ComkerAbstractDaoUnitTest {

    @Autowired
    @Qualifier("comkerSessionFactory")
    private SessionFactory testSessionFactory = null;

    @Autowired
    @Qualifier("comkerModuleDao")
    private ComkerModuleDao testModuleDao = null;

    private List<String> moduleIdx = new ArrayList<String>();

    private Set<String> moduleCodes = new HashSet<String>();
    
    @Before
    public void init() {
        ComkerModuleDPO item = null;
        Session session = testSessionFactory.getCurrentSession();
        
        for(int i=0; i<20; i++) {
            String authorityString = "MODULE_" + i;
            moduleCodes.add(authorityString);

            item = new ComkerModuleDPO(authorityString, "Module " + i, "Description Module " + i);
            session.saveOrUpdate(item);
            moduleIdx.add(item.getId());
        }
    }

    @Test
    public void test_count_modules() {
        Assert.assertTrue(testModuleDao.count() == 20);
    }

    @Test
    public void test_find_all_modules() {
        List list = testModuleDao.findAll(null);
        Set<String> resultSet = new HashSet<String>();
        for(Object item:list) {
            ComkerModuleDPO module = (ComkerModuleDPO) item;
            resultSet.add(module.getCode());
        }
        Assert.assertThat(resultSet, CoreMatchers.is(moduleCodes));
    }

    @Test
    public void test_find_all_modules_with_pager() {
        List list = testModuleDao.findAll(new ComkerQueryPager());
        Assert.assertTrue(list.size() == ComkerQueryPager.DEFAULT_LIMIT);
    }

    @Test
    public void test_get_by_code() {
        ComkerModuleDPO item = testModuleDao.getByCode("MODULE_1");
        Assert.assertNotNull(item);
    }

    @Test
    public void test_get_by_code_with_invalid_code() {
        ComkerModuleDPO item = testModuleDao.getByCode("MODULE_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_get_by_id() {
        ComkerModuleDPO item = testModuleDao.getByCode("MODULE_1");
        ComkerModuleDPO item2 = testModuleDao.get(item.getId());
        Assert.assertEquals(item, item2);
    }

    @Test
    public void test_get_by_id_with_invalid_id() {
        ComkerModuleDPO item = testModuleDao.get("ID_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_exists_by_id() {
        for(int i=0; i<moduleIdx.size(); i++) {
            Assert.assertTrue(testModuleDao.exists(moduleIdx.get(i)));
        }
    }

    @Test
    public void test_exists_by_invalid_id() {
        Assert.assertFalse(testModuleDao.exists(null));

        for(int i=0; i<10; i++) {
            Assert.assertFalse(testModuleDao.exists("module_id_" + i));
        }
    }

    @Test
    public void test_create() {
        int currentCount = testModuleDao.count();

        ComkerModuleDPO item = new ComkerModuleDPO(
                "MODULE_" + currentCount,
                "Module " + currentCount,
                "Description Module " + currentCount);
        moduleIdx.add(String.valueOf(testModuleDao.save(item)));

        Assert.assertTrue(testModuleDao.count() == currentCount + 1);
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
        public ComkerModuleDao comkerModuleDao(
                @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
            ComkerModuleDaoHibernate bean = new ComkerModuleDaoHibernate();
            bean.setSessionFactory(sessionFactory);
            return bean;
        }
        
        @Bean
        public AnnotationSessionFactoryBean comkerSessionFactory(
                @Qualifier("comkerDataSource") DataSource dataSource,
                @Qualifier("comkerHibernateProperties") Properties hibernateProperties) {
            AnnotationSessionFactoryBean asfb = new AnnotationSessionFactoryBean();
            
            asfb.setAnnotatedClasses(
                    net.cokkee.comker.model.dpo.ComkerModuleDPO.class);
            
            asfb.setDataSource(dataSource);
            asfb.setHibernateProperties(hibernateProperties);
            return asfb;
        }
    }
}
