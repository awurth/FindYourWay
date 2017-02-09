package boundary.Question;

import boundary.Representation;
import com.wordnik.swagger.annotations.Api;
import entity.Hint;
import entity.Question;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/hints")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/hints", description = "Hints management")
public class HintRepresentation extends Representation {
    @EJB
    private QuestionResource questionResource;

    @EJB
    private HintResource hintResource;

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        Hint hint = hintResource.findById(id);
        if (hint == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(hint, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/question/{id}")
    public Response getByQuestion(@PathParam("id") String id) {
        Question question = questionResource.findById(id);
        if (question == null)
            flash(404, "Error : Question does not exist");

        List<Hint> hints = hintResource.findByQuestion(question);

        GenericEntity<List<Hint>> list = new GenericEntity<List<Hint>>(hints){};

        return Response.ok(list, MediaType.APPLICATION_JSON).build();
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
