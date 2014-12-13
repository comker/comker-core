package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;

import net.cokkee.comker.model.dpo.ComkerSpotDPO;

/**
 *
 * @author drupalex
 */
public interface ComkerSpotDao extends ComkerAbstractDao {

    Integer count(ComkerQuerySieve sieve);

    List findAll(ComkerQuerySieve sieve,ComkerQueryPager pager);

    ComkerSpotDPO findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerQueryPager filter);

    Boolean exists(String id);

    ComkerSpotDPO get(String id);

    ComkerSpotDPO getByCode(String code);

    ComkerSpotDPO create(ComkerSpotDPO item);

    ComkerSpotDPO update(ComkerSpotDPO item);

    void delete(ComkerSpotDPO item);
}
