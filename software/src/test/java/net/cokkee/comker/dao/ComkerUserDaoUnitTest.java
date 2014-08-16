package net.cokkee.comker.dao;

import java.util.ArrayList;
import java.util.List;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerUser;
import net.cokkee.comker.model.po.ComkerUserJoinCrew;
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
@ContextConfiguration(locations = {"classpath:/dao/ComkerUserDaoUnitTest.xml"})
@Transactional
public class ComkerUserDaoUnitTest {

    @Autowired
    private SessionFactory testSessionFactory = null;

    @Autowired
    private ComkerUserDao testUserDao = null;

    private ComkerUser[] users = new ComkerUser[5];

    private String[] crewIds = new String[8];

    @Before
    public void init() {
        Session session = testSessionFactory.getCurrentSession();
        
        for(int n=0; n<users.length; n++) {
            String postfix = "0" + (n + 1);
            users[n] = new ComkerUser("email" + postfix + "@gmail.com",
                    "username" + postfix, "password" + postfix, "Fullname " + postfix);
        }
        
        for(int i=0; i<crewIds.length; i++) {
            String prefix = ((i<10) ? "0":"") + (i + 1);
            ComkerCrew item = new ComkerCrew("Crew " + prefix, "Description Crew" + prefix);
            session.saveOrUpdate(item);

            crewIds[i] = item.getId();

            if(i<4) {
                users[0].getUserJoinCrewList().add(new ComkerUserJoinCrew(users[0], item));
            }

            if (3<=i && i<7) {
                users[1].getUserJoinCrewList().add(new ComkerUserJoinCrew(users[1], item));
            }

            if (4<=i && i<8) {
                users[2].getUserJoinCrewList().add(new ComkerUserJoinCrew(users[2], item));
            }

            users[3].getUserJoinCrewList().add(new ComkerUserJoinCrew(users[3], item));
        }

        for(int n=0; n<users.length; n++) {
            session.saveOrUpdate(users[n]);
        }
    }

    @Test
    public void test_count() {
        Assert.assertTrue(testUserDao.count(null) == users.length);
    }

    @Test
    public void test_list_all() {
        List<ComkerUser> list = testUserDao.findAll(null, null);
        Assert.assertTrue(list.size() == users.length);

        int count = 0;
        List<String> authorities1 = new ArrayList<String>();
        List<String> authorities2 = new ArrayList<String>();
        ComkerUser user0 = list.get(0);
        for(ComkerUserJoinCrew perm:user0.getUserJoinCrewList()) {
            authorities1.add("Crew 0" + (++count));
            authorities2.add(perm.getCrew().getName());
        }
        Assert.assertTrue(count == 4);
        Assert.assertThat(authorities1, CoreMatchers.is(authorities2));
    }

    @Test
    public void test_find_all_with_pager() {
        List<ComkerUser> list = testUserDao.findAll(null, new ComkerPager(1, 3));
        Assert.assertTrue(list.size() == 3);
        Assert.assertEquals(list.get(0).getUsername(), "username02");
        Assert.assertEquals(list.get(2).getUsername(), "username04");
    }

    @Test
    public void test_find_all_with_pager_out_of_range() {
        List<ComkerUser> list = testUserDao.findAll(null, new ComkerPager(4, 10));
        Assert.assertTrue(list.size() == 1);
        Assert.assertEquals(list.get(0).getUsername(), "username05");
    }

    @Test
    public void test_get_by_id() {
        ComkerUser item1 = testUserDao.getByUsername("username03");
        ComkerUser item2 = testUserDao.get(item1.getId());
        Assert.assertEquals(item1, item2);
    }

    @Test
    public void test_get_by_id_with_invalid_id() {
        ComkerUser item = testUserDao.get("ID_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_get_by_username() {
        ComkerUser item = testUserDao.getByUsername("username02");
        Assert.assertNotNull(item);
    }

    @Test
    public void test_get_by_username_with_invalid_username() {
        ComkerUser item = testUserDao.getByUsername("USERNAME_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_get_by_email() {
        ComkerUser item = testUserDao.getByEmail("email04@gmail.com");
        Assert.assertNotNull(item);
    }

    @Test
    public void test_get_by_email_with_invalid_email() {
        ComkerUser item = testUserDao.getByEmail("invalid@gmail.com");
        Assert.assertNull(item);
    }

    @Test
    public void test_exists_by_id() {
        for(int i=0; i<users.length; i++) {
            Assert.assertTrue(testUserDao.exists(users[i].getId()));
        }
    }

    @Test
    public void test_exists_by_invalid_id() {
        Assert.assertFalse(testUserDao.exists(null));

        for(int i=0; i<10; i++) {
            Assert.assertFalse(testUserDao.exists("user_id_" + i));
        }
    }

    @Test
    public void test_create() {
        Session session = testSessionFactory.getCurrentSession();
        
        int currentCount = testUserDao.count(null);
        Assert.assertTrue(currentCount == users.length);

        ComkerUser item = new ComkerUser("email06@gmail.com", "username06",
                "password06", "Fullname 06");
        item.getUserJoinCrewList().add(new ComkerUserJoinCrew(item,
                (ComkerCrew) session.get(ComkerCrew.class, crewIds[0])));
        item.getUserJoinCrewList().add(new ComkerUserJoinCrew(item,
                (ComkerCrew) session.get(ComkerCrew.class, crewIds[1])));
        item.getUserJoinCrewList().add(new ComkerUserJoinCrew(item,
                (ComkerCrew) session.get(ComkerCrew.class, crewIds[3])));
        testUserDao.create(item);

        Assert.assertTrue(testUserDao.count(null) == currentCount + 1);
        
        ComkerUser result = testUserDao.getByUsername("username06");
        Assert.assertTrue(result.getUserJoinCrewList().size() == 3);
    }

    @Test
    public void test_update() {
        Session session = testSessionFactory.getCurrentSession();
        ComkerUser item = testUserDao.getByUsername("username01");
        Assert.assertTrue(item.getUserJoinCrewList().size() == 4);

        item.getUserJoinCrewList().clear();
        item.getUserJoinCrewList().add(new ComkerUserJoinCrew(item,
                (ComkerCrew) session.get(ComkerCrew.class, crewIds[5])));
        item.getUserJoinCrewList().add(new ComkerUserJoinCrew(item,
                (ComkerCrew) session.get(ComkerCrew.class, crewIds[6])));
        item.getUserJoinCrewList().add(new ComkerUserJoinCrew(item,
                (ComkerCrew) session.get(ComkerCrew.class, crewIds[7])));
        testUserDao.update(item);

        ComkerUser result = testUserDao.getByUsername("username01");
        Assert.assertTrue(result.getUserJoinCrewList().size() == 3);
    }

    @Test
    public void test_delete() {
        testUserDao.delete(testUserDao.getByUsername("username03"));
        Assert.assertNull(testUserDao.getByUsername("username03"));
        Assert.assertNull(testUserDao.getByEmail("email03@gmail.com"));
        Assert.assertTrue(testUserDao.count(null) == (users.length - 1));
    }
}
