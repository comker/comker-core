package net.cokkee.comker.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.po.ComkerModule;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.model.po.ComkerSpotJoinModule;
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
@ContextConfiguration(locations = {"classpath:/dao/ComkerSpotDaoUnitTest.xml"})
@Transactional
public class ComkerSpotDaoUnitTest {

    @Autowired
    private SessionFactory testSessionFactory = null;

    @Autowired
    private ComkerSpotDao testSpotDao = null;

    private ComkerSpot[] spotObjects = new ComkerSpot[5];

    private String[] moduleIds = new String[8];

    @Before
    public void init() {
        Session session = testSessionFactory.getCurrentSession();
        
        for(int n=0; n<spotObjects.length; n++) {
            String postfix = "0" + (n + 1);
            spotObjects[n] = new ComkerSpot("SPOT_" + postfix,
                    "Spot " + postfix, "Description Spot " + postfix);
        }
        
        for(int i=0; i<moduleIds.length; i++) {
            String prefix = ((i<10)?"0":"") + (i + 1);
            ComkerModule item = new ComkerModule("MODULE_" + prefix, "Module " + prefix, "");
            session.saveOrUpdate(item);
            moduleIds[i] = item.getId();

            if(i<4) {
                spotObjects[0].getSpotJoinModuleList().add(new ComkerSpotJoinModule(spotObjects[0], item));
            }

            if (3<=i && i<7) {
                spotObjects[1].getSpotJoinModuleList().add(new ComkerSpotJoinModule(spotObjects[1], item));
            }

            if (4<=i && i<8) {
                spotObjects[2].getSpotJoinModuleList().add(new ComkerSpotJoinModule(spotObjects[2], item));
            }

            spotObjects[3].getSpotJoinModuleList().add(new ComkerSpotJoinModule(spotObjects[3], item));
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
        List<ComkerSpot> list = testSpotDao.findAll(null, null);
        Assert.assertTrue(list.size() == spotObjects.length);

        int count = 0;
        Set<String> authorities1 = new HashSet();
        Set<String> authorities2 = new HashSet();
        ComkerSpot spot0 = list.get(0);
        for(ComkerSpotJoinModule perm:spot0.getSpotJoinModuleList()) {
            authorities1.add("MODULE_0" + (++count));
            authorities2.add(perm.getModule().getCode());
        }
        Assert.assertTrue(count == 4);
        Assert.assertThat(authorities1, CoreMatchers.is(authorities2));
    }

    @Test
    public void test_find_all_with_pager() {
        List<ComkerSpot> list = testSpotDao.findAll(null,new ComkerQueryPager(1, 3));
        Assert.assertTrue(list.size() == 3);
        Assert.assertEquals(list.get(0).getCode(), "SPOT_02");
        Assert.assertEquals(list.get(2).getCode(), "SPOT_04");
    }

    @Test
    public void test_find_all_with_pager_out_of_range() {
        List<ComkerSpot> list = testSpotDao.findAll(null,new ComkerQueryPager(4, 10));
        Assert.assertTrue(list.size() == 1);
        Assert.assertEquals(list.get(0).getCode(), "SPOT_05");
    }

    @Test
    public void test_get_by_id() {
        ComkerSpot item1 = testSpotDao.getByCode("SPOT_03");
        ComkerSpot item2 = testSpotDao.get(item1.getId());
        Assert.assertEquals(item1, item2);
    }

    @Test
    public void test_get_by_id_with_invalid_id() {
        ComkerSpot item = testSpotDao.get("ID_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_get_by_code() {
        ComkerSpot item = testSpotDao.getByCode("SPOT_02");
        Assert.assertNotNull(item);
    }

    @Test
    public void test_get_by_code_with_invalid_code() {
        ComkerSpot item = testSpotDao.getByCode("SPOT_CODE_NOT_FOUND");
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

        ComkerSpot item = new ComkerSpot("SPOT_06", "Spot 06", "Description Spot 06");
        item.getSpotJoinModuleList().add(new ComkerSpotJoinModule(item,
                (ComkerModule) session.get(ComkerModule.class, moduleIds[0])));
        item.getSpotJoinModuleList().add(new ComkerSpotJoinModule(item,
                (ComkerModule) session.get(ComkerModule.class, moduleIds[1])));
        item.getSpotJoinModuleList().add(new ComkerSpotJoinModule(item,
                (ComkerModule) session.get(ComkerModule.class, moduleIds[3])));
        testSpotDao.create(item);

        Assert.assertTrue(testSpotDao.count(null) == currentCount + 1);
        
        ComkerSpot result = testSpotDao.getByCode("SPOT_06");
        Assert.assertTrue(result.getSpotJoinModuleList().size() == 3);
    }

    @Test
    public void test_update() {
        Session session = testSessionFactory.getCurrentSession();
        ComkerSpot item = testSpotDao.getByCode("SPOT_01");
        Assert.assertTrue(item.getSpotJoinModuleList().size() == 4);

        item.getSpotJoinModuleList().clear();
        item.getSpotJoinModuleList().add(new ComkerSpotJoinModule(item,
                (ComkerModule) session.get(ComkerModule.class, moduleIds[5])));
        item.getSpotJoinModuleList().add(new ComkerSpotJoinModule(item,
                (ComkerModule) session.get(ComkerModule.class, moduleIds[6])));
        item.getSpotJoinModuleList().add(new ComkerSpotJoinModule(item,
                (ComkerModule) session.get(ComkerModule.class, moduleIds[7])));
        testSpotDao.update(item);

        ComkerSpot result = testSpotDao.getByCode("SPOT_01");
        Assert.assertTrue(result.getSpotJoinModuleList().size() == 3);
    }

    @Test
    public void test_delete() {
        testSpotDao.delete(testSpotDao.getByCode("SPOT_03"));
        Assert.assertNull(testSpotDao.getByCode("SPOT_03"));
        Assert.assertTrue(testSpotDao.count(null) == (spotObjects.length - 1));
    }
}
