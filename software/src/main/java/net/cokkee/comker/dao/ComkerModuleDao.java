package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;

import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.dpo.ComkerModuleDPO;

/**
 *
 * @author drupalex
 */
public interface ComkerModuleDao {

    ComkerModuleDPO find(String query, Map<String,Object> params);
    
    ComkerModuleDPO findWhere(Map<String,Object> params);

    Integer count();

    Integer countWhere(Map<String,Object> params);

    List findAll(ComkerQueryPager filter);

    List findAllWhere(Map<String,Object> params, ComkerQueryPager filter);

    Boolean exists(String id);

    ComkerModuleDPO get(String id);

    ComkerModuleDPO getByCode(String code);

    ComkerModuleDPO save(ComkerModuleDPO item);

}
