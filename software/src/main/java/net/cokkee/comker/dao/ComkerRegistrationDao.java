package net.cokkee.comker.dao;

import java.util.List;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dpo.ComkerRegistrationDPO;

/**
 *
 * @author drupalex
 */
public interface ComkerRegistrationDao extends ComkerAbstractDao {

    Integer count(ComkerQuerySieve sieve);

    List list(ComkerQuerySieve sieve,ComkerQueryPager pager);
    
    ComkerRegistrationDPO get(String id);
    
    String create(ComkerRegistrationDPO item);
    
    void update(ComkerRegistrationDPO item);
    
    void delete(ComkerRegistrationDPO item);
}

