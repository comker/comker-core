package net.cokkee.comker.test.unit.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.sql.DataSource;

import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.dao.impl.ComkerCrewDaoHibernate;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.dpo.ComkerRoleDPO;
import net.cokkee.comker.model.dpo.ComkerCrewDPO;
import net.cokkee.comker.model.dpo.ComkerCrewJoinGlobalRoleDPO;
import net.cokkee.comker.model.dpo.ComkerCrewJoinRoleWithSpotDPO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;

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
            ComkerCrewDaoUnitTest.GeneralConfig.class,
            ComkerCrewDaoUnitTest.ContextConfig.class
        }
)
@Transactional
public class ComkerCrewDaoUnitTest extends ComkerAbstractDaoUnitTest {

    @Autowired
    @Qualifier("comkerSessionFactory")
    private SessionFactory testSessionFactory = null;

    @Autowired
    @Qualifier("comkerCrewDao")
    private ComkerCrewDao testCrewDao = null;

    private String[] crewIds = new String[5];

    private String[] spotIds = new String[2];

    private String[] roleIds = new String[12];

    @Before
    public void init() {
        Session session = testSessionFactory.getCurrentSession();

        ComkerCrewDPO[] crewObjects = new ComkerCrewDPO[crewIds.length];
        for(int n=0; n<crewIds.length; n++) {
            String postfix = "0" + (n + 1);
            crewObjects[n] = new ComkerCrewDPO("Crew " + postfix, "Description Crew " + postfix);
            crewIds[n] = (String) session.save(crewObjects[n]);
        }

        ComkerSpotDPO[] spotObjects = new ComkerSpotDPO[spotIds.length];
        for(int n=0; n<spotObjects.length; n++) {
            String postfix = "0" + (n + 1);
            spotObjects[n] = new ComkerSpotDPO("SPOT_" + postfix,
                    "Spot " + postfix, "Description Spot " + postfix);
            spotIds[n] = (String) session.save(spotObjects[n]);
        }

        ComkerRoleDPO[] roleObjects = new ComkerRoleDPO[roleIds.length];
        for(int i=0; i<roleIds.length; i++) {
            String prefix = ((i<9)?"0":"") + (i + 1);
            roleObjects[i] = new ComkerRoleDPO(
                    "ROLE_" + prefix, 
                    "Role " + prefix, 
                    "",
                    Boolean.TRUE);
            roleIds[i] = (String) session.save(roleObjects[i]);
        }

        for(int i=0; i<4; i++) {
            crewObjects[0].getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRoleDPO(crewObjects[0], roleObjects[i]));
        }
        for(int i=2; i<6; i++) {
            crewObjects[1].getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRoleDPO(crewObjects[1], roleObjects[i]));
        }

        for(int i=6; i<10; i++) {
            crewObjects[0].getCrewJoinRoleWithSpotList().add(
                    new ComkerCrewJoinRoleWithSpotDPO(crewObjects[0], roleObjects[i], spotObjects[0]));
        }
        for(int i=8; i<12; i++) {
            crewObjects[0].getCrewJoinRoleWithSpotList().add(
                    new ComkerCrewJoinRoleWithSpotDPO(crewObjects[0], roleObjects[i], spotObjects[1]));
        }

        for(int i=6; i<12; i++) {
            crewObjects[1].getCrewJoinRoleWithSpotList().add(
                    new ComkerCrewJoinRoleWithSpotDPO(crewObjects[1], roleObjects[i], spotObjects[0]));
        }

        for(int n=0; n<crewIds.length; n++) {
            session.saveOrUpdate(crewObjects[n]);
        }
    }

    @Test
    public void test_count() {
        Assert.assertTrue(testCrewDao.count(null) == crewIds.length);
    }

    @Test
    public void test_list_all() {
        List<ComkerCrewDPO> list = testCrewDao.findAll(null, null);
        Assert.assertTrue(list.size() == crewIds.length);

        int count = 0;
        Set<String> authorities1 = new HashSet();
        Set<String> authorities2 = new HashSet();
        ComkerCrewDPO crew0 = list.get(0);
        for(ComkerCrewJoinGlobalRoleDPO perm:crew0.getCrewJoinGlobalRoleList()) {
            authorities1.add("ROLE_0" + (++count));
            authorities2.add(perm.getRole().getCode());
        }
        Assert.assertTrue(count == 4);
        Assert.assertThat(authorities1, CoreMatchers.is(authorities2));
    }

    @Test
    public void test_find_all_with_pager() {
        List<ComkerCrewDPO> list = testCrewDao.findAll(null,new ComkerQueryPager(1, 3));
        Assert.assertTrue(list.size() == 3);
        Assert.assertEquals(list.get(0).getName(), "Crew 02");
        Assert.assertEquals(list.get(2).getName(), "Crew 04");
    }

    @Test
    public void test_find_all_with_pager_out_of_range() {
        List<ComkerCrewDPO> list = testCrewDao.findAll(null,new ComkerQueryPager(4, 10));
        Assert.assertTrue(list.size() == 1);
        Assert.assertEquals(list.get(0).getName(), "Crew 05");
    }

    @Test
    public void test_get_by_id() {
        ComkerCrewDPO item = testCrewDao.get(crewIds[0]);
        Assert.assertEquals(item.getName(), "Crew 01");

        Map<String, Set<String>> spotMap = new HashMap<String, Set<String>>();
        for(ComkerCrewJoinRoleWithSpotDPO join:item.getCrewJoinRoleWithSpotList()) {
            Set<String> roleSet = spotMap.get(join.getSpot().getCode());
            if (roleSet == null) {
                roleSet = new HashSet<String>();
                spotMap.put(join.getSpot().getCode(), roleSet);
            }
            roleSet.add(join.getRole().getCode());
        }
        Assert.assertTrue(spotMap.size() == 2);

        Set<String> expectedSet;
        
        expectedSet = new HashSet(Arrays.asList(new String[] {"ROLE_07", "ROLE_08", "ROLE_09", "ROLE_10"}));
        Assert.assertThat(spotMap.get("SPOT_01"), CoreMatchers.is(expectedSet));

        expectedSet = new HashSet(Arrays.asList(new String[] {"ROLE_11", "ROLE_12", "ROLE_09", "ROLE_10"}));
        Assert.assertThat(spotMap.get("SPOT_02"), CoreMatchers.is(expectedSet));
    }

    @Test
    public void test_get_by_id_with_invalid_id() {
        ComkerCrewDPO item = testCrewDao.get("ID_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_exists_by_id() {
        for(int i=0; i<crewIds.length; i++) {
            Assert.assertTrue(testCrewDao.exists(crewIds[i]));
        }
    }

    @Test
    public void test_exists_by_invalid_id() {
        Assert.assertFalse(testCrewDao.exists(null));

        for(int i=0; i<10; i++) {
            Assert.assertFalse(testCrewDao.exists("crew_id_" + i));
        }
    }

    @Test
    public void findAllWhere_with_valid_GlobalRole() {
        Session session = testSessionFactory.getCurrentSession();
        ComkerRoleDPO role = (ComkerRoleDPO) session.get(ComkerRoleDPO.class, roleIds[3]);
        List<ComkerCrewDPO> list = testCrewDao.findAllWhere(role);
        Assert.assertTrue(list.size() == 2);
        Assert.assertEquals(crewIds[0], list.get(0).getId());
        Assert.assertEquals(crewIds[1], list.get(1).getId());
    }
    
    @Test
    public void test_create() {
        Session session = testSessionFactory.getCurrentSession();
        
        int currentCount = testCrewDao.count(null);
        Assert.assertTrue(currentCount == crewIds.length);

        ComkerCrewDPO item = new ComkerCrewDPO("Crew 06", "Description Crew 06");
        item.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRoleDPO(item,
                (ComkerRoleDPO) session.get(ComkerRoleDPO.class, roleIds[0])));
        item.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRoleDPO(item,
                (ComkerRoleDPO) session.get(ComkerRoleDPO.class, roleIds[1])));
        item.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRoleDPO(item,
                (ComkerRoleDPO) session.get(ComkerRoleDPO.class, roleIds[3])));
        testCrewDao.create(item);

        Assert.assertTrue(testCrewDao.count(null) == currentCount + 1);
        
        ComkerCrewDPO result = testCrewDao.getByName("Crew 06");
        Assert.assertTrue(result.getCrewJoinGlobalRoleList().size() == 3);
    }

    @Test
    public void test_update() {
        Session session = testSessionFactory.getCurrentSession();
        ComkerCrewDPO item = testCrewDao.getByName("Crew 01");
        Assert.assertTrue(item.getCrewJoinGlobalRoleList().size() == 4);

        item.getCrewJoinGlobalRoleList().clear();
        item.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRoleDPO(item,
                (ComkerRoleDPO) session.get(ComkerRoleDPO.class, roleIds[5])));
        item.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRoleDPO(item,
                (ComkerRoleDPO) session.get(ComkerRoleDPO.class, roleIds[6])));
        item.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRoleDPO(item,
                (ComkerRoleDPO) session.get(ComkerRoleDPO.class, roleIds[7])));
        testCrewDao.update(item);

        ComkerCrewDPO result = testCrewDao.getByName("Crew 01");
        Assert.assertTrue(result.getCrewJoinGlobalRoleList().size() == 3);
    }

    @Test
    public void test_delete() {
        testCrewDao.delete(testCrewDao.getByName("Crew 03"));
        Assert.assertNull(testCrewDao.getByName("Crew 03"));
        Assert.assertTrue(testCrewDao.count(null) == (crewIds.length - 1));
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
        public ComkerCrewDao comkerCrewDao(
                @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
            ComkerCrewDaoHibernate bean = new ComkerCrewDaoHibernate();
            bean.setSessionFactory(sessionFactory);
            return bean;
        }
        
        @Bean
        public AnnotationSessionFactoryBean comkerSessionFactory(
                @Qualifier("comkerDataSource") DataSource dataSource,
                @Qualifier("comkerHibernateProperties") Properties hibernateProperties) {
            AnnotationSessionFactoryBean asfb = new AnnotationSessionFactoryBean();
            asfb.setAnnotatedClasses(
                    net.cokkee.comker.model.dpo.ComkerPermissionDPO.class,
                    net.cokkee.comker.model.dpo.ComkerRoleDPO.class,
                    net.cokkee.comker.model.dpo.ComkerRoleJoinPermissionDPO.class,
                    net.cokkee.comker.model.dpo.ComkerRoleJoinPermissionPK.class,
                    net.cokkee.comker.model.dpo.ComkerModuleDPO.class,
                    net.cokkee.comker.model.dpo.ComkerSpotDPO.class,
                    net.cokkee.comker.model.dpo.ComkerSpotJoinModuleDPO.class,
                    net.cokkee.comker.model.dpo.ComkerSpotJoinModulePK.class,
                    net.cokkee.comker.model.dpo.ComkerCrewDPO.class,
                    net.cokkee.comker.model.dpo.ComkerCrewJoinGlobalRoleDPO.class,
                    net.cokkee.comker.model.dpo.ComkerCrewJoinGlobalRolePK.class,
                    net.cokkee.comker.model.dpo.ComkerCrewJoinRoleWithSpotDPO.class,
                    net.cokkee.comker.model.dpo.ComkerCrewJoinRoleWithSpotPK.class);
            asfb.setDataSource(dataSource);
            asfb.setHibernateProperties(hibernateProperties);
            return asfb;
        }
    }
}
