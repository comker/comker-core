package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;

import net.cokkee.comker.model.po.ComkerSpot;

/**
 *
 * @author drupalex
 */
public interface ComkerSpotDao extends ComkerAbstractDao {

    Integer count(ComkerQuerySieve sieve);

    List findAll(ComkerQuerySieve sieve,ComkerQueryPager pager);

    ComkerSpot findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerQueryPager filter);

    Boolean exists(String id);

    ComkerSpot get(String id);

    ComkerSpot getByCode(String code);

    ComkerSpot create(ComkerSpot item);

    ComkerSpot update(ComkerSpot item);

    void delete(ComkerSpot item);
}
