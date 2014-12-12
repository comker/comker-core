package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.po.ComkerPermission;

import net.cokkee.comker.model.po.ComkerRole;

/**
 *
 * @author drupalex
 */
public interface ComkerRoleDao extends ComkerAbstractDao {

    Integer count(ComkerQuerySieve sieve);

    List findAll(ComkerQuerySieve sieve, ComkerQueryPager pager);

    ComkerRole findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerQueryPager pager);

    Boolean exists(String id);

    ComkerRole get(String id);

    ComkerRole getByCode(String code);

    ComkerRole create(ComkerRole item);

    void update(ComkerRole item);

    void delete(ComkerRole item);

    void addPermission(ComkerRole role, ComkerPermission permission);

    void removePermission(ComkerRole role, ComkerPermission permission);

    void collectPermission(Set<ComkerPermission> bag, ComkerRole role);
}
