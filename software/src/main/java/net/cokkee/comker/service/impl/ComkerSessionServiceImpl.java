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

    private final Map<Class,ComkerQueryPager> pagers = new HashMap<Class, ComkerQueryPager>();

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

    //----------------------------------------------------------------------

    private final Map<Class, ComkerQuerySieve> sieves = new HashMap<Class, ComkerQuerySieve>();

    @Override
    public ComkerQuerySieve getSieve(Class clazz) {
        if (clazz == null) return null;

        ComkerQuerySieve sieve = sieves.get(clazz);
        if (sieve == null) {
            sieve = new ComkerQuerySieve();
            sieves.put(clazz, sieve);
        }

        return sieve;
    }
}
