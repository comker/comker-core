package net.cokkee.comker.storage;

import java.util.List;
import java.util.Set;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerRoleDTO;

/**
 *
 * @author drupalex
 */
public interface ComkerRoleStorage {

    Integer count();

    List<ComkerRoleDTO> findAll(ComkerPager pager);

    Integer count(ComkerRoleDTO.Filter filter);

    List<ComkerRoleDTO> findAll(ComkerRoleDTO.Filter filter, ComkerPager pager);

    ComkerRoleDTO get(String id);

    ComkerRoleDTO getByCode(String code);

    Set<String> getAuthorities(String id);

    ComkerRoleDTO create(ComkerRoleDTO item);

    void update(ComkerRoleDTO item);

    void delete(String id);
}
