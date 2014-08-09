package net.cokkee.comker.service;

import java.util.Map;
import net.cokkee.comker.model.ComkerPager;

/**
 *
 * @author drupalex
 */
public interface ComkerSessionService {

    Map<String, Object> getUserListCriteria();

    ComkerPager getUserListPager();

    Map<String, Object> getCrewListCriteria();

    ComkerPager getCrewListPager();

    Map<String, Object> getSpotListCriteria();

    ComkerPager getSpotListPager();

    Map<String, Object> getRoleListCriteria();

    ComkerPager getRoleListPager();

    Map<String, Object> getPermissionListCriteria();

    ComkerPager getPermissionListPager();

    Map<String, Object> getWatchdogListCriteria();

    ComkerPager getWatchdogListPager();
}
