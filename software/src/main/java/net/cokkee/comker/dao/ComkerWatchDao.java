package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerWatchLog;

/**
 *
 * @author drupalex
 */
public interface ComkerWatchDao extends ComkerAbstractDao {

    ComkerWatchLog findWhere(Map<String,Object> params);

    List findAllWhere(Map<String,Object> params, ComkerPager filter);

    ComkerWatchLog get(String id);

    ComkerWatchLog create(ComkerWatchLog item);

    ComkerWatchLog update(ComkerWatchLog item);

    void delete(ComkerWatchLog item);

    void delete(String id);
}

