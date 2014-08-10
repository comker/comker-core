package net.cokkee.comker.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.cokkee.comker.model.po.ComkerNavbarNode;

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

    ComkerNavbarNode getTree();

    ComkerNavbarNode getTree(String fromNodeId, String excludeNodeId);

    List getList();
    
    List getList(String fromNodeId, String excludedNodeId);

    ComkerNavbarNode get(String id);

    ComkerNavbarNode getByCode(String code);

    ComkerNavbarNode create(ComkerNavbarNode item);
    
    void update(ComkerNavbarNode item);

    void delete(ComkerNavbarNode item);

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
