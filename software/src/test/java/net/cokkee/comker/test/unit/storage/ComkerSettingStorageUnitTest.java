package net.cokkee.comker.test.unit.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.cokkee.comker.dao.ComkerSettingDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.storage.impl.ComkerSettingStorageImpl;
import net.cokkee.comker.model.dpo.ComkerSettingEntryDPO;
import net.cokkee.comker.model.dpo.ComkerSettingKeyDPO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;
import net.cokkee.comker.model.dpo.ComkerUserDPO;
import net.cokkee.comker.util.ComkerDataUtil;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.Assert;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;

/**
 *
 * @author drupalex
 */
@RunWith(MockitoJUnitRunner.class)
public class ComkerSettingStorageUnitTest {

    private Map<String, ComkerSpotDPO> spotMap = new HashMap<String, ComkerSpotDPO>();
    private Map<String, ComkerUserDPO> userMap = new HashMap<String, ComkerUserDPO>();
    private Map<String, ComkerSettingKeyDPO> settingKeyMap = new HashMap<String, ComkerSettingKeyDPO>();
    private Map<String, ComkerSettingEntryDPO> settingEntryMap = new HashMap<String, ComkerSettingEntryDPO>();

    @InjectMocks
    private ComkerSettingStorageImpl settingStorage;

    @Mock
    private ComkerSettingDao settingDao;

    @Mock
    private ComkerSpotDao spotDao;

    @Mock
    private ComkerUserDao userDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        for(int i=0; i<2; i++) {
            ComkerSpotDPO spot = new ComkerSpotDPO("SPOT_" + i, "Spot " + i, "This is spot " + i);
            spot.setId("ID_" + spot.getCode());
            spotMap.put(spot.getId(), spot);
        }

        Mockito.when(spotDao.getByCode(Mockito.anyString())).thenAnswer(new Answer<ComkerSpotDPO>() {
            @Override
            public ComkerSpotDPO answer(InvocationOnMock invocation) throws Throwable {
                String code = (String) invocation.getArguments()[0];
                return spotMap.get("ID_" + code);
            }
        });

        for(int i=0; i<3; i++) {
            ComkerUserDPO user = new ComkerUserDPO("user-" + i + "@comker.net", "user" + i, "123456", "User " + i);
            user.setId("ID_" + user.getUsername());
            userMap.put(user.getId(), user);
        }

        Mockito.when(userDao.getByUsername(Mockito.anyString())).thenAnswer(new Answer<ComkerUserDPO>() {
            @Override
            public ComkerUserDPO answer(InvocationOnMock invocation) throws Throwable {
                String username = (String) invocation.getArguments()[0];
                return userMap.get("ID_" + username);
            }
        });

        for(int i=0; i<8; i++) {
            ComkerSettingKeyDPO settingKey = new ComkerSettingKeyDPO("APPLICATION_SETTING_" + i, null, null);
            settingKey.setId("ID_" + settingKey.getCode());
            settingKeyMap.put(settingKey.getId(), settingKey);
        }

        Mockito.when(settingDao.getByCode(Mockito.anyString())).thenAnswer(new Answer<ComkerSettingKeyDPO>() {
            @Override
            public ComkerSettingKeyDPO answer(InvocationOnMock invocation) throws Throwable {
                String code = (String) invocation.getArguments()[0];
                return settingKeyMap.get("ID_" + code);
            }
        });

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerSpotDPO spot = (ComkerSpotDPO) invocation.getArguments()[0];
                ComkerUserDPO user = (ComkerUserDPO) invocation.getArguments()[1];
                ComkerSettingKeyDPO key = (ComkerSettingKeyDPO) invocation.getArguments()[2];
                Class clazz = (Class) invocation.getArguments()[3];
                Object defaultValue =  invocation.getArguments()[4];

                String id = spot.getCode() + "-" + user.getUsername() + "-" + key.getCode();
                ComkerSettingEntryDPO entry = new ComkerSettingEntryDPO(key, spot, user);
                setSettingEntryValue(entry, clazz, defaultValue);
                settingEntryMap.put(id, entry);

                return null;
            }
        }).when(settingDao).define(Mockito.any(ComkerSpotDPO.class),
                Mockito.any(ComkerUserDPO.class), Mockito.any(ComkerSettingKeyDPO.class),
                Mockito.any(Class.class), Mockito.any());
    }

    @Test
    public void test_define_setting() {
        settingStorage.define("SPOT_1", "user1", "APPLICATION_SETTING_1", Integer.class, 100);
        settingStorage.define("SPOT_1", "user1", "APPLICATION_SETTING_2", String.class, "Hello World");
        settingStorage.define("SPOT_1", "user1", "APPLICATION_SETTING_3", Double.class, 3.14);
        settingStorage.define("SPOT_1", "user1", "APPLICATION_SETTING_4", Boolean.class, true);
        
        ComkerSettingEntryDPO entry = settingEntryMap.get("SPOT_1-user1-APPLICATION_SETTING_1");
        Assert.assertNotNull(entry);
        Assert.assertTrue(entry.getValueInteger() == 100);

        entry = settingEntryMap.get("SPOT_1-user1-APPLICATION_SETTING_2");
        Assert.assertNotNull(entry);
        Assert.assertEquals(entry.getValueString(),"Hello World");

        entry = settingEntryMap.get("SPOT_1-user1-APPLICATION_SETTING_3");
        Assert.assertNotNull(entry);
        Assert.assertTrue(entry.getValueDouble() == 3.14);
    }

//    @Test
//    public void test_set_value() {
//        settingStorage.define(ComkerSpotDPO.UNKNOWN, ComkerUserDPO.UNKNOWN, "APPLICATION_SETTING_1", Integer.class, 100);
//        settingStorage.define("SPOT_1", "user1", "APPLICATION_SETTING_1", Integer.class, 100);
//
//        ComkerSettingEntryDPO entry = settingEntryMap.get("SPOT_1-user1-APPLICATION_SETTING_1");
//        Assert.assertNotNull(entry);
//        Assert.assertTrue(entry.getValueInteger() == 100);
//    }

    //--------------------------------------------------------------------------
    
    private <T> T getSettingEntryValue(ComkerSettingEntryDPO item, Class<T> clazz) {
        T result = null;
        if (clazz == String.class) {
            result = clazz.cast(item.getValueString());
        } else if (clazz == Double.class) {
            result = clazz.cast(item.getValueDouble());
        } else if (clazz == Integer.class) {
            result = clazz.cast(item.getValueInteger());
        } else if (clazz == Boolean.class) {
            result = clazz.cast(item.getValueBoolean());
        } else {
            result = clazz.cast(ComkerDataUtil.convertXStreamToObject(item.getValueXStream()));
        }
        return result;
    }

    private <T> void setSettingEntryValue(ComkerSettingEntryDPO item, Class<T> clazz, T value) {
        if (clazz == String.class) {
            item.setValueString((String)value);
        } else if (clazz == Double.class) {
            item.setValueDouble((Double)value);
        } else if (clazz == Integer.class) {
            item.setValueInteger((Integer)value);
        } else if (clazz == Boolean.class) {
            item.setValueBoolean((Boolean)value);
        } else {
            item.setValueXStream(ComkerDataUtil.convertObjectToXStream(value));
        }
    }
}
