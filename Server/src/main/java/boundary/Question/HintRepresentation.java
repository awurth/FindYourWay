package boundary.Question;

import boundary.Representation;
import entity.Hint;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/hints")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HintRepresentation extends Representation{
    @EJB
    private QuestionResource questionResource;

    @EJB
    private HintResource hintResource;

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        Hint hint = hintResource.findById(id);
        if (hint == null)
            return Response.noContent().build();
        return Response.ok(hint, MediaType.APPLICATION_JSON).build();
    }

    @POST
    public Response add(Hint hint) {
        if (hint == null)
            flash(400, EMPTY_JSON);

        if (!hint.isValid())
            flash(400,INVALID_JSON);

        if (questionResource.findById(hint.getQuestion().getId()) == null)
            flash(400, "Error : question does not exist");

        return Response.ok(hint,MediaType.APPLICATION_JSON).build();
    }

}
