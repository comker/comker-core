package net.cokkee.comker.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerRoleJoinPermission;
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
@ContextConfiguration(locations = {"classpath:/dao/ComkerRoleDaoUnitTest.xml"})
@Transactional
public class ComkerRoleDaoUnitTest {

    @Autowired
    private SessionFactory testSessionFactory = null;

    @Autowired
    private ComkerRoleDao testRoleDao = null;

    private ComkerRole[] roles = new ComkerRole[5];

    private String[] permissionIds = new String[8];

    @Before
    public void init() {
        Session session = testSessionFactory.getCurrentSession();
        
        for(int n=0; n<roles.length; n++) {
            String postfix = "0" + (n + 1);
            roles[n] = new ComkerRole("ROLE_" + postfix,
                    "Role " + postfix, "Description Role " + postfix);
        }
        
        for(int i=0; i<permissionIds.length; i++) {
            String prefix = (i<10)?"0":"";

            String authorityString = "PERMISSION_" + prefix + (i + 1);
            ComkerPermission item = new ComkerPermission(authorityString);
            session.saveOrUpdate(item);

            permissionIds[i] = item.getId();

            if(i<4) {
                roles[0].getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(roles[0], item));
            }

            if (3<=i && i<7) {
                roles[1].getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(roles[1], item));
            }

            if (4<=i && i<8) {
                roles[2].getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(roles[2], item));
            }

            roles[3].getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(roles[3], item));
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
        List<ComkerRole> list = testRoleDao.findAll(null, null);
        Assert.assertTrue(list.size() == roles.length);

        int count = 0;
        Set<String> authorities1 = new HashSet();
        Set<String> authorities2 = new HashSet();
        ComkerRole role0 = list.get(0);
        for(ComkerRoleJoinPermission perm:role0.getRoleJoinPermissionList()) {
            authorities1.add("PERMISSION_0" + (++count));
            authorities2.add(perm.getPermission().getAuthority());
        }
        Assert.assertTrue(count == 4);
        Assert.assertThat(authorities1, CoreMatchers.is(authorities2));
    }

    @Test
    public void test_find_all_with_pager() {
        List<ComkerRole> list = testRoleDao.findAll(null, new ComkerQueryPager(1, 3));
        Assert.assertTrue(list.size() == 3);
        Assert.assertEquals(list.get(0).getCode(), "ROLE_02");
        Assert.assertEquals(list.get(2).getCode(), "ROLE_04");
    }

    @Test
    public void test_find_all_with_pager_out_of_range() {
        List<ComkerRole> list = testRoleDao.findAll(null, new ComkerQueryPager(4, 10));
        Assert.assertTrue(list.size() == 1);
        Assert.assertEquals(list.get(0).getCode(), "ROLE_05");
    }

    @Test
    public void test_get_by_id() {
        ComkerRole item1 = testRoleDao.getByCode("ROLE_03");
        ComkerRole item2 = testRoleDao.get(item1.getId());
        Assert.assertEquals(item1, item2);
    }

    @Test
    public void test_get_by_id_with_invalid_id() {
        ComkerRole item = testRoleDao.get("ID_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_get_by_code() {
        ComkerRole item = testRoleDao.getByCode("ROLE_02");
        Assert.assertNotNull(item);
    }

    @Test
    public void test_get_by_code_with_invalid_code() {
        ComkerRole item = testRoleDao.getByCode("ROLE_CODE_NOT_FOUND");
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

        ComkerRole item = new ComkerRole("ROLE_06", "Role 06", "Description Role 06");
        item.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(item, 
                (ComkerPermission) session.get(ComkerPermission.class, permissionIds[0])));
        item.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(item,
                (ComkerPermission) session.get(ComkerPermission.class, permissionIds[1])));
        item.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(item,
                (ComkerPermission) session.get(ComkerPermission.class, permissionIds[3])));
        testRoleDao.create(item);

        Assert.assertTrue(testRoleDao.count(null) == currentCount + 1);
        
        ComkerRole result = testRoleDao.getByCode("ROLE_06");
        Assert.assertTrue(result.getRoleJoinPermissionList().size() == 3);
    }

    @Test
    public void test_update() {
        Session session = testSessionFactory.getCurrentSession();
        ComkerRole item = testRoleDao.getByCode("ROLE_01");
        Assert.assertTrue(item.getRoleJoinPermissionList().size() == 4);

        item.getRoleJoinPermissionList().clear();
        item.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(item,
                (ComkerPermission) session.get(ComkerPermission.class, permissionIds[5])));
        item.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(item,
                (ComkerPermission) session.get(ComkerPermission.class, permissionIds[6])));
        item.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(item,
                (ComkerPermission) session.get(ComkerPermission.class, permissionIds[7])));
        testRoleDao.update(item);

        ComkerRole result = testRoleDao.getByCode("ROLE_01");
        Assert.assertTrue(result.getRoleJoinPermissionList().size() == 3);
    }

    @Test
    public void test_delete() {
        testRoleDao.delete(testRoleDao.getByCode("ROLE_03"));
        Assert.assertNull(testRoleDao.getByCode("ROLE_03"));
        Assert.assertTrue(testRoleDao.count(null) == (roles.length - 1));
    }
}
