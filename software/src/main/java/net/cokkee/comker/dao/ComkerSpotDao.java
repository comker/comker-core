package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import net.cokkee.comker.model.ComkerPager;

import net.cokkee.comker.model.po.ComkerSpot;

/**
 *
 * @author drupalex
 */
public interface ComkerSpotDao extends ComkerAbstractDao {

    Integer count();

    List findAll(ComkerPager filter);

    ComkerSpot findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerPager filter);

    Boolean exists(String id);

    ComkerSpot get(String id);

    ComkerSpot getByCode(String code);

    ComkerSpot create(ComkerSpot item);

    ComkerSpot update(ComkerSpot item);

    void delete(ComkerSpot item);

    void delete(String id);

    //Map<String,Set<String>> getCodeOfCrewWithRole(ComkerSpot spot);
}
