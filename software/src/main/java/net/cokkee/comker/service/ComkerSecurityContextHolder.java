package net.cokkee.comker.service;

import org.springframework.security.core.context.SecurityContext;

/**
 *
 * @author drupalex
 */
public interface ComkerSecurityContextHolder {

    void clearContext();

    SecurityContext getContext();

    void setContext(SecurityContext context);
}
