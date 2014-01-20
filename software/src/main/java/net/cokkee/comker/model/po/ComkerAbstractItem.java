package net.cokkee.comker.model.po;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.cokkee.comker.model.ComkerObject;

import org.hibernate.Query;
import org.springframework.validation.Errors;

/**
 *
 * @author drupalex
 */
public class ComkerAbstractItem  extends ComkerObject {

    public static final String NULL = "--null--";
    public static final String EMPTY = "";

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public abstract static class Validator
            implements org.springframework.validation.Validator {

        private String interceptedMethodName = null;

        public String getInterceptedMethodName() {
            return interceptedMethodName;
        }

        public void setInterceptedMethodName(String interceptedMethodName) {
            this.interceptedMethodName = interceptedMethodName;
        }

        /**
         * @see org.springframework.validation.Validator#supports(java.lang.Class)
         */
        @Override
        public boolean supports(final Class clazz) {
            return clazz.isAssignableFrom(getValidatorSupportClass());
        }

        /**
         * @see org.springframework.validation.Validator#validate(java.lang.Object,
         *      org.springframework.validation.Errors)
         */
        @Override
        public abstract void validate(final Object obj, final Errors errors);

        /**
         * @return validator supported Class Object
         */
        protected abstract Class getValidatorSupportClass();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @XmlRootElement(name = "comkerCriteria")
    @XmlType(name = "comkerCriteria")
    public static class Criteria {
        private String term = null;

        public String getMatchName() {
            return term;
        }

        public void setMatchName(String matchName) {
            this.term = matchName;
        }

        public void updateTo(Criteria target) {
            if (target == null) return;
            if (this.getMatchName() != null) {
                target.setMatchName(this.getMatchName());
            }
        }

        public boolean isEmpty() {
            return (this.getMatchName() == null);
        }
    }

    @XmlRootElement(name = "comkerFilter")
    @XmlType(name = "comkerFilter")
    public static class Filter {

        public static final Integer DEFAULT_START = new Integer(0);
        public static final Integer DEFAULT_LIMIT = new Integer(10);

        public Filter() {}

        public Filter(Integer start, Integer limit) {
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

        public void updateTo(Filter target) {
            if (target == null) return;
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
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static class QueryBuilder {
        public static void applyPagination(Query query, ComkerAbstractItem.Filter filter) {
            if (filter != null) {
                if (filter.getStart() != null) {
                    query.setFirstResult(filter.getStart());
                }
                if (filter.getLimit() != null) {
                    query.setMaxResults(filter.getLimit());
                }
            }
        }

        public static String appendQueryFilter(String qstr, ComkerAbstractItem.Filter filter) {
            return qstr;
        }
    }
}
