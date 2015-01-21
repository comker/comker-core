package net.cokkee.comker.service;

import java.io.Serializable;

import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;

/**
 *
 * @author drupalex
 */
public interface ComkerSessionService extends Serializable {

    ComkerQueryPager getPager(Class clazz);
    
    ComkerQuerySieve getSieve(Class clazz);
}
