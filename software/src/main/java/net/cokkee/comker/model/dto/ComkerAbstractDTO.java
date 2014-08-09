package net.cokkee.comker.model.dto;

import net.cokkee.comker.model.ComkerObject;
import org.springframework.validation.Errors;

/**
 *
 * @author drupalex
 */
public class ComkerAbstractDTO  extends ComkerObject {

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
}
