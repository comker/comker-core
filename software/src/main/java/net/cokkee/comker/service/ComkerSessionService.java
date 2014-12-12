package net.cokkee.comker.service;

import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;

/**
 *
 * @author drupalex
 */
public interface ComkerSessionService {

    ComkerQueryPager getPager(Class clazz);

    ComkerQueryPager getPager(Class clazz, Integer start, Integer limit);
    
    ComkerQuerySieve getFilter(Class clazz);
}
