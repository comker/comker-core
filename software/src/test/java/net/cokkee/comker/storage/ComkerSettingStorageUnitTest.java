package net.cokkee.comker.storage;

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
import net.cokkee.comker.model.po.ComkerSettingEntry;
import net.cokkee.comker.model.po.ComkerSettingKey;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.model.po.ComkerUser;
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

    private Map<String, ComkerSpot> spotMap = new HashMap<String, ComkerSpot>();
    private Map<String, ComkerUser> userMap = new HashMap<String, ComkerUser>();
    private Map<String, ComkerSettingKey> settingKeyMap = new HashMap<String, ComkerSettingKey>();
    private Map<String, ComkerSettingEntry> settingEntryMap = new HashMap<String, ComkerSettingEntry>();

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
            ComkerSpot spot = new ComkerSpot("SPOT_" + i, "Spot " + i, "This is spot " + i);
            spot.setId("ID_" + spot.getCode());
            spotMap.put(spot.getId(), spot);
        }

        Mockito.when(spotDao.getByCode(Mockito.anyString())).thenAnswer(new Answer<ComkerSpot>() {
            @Override
            public ComkerSpot answer(InvocationOnMock invocation) throws Throwable {
                String code = (String) invocation.getArguments()[0];
                return spotMap.get("ID_" + code);
            }
        });

        for(int i=0; i<3; i++) {
            ComkerUser user = new ComkerUser("user-" + i + "@comker.net", "user" + i, "123456", "User " + i);
            user.setId("ID_" + user.getUsername());
            userMap.put(user.getId(), user);
        }

        Mockito.when(userDao.getByUsername(Mockito.anyString())).thenAnswer(new Answer<ComkerUser>() {
            @Override
            public ComkerUser answer(InvocationOnMock invocation) throws Throwable {
                String username = (String) invocation.getArguments()[0];
                return userMap.get("ID_" + username);
            }
        });

        for(int i=0; i<8; i++) {
            ComkerSettingKey settingKey = new ComkerSettingKey("APPLICATION_SETTING_" + i, null, null);
            settingKey.setId("ID_" + settingKey.getCode());
            settingKeyMap.put(settingKey.getId(), settingKey);
        }

        Mockito.when(settingDao.getByCode(Mockito.anyString())).thenAnswer(new Answer<ComkerSettingKey>() {
            @Override
            public ComkerSettingKey answer(InvocationOnMock invocation) throws Throwable {
                String code = (String) invocation.getArguments()[0];
                return settingKeyMap.get("ID_" + code);
            }
        });

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerSpot spot = (ComkerSpot) invocation.getArguments()[0];
                ComkerUser user = (ComkerUser) invocation.getArguments()[1];
                ComkerSettingKey key = (ComkerSettingKey) invocation.getArguments()[2];
                Class clazz = (Class) invocation.getArguments()[3];
                Object defaultValue =  invocation.getArguments()[4];

                String id = spot.getCode() + "-" + user.getUsername() + "-" + key.getCode();
                ComkerSettingEntry entry = new ComkerSettingEntry(key, spot, user);
                setSettingEntryValue(entry, clazz, defaultValue);
                settingEntryMap.put(id, entry);

                return null;
            }
        }).when(settingDao).define(Mockito.any(ComkerSpot.class),
                Mockito.any(ComkerUser.class), Mockito.any(ComkerSettingKey.class),
                Mockito.any(Class.class), Mockito.any());
    }

    @Test
    public void test_define_setting() {
        settingStorage.define("SPOT_1", "user1", "APPLICATION_SETTING_1", Integer.class, 100);
        settingStorage.define("SPOT_1", "user1", "APPLICATION_SETTING_2", String.class, "Hello World");
        settingStorage.define("SPOT_1", "user1", "APPLICATION_SETTING_3", Double.class, 3.14);
        settingStorage.define("SPOT_1", "user1", "APPLICATION_SETTING_4", Boolean.class, true);
        
        ComkerSettingEntry entry = settingEntryMap.get("SPOT_1-user1-APPLICATION_SETTING_1");
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
//        settingStorage.define(ComkerSpot.UNKNOWN, ComkerUser.UNKNOWN, "APPLICATION_SETTING_1", Integer.class, 100);
//        settingStorage.define("SPOT_1", "user1", "APPLICATION_SETTING_1", Integer.class, 100);
//
//        ComkerSettingEntry entry = settingEntryMap.get("SPOT_1-user1-APPLICATION_SETTING_1");
//        Assert.assertNotNull(entry);
//        Assert.assertTrue(entry.getValueInteger() == 100);
//    }

    //--------------------------------------------------------------------------
    
    private <T> T getSettingEntryValue(ComkerSettingEntry item, Class<T> clazz) {
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

    private <T> void setSettingEntryValue(ComkerSettingEntry item, Class<T> clazz, T value) {
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
