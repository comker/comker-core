package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;

import net.cokkee.comker.model.ComkerPager;
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

    List findAll(ComkerPager filter);

    List findAllWhere(Map<String,Object> params, ComkerPager filter);

    ComkerModule get(String id);

    ComkerModule getByCode(String code);

    ComkerModule save(ComkerModule item);

}
