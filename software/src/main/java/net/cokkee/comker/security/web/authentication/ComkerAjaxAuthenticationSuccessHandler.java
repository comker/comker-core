package net.cokkee.comker.security.web.authentication;

import java.io.IOException;
import java.text.MessageFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.cokkee.comker.model.error.ComkerExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.util.Assert;

/**
 *
 * @author drupalex
 */
public class ComkerAjaxAuthenticationSuccessHandler 
        extends SavedRequestAwareAuthenticationSuccessHandler implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(ComkerAjaxAuthenticationSuccessHandler.class);

    private ComkerAjaxAuthenticationHelper helper = null;

    public ComkerAjaxAuthenticationHelper getHelper() {
        return helper;
    }

    public void setHelper(ComkerAjaxAuthenticationHelper helper) {
        this.helper = helper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.isInstanceOf(ComkerAjaxAuthenticationHelper.class, helper, "helper must be specified");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
            HttpServletResponse response, Authentication authentication) 
                throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("run onAuthenticationSuccess()", new Object[]{}));
        }
        if (getHelper().checkAjaxRequest(request, response, authentication)) {
            if (log.isDebugEnabled()) {
                log.debug(" =@ Ajax Request");
            }
            getHelper().writeAjaxResponse(request, response, HttpServletResponse.SC_OK,
                    new ComkerExceptionResponse("Ok"));
            return;
        } else {
            if (log.isDebugEnabled()) {
                log.debug(" =@ Default Request");
            }
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
