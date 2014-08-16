package net.cokkee.comker.storage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerCrewDTO;

/**
 *
 * @author drupalex
 */
public interface ComkerCrewStorage {

    Integer count();

    List<ComkerCrewDTO> findAll(ComkerPager pager);

    Integer count(ComkerCrewDTO.Filter filter);

    List<ComkerCrewDTO> findAll(ComkerCrewDTO.Filter filter, ComkerPager pager);

    ComkerCrewDTO get(String id);

    Set<String> getGlobalRoleCodes(String id);

    Set<String> getGlobalAuthorities(String id);

    Map<String,Set<String>> getSpotCodeWithRoleCodes(String id);

    Map<String,Set<String>> getSpotCodeWithAuthorities(String id);

    ComkerCrewDTO create(ComkerCrewDTO item);

    void update(ComkerCrewDTO item);

    void delete(String id);
}
