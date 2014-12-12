package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;

import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.po.ComkerPermission;

/**
 *
 * @author drupalex
 */
public interface ComkerPermissionDao {

    ComkerPermission findWhere(Map<String,Object> params);

    Integer count(ComkerQuerySieve sieve);

    List findAll(ComkerQuerySieve sieve,ComkerQueryPager pager);

    List findAllWhere(Map<String,Object> params, ComkerQueryPager filter);

    Boolean exists(String id);

    ComkerPermission get(String id);

    ComkerPermission getByAuthority(String authority);

    ComkerPermission save(ComkerPermission item);
}
