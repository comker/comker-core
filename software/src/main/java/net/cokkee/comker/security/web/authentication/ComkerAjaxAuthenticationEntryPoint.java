package net.cokkee.comker.security.web.authentication;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.cokkee.comker.model.ComkerExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 *
 * @author drupalex
 */
public class ComkerAjaxAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {

    private final Logger log = LoggerFactory.getLogger(ComkerAjaxAuthenticationEntryPoint.class);

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
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {
        helper.writeAjaxResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED,
                new ComkerExceptionResponse("Unauthorized"));
    }
}
