package control;

import boundary.Account.UserResource;
import entity.User;
import provider.AuthenticatedUser;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@RequestScoped
public class UserManagement {

    @Produces
    @RequestScoped
    @AuthenticatedUser
    private User user;

    @Inject
    UserResource userResource;

    public void handleAuthenticationEvent(@Observes @AuthenticatedUser String email) {
        this.user = userResource.findByEmail(email);
    }

}