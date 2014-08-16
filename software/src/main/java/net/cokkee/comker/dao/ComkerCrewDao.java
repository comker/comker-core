package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerCrewDTO;
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

    Integer count(ComkerCrewDTO.Filter filter);

    List findAll(ComkerCrewDTO.Filter filter,ComkerPager pager);
    
    ComkerCrew findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerPager filter);

    Boolean exists(String id);

    ComkerCrew get(String id);

    ComkerCrew getByName(String name);

    ComkerCrew getBySpotWithRole(ComkerSpot spot, ComkerRole role);

    @Deprecated
    ComkerCrew save(ComkerCrew item);

    ComkerCrew create(ComkerCrew item);

    ComkerCrew update(ComkerCrew item);

    void delete(ComkerCrew item);

    void addGlobalRole(ComkerCrew crew, ComkerRole role);

    void removeGlobalRole(ComkerCrew crew, ComkerRole role);

    void collectGlobalRole(Set<ComkerRole> bag, ComkerCrew crew);

    void addRoleWithSpot(ComkerCrew crew, ComkerRole role, ComkerSpot spot);

    void removeRoleWithSpot(ComkerCrew crew, ComkerRole role, ComkerSpot spot);

    void collectSpotWithRole(Map<ComkerSpot,Set<ComkerRole>> bag, ComkerCrew crew);

    @Deprecated
    Set<String> getCodeOfGlobalRole(ComkerCrew crew);

    @Deprecated
    void collectCodeOfGlobalRole(Set<String> bag, ComkerCrew crew);

    @Deprecated
    Set<String> getCodeOfGlobalPermission(ComkerCrew crew);

    @Deprecated
    void collectCodeOfGlobalPermission(Set<String> bag, ComkerCrew crew);

    @Deprecated
    Map<String,Set<String>> getCodeOfSpotWithRole(ComkerCrew crew);

    @Deprecated
    void collectCodeOfSpotWithRole(Map<String,Set<String>> bag, ComkerCrew crew);

    @Deprecated
    Map<String,Set<String>> getCodeOfSpotWithPermission(ComkerCrew crew);

    @Deprecated
    void collectCodeOfSpotWithPermission(Map<String,Set<String>> bag, ComkerCrew crew);
}
