package net.cokkee.comker.storage;

import java.util.List;
import java.util.Set;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerRoleDTO;

/**
 *
 * @author drupalex
 */
public interface ComkerRoleStorage {

    Integer count();

    List<ComkerRoleDTO> findAll(ComkerQueryPager pager);

    Integer count(ComkerQuerySieve sieve);

    List<ComkerRoleDTO> findAll(ComkerQuerySieve sieve, ComkerQueryPager pager);

    ComkerRoleDTO get(String id);

    ComkerRoleDTO getByCode(String code);

    Set<String> getAuthorities(String id);

    ComkerRoleDTO create(ComkerRoleDTO item);

    void update(ComkerRoleDTO item);

    void delete(String id);
}
