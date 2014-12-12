package net.cokkee.comker.security.web.authentication;

import java.io.IOException;
import java.text.MessageFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.cokkee.comker.model.ComkerExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.Assert;

/**
 *
 * @author drupalex
 */
public class ComkerAjaxAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler
            implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(ComkerAjaxAuthenticationFailureHandler.class);

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
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws ServletException, IOException {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("run onAuthenticationFailure()", new Object[] {}));
        }

        if (getHelper().checkAjaxRequest(request, response, null)) {
            getHelper().writeAjaxResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED,
                    new ComkerExceptionResponse("Authentication failed"));
            return;
        }
    }
}
