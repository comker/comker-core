package net.cokkee.comker.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.cokkee.comker.dao.ComkerModuleDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.exception.ComkerValidationFailedException;
import net.cokkee.comker.storage.impl.ComkerSpotStorageImpl;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.dto.ComkerSpotDTO;
import net.cokkee.comker.model.po.ComkerModule;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.model.po.ComkerSpotJoinModule;
import net.cokkee.comker.util.ComkerDataUtil;
import net.cokkee.comker.validation.ComkerSpotValidator;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

/**
 *
 * @author drupalex
 */
@RunWith(MockitoJUnitRunner.class)
public class ComkerSpotStorageUnitTest {

    private List<ComkerSpot> spotIdx = new ArrayList<ComkerSpot>();

    private List<ComkerModule> moduleIdx = new ArrayList<ComkerModule>();

    @InjectMocks
    private ComkerSpotStorageImpl spotStorage;

    @InjectMocks
    private ComkerSpotValidator spotValidator;
    
    @Mock
    private ComkerSpotDao spotDao;

    @Mock
    private ComkerModuleDao moduleDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        spotStorage.setSpotValidator(spotValidator);
        
        for(int i=0; i<7; i++) {
            ComkerModule module = new ComkerModule(
                    "MODULE_0" + i,
                    "Module " + i,
                    "Description for module " + i);
            module.setId(UUID.randomUUID().toString());
            moduleIdx.add(module);
        }

