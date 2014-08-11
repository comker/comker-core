package net.cokkee.comker.service;

import java.util.Map;
import net.cokkee.comker.model.ComkerPager;

/**
 *
 * @author drupalex
 */
public interface ComkerSessionService {

    ComkerPager getPager(Class clazz);

    ComkerPager getPager(Class clazz, Integer start, Integer limit);

    Map<String, Object> getUserListCriteria();

    ComkerPager getUserListPager();

    Map<String, Object> getCrewListCriteria();

    ComkerPager getCrewListPager();

    Map<String, Object> getSpotListCriteria();

    Map<String, Object> getRoleListCriteria();

    Map<String, Object> getPermissionListCriteria();

    ComkerPager getPermissionListPager();

    Map<String, Object> getWatchdogListCriteria();

    ComkerPager getWatchdogListPager();
}
