package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dpo.ComkerWatchdogDPO;

/**
 *
 * @author drupalex
 */
public interface ComkerWatchdogDao extends ComkerAbstractDao {

    Integer count(ComkerQuerySieve sieve);

    List findAll(ComkerQuerySieve sieve,ComkerQueryPager pager);
    
    ComkerWatchdogDPO findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerQueryPager pager);

    ComkerWatchdogDPO get(String id);

    String create(ComkerWatchdogDPO item);

    void update(ComkerWatchdogDPO item);

    void delete(ComkerWatchdogDPO item);
}

