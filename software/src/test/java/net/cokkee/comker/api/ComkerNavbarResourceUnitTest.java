package net.cokkee.comker.api;

import javax.ws.rs.core.Response;
import net.cokkee.comker.api.impl.ComkerNavbarResourceImpl;
import net.cokkee.comker.storage.ComkerNavbarStorage;
import net.cokkee.comker.model.dto.ComkerNavNodeViewDTO;
import org.junit.Assert;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/api/ComkerNavbarResourceUnitTest.xml"})
public class ComkerNavbarResourceUnitTest {

    @Autowired
    protected ComkerNavbarResource navbarClient;

    @Autowired
    @InjectMocks
    private ComkerNavbarResourceImpl navbarServer;

    @Mock
    private ComkerNavbarStorage navbarStorage;

    private ComkerNavNodeViewDTO navbarRoot = null;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        navbarRoot = new ComkerNavNodeViewDTO(
                "NAVBAR_ROOT",
                "http://comker.net/",
                new String[] {"PERMISSION_01", "PERMISSION_02"});

        ComkerNavNodeViewDTO parent = null;
        ComkerNavNodeViewDTO item = null;

        parent = navbarRoot;
        item = new ComkerNavNodeViewDTO(
                "NAVBAR_BRANCH_01",
                "http://comker.net/branch01",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        parent.getChildren().add(item);

        parent = item;

        item = new ComkerNavNodeViewDTO(
                "NAVBAR_BRANCH01_SUB01",
                "http://comker.net/branch01/sub01",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        parent.getChildren().add(item);

        item = new ComkerNavNodeViewDTO(
                "NAVBAR_BRANCH01_SUB02",
                "http://comker.net/branch01/sub02",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        parent.getChildren().add(item);

        parent = navbarRoot;
        item = new ComkerNavNodeViewDTO(
                "NAVBAR_BRANCH_02",
                "http://comker.net/branch02",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        parent.getChildren().add(item);

        Mockito.when(navbarStorage.getTree()).thenAnswer(new Answer<ComkerNavNodeViewDTO>() {
            @Override
            public ComkerNavNodeViewDTO answer(InvocationOnMock invocation) throws Throwable {
                return navbarRoot;
            }
        });
    }

    @Test
    public void test_navbar_get_tree_root() {
        Response resp = navbarClient.getTreeFromRoot();
        Assert.assertTrue(resp.getStatus() == 200);
        
        ComkerNavNodeViewDTO result = resp.readEntity(ComkerNavNodeViewDTO.class);
        Assert.assertEquals(result.getCode(), navbarRoot.getCode());
    }

    @Test
    public void test_navbar_get_tree_root_exclude_node() {
        System.out.println("------------- Not supported yet -------------");
    }

    @Test
    public void test_navbar_get_tree_node() {
        System.out.println("------------- Not supported yet -------------");
    }

    @Test
    public void test_navbar_get_tree_node_exclude_node() {
        System.out.println("------------- Not supported yet -------------");
    }
}
