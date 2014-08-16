package net.cokkee.comker.model.dto;

import net.cokkee.comker.model.ComkerObject;

/**
 *
 * @author drupalex
 */
public abstract class ComkerAbstractDTO  extends ComkerObject {

    public static final String NULL = "--null--";
    public static final String EMPTY = "";

    public static abstract class Filter {

        public Filter() {
            super();
        }

        public Filter(String queryString) {
            super();
            this.queryString = queryString;
        }

        private String queryString = null;

        public String getQueryString() {
            return queryString;
        }

        public void setQueryString(String queryString) {
            this.queryString = queryString;
        }

        public void updateTo(Filter target) {
            if (target == null) return;
            if (this.getQueryString() != null) {
                target.setQueryString(this.getQueryString());
            }
        }

        public boolean isEmpty() {
            return (this.getQueryString() == null);
        }
    }
}