        when(moduleDao.exists(anyString())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(ComkerModule module:moduleIdx) {
                    if(module.getId().equals(id)) return true;
                }
                return false;
            }
        });

        when(moduleDao.get(anyString())).thenAnswer(new Answer<ComkerModule>() {
            @Override
            public ComkerModule answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(ComkerModule module:moduleIdx) {
                    if(module.getId().equals(id)) return module;
                }
                return null;
            }
        });

        ComkerSpot spot;

        for(int i=0; i<4; i++) {
            spot = new ComkerSpot("SPOT_0" + i, "Spot 0" + i, "This is spot 0" + i);
            spot.setId(UUID.randomUUID().toString());
            spotIdx.add(spot);
        }

        spot = spotIdx.get(1);
        spot.getSpotJoinModuleList().add(new ComkerSpotJoinModule(spot, moduleIdx.get(1)));
        spot.getSpotJoinModuleList().add(new ComkerSpotJoinModule(spot, moduleIdx.get(2)));

        spot = spotIdx.get(2);
        spot.getSpotJoinModuleList().add(new ComkerSpotJoinModule(spot, moduleIdx.get(1)));
        spot.getSpotJoinModuleList().add(new ComkerSpotJoinModule(spot, moduleIdx.get(2)));
        spot.getSpotJoinModuleList().add(new ComkerSpotJoinModule(spot, moduleIdx.get(3)));
        spot.getSpotJoinModuleList().add(new ComkerSpotJoinModule(spot, moduleIdx.get(4)));

        /*
         * mocks for count() method
         */
        when(spotDao.count(any(ComkerSpotDTO.Filter.class))).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return spotIdx.size();
            }
        });

        when(spotDao.findAll(any(ComkerSpotDTO.Filter.class),any(ComkerQueryPager.class)))
                .thenAnswer(new Answer<List<ComkerSpot>>() {
            @Override
            public List<ComkerSpot> answer(InvocationOnMock invocation) throws Throwable {
                List<ComkerSpot> result = new ArrayList<ComkerSpot>(spotIdx);
                return result;
            }
        });

        when(spotDao.get(anyString())).thenAnswer(new Answer<ComkerSpot>() {
            @Override
            public ComkerSpot answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(ComkerSpot spot:spotIdx) {
                    if(spot.getId().equals(id)) return spot;
                }
                return null;
            }
        });

        when(spotDao.getByCode(anyString())).thenAnswer(new Answer<ComkerSpot>() {
            @Override
            public ComkerSpot answer(InvocationOnMock invocation) throws Throwable {
                String code = (String) invocation.getArguments()[0];
                for(ComkerSpot spot:spotIdx) {
                    if(spot.getCode().equals(code)) return spot;
                }
                return null;
            }
        });

        doAnswer(new Answer<ComkerSpot>() {
            @Override
            public ComkerSpot answer(InvocationOnMock invocation) throws Throwable {
                ComkerSpot spot = (ComkerSpot) invocation.getArguments()[0];
                spot.setId(UUID.randomUUID().toString());
                spotIdx.add(spot);
                return spot;
            }
        }).when(spotDao).create(any(ComkerSpot.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerSpot spot = (ComkerSpot) invocation.getArguments()[0];

                ComkerSpot spotOnDB = null;
                for(ComkerSpot item:spotIdx) {
                    if(item.getId().equals(spot.getId())) {
                        spotOnDB = item;
                        break;
                    }
                }

                if (spotOnDB != null) {
                    ComkerDataUtil.copyProperties(spot, spotOnDB);
                }
                return null;
            }
        }).when(spotDao).update(any(ComkerSpot.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerSpot spot = (ComkerSpot) invocation.getArguments()[0];

                ComkerSpot spotOnDB = null;
                for(ComkerSpot item:spotIdx) {
                    if(item.getId().equals(spot.getId())) {
                        spotOnDB = item;
                        break;
                    }
                }

                if (spotOnDB != null) {
                    spotIdx.remove(spotOnDB);
                }
                return null;
            }
        }).when(spotDao).delete(any(ComkerSpot.class));
    }

    @Test
    public void test_count() {
        Integer result = spotStorage.count();
        assertEquals(result.intValue(), spotIdx.size());
    }

    @Test
    public void test_find_all_spot_objects() {
        List<ComkerSpotDTO> result = spotStorage.findAll(null);
        assertEquals(result.size(), spotIdx.size());
    }

    @Test
    public void test_get_spot_object_by_id() {
        ComkerSpotDTO result = spotStorage.get(spotIdx.get(1).getId());
        verify(spotDao).get(spotIdx.get(1).getId());
        assertArrayEquals(
                new String[] {moduleIdx.get(1).getId(), moduleIdx.get(2).getId()},
                result.getModuleIds());
        
        result = spotStorage.get(spotIdx.get(2).getId());
        assertEquals(result.getModuleIds().length, 4);

        result = spotStorage.get(spotIdx.get(3).getId());
        assertEquals(result.getModuleIds().length, 0);
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_get_spot_object_by_invalid_id() {
        ComkerSpotDTO result = spotStorage.get("spot-not-found");
        verify(spotDao).get("spot-not-found");
    }

    @Test
    public void test_get_spot_object_by_code() {
        ComkerSpotDTO result = spotStorage.getByCode(spotIdx.get(1).getCode());
        verify(spotDao).getByCode(spotIdx.get(1).getCode());
        assertEquals(result.getModuleIds().length, 2);
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_get_spot_object_by_wrong_code() {
        ComkerSpotDTO result = spotStorage.get("SPOT_NOT_FOUND");
    }

    @Test(expected = ComkerValidationFailedException.class)
    public void test_create_spot_object_with_duplicated_code() {
        ComkerSpotDTO param = new ComkerSpotDTO(spotIdx.get(2).getCode(), "A new spot", null);
        spotStorage.create(param);
    }

    @Test
    public void test_create_spot_object_with_valid_code() {
        int count = spotIdx.size();
        ComkerSpotDTO param = new ComkerSpotDTO(
                "SPOT_0" + count,
                "Spot 0" + count,
                "This is spot " + count);
        param.setModuleIds(new String[] {
            moduleIdx.get(1).getId(), moduleIdx.get(6).getId()});
        ComkerSpotDTO result = spotStorage.create(param);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(result.getCode(), param.getCode());
        assertEquals(result.getName(), param.getName());
        assertEquals(result.getDescription(), param.getDescription());
        assertArrayEquals(
                new String[] {moduleIdx.get(1).getId(), moduleIdx.get(6).getId()},
                result.getModuleIds());
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_update_spot_object_with_invalid_id() {
        ComkerSpotDTO param = new ComkerSpotDTO("SOMETHING", "Something - modified", null);
        param.setId("spot-not-found");
        spotStorage.update(param);
    }

    @Test
    public void test_update_spot_object_with_duplicated_both_id_and_code() {
        ComkerSpotDTO param = new ComkerSpotDTO(
                spotIdx.get(2).getCode(),
                spotIdx.get(2).getName() + " - modified", null);
        param.setId(spotIdx.get(2).getId());
        spotStorage.update(param);
        ComkerSpotDTO result = spotStorage.get(spotIdx.get(2).getId());
        assertEquals(result.getName(), param.getName());
    }

    @Test(expected = ComkerValidationFailedException.class)
    public void test_update_spot_object_with_duplicated_code() {
        ComkerSpotDTO param = new ComkerSpotDTO(
                spotIdx.get(2).getCode(),
                spotIdx.get(1).getName() + " - modified", null);
        param.setId(spotIdx.get(1).getId());
        spotStorage.update(param);
    }

    @Test
    public void test_update_spot_object_with_null_moduleIds() {
        ComkerSpot source = spotIdx.get(1);
        ComkerSpotDTO param = new ComkerSpotDTO(source.getCode(), source.getName() + " - modified", null);
        param.setId(source.getId());
        spotStorage.update(param);
        assertEquals(source.getCode(), param.getCode());
        assertEquals(source.getName(), param.getName());
        assertNull(source.getDescription());
    }

    @Test
    public void test_update_spot_object_with_valid_moduleIds() {
        ComkerSpot source = spotIdx.get(1);

        ComkerSpotDTO param = new ComkerSpotDTO(source.getCode(),
                source.getName() + " - modified", source.getDescription() + " - modified");
        param.setId(source.getId());
        param.setModuleIds(new String[] {
            moduleIdx.get(2).getId(),
            moduleIdx.get(5).getId(),
            moduleIdx.get(6).getId()});

        spotStorage.update(param);

        ComkerSpotDTO result = spotStorage.get(source.getId());
        assertEquals(source.getCode(), result.getCode());
        assertEquals(source.getName(), result.getName());
        assertEquals(source.getDescription(), result.getDescription());
        assertArrayEquals(new String[] {
            moduleIdx.get(2).getId(),
            moduleIdx.get(5).getId(),
            moduleIdx.get(6).getId()}, result.getModuleIds());
    }

    @Test(expected=ComkerValidationFailedException.class)
    public void test_update_spot_object_with_invalid_moduleIds() {
        ComkerSpotDTO param = new ComkerSpotDTO(
                spotIdx.get(1).getCode(),
                spotIdx.get(1).getName() + " - modified",
                "Spot 01 description");
        param.setId(spotIdx.get(1).getId());
        param.setModuleIds(new String[] {
            moduleIdx.get(1).getId(),
            moduleIdx.get(2).getId(), "module-not-found"});
        spotStorage.update(param);
        ComkerSpotDTO result = spotStorage.get(spotIdx.get(1).getId());
        assertArrayEquals(new String[] {
            moduleIdx.get(1).getId(),
            moduleIdx.get(2).getId(),}, result.getModuleIds());
    }

    @Test
    public void test_delete_spot_object_by_id() {
        int total = spotIdx.size();
        spotStorage.delete(spotIdx.get(2).getId());
        assertEquals(total - 1, spotIdx.size());
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_delete_spot_object_by_invalid_id() {
        spotStorage.delete("spot-not-found");
    }
}
