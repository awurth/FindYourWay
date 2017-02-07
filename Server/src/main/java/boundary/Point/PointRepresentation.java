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
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

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
    
    @POST
    public Response add(@Context UriInfo uriInfo, Point point) {
        
         if (point == null)
            return Response.status(400)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity("Error : you sent an empty object")
                    .build();
         
         point = pointResource.insert(point);
       
        return Response.ok(point, MediaType.APPLICATION_JSON).build();
    }
}
