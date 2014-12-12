package net.cokkee.comker.api.impl;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.ComkerMemberResource;
import net.cokkee.comker.storage.ComkerUserStorage;
import net.cokkee.comker.model.dto.ComkerUserDTO;
import net.cokkee.comker.service.ComkerSecurityService;

/**
 *
 * @author drupalex
 */
@Produces("application/json")
public class ComkerMemberResourceImpl implements ComkerMemberResource {

    private ComkerSecurityService securityService;

    public void setSecurityService(ComkerSecurityService securityService) {
        this.securityService = securityService;
    }

    private ComkerUserStorage userStorage;

    public void setUserStorage(ComkerUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Response getUserProfile(String username) {
        ComkerUserDTO item = userStorage.getByUsername(username);
        return Response.ok().entity(item).build();
    }

    @Override
    public Response loadUserProfile(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Response saveUserProfile(String username, ComkerUserDTO userProfile) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Response changePassword(String currentPassword, String newPassword) {
        securityService.changePassword(currentPassword, newPassword);
        return Response.ok().build();
    }

    public Response resetPassword(String emailOrUsername) {
        return Response.serverError().build();
    }

    public Response resetPasswordConfirmation(String confirmationCode) {
        return Response.serverError().build();
    }
}
