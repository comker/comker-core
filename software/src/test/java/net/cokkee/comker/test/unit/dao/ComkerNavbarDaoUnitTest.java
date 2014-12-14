package net.cokkee.comker.test.unit.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import net.cokkee.comker.dao.ComkerNavbarDao;

import net.cokkee.comker.model.dpo.ComkerNavbarNodeDPO;
import org.hamcrest.CoreMatchers;

import org.hibernate.Criteria;
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
@ContextConfiguration(locations = {"classpath:/test/unit/dao/ComkerNavbarDaoUnitTest.xml"})
@Transactional
public class ComkerNavbarDaoUnitTest {

    @Autowired
    private SessionFactory testSessionFactory = null;

    @Autowired
    private ComkerNavbarDao testNavbarDao = null;
    
    @Before
    public void init() {
        Session session = testSessionFactory.getCurrentSession();
        
        ComkerNavbarNodeDPO navbarRootDPO = null;
        ComkerNavbarNodeDPO item = null;
        ComkerNavbarNodeDPO subitem = null;
        ComkerNavbarNodeDPO subsubitem = null;

        navbarRootDPO = new ComkerNavbarNodeDPO(
                "NAVBAR_ROOT",
                "http://comker.net/",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        navbarRootDPO.setParent(null);
        session.saveOrUpdate(navbarRootDPO);

        item = new ComkerNavbarNodeDPO(
                "NAVBAR_BRANCH01",
                "http://comker.net/branch01",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        item.setParent(navbarRootDPO);
        navbarRootDPO.getChildren().add(item);
        session.saveOrUpdate(item);

        subitem = new ComkerNavbarNodeDPO(
                item.getCode() + "_SUB01",
                item.getUrl() + "/sub01",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        subitem.setParent(item);
        item.getChildren().add(subitem);
        session.saveOrUpdate(subitem);

        subitem = new ComkerNavbarNodeDPO(
                item.getCode() + "_SUB02",
                item.getUrl() + "/sub02",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        subitem.setParent(item);
        item.getChildren().add(subitem);
        session.saveOrUpdate(subitem);

        subsubitem = new ComkerNavbarNodeDPO(
                subitem.getCode() + "_SUBSUB01",
                subitem.getUrl() + "/subsub01",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        subsubitem.setParent(subitem);
        subitem.getChildren().add(subsubitem);
        session.saveOrUpdate(subsubitem);

        subsubitem = new ComkerNavbarNodeDPO(
                subitem.getCode() + "_SUBSUB02",
                subitem.getUrl() + "/subsub02",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        subsubitem.setParent(subitem);
        subitem.getChildren().add(subsubitem);
        session.saveOrUpdate(subsubitem);

        subsubitem = new ComkerNavbarNodeDPO(
                subitem.getCode() + "_SUBSUB03",
                subitem.getUrl() + "/subsub03",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        subsubitem.setParent(subitem);
        subitem.getChildren().add(subsubitem);
        session.saveOrUpdate(subsubitem);

        item = new ComkerNavbarNodeDPO(
                "NAVBAR_BRANCH02",
                "http://comker.net/branch02",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        item.setParent(navbarRootDPO);
        navbarRootDPO.getChildren().add(item);
        session.saveOrUpdate(item);

        ComkerNavbarNodeDPO root = testNavbarDao.getByCode("NAVBAR_ROOT");
        testNavbarDao.update(root);
    }

    @Test
    public void test_database() {
        Session session = testSessionFactory.getCurrentSession();
        Criteria c = session.createCriteria(ComkerNavbarNodeDPO.class);
        List all = c.list();
        Assert.assertTrue(all.size() == 8);
    }

    @Test
    public void test_navbar_count() {
        Assert.assertTrue(testNavbarDao.count() == 8);
    }

    @Test
    public void test_navbar_get_tree() {
        ComkerNavbarNodeDPO root = testNavbarDao.getTree();
        Map<String, Object> info = extractTreeInformation(root);
        Assert.assertTrue((Integer)info.get("depth")== 4);
        Assert.assertTrue((Integer)info.get("total")== 8);
    }

    @Test
    public void test_navbar_get_node_by_code() {
        ComkerNavbarNodeDPO root = testNavbarDao.getByCode("NAVBAR_ROOT");
        Assert.assertNotNull(root);

        ComkerNavbarNodeDPO node = testNavbarDao.getByCode("NAVBAR_BRANCH02");
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getUrl(), "http://comker.net/branch02");
    }

    @Test
    public void test_navbar_get_node_by_code_with_invalid_code() {
        ComkerNavbarNodeDPO root = testNavbarDao.getByCode("NAVBAR_NOT_FOUND");
        Assert.assertNull(root);
    }

    @Test
    public void test_navbar_get_node_by_id() {
        ComkerNavbarNodeDPO node1 = testNavbarDao.getByCode("NAVBAR_BRANCH01");
        ComkerNavbarNodeDPO node2 = testNavbarDao.get(node1.getId());
        Assert.assertTrue(node1 == node2);
    }

    @Test
    public void test_navbar_get_node_by_id_with_invalid_id() {
        Assert.assertNull(testNavbarDao.get("NODE_NOT_FOUND"));
    }

    @Test
    public void test_navbar_create_node() {
        ComkerNavbarNodeDPO subitem = null;
        ComkerNavbarNodeDPO subsubitem = null;

        subitem = testNavbarDao.getByCode("NAVBAR_BRANCH01_SUB02");
        Assert.assertNotNull(subitem);

        subsubitem = new ComkerNavbarNodeDPO(
                subitem.getCode() + "_SUBSUB04",
                subitem.getUrl() + "/subsub04",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        subsubitem.setParent(subitem);
        subitem.getChildren().add(subsubitem);
        ComkerNavbarNodeDPO result = testNavbarDao.create(subsubitem);

        Assert.assertNotNull(result);
        Assert.assertEquals(subsubitem.getCode(), result.getCode());
        Assert.assertEquals(subsubitem.getUrl(), result.getUrl());
        Assert.assertEquals(subsubitem.getLabel(), result.getLabel());
        Assert.assertEquals(subsubitem.getDescription(), result.getDescription());

        ComkerNavbarNodeDPO node = result;
        String treeId = result.getId();
        while(node.getParent() != null) {
            node = node.getParent();
            treeId = node.getId() + ">" + treeId;
        }
        Assert.assertEquals(result.getTreeId(), treeId);
    }

    @Test
    public void test_navbar_update_node() {
        
        ComkerNavbarNodeDPO item = testNavbarDao.getByCode("NAVBAR_BRANCH02");
        Assert.assertNotNull(item);

        ComkerNavbarNodeDPO subitem = testNavbarDao.getByCode("NAVBAR_BRANCH01_SUB02");
        Assert.assertNotNull(subitem);

        item.getChildren().add(subitem);
        subitem.setParent(item);
        testNavbarDao.update(subitem);

        // assert the relationship between parent and new child
        ComkerNavbarNodeDPO result = testNavbarDao.getByCode("NAVBAR_BRANCH01_SUB02");
        item = result.getParent();
        Assert.assertEquals("NAVBAR_BRANCH02", item.getCode());
        Assert.assertThat(item.getChildren(), CoreMatchers.hasItem(result));

        // assert the new treeId
        ComkerNavbarNodeDPO node = result;
        String treeId = result.getId();
        while(node.getParent() != null) {
            node = node.getParent();
            treeId = node.getId() + ">" + treeId;
        }
        Assert.assertEquals(result.getTreeId(), treeId);
    }

    @Test
    public void test_navbar_delete_node() {
        ComkerNavbarNodeDPO node = testNavbarDao.getByCode("NAVBAR_BRANCH01");
        testNavbarDao.delete(node);

        ComkerNavbarNodeDPO root = testNavbarDao.getTree();
        Map<String, Object> info = extractTreeInformation(root);
        Assert.assertTrue((Integer)info.get("depth")== 2);
        Assert.assertTrue((Integer)info.get("total")== 2);
    }

    @Test
    public void test_navbar_delete_root() {
        ComkerNavbarNodeDPO node = testNavbarDao.getByCode("NAVBAR_ROOT");
        testNavbarDao.delete(node);

        ComkerNavbarNodeDPO root = testNavbarDao.getTree();
        Assert.assertNull(root);
    }

    private Map<String, Object> extractTreeInformation(ComkerNavbarNodeDPO root) {
        int depth = 0;
        int count = 0;

        Queue<ComkerNavbarNodeDPO> queue = new LinkedList<ComkerNavbarNodeDPO>();
        Queue<ComkerNavbarNodeDPO> subqueue = new LinkedList<ComkerNavbarNodeDPO>();
        Queue<ComkerNavbarNodeDPO> tempo = null;

        if (root != null) {
            queue.add(root);
        }

        while (!queue.isEmpty()) {
            depth++;
            subqueue.clear();
            while(!queue.isEmpty()) {
                count++;
                ComkerNavbarNodeDPO node = queue.remove();
                subqueue.addAll(node.getChildren());
            }
            tempo = subqueue;
            subqueue = queue;
            queue = tempo;
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("depth", depth);
        result.put("total", count);

        return result;
    }
}
