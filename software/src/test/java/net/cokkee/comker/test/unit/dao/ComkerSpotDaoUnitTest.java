package net.cokkee.comker.test.unit.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.sql.DataSource;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.dao.impl.ComkerSpotDaoHibernate;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.dpo.ComkerModuleDPO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;
import net.cokkee.comker.model.dpo.ComkerSpotJoinModuleDPO;
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
            ComkerSpotDaoUnitTest.GeneralConfig.class,
            ComkerSpotDaoUnitTest.ContextConfig.class
        }
)
@Transactional
public class ComkerSpotDaoUnitTest extends ComkerAbstractDaoUnitTest {

    @Autowired
    @Qualifier("comkerSessionFactory")
    private SessionFactory testSessionFactory = null;

    @Autowired
    @Qualifier("comkerSpotDao")
    private ComkerSpotDao testSpotDao = null;

    private ComkerSpotDPO[] spotObjects = new ComkerSpotDPO[5];

    private String[] moduleIds = new String[8];

    @Before
    public void init() {
        Session session = testSessionFactory.getCurrentSession();
        
        for(int n=0; n<spotObjects.length; n++) {
            String postfix = "0" + (n + 1);
            spotObjects[n] = new ComkerSpotDPO("SPOT_" + postfix,
                    "Spot " + postfix, "Description Spot " + postfix);
        }
        
        for(int i=0; i<moduleIds.length; i++) {
            String prefix = ((i<10)?"0":"") + (i + 1);
            ComkerModuleDPO item = new ComkerModuleDPO("MODULE_" + prefix, "Module " + prefix, "");
            session.saveOrUpdate(item);
            moduleIds[i] = item.getId();

            if(i<4) {
                spotObjects[0].getSpotJoinModuleList().add(new ComkerSpotJoinModuleDPO(spotObjects[0], item));
            }

            if (3<=i && i<7) {
                spotObjects[1].getSpotJoinModuleList().add(new ComkerSpotJoinModuleDPO(spotObjects[1], item));
            }

            if (4<=i && i<8) {
                spotObjects[2].getSpotJoinModuleList().add(new ComkerSpotJoinModuleDPO(spotObjects[2], item));
            }

            spotObjects[3].getSpotJoinModuleList().add(new ComkerSpotJoinModuleDPO(spotObjects[3], item));
        }

        for(int n=0; n<spotObjects.length; n++) {
            session.saveOrUpdate(spotObjects[n]);
        }
    }

    @Test
    public void test_count() {
        Assert.assertTrue(testSpotDao.count(null) == spotObjects.length);
    }

    @Test
    public void test_list_all() {
        List<ComkerSpotDPO> list = testSpotDao.findAll(null, null);
        Assert.assertTrue(list.size() == spotObjects.length);

        int count = 0;
        Set<String> authorities1 = new HashSet();
        Set<String> authorities2 = new HashSet();
        ComkerSpotDPO spot0 = list.get(0);
        for(ComkerSpotJoinModuleDPO perm:spot0.getSpotJoinModuleList()) {
            authorities1.add("MODULE_0" + (++count));
            authorities2.add(perm.getModule().getCode());
        }
        Assert.assertTrue(count == 4);
        Assert.assertThat(authorities1, CoreMatchers.is(authorities2));
    }

    @Test
    public void test_find_all_with_pager() {
        List<ComkerSpotDPO> list = testSpotDao.findAll(null,new ComkerQueryPager(1, 3));
        Assert.assertTrue(list.size() == 3);
        Assert.assertEquals(list.get(0).getCode(), "SPOT_02");
        Assert.assertEquals(list.get(2).getCode(), "SPOT_04");
    }

    @Test
    public void test_find_all_with_pager_out_of_range() {
        List<ComkerSpotDPO> list = testSpotDao.findAll(null,new ComkerQueryPager(4, 10));
        Assert.assertTrue(list.size() == 1);
        Assert.assertEquals(list.get(0).getCode(), "SPOT_05");
    }

    @Test
    public void test_get_by_id() {
        ComkerSpotDPO item1 = testSpotDao.getByCode("SPOT_03");
        ComkerSpotDPO item2 = testSpotDao.get(item1.getId());
        Assert.assertEquals(item1, item2);
    }

