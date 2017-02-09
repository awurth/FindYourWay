package boundary.Point;

import boundary.Representation;
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
public class PointRepresentation extends Representation {

    @EJB
    private PointResource pointResource;

    @GET
    public Response getAll() {
        GenericEntity<List<Point>> list = new GenericEntity<List<Point>>(pointResource.findAll()){};
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
    public Response update(Point point) {
        if (point == null)
            flash(400, EMPTY_JSON);
        
        point = pointResource.findById(point.getId());

        if (point == null)
            return Response.noContent().build();

        if (point.isValid())
            flash(400, EMPTY_JSON);

        pointResource.update(point);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    public Response delete(Point point) {
        if (point == null)
            flash(400, EMPTY_JSON);

        if (pointResource.findById(point.getId()) == null)
            flash(404, "Error : point does not exist");

        pointResource.delete(point);

        return Response.status(204).build();
    }
}
