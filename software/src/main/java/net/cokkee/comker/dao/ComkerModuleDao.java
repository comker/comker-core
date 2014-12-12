package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;

import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.po.ComkerModule;

/**
 *
 * @author drupalex
 */
public interface ComkerModuleDao {

    ComkerModule find(String query, Map<String,Object> params);
    
    ComkerModule findWhere(Map<String,Object> params);

    Integer count();

    Integer countWhere(Map<String,Object> params);

    List findAll(ComkerQueryPager filter);

    List findAllWhere(Map<String,Object> params, ComkerQueryPager filter);

    Boolean exists(String id);

    ComkerModule get(String id);

    ComkerModule getByCode(String code);

    ComkerModule save(ComkerModule item);

}
