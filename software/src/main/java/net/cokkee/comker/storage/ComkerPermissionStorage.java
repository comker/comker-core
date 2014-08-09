package net.cokkee.comker.storage;

import java.util.List;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerPermissionDTO;

/**
 *
 * @author drupalex
 */
public interface ComkerPermissionStorage {

    Integer count();

    List findAll(ComkerPager filter);

    ComkerPermissionDTO get(String id);

    ComkerPermissionDTO getByAuthority(String authority);
}
