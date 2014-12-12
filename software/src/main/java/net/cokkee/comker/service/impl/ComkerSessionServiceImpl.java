package net.cokkee.comker.service.impl;

import java.util.HashMap;
import java.util.Map;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.service.ComkerSessionService;

/**
 *
 * @author drupalex
 */
public class ComkerSessionServiceImpl implements ComkerSessionService {

    private Map<Class,ComkerQueryPager> pagers = new HashMap<Class, ComkerQueryPager>();

    @Override
    public ComkerQueryPager getPager(Class clazz) {
        if (clazz == null) return null;

        ComkerQueryPager pager = pagers.get(clazz);
        if (pager == null) {
            pager = new ComkerQueryPager();
            pagers.put(clazz, pager);
        }

        return pager;
    }

    @Override
    public ComkerQueryPager getPager(Class clazz, Integer start, Integer limit) {
        ComkerQueryPager pager = getPager(clazz);
        if (start != null) pager.setStart(start);
        if (limit != null) pager.setLimit(limit);
        return pager;
    }

    //----------------------------------------------------------------------

    private final Map<Class, ComkerQuerySieve> filters = new HashMap<Class, ComkerQuerySieve>();

    @Override
    public ComkerQuerySieve getFilter(Class clazz) {
        if (clazz == null) return null;

        ComkerQuerySieve filter = filters.get(clazz);
        if (filter == null) {
            filter = new ComkerQuerySieve();
            filters.put(clazz, filter);
        }

        return filter;
    }
}
