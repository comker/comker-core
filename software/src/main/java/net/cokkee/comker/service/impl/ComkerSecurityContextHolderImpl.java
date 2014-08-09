package net.cokkee.comker.service.impl;

import net.cokkee.comker.service.ComkerSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author drupalex
 */
public class ComkerSecurityContextHolderImpl implements ComkerSecurityContextHolder {

    @Override
    public void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public SecurityContext getContext() {
        return SecurityContextHolder.getContext();
    }

    @Override
    public void setContext(SecurityContext context) {
        SecurityContextHolder.setContext(context);
    }
}
