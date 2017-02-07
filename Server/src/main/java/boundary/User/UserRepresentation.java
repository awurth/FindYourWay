package boundary.User;

import boundary.Representation;
import control.PasswordManagement;
import entity.User;
import entity.UserRole;
import provider.Secured;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/user")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRepresentation extends Representation {

    @EJB
    UserResource userResource;

    @GET
    @Secured({UserRole.ADMIN})
    @Path("/{email}")
    public Response get(@PathParam("email") String email) {
        User user = userResource.findByEmail(email);
        if (user != null)
            return Response.ok(user, MediaType.APPLICATION_JSON).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Secured({UserRole.ADMIN})
    public Response getAll(){
        GenericEntity<List<User>> list = new GenericEntity<List<User>>(userResource.findAll()){};
        return Response.ok(list, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/signup")
    public Response signup(User user) {
        if ((user.getName() == null || user.getEmail() == null || user.getPassword() == null))
            return Response.status(Response.Status.NOT_FOUND).build();

        if (userResource.findByEmail(user.getEmail()) != null)
            flash(409, "This email address is already used");

        try {
            userResource.insert(new User(user.getName(), user.getEmail(), PasswordManagement.digestPassword(user.getPassword())));
            return Response.ok().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

}
