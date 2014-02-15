package net.cokkee.comker.security.web.authentication;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.cokkee.comker.model.ComkerExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.ELRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;

/**
 *
 * @author drupalex
 */
public class ComkerAjaxAuthenticationHelper implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(ComkerAjaxAuthenticationHelper.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.requestMatcher == null) {
            this.requestMatcher = new ELRequestMatcher("hasHeader('X-Requested-With','ComkerAjaxRequest')");
        }
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("RequestMatcher is: {0}", new Object[]{requestMatcher.toString()}));
        }
    }

    private RequestMatcher requestMatcher = null;

	public void setRequestMatcher(RequestMatcher requestMatcher) {
		this.requestMatcher = requestMatcher;
	}

    private Gson gson = new Gson();

    public boolean checkAjaxRequest(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
		return requestMatcher.matches(request);
	}
    
    public void writeAjaxResponse(HttpServletRequest request, HttpServletResponse response,
            int status, ComkerExceptionResponse info) throws ServletException, IOException {
        
        response.setStatus(status);
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json");
        PrintWriter pw = response.getWriter();
        String json = gson.toJson(info);
        pw.print(json);
        pw.flush();

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Write AjaxResponse ... done.", new Object[]{}));
        }
    }
}
