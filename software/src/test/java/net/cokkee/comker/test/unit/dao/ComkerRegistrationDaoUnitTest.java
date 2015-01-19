package net.cokkee.comker.test.unit.dao;

import net.cokkee.comker.dao.ComkerRegistrationDao;
import net.cokkee.comker.model.dpo.ComkerRegistrationDPO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test/unit/dao/ComkerRegistrationDaoUnitTest.xml"})
@Transactional
public class ComkerRegistrationDaoUnitTest {

    @Autowired
    private SessionFactory testSessionFactory;

    @Autowired
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
}