    @Test
    public void test_get_by_id_with_invalid_id() {
        ComkerSpotDPO item = testSpotDao.get("ID_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_get_by_code() {
        ComkerSpotDPO item = testSpotDao.getByCode("SPOT_02");
        Assert.assertNotNull(item);
    }

    @Test
    public void test_get_by_code_with_invalid_code() {
        ComkerSpotDPO item = testSpotDao.getByCode("SPOT_CODE_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_exists_by_id() {
        for(int i=0; i<spotObjects.length; i++) {
            Assert.assertTrue(testSpotDao.exists(spotObjects[i].getId()));
        }
    }

    @Test
    public void test_exists_by_invalid_id() {
        Assert.assertFalse(testSpotDao.exists(null));

        for(int i=0; i<10; i++) {
            Assert.assertFalse(testSpotDao.exists("spot_id_" + i));
        }
    }

    @Test
    public void test_create() {
        Session session = testSessionFactory.getCurrentSession();
        
        int currentCount = testSpotDao.count(null);
        Assert.assertTrue(currentCount == spotObjects.length);

        ComkerSpotDPO item = new ComkerSpotDPO("SPOT_06", "Spot 06", "Description Spot 06");
        item.getSpotJoinModuleList().add(new ComkerSpotJoinModuleDPO(item,
                (ComkerModuleDPO) session.get(ComkerModuleDPO.class, moduleIds[0])));
        item.getSpotJoinModuleList().add(new ComkerSpotJoinModuleDPO(item,
                (ComkerModuleDPO) session.get(ComkerModuleDPO.class, moduleIds[1])));
        item.getSpotJoinModuleList().add(new ComkerSpotJoinModuleDPO(item,
                (ComkerModuleDPO) session.get(ComkerModuleDPO.class, moduleIds[3])));
        testSpotDao.create(item);

        Assert.assertTrue(testSpotDao.count(null) == currentCount + 1);
        
        ComkerSpotDPO result = testSpotDao.getByCode("SPOT_06");
        Assert.assertTrue(result.getSpotJoinModuleList().size() == 3);
    }

    @Test
    public void test_update() {
        Session session = testSessionFactory.getCurrentSession();
        ComkerSpotDPO item = testSpotDao.getByCode("SPOT_01");
        Assert.assertTrue(item.getSpotJoinModuleList().size() == 4);

        item.getSpotJoinModuleList().clear();
        item.getSpotJoinModuleList().add(new ComkerSpotJoinModuleDPO(item,
                (ComkerModuleDPO) session.get(ComkerModuleDPO.class, moduleIds[5])));
        item.getSpotJoinModuleList().add(new ComkerSpotJoinModuleDPO(item,
                (ComkerModuleDPO) session.get(ComkerModuleDPO.class, moduleIds[6])));
        item.getSpotJoinModuleList().add(new ComkerSpotJoinModuleDPO(item,
                (ComkerModuleDPO) session.get(ComkerModuleDPO.class, moduleIds[7])));
        testSpotDao.update(item);

        ComkerSpotDPO result = testSpotDao.getByCode("SPOT_01");
        Assert.assertTrue(result.getSpotJoinModuleList().size() == 3);
    }

    @Test
    public void test_delete() {
        testSpotDao.delete(testSpotDao.getByCode("SPOT_03"));
        Assert.assertNull(testSpotDao.getByCode("SPOT_03"));
        Assert.assertTrue(testSpotDao.count(null) == (spotObjects.length - 1));
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
        public ComkerSpotDao comkerSpotDao(
                @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
            ComkerSpotDaoHibernate bean = new ComkerSpotDaoHibernate();
            bean.setSessionFactory(sessionFactory);
            return bean;
        }
        
        @Bean
        public AnnotationSessionFactoryBean comkerSessionFactory(
                @Qualifier("comkerDataSource") DataSource dataSource,
                @Qualifier("comkerHibernateProperties") Properties hibernateProperties) {
            AnnotationSessionFactoryBean asfb = new AnnotationSessionFactoryBean();
            asfb.setAnnotatedClasses(
                    net.cokkee.comker.model.dpo.ComkerModuleDPO.class,
                    net.cokkee.comker.model.dpo.ComkerSpotDPO.class,
                    net.cokkee.comker.model.dpo.ComkerSpotJoinModuleDPO.class,
                    net.cokkee.comker.model.dpo.ComkerSpotJoinModulePK.class);
            asfb.setDataSource(dataSource);
            asfb.setHibernateProperties(hibernateProperties);
            return asfb;
        }
    }
}
