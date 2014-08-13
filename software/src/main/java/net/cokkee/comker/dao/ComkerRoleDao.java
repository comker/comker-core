package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerPermission;

import net.cokkee.comker.model.po.ComkerRole;

/**
 *
 * @author drupalex
 */
public interface ComkerRoleDao extends ComkerAbstractDao {

    Integer count();

    List findAll(ComkerPager filter);

    ComkerRole findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerPager filter);

    @Deprecated
    Set<String> getAuthorities(String id);

    @Deprecated
    Set<String> getAuthorities(ComkerRole role);

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
