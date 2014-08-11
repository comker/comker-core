package net.cokkee.comker.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerModule;
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
@ContextConfiguration(locations = {"classpath:/dao/ComkerModuleDaoUnitTest.xml"})
@Transactional
public class ComkerModuleDaoUnitTest {

    @Autowired
    private SessionFactory testSessionFactory = null;

    @Autowired
    private ComkerModuleDao testModuleDao = null;

    private List<String> moduleIdx = new ArrayList<String>();

    private Set<String> moduleCodes = new HashSet<String>();
    
    @Before
    public void init() {
        ComkerModule item = null;
        Session session = testSessionFactory.getCurrentSession();
        
        for(int i=0; i<20; i++) {
            String authorityString = "MODULE_" + i;
            moduleCodes.add(authorityString);

            item = new ComkerModule(authorityString, "Module " + i, "Description Module " + i);
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
            ComkerModule module = (ComkerModule) item;
            resultSet.add(module.getCode());
        }
        Assert.assertThat(resultSet, CoreMatchers.is(moduleCodes));
    }

    @Test
    public void test_find_all_modules_with_pager() {
        List list = testModuleDao.findAll(new ComkerPager());
        Assert.assertTrue(list.size() == ComkerPager.DEFAULT_LIMIT);
    }

    @Test
    public void test_get_by_code() {
        ComkerModule item = testModuleDao.getByCode("MODULE_1");
        Assert.assertNotNull(item);
    }

    @Test
    public void test_get_by_code_with_invalid_code() {
        ComkerModule item = testModuleDao.getByCode("MODULE_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_get_by_id() {
        ComkerModule item = testModuleDao.getByCode("MODULE_1");
        ComkerModule item2 = testModuleDao.get(item.getId());
        Assert.assertEquals(item, item2);
    }

    @Test
    public void test_get_by_id_with_invalid_id() {
        ComkerModule item = testModuleDao.get("ID_NOT_FOUND");
        Assert.assertNull(item);
    }

    @Test
    public void test_create() {
        int currentCount = testModuleDao.count();

        ComkerModule item = new ComkerModule(
                "MODULE_" + currentCount,
                "Module " + currentCount,
                "Description Module " + currentCount);
        moduleIdx.add(String.valueOf(testModuleDao.save(item)));

        Assert.assertTrue(testModuleDao.count() == currentCount + 1);
    }
}
