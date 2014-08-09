package net.cokkee.comker.storage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import net.cokkee.comker.dao.ComkerNavbarDao;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.storage.impl.ComkerNavbarStorageImpl;
import net.cokkee.comker.model.dto.ComkerNavNodeFormDTO;
import net.cokkee.comker.model.dto.ComkerNavNodeViewDTO;

import net.cokkee.comker.model.po.ComkerNavbarNode;
import org.junit.Assert;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComkerNavbarStorageUnitTest {
    
    @InjectMocks
    private ComkerNavbarStorageImpl navbarStorage;

    @Mock
    private ComkerNavbarDao navbarDao;

    private ComkerNavbarNode navbarRootDPO = null;

    private Map<String, ComkerNavbarNode> navbarMap = new HashMap<String, ComkerNavbarNode>();

    @Before
    public void init() {
        ComkerNavbarNode item = null;
        ComkerNavbarNode subitem = null;
        ComkerNavbarNode subsubitem = null;

        navbarRootDPO = new ComkerNavbarNode(
                "NAVBAR_ROOT",
                "http://comker.net/",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        navbarRootDPO.setId("ID_" + navbarRootDPO.getCode());
        navbarRootDPO.setParent(null);
        navbarMap.put(navbarRootDPO.getId(), navbarRootDPO);

        item = new ComkerNavbarNode(
                "NAVBAR_BRANCH01",
                "http://comker.net/branch01",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        item.setId("ID_" + item.getCode());
        item.setParent(navbarRootDPO);
        navbarRootDPO.getChildren().add(item);
        navbarMap.put(item.getId(), item);

        subitem = new ComkerNavbarNode(
                "NAVBAR_BRANCH01_SUB01",
                "http://comker.net/branch01/sub01",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        subitem.setId("ID_" + subitem.getCode());
        subitem.setParent(item);
        item.getChildren().add(subitem);
        navbarMap.put(subitem.getId(), subitem);

        subitem = new ComkerNavbarNode(
                "NAVBAR_BRANCH01_SUB02",
                "http://comker.net/branch01/sub02",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        subitem.setId("ID_" + subitem.getCode());
        subitem.setParent(item);
        item.getChildren().add(subitem);
        navbarMap.put(subitem.getId(), subitem);

        item = new ComkerNavbarNode(
                "NAVBAR_BRANCH02",
                "http://comker.net/branch02",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        item.setId("ID_" + item.getCode());
        item.setParent(navbarRootDPO);
        navbarRootDPO.getChildren().add(item);
        navbarMap.put(item.getId(), item);

        Mockito.when(navbarDao.count()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return navbarMap.size();
            }
        });

        Mockito.when(navbarDao.getTree()).thenAnswer(new Answer<ComkerNavbarNode>() {
            @Override
            public ComkerNavbarNode answer(InvocationOnMock invocation) throws Throwable {
                return navbarRootDPO;
            }
        });

        Mockito.when(navbarDao.getTree(anyString(), anyString())).thenAnswer(new Answer<ComkerNavbarNode>() {
            @Override
            public ComkerNavbarNode answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return navbarMap.get(id);
            }
        });

        Mockito.when(navbarDao.get(anyString())).thenAnswer(new Answer<ComkerNavbarNode>() {
            @Override
            public ComkerNavbarNode answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return navbarMap.get(id);
            }
        });
        
        Mockito.when(navbarDao.create(any(ComkerNavbarNode.class)))
                .thenAnswer(new Answer<ComkerNavbarNode>() {
            @Override
            public ComkerNavbarNode answer(InvocationOnMock invocation) throws Throwable {
                ComkerNavbarNode item = (ComkerNavbarNode) invocation.getArguments()[0];

                item.setId("ID_" + item.getCode());
                ComkerNavbarNode parent = item.getParent();
                parent.getChildren().add(item);
                item.setParent(parent);

                return item;
            }
        });

        Mockito.doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerNavbarNode item = (ComkerNavbarNode) invocation.getArguments()[0];
                if(item == null) return null;

                if (item == navbarRootDPO) {
                    navbarRootDPO = null;
                    navbarMap.clear();
                } else {
                    item.getParent().getChildren().remove(item);
                    
                    Queue<ComkerNavbarNode> queue = new LinkedList<ComkerNavbarNode>();
                    queue.add(item);

                    while(!queue.isEmpty()) {
                        item = queue.remove();
                        queue.addAll(item.getChildren());
                        navbarMap.remove(item.getId());
                    }
                }
                return null;
            }

        }).when(navbarDao).delete(any(ComkerNavbarNode.class));
    }

    @Test
    public void test_navbar_get_tree() {
        ComkerNavNodeViewDTO root = navbarStorage.getTree();
        Map<String, Object> info = extractTreeInformation(root);

        Assert.assertTrue((Integer)info.get("depth")== 3);
        Assert.assertTrue((Integer)info.get("total")== navbarMap.size());
    }

    @Test
    public void test_navbar_create_node() {
        ComkerNavNodeFormDTO form = new ComkerNavNodeFormDTO(
                "NAVBAR_BRANCH01_SUB03",
                "http://comker.net/branch01/sub03",
                new String[] {"PERMISSION_01", "PERMISSION_02"},
                "Branch 01 - Sub 03",
                "This is Branch 01, with the third Sub");
        form.setParentId("ID_NAVBAR_BRANCH01");
        ComkerNavNodeViewDTO view = navbarStorage.create(form);

        Assert.assertNotNull(view);
        Assert.assertEquals(form.getCode(), view.getCode());
        Assert.assertEquals(form.getUrl(), view.getUrl());
        Assert.assertEquals(form.getLabel(), view.getLabel());
        Assert.assertEquals(form.getDescription(), view.getDescription());

        boolean found = false;
        ComkerNavNodeViewDTO parent = navbarStorage.getTree(form.getParentId(), null);
        for(ComkerNavNodeViewDTO child:parent.getChildren()) {
            if (child.getId().equals(view.getId())) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(found);
    }

    @Test(expected = ComkerInvalidParameterException.class)
    public void test_navbar_create_node_with_null_parent_id() {
        ComkerNavNodeFormDTO form = new ComkerNavNodeFormDTO(
                "NAVBAR_BRANCH01_SUB09",
                "http://comker.net/branch01/sub09",
                new String[] {"PERMISSION_01", "PERMISSION_02"},
                "Branch 01 - Sub 09",
                "This is Branch 01, with the 9th Sub");
        form.setParentId(null);
        ComkerNavNodeViewDTO view = navbarStorage.create(form);
    }

    @Test
    public void test_navbar_delete_node() {
        navbarStorage.delete("ID_NAVBAR_BRANCH01");

        ComkerNavNodeViewDTO root = navbarStorage.getTree();
        Map<String, Object> info = extractTreeInformation(root);

        Assert.assertTrue((Integer)info.get("depth")== 2);
        Assert.assertTrue((Integer)info.get("total")== 2);
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_navbar_delete_node_with_invalid_id() {
        navbarStorage.delete("NODE_NOT_FOUND");
    }

    private Map<String, Object> extractTreeInformation(ComkerNavNodeViewDTO root) {
        int depth = 0;
        int count = 0;

        Queue<ComkerNavNodeViewDTO> queue = new LinkedList<ComkerNavNodeViewDTO>();
        Queue<ComkerNavNodeViewDTO> subqueue = new LinkedList<ComkerNavNodeViewDTO>();
        Queue<ComkerNavNodeViewDTO> tempo = null;

        if (root != null) {
            queue.add(root);
        }

        while (!queue.isEmpty()) {
            depth++;
            subqueue.clear();
            while(!queue.isEmpty()) {
                count++;
                ComkerNavNodeViewDTO node = queue.remove();
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
