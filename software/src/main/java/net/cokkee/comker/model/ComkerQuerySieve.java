package net.cokkee.comker.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author drupalex
 */
public class ComkerQuerySieve implements Serializable {
    
    protected final Map<String,Object> criteria = new HashMap<String, Object>();
    
    public Object getCriterion(String key) {
        return criteria.get(key);
    }
    
    public ComkerQuerySieve setCriterion(String key, Object value) {
        criteria.put(key, value);
        return this;
    }
    
    public boolean isEmpty() {
        return (this.criteria.isEmpty());
    }
    
    public void updateTo(ComkerQuerySieve target) {
        if (target == null) return;
        target.criteria.putAll(this.criteria);
    }
}
