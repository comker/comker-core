package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dpo.ComkerCrewDPO;
import net.cokkee.comker.model.dpo.ComkerUserDPO;

/**
 *
 * @author drupalex
 */
public interface ComkerUserDao extends ComkerAbstractDao {

    Integer count(ComkerQuerySieve sieve);

    List findAll(ComkerQuerySieve sieve, ComkerQueryPager pager);

    ComkerUserDPO findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerQueryPager pager);

    Boolean exists(String id);
    
    ComkerUserDPO get(String id);

    ComkerUserDPO getByEmail(String email);

    ComkerUserDPO getByUsername(String username);

    ComkerUserDPO create(ComkerUserDPO item);

    ComkerUserDPO update(ComkerUserDPO item);

    void delete(ComkerUserDPO item);

    void delete(String id);

    void addCrew(ComkerUserDPO user, ComkerCrewDPO crew);

    void removeCrew(ComkerUserDPO user, ComkerCrewDPO crew);

    void collectCrew(Set<ComkerCrewDPO> bag, ComkerUserDPO user);
}
