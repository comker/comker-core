package net.cokkee.comker.storage;

import java.util.List;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerWatchdogDTO;

/**
 *
 * @author drupalex
 */
public interface ComkerWatchdogStorage {

    Integer count();

    List<ComkerWatchdogDTO> findAll(ComkerQueryPager pager);

    Integer count(ComkerQuerySieve sieve);

    List<ComkerWatchdogDTO> findAll(ComkerQuerySieve sieve, ComkerQueryPager pager);

    ComkerWatchdogDTO get(String id);

    String create(ComkerWatchdogDTO item);

    void update(ComkerWatchdogDTO item);

    void delete(String id);
}
