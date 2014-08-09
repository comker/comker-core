package net.cokkee.comker.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerPermission;
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
@ContextConfiguration(locations = {"classpath:/dao/ComkerPermissionDaoUnitTest.xml"})
@Transactional
public class ComkerPermissionDaoUnitTest {

    @Autowired
    private SessionFactory testSessionFactory = null;

    @Autowired
    private ComkerPermissionDao testPermissionDao = null;

    private Set<String> authorities = new HashSet<String>();
    
    @Before
    public void init() {
        ComkerPermission item = null;
        Session session = testSessionFactory.getCurrentSession();
        
        for(int i=0; i<20; i++) {
            String prefix = (i<10)?"0":"";

            String authorityString = "PERMISSION_" + prefix + (i + 1);
            authorities.add(authorityString);

            item = new ComkerPermission(authorityString);
            session.saveOrUpdate(item);
        }
    }

    @Test
    public void test_count_permissions() {
        Assert.assertTrue(testPermissionDao.count() == 20);
    }

    @Test
    public void test_find_all_permissions() {
        List list = testPermissionDao.findAll(null);
        Set<String> resultSet = new HashSet<String>();
        for(Object item:list) {
            ComkerPermission permission = (ComkerPermission) item;
            resultSet.add(permission.getAuthority());
        }
        Assert.assertThat(resultSet, CoreMatchers.is(authorities));
    }

    @Test
    public void test_find_all_permissions_with_pager() {
        List list = testPermissionDao.findAll(new ComkerPager());
        Assert.assertTrue(list.size() == ComkerPager.DEFAULT_LIMIT);
    }

    @Test
    public void test_get_by_authoritiy() {
        ComkerPermission item = testPermissionDao.getByAuthority("PERMISSION_01");
        Assert.assertNotNull(item);
    }

    @Test
    public void test_get_by_authoritiy_with_invalid_authority_code() {
        ComkerPermission item = testPermissionDao.getByAuthority("PERMISSION_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_get_by_id() {
        ComkerPermission item = testPermissionDao.getByAuthority("PERMISSION_01");
        ComkerPermission item2 = testPermissionDao.get(item.getId());
        Assert.assertEquals(item, item2);
    }

    @Test
    public void test_get_by_id_with_invalid_id() {
        ComkerPermission item = testPermissionDao.get("ID_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_create() {
        int currentCount = testPermissionDao.count();

        ComkerPermission item = new ComkerPermission("PERMISSION_0A");
        testPermissionDao.save(item);

        Assert.assertTrue(testPermissionDao.count() == currentCount + 1);
    }
}
