package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.po.ComkerWatchdog;

/**
 *
 * @author drupalex
 */
public interface ComkerWatchdogDao extends ComkerAbstractDao {

    Integer count(ComkerQuerySieve sieve);

    List findAll(ComkerQuerySieve sieve,ComkerQueryPager pager);
    
    ComkerWatchdog findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerQueryPager pager);

    ComkerWatchdog get(String id);

    ComkerWatchdog create(ComkerWatchdog item);

    ComkerWatchdog update(ComkerWatchdog item);

    void delete(ComkerWatchdog item);
}

