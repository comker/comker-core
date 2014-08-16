package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;

import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerPermissionDTO;
import net.cokkee.comker.model.po.ComkerPermission;

/**
 *
 * @author drupalex
 */
public interface ComkerPermissionDao {

    ComkerPermission find(String query, Map<String,Object> params);
    
    ComkerPermission findWhere(Map<String,Object> params);

    Integer count(ComkerPermissionDTO.Filter filter);

    Integer countWhere(Map<String,Object> params);

    List findAll(ComkerPermissionDTO.Filter filter,ComkerPager pager);

    List findAllWhere(Map<String,Object> params, ComkerPager filter);

    Boolean exists(String id);

    ComkerPermission get(String id);

    ComkerPermission getByAuthority(String authority);

    ComkerPermission save(ComkerPermission item);
}
