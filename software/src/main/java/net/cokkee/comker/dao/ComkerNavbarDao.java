package net.cokkee.comker.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.cokkee.comker.model.dpo.ComkerNavbarNodeDPO;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

/**
 *
 * @author drupalex
 */
public interface ComkerNavbarDao {

    public static final String FILTER_ID = "id";
    public static final String FILTER_EXCLUDE_ID = "excludeId";

    public static final String FIELD_TREE_ID = "treeId";
    
    Integer count();

    ComkerNavbarNodeDPO getTree();

    ComkerNavbarNodeDPO getTree(String fromNodeId, String excludeNodeId);

    List getList();
    
    List getList(String fromNodeId, String excludedNodeId);

    Boolean exists(String id);

    ComkerNavbarNodeDPO get(String id);

    ComkerNavbarNodeDPO getByCode(String code);

    ComkerNavbarNodeDPO create(ComkerNavbarNodeDPO item);
    
    void update(ComkerNavbarNodeDPO item);

    void delete(ComkerNavbarNodeDPO item);

    public static class Interceptor extends EmptyInterceptor {
        private List<String> dirtyPropertyNames = new ArrayList<String>();

        @Override
        public boolean onFlushDirty(Object entity, Serializable id,
                Object[] currentState, Object[] previousState,
                String[] propertyNames, Type[] types) {

            return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
        }
    }
}
