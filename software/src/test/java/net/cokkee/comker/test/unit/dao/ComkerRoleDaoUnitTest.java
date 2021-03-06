package net.cokkee.comker.test.unit.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.sql.DataSource;

import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.dao.impl.ComkerRoleDaoHibernate;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.dpo.ComkerPermissionDPO;
import net.cokkee.comker.model.dpo.ComkerRoleDPO;
import net.cokkee.comker.model.dpo.ComkerRoleJoinPermissionDPO;
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
            ComkerRoleDaoUnitTest.GeneralConfig.class,
            ComkerRoleDaoUnitTest.ContextConfig.class
        }
)
@Transactional
public class ComkerRoleDaoUnitTest extends ComkerAbstractDaoUnitTest {

    @Autowired
    @Qualifier("comkerSessionFactory")
    private SessionFactory testSessionFactory = null;

    @Autowired
    @Qualifier("comkerRoleDao")
    private ComkerRoleDao testRoleDao = null;

    private ComkerRoleDPO[] roles = new ComkerRoleDPO[5];

    private String[] permissionIds = new String[8];

    @Before
    public void init() {
        Session session = testSessionFactory.getCurrentSession();
        
        for(int n=0; n<roles.length; n++) {
            String postfix = "0" + (n + 1);
            roles[n] = new ComkerRoleDPO(
                    "ROLE_" + postfix,
                    "Role " + postfix, 
                    "Description Role " + postfix,
                    Boolean.TRUE);
        }
        
        for(int i=0; i<permissionIds.length; i++) {
            String prefix = (i<10)?"0":"";

            String authorityString = "PERMISSION_" + prefix + (i + 1);
            ComkerPermissionDPO item = new ComkerPermissionDPO(authorityString);
            session.saveOrUpdate(item);

            permissionIds[i] = item.getId();

            if(i<4) {
                roles[0].getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(roles[0], item));
            }

            if (3<=i && i<7) {
                roles[1].getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(roles[1], item));
            }

            if (4<=i && i<8) {
                roles[2].getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(roles[2], item));
            }

            roles[3].getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(roles[3], item));
        }

        for(int n=0; n<roles.length; n++) {
            session.saveOrUpdate(roles[n]);
        }
    }

    @Test
    public void test_count() {
        Assert.assertTrue(testRoleDao.count(null) == roles.length);
    }

    @Test
    public void test_list_all() {
        List<ComkerRoleDPO> list = testRoleDao.findAll(null, null);
        Assert.assertTrue(list.size() == roles.length);

        int count = 0;
        Set<String> authorities1 = new HashSet();
        Set<String> authorities2 = new HashSet();
        ComkerRoleDPO role0 = list.get(0);
        for(ComkerRoleJoinPermissionDPO perm:role0.getRoleJoinPermissionList()) {
            authorities1.add("PERMISSION_0" + (++count));
            authorities2.add(perm.getPermission().getAuthority());
        }
        Assert.assertTrue(count == 4);
        Assert.assertThat(authorities1, CoreMatchers.is(authorities2));
    }

    @Test
    public void test_find_all_with_pager() {
        List<ComkerRoleDPO> list = testRoleDao.findAll(null, new ComkerQueryPager(1, 3));
        Assert.assertTrue(list.size() == 3);
        Assert.assertEquals(list.get(0).getCode(), "ROLE_02");
        Assert.assertEquals(list.get(2).getCode(), "ROLE_04");
    }

    @Test
    public void test_find_all_with_pager_out_of_range() {
        List<ComkerRoleDPO> list = testRoleDao.findAll(null, new ComkerQueryPager(4, 10));
        Assert.assertTrue(list.size() == 1);
        Assert.assertEquals(list.get(0).getCode(), "ROLE_05");
    }

    @Test
    public void test_get_by_id() {
        ComkerRoleDPO item1 = testRoleDao.getByCode("ROLE_03");
        ComkerRoleDPO item2 = testRoleDao.get(item1.getId());
        Assert.assertEquals(item1, item2);
    }

    @Test
    public void test_get_by_id_with_invalid_id() {
        ComkerRoleDPO item = testRoleDao.get("ID_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_get_by_code() {
        ComkerRoleDPO item = testRoleDao.getByCode("ROLE_02");
        Assert.assertNotNull(item);
    }

    @Test
    public void test_get_by_code_with_invalid_code() {
        ComkerRoleDPO item = testRoleDao.getByCode("ROLE_CODE_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_exists_by_id() {
        for(int i=0; i<roles.length; i++) {
            Assert.assertTrue(testRoleDao.exists(roles[i].getId()));
        }
    }

    @Test
    public void test_exists_by_invalid_id() {
        Assert.assertFalse(testRoleDao.exists(null));

        for(int i=0; i<10; i++) {
            Assert.assertFalse(testRoleDao.exists("role_id_" + i));
        }
    }
    
    @Test
    public void test_create() {
        Session session = testSessionFactory.getCurrentSession();
        
        int currentCount = testRoleDao.count(null);
        Assert.assertTrue(currentCount == roles.length);

        ComkerRoleDPO item = new ComkerRoleDPO(
                "ROLE_06", 
                "Role 06", 
                "Description Role 06",
                Boolean.TRUE);
        item.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(item, 
                (ComkerPermissionDPO) session.get(ComkerPermissionDPO.class, permissionIds[0])));
        item.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(item,
                (ComkerPermissionDPO) session.get(ComkerPermissionDPO.class, permissionIds[1])));
        item.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(item,
                (ComkerPermissionDPO) session.get(ComkerPermissionDPO.class, permissionIds[3])));
        testRoleDao.create(item);

        Assert.assertTrue(testRoleDao.count(null) == currentCount + 1);
        
        ComkerRoleDPO result = testRoleDao.getByCode("ROLE_06");
        Assert.assertTrue(result.getRoleJoinPermissionList().size() == 3);
    }

    @Test
    public void test_update() {
        Session session = testSessionFactory.getCurrentSession();
        ComkerRoleDPO item = testRoleDao.getByCode("ROLE_01");
        Assert.assertTrue(item.getRoleJoinPermissionList().size() == 4);

        item.getRoleJoinPermissionList().clear();
        item.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(item,
                (ComkerPermissionDPO) session.get(ComkerPermissionDPO.class, permissionIds[5])));
        item.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(item,
                (ComkerPermissionDPO) session.get(ComkerPermissionDPO.class, permissionIds[6])));
        item.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(item,
                (ComkerPermissionDPO) session.get(ComkerPermissionDPO.class, permissionIds[7])));
        testRoleDao.update(item);

        ComkerRoleDPO result = testRoleDao.getByCode("ROLE_01");
        Assert.assertTrue(result.getRoleJoinPermissionList().size() == 3);
    }

    @Test
    public void test_delete() {
        testRoleDao.delete(testRoleDao.getByCode("ROLE_03"));
        Assert.assertNull(testRoleDao.getByCode("ROLE_03"));
        Assert.assertTrue(testRoleDao.count(null) == (roles.length - 1));
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
        public ComkerRoleDao comkerRoleDao(
                @Qualifier("comkerSessionFactory") SessionFactory sessionFactory) {
            ComkerRoleDaoHibernate bean = new ComkerRoleDaoHibernate();
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
                    net.cokkee.comker.model.dpo.ComkerRoleJoinPermissionPK.class);
            asfb.setDataSource(dataSource);
            asfb.setHibernateProperties(hibernateProperties);
            return asfb;
        }
    }
}
