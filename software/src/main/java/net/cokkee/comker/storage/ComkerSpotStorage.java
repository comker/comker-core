package net.cokkee.comker.storage;

import java.util.List;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerSpotDTO;

/**
 *
 * @author drupalex
 */
public interface ComkerSpotStorage {

    Integer count();

    List<ComkerSpotDTO> findAll(ComkerPager filter);

    ComkerSpotDTO get(String id);

    ComkerSpotDTO getByCode(String code);

    ComkerSpotDTO create(ComkerSpotDTO item);

    void update(ComkerSpotDTO item);

    void delete(String id);
}
