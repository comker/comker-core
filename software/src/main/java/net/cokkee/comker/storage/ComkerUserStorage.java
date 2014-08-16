package net.cokkee.comker.storage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerUserDTO;

/**
 *
 * @author drupalex
 */
public interface ComkerUserStorage {

    Integer count();

    List<ComkerUserDTO> findAll(ComkerPager pager);

    Integer count(ComkerUserDTO.Filter filter);

    List<ComkerUserDTO> findAll(ComkerUserDTO.Filter filter, ComkerPager pager);

    ComkerUserDTO get(String id);

    ComkerUserDTO getByUsername(String username);

    ComkerUserDTO getByEmail(String email);

    Set<String> getGlobalAuthorities(String id);

    Map<String,Set<String>> getSpotCodeWithAuthorities(String id);

    ComkerUserDTO create(ComkerUserDTO item);

    void update(ComkerUserDTO item);

    void delete(String id);
}
