package provider.Filter;


import entity.User;
import entity.UserRole;
import provider.AuthenticatedUser;
import provider.Secured;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Inject
    @AuthenticatedUser
    User userLogged;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the resources that match with the requested URL
        List<UserRole> classRoles = extractRoles(resourceInfo.getResourceClass());
        List<UserRole> methodRoles = extractRoles(resourceInfo.getResourceMethod());

        try {

            if (methodRoles.isEmpty())
                checkPermissions(classRoles);
            else
                checkPermissions(methodRoles);

        } catch (Exception e) {
            requestContext.abortWith( Response.status(Response.Status.FORBIDDEN)
                    .type("text/plain")
                    .entity("You are not allowed to do this !")
                    .build() );
        }
    }

    /**
     * Method that extracts the roles from the annotated element
     * @param annotatedElement
     * @return
     */
    private List<UserRole> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null)
            return new ArrayList<>();

        Secured secured = annotatedElement.getAnnotation(Secured.class);
        if (secured == null)
            return new ArrayList<>();

        return Arrays.asList(secured.value());
    }

    private void checkPermissions(List<UserRole> allowedRoles) throws Exception {
        if (!allowedRoles.contains(userLogged.getRole())) {
            throw new ForbiddenException(userLogged.getRole()+ " is not allowed to do this action");
        }
    }
}
