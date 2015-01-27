package net.cokkee.comker.storage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerUserDTO;

/**
 *
 * @author drupalex
 */
public interface ComkerUserStorage {

    Integer count();

    List<ComkerUserDTO> findAll(ComkerQueryPager pager);

    Integer count(ComkerQuerySieve sieve);

    List<ComkerUserDTO> findAll(ComkerQuerySieve sieve, ComkerQueryPager pager);

    ComkerUserDTO get(String id);

    ComkerUserDTO getByUsername(String username);

    ComkerUserDTO getByEmail(String email);

    Set<String> getGlobalAuthorities(String id);

    Map<String,Set<String>> getSpotCodeWithAuthorities(String id);

    String create(ComkerUserDTO item);

    void update(ComkerUserDTO item);

    void delete(String id);
}
