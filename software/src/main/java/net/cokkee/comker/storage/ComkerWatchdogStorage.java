package net.cokkee.comker.storage;

import java.util.List;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerWatchdogDTO;

/**
 *
 * @author drupalex
 */
public interface ComkerWatchdogStorage {

    Integer count();

    List<ComkerWatchdogDTO> findAll(ComkerPager pager);

    Integer count(ComkerWatchdogDTO.Filter filter);

    List<ComkerWatchdogDTO> findAll(ComkerWatchdogDTO.Filter filter, ComkerPager pager);

    ComkerWatchdogDTO get(String id);

    ComkerWatchdogDTO create(ComkerWatchdogDTO item);

    void update(ComkerWatchdogDTO item);

    void delete(String id);
}
