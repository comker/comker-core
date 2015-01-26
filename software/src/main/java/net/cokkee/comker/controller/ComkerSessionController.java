package net.cokkee.comker.controller;

import com.wordnik.swagger.annotations.*;
import java.util.Calendar;
import net.cokkee.comker.model.ComkerSessionInfo;
import net.cokkee.comker.model.ComkerUserDetails;
import net.cokkee.comker.service.ComkerSecurityContextReader;
import net.cokkee.comker.service.ComkerSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(value = "Session", description = "Session Information API")
@Controller
@RequestMapping("/comker/session")
public class ComkerSessionController {

    private ComkerSessionService sessionService = null;

    @Autowired(required = false)
    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerSecurityContextReader securityContextReader = null;

    @Autowired(required = false)
    public void setSecurityContextReader(ComkerSecurityContextReader securityContextReader) {
        this.securityContextReader = securityContextReader;
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
        
        ComkerUserDetails userDetails = securityContextReader.getUserDetails();
        if (userDetails != null) {
            item.setPermissions(userDetails.getPermissions());
        }
        
        return item;
    }
}
