package boundary.Point;

import boundary.Representation;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import entity.Point;
import entity.UserRole;
import provider.Secured;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import static javax.ws.rs.HttpMethod.PUT;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("/points")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/points", description = "Points management")
public class PointRepresentation extends Representation {

    @EJB
    private PointResource pointResource;

    @GET
    //@Secured({UserRole.ADMIN})
    @ApiOperation(value = "Get all the points", notes = "Access : Admin only")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response getAll() {
        GenericEntity<List<Point>> list = new GenericEntity<List<Point>>(pointResource.findAll()){};
        return Response.ok(list, MediaType.APPLICATION_JSON).build();
    }

    @GET
    //@Secured({UserRole.ADMIN})
    @Path("/{id}")
    @ApiOperation(value = "Get a point by its id", notes = "Access : Admin only")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response get(@PathParam("id") String id) {
        Point point = pointResource.findById(id);

        if (point == null)
            flash(404, "Error : Point does not exist");

        return Response.ok(point, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    //@Secured({UserRole.ADMIN})
    @ApiOperation(value = "Add a point", notes = "Access : Admin only")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response add(@Context UriInfo uriInfo, Point point) {
         if (point == null)
             flash(400, EMPTY_JSON);

        point = pointResource.insert(point);
        return Response.ok(point, MediaType.APPLICATION_JSON).build();
    }
    
    @PUT
    @Path("/{id}")
    //@Secured({UserRole.ADMIN})
    @ApiOperation(value = "Update a point", notes = "Access : Admin only")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response update(@FormParam("id") String id, Point point) {
        if (point == null)
            flash(400, EMPTY_JSON);
        
        Point original = pointResource.findById(id);

        if (original == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        if (!original.isValid())
            flash(400, "Error : invalid object");

        original.update(point);
        pointResource.update(original);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{id}")
    //@Secured({UserRole.ADMIN})
    @ApiOperation(value = "Delete a point", notes = "Access : Admin only")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public Response delete(@FormParam("id") String id) {
        Point point = pointResource.findById(id);

        if (point == null)
            flash(404, "Error : point does not exist");

        pointResource.delete(point);

        return Response.status(204).build();
    }
}
