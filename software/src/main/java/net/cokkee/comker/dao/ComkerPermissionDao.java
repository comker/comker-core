package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;

import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dpo.ComkerPermissionDPO;

/**
 *
 * @author drupalex
 */
public interface ComkerPermissionDao {

    ComkerPermissionDPO findWhere(Map<String,Object> params);

    Integer count(ComkerQuerySieve sieve);

    List findAll(ComkerQuerySieve sieve,ComkerQueryPager pager);

    List findAllWhere(Map<String,Object> params, ComkerQueryPager filter);

    Boolean exists(String id);

    ComkerPermissionDPO get(String id);

    ComkerPermissionDPO getByAuthority(String authority);

    ComkerPermissionDPO save(ComkerPermissionDPO item);
}
