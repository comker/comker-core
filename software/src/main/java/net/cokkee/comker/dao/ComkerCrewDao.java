package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dpo.ComkerCrewDPO;
import net.cokkee.comker.model.dpo.ComkerRoleDPO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;

/**
 *
 * @author drupalex
 */
public interface ComkerCrewDao extends ComkerAbstractDao {

    public static final String FIELD_SPOT = "pk.spot";
    public static final String FIELD_ROLE = "pk.role";

    Integer count(ComkerQuerySieve sieve);

    List findAll(ComkerQuerySieve sieve,ComkerQueryPager pager);
    
    ComkerCrewDPO findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerQueryPager filter);
    
    List findAllWhere(ComkerRoleDPO globalRole);

    Boolean exists(String id);

    ComkerCrewDPO get(String id);

    ComkerCrewDPO getByName(String name);

    ComkerCrewDPO getBySpotWithRole(ComkerSpotDPO spot, ComkerRoleDPO role);

    String create(ComkerCrewDPO item);

    void update(ComkerCrewDPO item);

    void delete(ComkerCrewDPO item);

    void addGlobalRole(ComkerCrewDPO crew, ComkerRoleDPO role);

    void removeGlobalRole(ComkerCrewDPO crew, ComkerRoleDPO role);

    void collectGlobalRole(Set<ComkerRoleDPO> bag, ComkerCrewDPO crew);

    void addRoleWithSpot(ComkerCrewDPO crew, ComkerRoleDPO role, ComkerSpotDPO spot);

    void removeRoleWithSpot(ComkerCrewDPO crew, ComkerRoleDPO role, ComkerSpotDPO spot);

    void collectSpotWithRole(Map<ComkerSpotDPO,Set<ComkerRoleDPO>> bag, ComkerCrewDPO crew);
}
