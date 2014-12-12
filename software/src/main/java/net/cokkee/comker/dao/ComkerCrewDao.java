package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerSpot;

/**
 *
 * @author drupalex
 */
public interface ComkerCrewDao extends ComkerAbstractDao {

    public static final String FIELD_SPOT = "pk.spot";
    public static final String FIELD_ROLE = "pk.role";

    Integer count(ComkerQuerySieve sieve);

    List findAll(ComkerQuerySieve sieve,ComkerQueryPager pager);
    
    ComkerCrew findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerQueryPager filter);
    
    List findAllWhere(ComkerRole globalRole);

    Boolean exists(String id);

    ComkerCrew get(String id);

    ComkerCrew getByName(String name);

    ComkerCrew getBySpotWithRole(ComkerSpot spot, ComkerRole role);

    ComkerCrew create(ComkerCrew item);

    ComkerCrew update(ComkerCrew item);

    void delete(ComkerCrew item);

    void addGlobalRole(ComkerCrew crew, ComkerRole role);

    void removeGlobalRole(ComkerCrew crew, ComkerRole role);

    void collectGlobalRole(Set<ComkerRole> bag, ComkerCrew crew);

    void addRoleWithSpot(ComkerCrew crew, ComkerRole role, ComkerSpot spot);

    void removeRoleWithSpot(ComkerCrew crew, ComkerRole role, ComkerSpot spot);

    void collectSpotWithRole(Map<ComkerSpot,Set<ComkerRole>> bag, ComkerCrew crew);
}
