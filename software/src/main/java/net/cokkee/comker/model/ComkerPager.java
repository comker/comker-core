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
public class ComkerPager implements Serializable {

    public static final Integer DEFAULT_START = new Integer(0);
    public static final Integer DEFAULT_LIMIT = new Integer(10);

    public static final ComkerPager ONE = new ComkerPager(DEFAULT_START, new Integer(1));

    public ComkerPager() {
    }

    public ComkerPager(Integer start, Integer limit) {
        this.start = start;
        this.limit = limit;
    }
    private Integer start;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }
    private Integer limit;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
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

    public void updateTo(ComkerPager target) {
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

    public static void apply(Query query, ComkerPager filter) {
        if (filter != null) {
            if (filter.getStart() != null) {
                query.setFirstResult(filter.getStart());
            }
            if (filter.getLimit() != null) {
                query.setMaxResults(filter.getLimit());
            }
        }
    }

    public static void apply(Criteria c, ComkerPager filter) {
        if (filter != null) {
            if (filter.getStart() != null) {
                c.setFirstResult(filter.getStart());
            }
            if (filter.getLimit() != null) {
                c.setMaxResults(filter.getLimit());
            }
        }
    }
}
