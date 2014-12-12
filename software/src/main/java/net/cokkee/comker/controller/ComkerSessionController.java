package net.cokkee.comker.controller;

import com.wordnik.swagger.annotations.*;
import java.util.Calendar;
import net.cokkee.comker.model.ComkerSessionInfo;
import net.cokkee.comker.model.ComkerUserDetails;
import net.cokkee.comker.service.ComkerSecurityService;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(value = "session", description = "Session API")
@Controller
@RequestMapping("/comker/session")
public class ComkerSessionController {

    private static final Logger log = LoggerFactory.getLogger(ComkerSessionController.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    @Autowired(required = false)
    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerSecurityService securityService = null;

    @Autowired(required = false)
    public void setSecurityService(ComkerSecurityService securityService) {
        this.securityService = securityService;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @ApiOperation(
        value = "Returns the information of current session",
        notes = "Returns the information of current session: logedin username, permissions, checkpoint.",
        response = ComkerSessionInfo.class)
    @RequestMapping(method = RequestMethod.GET, value = "/information", produces="application/json")
    public @ResponseBody ComkerSessionInfo getInformation() {
        
        ComkerSessionInfo item = new ComkerSessionInfo();

        item.setTimestamp(Calendar.getInstance().getTime());
        
        ComkerUserDetails userDetails = securityService.getUserDetails();
        if (userDetails != null) {
            item.setPermissions(userDetails.getPermissions());
        }
        
        return item;
    }
}
