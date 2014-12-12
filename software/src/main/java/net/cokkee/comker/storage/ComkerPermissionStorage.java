package net.cokkee.comker.storage;

import java.util.List;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerPermissionDTO;

/**
 *
 * @author drupalex
 */
public interface ComkerPermissionStorage {

    Integer count();

    List findAll(ComkerQueryPager pager);

    Integer count(ComkerQuerySieve sieve);

    List findAll(ComkerQuerySieve sieve, ComkerQueryPager pager);

    ComkerPermissionDTO get(String id);

    ComkerPermissionDTO getByAuthority(String authority);

    ComkerPermissionDTO create(ComkerPermissionDTO item);
}
