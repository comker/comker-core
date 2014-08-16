package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerWatchdogDTO;
import net.cokkee.comker.model.po.ComkerWatchdog;

/**
 *
 * @author drupalex
 */
public interface ComkerWatchdogDao extends ComkerAbstractDao {

    Integer count(ComkerWatchdogDTO.Filter filter);

    List findAll(ComkerWatchdogDTO.Filter filter,ComkerPager pager);
    
    ComkerWatchdog findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerPager pager);

    ComkerWatchdog get(String id);

    ComkerWatchdog create(ComkerWatchdog item);

    ComkerWatchdog update(ComkerWatchdog item);

    void delete(ComkerWatchdog item);
}

