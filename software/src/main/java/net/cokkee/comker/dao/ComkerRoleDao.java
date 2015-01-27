package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dpo.ComkerPermissionDPO;

import net.cokkee.comker.model.dpo.ComkerRoleDPO;

/**
 *
 * @author drupalex
 */
public interface ComkerRoleDao extends ComkerAbstractDao {

    Integer count(ComkerQuerySieve sieve);

    List findAll(ComkerQuerySieve sieve, ComkerQueryPager pager);

    ComkerRoleDPO findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerQueryPager pager);

    Boolean exists(String id);

    ComkerRoleDPO get(String id);

    ComkerRoleDPO getByCode(String code);

    String create(ComkerRoleDPO item);

    void update(ComkerRoleDPO item);

    void delete(ComkerRoleDPO item);

    void addPermission(ComkerRoleDPO role, ComkerPermissionDPO permission);

    void removePermission(ComkerRoleDPO role, ComkerPermissionDPO permission);

    void collectPermission(Set<ComkerPermissionDPO> bag, ComkerRoleDPO role);
}
