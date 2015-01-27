package net.cokkee.comker.storage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerCrewDTO;

/**
 *
 * @author drupalex
 */
public interface ComkerCrewStorage {

    Integer count();

    List<ComkerCrewDTO> findAll(ComkerQueryPager pager);

    Integer count(ComkerQuerySieve sieve);

    List<ComkerCrewDTO> findAll(ComkerQuerySieve sieve, ComkerQueryPager pager);

    ComkerCrewDTO get(String id);

    Set<String> getGlobalRoleCodes(String id);

    Set<String> getGlobalAuthorities(String id);

    Map<String,Set<String>> getSpotCodeWithRoleCodes(String id);

    Map<String,Set<String>> getSpotCodeWithAuthorities(String id);

    String create(ComkerCrewDTO item);

    void update(ComkerCrewDTO item);

    void delete(String id);
}
