package boundary.Point;

import entity.Point;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/point")
public class PointRepresentation {

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
}
