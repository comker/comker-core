package net.cokkee.comker.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.Criteria;
import org.hibernate.Query;

/**
 *
 * @author drupalex
 */
@XmlRootElement
public class ComkerQueryPager implements Serializable {

    public static final Integer DEFAULT_START = new Integer(0);
    public static final Integer DEFAULT_LIMIT = new Integer(10);

    public static final ComkerQueryPager ONE = new ComkerQueryPager(DEFAULT_START, new Integer(1));

    public ComkerQueryPager() {
         this.start = DEFAULT_START;
         this.limit = DEFAULT_LIMIT;
    }

    public ComkerQueryPager(Integer start, Integer limit) {
        this.start = start;
        this.limit = limit;
    }

    private Integer start;
    private Integer limit;

    public Integer getStart() {
        return start;
    }

    public ComkerQueryPager setStart(Integer start) {
        this.start = (start == null || start > 0) ? start : 0;
        return this;
    }
    
    public ComkerQueryPager updateStart(Integer start) {
        if (start != null) {
            this.start = (start > 0) ? start : 0;
        }
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public ComkerQueryPager setLimit(Integer limit) {
        this.limit = (limit == null || limit > 0) ? limit : 0;
        return this;
    }
    
    public ComkerQueryPager updateLimit(Integer limit) {
        if (limit != null) {
            this.limit = (limit > 0) ? limit : 0;
        }
        return this;
    }

    private String sortedCol = null;

    public String getSortedCol() {
        return sortedCol;
    }

    public void setSortedCol(String sortedCol) {
        this.sortedCol = sortedCol;
    }

    private String sortedDir = null;

    public String getSortedDir() {
        return sortedDir;
    }

    public void setSortedDir(String sortedDir) {
        this.sortedDir = sortedDir;
    }

    public void updateTo(ComkerQueryPager target) {
        if (target == null) {
            return;
        }
        if (this.start != null) {
            target.setStart(this.start);
        }
        if (this.limit != null) {
            target.setLimit(this.limit);
        }
        if (this.sortedCol != null) {
            target.setSortedCol(this.sortedCol);
        }
        if (this.sortedDir != null) {
            target.setSortedDir(this.sortedDir);
        }
    }

    public Query applyTo(Query query) {
        if (this.getStart() != null) {
            query.setFirstResult(this.getStart());
        }
        if (this.getLimit() != null) {
            query.setMaxResults(this.getLimit());
        }
        return query;
    }

    public Criteria applyTo(Criteria c) {
        if (this.getStart() != null) {
            c.setFirstResult(this.getStart());
        }
        if (this.getLimit() != null) {
            c.setMaxResults(this.getLimit());
        }
        return c;
    }

    public static Query apply(Query query, ComkerQueryPager pager) {
        if (pager != null) {
            pager.applyTo(query);
        }
        return query;
    }

    public static Criteria apply(Criteria c, ComkerQueryPager pager) {
        if (pager != null) {
            pager.applyTo(c);
        }
        return c;
    }
}
