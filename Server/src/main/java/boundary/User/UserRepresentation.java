package boundary.User;

import boundary.Representation;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import control.PasswordManagement;
import entity.User;
import entity.UserRole;
import provider.Secured;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/users")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/users", description = "Users management")
public class UserRepresentation extends Representation {

    @EJB
    UserResource userResource;

    @GET
    @Secured({UserRole.ADMIN})
    @Path("/{email}")
    @ApiOperation(value = "Get a user by its email address", notes = "Access : Admin only")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response get(@PathParam("email") String email) {
        User user = userResource.findByEmail(email);
        if (user != null)
            return Response.ok(user, MediaType.APPLICATION_JSON).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Secured({UserRole.ADMIN})
    @ApiOperation(value = "Get all the users", notes = "Access: Admin only")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response getAll() {
        GenericEntity<List<User>> list = new GenericEntity<List<User>>(userResource.findAll()) {};
        return Response.ok(list, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Secured({UserRole.CUSTOMER})
    @ApiOperation(value = "Get a user by a JWT", notes = "Access : Logged user only")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Path("/signedin")
    public Response getClientInfo(@Context SecurityContext securityContext) {
        User user = userResource.findByEmail(securityContext.getUserPrincipal().getName());
        JsonObject json = Json.createObjectBuilder()
                .add("user", Json.createObjectBuilder()
                        .add("email", user.getEmail())
                        .add("name", user.getName())
                        .add("role", user.getRole().toString()))
                .build();

        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/signup")
    @ApiOperation(value = "Create a customer account", notes = "Email address is unique")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 409, message = "Conflict : email address is already used"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response signup(User user) {
        if ((user.getName() == null || user.getEmail() == null || user.getPassword() == null))
            return Response.status(Response.Status.NOT_FOUND).build();

        if (userResource.findByEmail(user.getEmail()) != null)
            return flash(409, "This email address is already used");

        try {
            userResource.insert(new User(user.getName(), user.getEmail(), PasswordManagement.digestPassword(user.getPassword())));
            return Response.ok().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

}
