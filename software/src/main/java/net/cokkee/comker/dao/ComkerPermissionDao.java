package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;

import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerPermission;

/**
 *
 * @author drupalex
 */
public interface ComkerPermissionDao {

    ComkerPermission find(String query, Map<String,Object> params);
    
    ComkerPermission findWhere(Map<String,Object> params);

    Integer count();

    Integer countWhere(Map<String,Object> params);

    List findAll(ComkerPager filter);

    List findAllWhere(Map<String,Object> params, ComkerPager filter);

    ComkerPermission get(String id);

    ComkerPermission getByAuthority(String authority);

    ComkerPermission save(ComkerPermission item);
}
