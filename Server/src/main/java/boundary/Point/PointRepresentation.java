package boundary.Point;

import boundary.Representation;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import entity.Point;

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
    public Response getAll() {
        GenericEntity<List<Point>> list = new GenericEntity<List<Point>>(pointResource.findAll()) {};
        return Response.ok(list, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        Point point = pointResource.findById(id);

        if (point == null)
            return Response.noContent().build();

        return Response.ok(point, MediaType.APPLICATION_JSON).build();
    }

    @POST
    public Response add(@Context UriInfo uriInfo, Point point) {
        if (point == null)
            flash(400, EMPTY_JSON);

        point = pointResource.insert(point);
        return Response.ok(point, MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, Point point) {
        if (point == null)
            flash(400, EMPTY_JSON);

        Point originalPoint = pointResource.findById(id);
        if (originalPoint == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        if (!point.isValid())
            flash(400, INVALID_JSON);

        originalPoint.update(point);
        pointResource.update(point);
      
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        Point point = pointResource.findById(id);
      
        if (point == null)
            flash(404, "Error : point does not exist");

        pointResource.delete(point);

        return Response.status(204).build();
    }
}
