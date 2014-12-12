package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerUser;

/**
 *
 * @author drupalex
 */
public interface ComkerUserDao extends ComkerAbstractDao {

    Integer count(ComkerQuerySieve sieve);

    List findAll(ComkerQuerySieve sieve, ComkerQueryPager pager);

    ComkerUser findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerQueryPager pager);

    Boolean exists(String id);
    
    ComkerUser get(String id);

    ComkerUser getByEmail(String email);

    ComkerUser getByUsername(String username);

    ComkerUser create(ComkerUser item);

    ComkerUser update(ComkerUser item);

    void delete(ComkerUser item);

    void delete(String id);

    void addCrew(ComkerUser user, ComkerCrew crew);

    void removeCrew(ComkerUser user, ComkerCrew crew);

    void collectCrew(Set<ComkerCrew> bag, ComkerUser user);
}
