package net.cokkee.comker.storage;

import java.util.List;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerSpotDTO;

/**
 *
 * @author drupalex
 */
public interface ComkerSpotStorage {

    Integer count();

    List<ComkerSpotDTO> findAll(ComkerQueryPager pager);

    Integer count(ComkerQuerySieve sieve);

    List<ComkerSpotDTO> findAll(ComkerQuerySieve sieve, ComkerQueryPager pager);

    ComkerSpotDTO get(String id);

    ComkerSpotDTO getByCode(String code);

    String create(ComkerSpotDTO item);

    void update(ComkerSpotDTO item);

    void delete(String id);
}
